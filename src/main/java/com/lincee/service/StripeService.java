package com.lincee.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StripeService {

    @Value("${stripe.currency}")
    private String currency;

    @Value("${stripe.success.url}")
    private String successUrl;

    @Value("${stripe.cancel.url}")
    private String cancelUrl;

    /**
     * Create a Payment Intent for processing payments
     */
    public PaymentIntent createPaymentIntent(BigDecimal amount, String currency, Map<String, String> metadata) throws StripeException {
        // Convert amount to cents (Stripe uses smallest currency unit)
        long amountInCents = amount.multiply(new BigDecimal("100")).longValue();
        
        PaymentIntentCreateParams.Builder paramsBuilder = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency(currency != null ? currency : this.currency)
                .addPaymentMethodType("card");

        // Add metadata if provided
        if (metadata != null && !metadata.isEmpty()) {
            paramsBuilder.putAllMetadata(metadata);
        }

        return PaymentIntent.create(paramsBuilder.build());
    }

    /**
     * Create a Checkout Session for hosted checkout page
     */
    public Session createCheckoutSession(Long orderId, BigDecimal amount, String customerEmail, List<Map<String, Object>> lineItems) throws StripeException {
        // Convert amount to cents
        long amountInCents = amount.multiply(new BigDecimal("100")).longValue();

        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(cancelUrl)
                .putMetadata("orderId", orderId.toString());

        // Add customer email if provided
        if (customerEmail != null && !customerEmail.isEmpty()) {
            paramsBuilder.setCustomerEmail(customerEmail);
        }

        // Add line items
        if (lineItems != null && !lineItems.isEmpty()) {
            for (Map<String, Object> item : lineItems) {
                paramsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(currency)
                                .setUnitAmount((Long) item.get("unitAmount"))
                                .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName((String) item.get("name"))
                                        .setDescription((String) item.getOrDefault("description", ""))
                                        .build()
                                )
                                .build()
                        )
                        .setQuantity((Long) item.get("quantity"))
                        .build()
                );
            }
        } else {
            // Create a single line item for the total amount
            paramsBuilder.addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency(currency)
                            .setUnitAmount(amountInCents)
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName("Order #" + orderId)
                                    .setDescription("Payment for order #" + orderId)
                                    .build()
                            )
                            .build()
                    )
                    .setQuantity(1L)
                    .build()
            );
        }

        return Session.create(paramsBuilder.build());
    }

    /**
     * Retrieve a Payment Intent by ID
     */
    public PaymentIntent retrievePaymentIntent(String paymentIntentId) throws StripeException {
        return PaymentIntent.retrieve(paymentIntentId);
    }

    /**
     * Retrieve a Checkout Session by ID
     */
    public Session retrieveCheckoutSession(String sessionId) throws StripeException {
        return Session.retrieve(sessionId);
    }

    /**
     * Cancel a Payment Intent
     */
    public PaymentIntent cancelPaymentIntent(String paymentIntentId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        return paymentIntent.cancel();
    }

    /**
     * Create metadata for Stripe payment
     */
    public Map<String, String> createMetadata(Long orderId, Long userId, String orderNumber) {
        Map<String, String> metadata = new HashMap<>();
        if (orderId != null) metadata.put("orderId", orderId.toString());
        if (userId != null) metadata.put("userId", userId.toString());
        if (orderNumber != null) metadata.put("orderNumber", orderNumber);
        return metadata;
    }
}

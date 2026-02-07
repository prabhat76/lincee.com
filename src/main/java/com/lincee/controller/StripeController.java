package com.lincee.controller;

import com.lincee.entity.Order;
import com.lincee.entity.Payment;
import com.lincee.repository.OrderRepository;
import com.lincee.repository.PaymentRepository;
import com.lincee.service.StripeService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/stripe")
@Tag(name = "Stripe Payment", description = "Stripe payment gateway integration endpoints")
public class StripeController {

    @Autowired
    private StripeService stripeService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostMapping("/create-payment-intent")
    @Operation(summary = "Create Payment Intent", description = "Create a Stripe Payment Intent for processing payment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment Intent created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<Map<String, Object>> createPaymentIntent(
            @Parameter(description = "Order ID") @RequestParam Long orderId) {
        try {
            Optional<Order> orderOpt = orderRepository.findById(orderId);
            if (!orderOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Order not found"));
            }

            Order order = orderOpt.get();
            
            // Create metadata
            Map<String, String> metadata = stripeService.createMetadata(
                order.getId(),
                order.getUser().getId(),
                order.getOrderNumber()
            );

            // Create payment intent
            PaymentIntent paymentIntent = stripeService.createPaymentIntent(
                order.getTotalAmount(),
                "usd",
                metadata
            );

            // Create or update payment record
            Payment payment = paymentRepository.findByOrderId(orderId)
                    .orElse(new Payment());
            
            payment.setOrder(order);
            payment.setAmount(order.getTotalAmount());
            payment.setPaymentMethod(Payment.PaymentMethod.CREDIT_CARD);
            payment.setPaymentGateway("STRIPE");
            payment.setTransactionId(paymentIntent.getId());
            payment.setStatus(Payment.PaymentStatus.PENDING);
            paymentRepository.save(payment);

            Map<String, Object> response = new HashMap<>();
            response.put("clientSecret", paymentIntent.getClientSecret());
            response.put("paymentIntentId", paymentIntent.getId());
            response.put("amount", order.getTotalAmount());
            response.put("currency", paymentIntent.getCurrency());

            return ResponseEntity.ok(response);

        } catch (StripeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/create-checkout-session")
    @Operation(summary = "Create Checkout Session", description = "Create a Stripe Checkout Session for hosted payment page")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Checkout Session created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<Map<String, Object>> createCheckoutSession(
            @Parameter(description = "Order ID") @RequestParam Long orderId) {
        try {
            Optional<Order> orderOpt = orderRepository.findById(orderId);
            if (!orderOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Order not found"));
            }

            Order order = orderOpt.get();
            String customerEmail = order.getUser().getEmail();

            // Create line items from order items
            List<Map<String, Object>> lineItems = new ArrayList<>();
            order.getOrderItems().forEach(item -> {
                Map<String, Object> lineItem = new HashMap<>();
                lineItem.put("name", item.getProduct().getName());
                lineItem.put("description", item.getProduct().getDescription());
                lineItem.put("unitAmount", item.getUnitPrice().multiply(new BigDecimal("100")).longValue());
                lineItem.put("quantity", (long) item.getQuantity());
                lineItems.add(lineItem);
            });

            Session session = stripeService.createCheckoutSession(
                order.getId(),
                order.getTotalAmount(),
                customerEmail,
                lineItems
            );

            // Create or update payment record
            Payment payment = paymentRepository.findByOrderId(orderId)
                    .orElse(new Payment());
            
            payment.setOrder(order);
            payment.setAmount(order.getTotalAmount());
            payment.setPaymentMethod(Payment.PaymentMethod.CREDIT_CARD);
            payment.setPaymentGateway("STRIPE");
            payment.setTransactionId(session.getId());
            payment.setStatus(Payment.PaymentStatus.PENDING);
            paymentRepository.save(payment);

            Map<String, Object> response = new HashMap<>();
            response.put("sessionId", session.getId());
            response.put("url", session.getUrl());
            response.put("amount", order.getTotalAmount());

            return ResponseEntity.ok(response);

        } catch (StripeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/payment-intent/{paymentIntentId}")
    @Operation(summary = "Get Payment Intent", description = "Retrieve a Payment Intent by ID")
    @ApiResponse(responseCode = "200", description = "Payment Intent retrieved successfully")
    public ResponseEntity<Map<String, Object>> getPaymentIntent(
            @Parameter(description = "Payment Intent ID") @PathVariable String paymentIntentId) {
        try {
            PaymentIntent paymentIntent = stripeService.retrievePaymentIntent(paymentIntentId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", paymentIntent.getId());
            response.put("status", paymentIntent.getStatus());
            response.put("amount", new BigDecimal(paymentIntent.getAmount()).divide(new BigDecimal("100")));
            response.put("currency", paymentIntent.getCurrency());
            response.put("clientSecret", paymentIntent.getClientSecret());
            
            return ResponseEntity.ok(response);

        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/session/{sessionId}")
    @Operation(summary = "Get Checkout Session", description = "Retrieve a Checkout Session by ID")
    @ApiResponse(responseCode = "200", description = "Session retrieved successfully")
    public ResponseEntity<Map<String, Object>> getCheckoutSession(
            @Parameter(description = "Session ID") @PathVariable String sessionId) {
        try {
            Session session = stripeService.retrieveCheckoutSession(sessionId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", session.getId());
            response.put("paymentStatus", session.getPaymentStatus());
            response.put("amountTotal", new BigDecimal(session.getAmountTotal()).divide(new BigDecimal("100")));
            response.put("currency", session.getCurrency());
            response.put("customerEmail", session.getCustomerEmail());
            
            return ResponseEntity.ok(response);

        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/webhook")
    @Operation(summary = "Stripe Webhook", description = "Handle Stripe webhook events")
    @ApiResponse(responseCode = "200", description = "Webhook processed successfully")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        // Handle the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        }

        switch (event.getType()) {
            case "payment_intent.succeeded":
                handlePaymentIntentSucceeded((PaymentIntent) stripeObject);
                break;
            case "payment_intent.payment_failed":
                handlePaymentIntentFailed((PaymentIntent) stripeObject);
                break;
            case "checkout.session.completed":
                handleCheckoutSessionCompleted((Session) stripeObject);
                break;
            default:
                System.out.println("Unhandled event type: " + event.getType());
        }

        return ResponseEntity.ok("Success");
    }

    private void handlePaymentIntentSucceeded(PaymentIntent paymentIntent) {
        String paymentIntentId = paymentIntent.getId();
        
        Optional<Payment> paymentOpt = paymentRepository.findByTransactionId(paymentIntentId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setStatus(Payment.PaymentStatus.COMPLETED);
            payment.setPaidAt(LocalDateTime.now());
            
            // Update order status
            Order order = payment.getOrder();
            order.setStatus(Order.OrderStatus.CONFIRMED);
            orderRepository.save(order);
            
            paymentRepository.save(payment);
            System.out.println("Payment succeeded for order: " + order.getOrderNumber());
        }
    }

    private void handlePaymentIntentFailed(PaymentIntent paymentIntent) {
        String paymentIntentId = paymentIntent.getId();
        
        Optional<Payment> paymentOpt = paymentRepository.findByTransactionId(paymentIntentId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setStatus(Payment.PaymentStatus.FAILED);
            paymentRepository.save(payment);
            System.out.println("Payment failed for payment intent: " + paymentIntentId);
        }
    }

    private void handleCheckoutSessionCompleted(Session session) {
        String sessionId = session.getId();
        
        Optional<Payment> paymentOpt = paymentRepository.findByTransactionId(sessionId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setStatus(Payment.PaymentStatus.COMPLETED);
            payment.setPaidAt(LocalDateTime.now());
            payment.setReferenceNumber(session.getPaymentIntent());
            
            // Update order status
            Order order = payment.getOrder();
            order.setStatus(Order.OrderStatus.CONFIRMED);
            orderRepository.save(order);
            
            paymentRepository.save(payment);
            System.out.println("Checkout session completed for order: " + order.getOrderNumber());
        }
    }
}

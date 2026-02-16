package com.lincee.controller;

import com.lincee.dto.AddressDTO;
import com.lincee.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@Tag(name = "Address Management", description = "APIs for managing user addresses")
public class AddressController {
    
    @Autowired
    private AddressService addressService;
    
    @PostMapping
    @Operation(summary = "Add new address", description = "Add a new address for the user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Address added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid address data"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> addAddress(
            @Parameter(description = "User ID") @RequestParam(required = false) Long userId,
            @RequestBody AddressDTO addressDTO) {
        Long resolvedUserId = userId != null ? userId : addressDTO.getUserId();
        if (resolvedUserId == null) {
            return ResponseEntity.badRequest().body("userId is required");
        }
        AddressDTO createdAddress = addressService.addAddress(resolvedUserId, addressDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get address by ID", description = "Retrieve a specific address by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Address found"),
        @ApiResponse(responseCode = "404", description = "Address not found")
    })
    public ResponseEntity<AddressDTO> getAddressById(
            @Parameter(description = "Address ID") @PathVariable Long id) {
        AddressDTO address = addressService.getAddressById(id);
        return ResponseEntity.ok(address);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all user addresses", description = "Retrieve all addresses for a specific user")
    @ApiResponse(responseCode = "200", description = "Addresses retrieved successfully")
    public ResponseEntity<List<AddressDTO>> getUserAddresses(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        List<AddressDTO> addresses = addressService.getUserAddresses(userId);
        return ResponseEntity.ok(addresses);
    }
    
    @GetMapping("/user/{userId}/shipping")
    @Operation(summary = "Get shipping addresses", description = "Retrieve all shipping addresses for a user")
    @ApiResponse(responseCode = "200", description = "Shipping addresses retrieved successfully")
    public ResponseEntity<List<AddressDTO>> getShippingAddresses(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        List<AddressDTO> addresses = addressService.getShippingAddresses(userId);
        return ResponseEntity.ok(addresses);
    }
    
    @GetMapping("/user/{userId}/billing")
    @Operation(summary = "Get billing addresses", description = "Retrieve all billing addresses for a user")
    @ApiResponse(responseCode = "200", description = "Billing addresses retrieved successfully")
    public ResponseEntity<List<AddressDTO>> getBillingAddresses(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        List<AddressDTO> addresses = addressService.getBillingAddresses(userId);
        return ResponseEntity.ok(addresses);
    }
    
    @GetMapping("/user/{userId}/default")
    @Operation(summary = "Get default address", description = "Retrieve the default address for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Default address found"),
        @ApiResponse(responseCode = "404", description = "Default address not found")
    })
    public ResponseEntity<AddressDTO> getDefaultAddress(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        AddressDTO address = addressService.getDefaultAddress(userId);
        return ResponseEntity.ok(address);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update address", description = "Update a specific address")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Address updated successfully"),
        @ApiResponse(responseCode = "404", description = "Address not found")
    })
    public ResponseEntity<AddressDTO> updateAddress(
            @Parameter(description = "Address ID") @PathVariable Long id,
            @RequestBody AddressDTO addressDTO) {
        AddressDTO updatedAddress = addressService.updateAddress(id, addressDTO);
        return ResponseEntity.ok(updatedAddress);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete address", description = "Delete a specific address")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Address deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Address not found")
    })
    public ResponseEntity<Void> deleteAddress(
            @Parameter(description = "Address ID") @PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}

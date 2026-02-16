package com.lincee.service;

import com.lincee.dto.AddressDTO;
import com.lincee.entity.Address;
import com.lincee.entity.User;
import com.lincee.repository.AddressRepository;
import com.lincee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AddressService {
    
    @Autowired
    private AddressRepository addressRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public AddressDTO addAddress(Long userId, AddressDTO addressDTO) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new RuntimeException("User not found");
        }
        
        Address address = new Address();
        address.setUser(user.get());
        address.setAddressLine1(addressDTO.getAddressLine1());
        address.setAddressLine2(addressDTO.getAddressLine2());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setZipCode(addressDTO.getZipCode());
        address.setCountry(addressDTO.getCountry());
        address.setPhoneNumber(addressDTO.getPhoneNumber());
        address.setIsDefault(addressDTO.getIsDefault() != null ? addressDTO.getIsDefault() : false);
        address.setIsShippingAddress(addressDTO.getIsShippingAddress() != null ? addressDTO.getIsShippingAddress() : true);
        address.setIsBillingAddress(addressDTO.getIsBillingAddress() != null ? addressDTO.getIsBillingAddress() : false);
        address.setAddressType(addressDTO.getAddressType());
        applyAddressTypeDefaults(address, addressDTO);
        
        Address savedAddress = addressRepository.save(address);
        return convertToDTO(savedAddress);
    }
    
    public AddressDTO getAddressById(Long addressId) {
        Optional<Address> address = addressRepository.findById(addressId);
        return address.map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Address not found"));
    }
    
    public List<AddressDTO> getUserAddresses(Long userId) {
        return addressRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<AddressDTO> getShippingAddresses(Long userId) {
        return addressRepository.findShippingAddressesByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<AddressDTO> getBillingAddresses(Long userId) {
        return addressRepository.findBillingAddressesByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public AddressDTO getDefaultAddress(Long userId) {
        Optional<Address> address = addressRepository.findByUserIdAndIsDefaultTrue(userId);
        return address.map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Default address not found"));
    }
    
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
        Optional<Address> address = addressRepository.findById(addressId);
        if (!address.isPresent()) {
            throw new RuntimeException("Address not found");
        }
        
        Address existingAddress = address.get();
        if (addressDTO.getAddressLine1() != null) {
            existingAddress.setAddressLine1(addressDTO.getAddressLine1());
        }
        if (addressDTO.getAddressLine2() != null) {
            existingAddress.setAddressLine2(addressDTO.getAddressLine2());
        }
        if (addressDTO.getCity() != null) {
            existingAddress.setCity(addressDTO.getCity());
        }
        if (addressDTO.getState() != null) {
            existingAddress.setState(addressDTO.getState());
        }
        if (addressDTO.getZipCode() != null) {
            existingAddress.setZipCode(addressDTO.getZipCode());
        }
        if (addressDTO.getCountry() != null) {
            existingAddress.setCountry(addressDTO.getCountry());
        }
        if (addressDTO.getPhoneNumber() != null) {
            existingAddress.setPhoneNumber(addressDTO.getPhoneNumber());
        }
        if (addressDTO.getIsDefault() != null) {
            existingAddress.setIsDefault(addressDTO.getIsDefault());
        }
        if (addressDTO.getIsShippingAddress() != null) {
            existingAddress.setIsShippingAddress(addressDTO.getIsShippingAddress());
        }
        if (addressDTO.getIsBillingAddress() != null) {
            existingAddress.setIsBillingAddress(addressDTO.getIsBillingAddress());
        }
        if (addressDTO.getAddressType() != null) {
            existingAddress.setAddressType(addressDTO.getAddressType());
        }
        applyAddressTypeDefaults(existingAddress, addressDTO);
        
        Address updatedAddress = addressRepository.save(existingAddress);
        return convertToDTO(updatedAddress);
    }

    private void applyAddressTypeDefaults(Address address, AddressDTO addressDTO) {
        if (addressDTO.getAddressType() == null) {
            return;
        }

        boolean shippingSet = addressDTO.getIsShippingAddress() != null;
        boolean billingSet = addressDTO.getIsBillingAddress() != null;
        if (shippingSet || billingSet) {
            return;
        }

        String type = addressDTO.getAddressType().trim().toUpperCase();
        if ("SHIPPING".equals(type)) {
            address.setIsShippingAddress(true);
            address.setIsBillingAddress(false);
        } else if ("BILLING".equals(type)) {
            address.setIsShippingAddress(false);
            address.setIsBillingAddress(true);
        }
    }
    
    public void deleteAddress(Long addressId) {
        if (!addressRepository.existsById(addressId)) {
            throw new RuntimeException("Address not found");
        }
        addressRepository.deleteById(addressId);
    }
    
    private AddressDTO convertToDTO(Address address) {
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setUserId(address.getUser().getId());
        dto.setAddressLine1(address.getAddressLine1());
        dto.setAddressLine2(address.getAddressLine2());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setZipCode(address.getZipCode());
        dto.setCountry(address.getCountry());
        dto.setPhoneNumber(address.getPhoneNumber());
        dto.setIsDefault(address.getIsDefault());
        dto.setIsShippingAddress(address.getIsShippingAddress());
        dto.setIsBillingAddress(address.getIsBillingAddress());
        dto.setAddressType(address.getAddressType());
        dto.setCreatedAt(address.getCreatedAt());
        dto.setUpdatedAt(address.getUpdatedAt());
        return dto;
    }
}

package com.lincee.repository;

import com.lincee.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserId(Long userId);
    Optional<Address> findByUserIdAndIsDefaultTrue(Long userId);
    
    @Query("SELECT a FROM Address a WHERE a.user.id = :userId AND a.isShippingAddress = true")
    List<Address> findShippingAddressesByUserId(@Param("userId") Long userId);
    
    @Query("SELECT a FROM Address a WHERE a.user.id = :userId AND a.isBillingAddress = true")
    List<Address> findBillingAddressesByUserId(@Param("userId") Long userId);
}

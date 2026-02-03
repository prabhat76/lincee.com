package com.lincee.repository;

import com.lincee.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
    
    Optional<Collection> findBySlug(String slug);
    
    List<Collection> findByActiveTrue();
    
    List<Collection> findByFeaturedTrueAndActiveTrue();
    
    @Query("SELECT c FROM Collection c ORDER BY c.displayOrder ASC, c.name ASC")
    List<Collection> findAllOrderedByDisplayOrder();
    
    @Query("SELECT c FROM Collection c WHERE c.active = true ORDER BY c.displayOrder ASC, c.name ASC")
    List<Collection> findActiveOrderedByDisplayOrder();
    
    boolean existsBySlug(String slug);
    
    boolean existsByName(String name);
}

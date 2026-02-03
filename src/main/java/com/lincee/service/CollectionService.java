package com.lincee.service;

import com.lincee.dto.CollectionDTO;
import com.lincee.entity.Collection;
import com.lincee.entity.Product;
import com.lincee.repository.CollectionRepository;
import com.lincee.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollectionService {

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<CollectionDTO> getAllCollections() {
        return collectionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CollectionDTO> getActiveCollections() {
        return collectionRepository.findActiveOrderedByDisplayOrder().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CollectionDTO> getFeaturedCollections() {
        return collectionRepository.findByFeaturedTrueAndActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CollectionDTO getCollectionById(Long id) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collection not found with id: " + id));
        return convertToDTO(collection);
    }

    public CollectionDTO getCollectionBySlug(String slug) {
        Collection collection = collectionRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Collection not found with slug: " + slug));
        return convertToDTO(collection);
    }

    @Transactional
    public CollectionDTO createCollection(Collection collection) {
        // Check if slug already exists
        if (collectionRepository.existsBySlug(collection.getSlug())) {
            throw new RuntimeException("Collection with slug '" + collection.getSlug() + "' already exists");
        }
        
        // Check if name already exists
        if (collectionRepository.existsByName(collection.getName())) {
            throw new RuntimeException("Collection with name '" + collection.getName() + "' already exists");
        }

        Collection savedCollection = collectionRepository.save(collection);
        return convertToDTO(savedCollection);
    }

    @Transactional
    public CollectionDTO updateCollection(Long id, Collection collectionDetails) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collection not found with id: " + id));

        // Check if new slug already exists for another collection
        if (!collection.getSlug().equals(collectionDetails.getSlug()) && 
            collectionRepository.existsBySlug(collectionDetails.getSlug())) {
            throw new RuntimeException("Collection with slug '" + collectionDetails.getSlug() + "' already exists");
        }

        // Check if new name already exists for another collection
        if (!collection.getName().equals(collectionDetails.getName()) && 
            collectionRepository.existsByName(collectionDetails.getName())) {
            throw new RuntimeException("Collection with name '" + collectionDetails.getName() + "' already exists");
        }

        collection.setName(collectionDetails.getName());
        collection.setSlug(collectionDetails.getSlug());
        collection.setDescription(collectionDetails.getDescription());
        collection.setImageUrl(collectionDetails.getImageUrl());
        collection.setActive(collectionDetails.getActive());
        collection.setFeatured(collectionDetails.getFeatured());
        collection.setDisplayOrder(collectionDetails.getDisplayOrder());

        Collection updatedCollection = collectionRepository.save(collection);
        return convertToDTO(updatedCollection);
    }

    @Transactional
    public void deleteCollection(Long id) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collection not found with id: " + id));
        
        // Remove all product associations before deleting
        collection.getProducts().forEach(product -> product.getCollections().remove(collection));
        collectionRepository.delete(collection);
    }

    @Transactional
    public CollectionDTO addProductToCollection(Long collectionId, Long productId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("Collection not found with id: " + collectionId));
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        collection.addProduct(product);
        Collection updatedCollection = collectionRepository.save(collection);
        return convertToDTO(updatedCollection);
    }

    @Transactional
    public CollectionDTO removeProductFromCollection(Long collectionId, Long productId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("Collection not found with id: " + collectionId));
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        collection.removeProduct(product);
        Collection updatedCollection = collectionRepository.save(collection);
        return convertToDTO(updatedCollection);
    }

    @Transactional
    public CollectionDTO addMultipleProductsToCollection(Long collectionId, List<Long> productIds) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("Collection not found with id: " + collectionId));

        for (Long productId : productIds) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
            collection.addProduct(product);
        }

        Collection updatedCollection = collectionRepository.save(collection);
        return convertToDTO(updatedCollection);
    }

    private CollectionDTO convertToDTO(Collection collection) {
        CollectionDTO dto = new CollectionDTO();
        dto.setId(collection.getId());
        dto.setName(collection.getName());
        dto.setSlug(collection.getSlug());
        dto.setDescription(collection.getDescription());
        dto.setImageUrl(collection.getImageUrl());
        dto.setActive(collection.getActive());
        dto.setFeatured(collection.getFeatured());
        dto.setDisplayOrder(collection.getDisplayOrder());
        dto.setCreatedAt(collection.getCreatedAt());
        dto.setUpdatedAt(collection.getUpdatedAt());
        
        // Convert products to summary DTOs
        List<CollectionDTO.ProductSummaryDTO> productSummaries = collection.getProducts().stream()
                .map(product -> {
                    String imageUrl = product.getImageUrls() != null && !product.getImageUrls().isEmpty() 
                        ? product.getImageUrls().get(0) 
                        : null;
                    return new CollectionDTO.ProductSummaryDTO(
                        product.getId(), 
                        product.getName(), 
                        imageUrl,
                        product.getActive()
                    );
                })
                .collect(Collectors.toList());
        
        dto.setProducts(productSummaries);
        dto.setProductCount(productSummaries.size());
        
        return dto;
    }
}

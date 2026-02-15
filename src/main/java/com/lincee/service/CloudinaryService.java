package com.lincee.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    
    @Autowired
    private Cloudinary cloudinary;
    
    /**
     * Upload image file to Cloudinary
     * @param file File to upload
     * @param folder Cloudinary folder (e.g., "products/sweatshirts")
     * @return Cloudinary secure URL
     */
    public String uploadImage(File file, String folder) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file, 
            ObjectUtils.asMap(
                "folder", folder,
                "resource_type", "image"
            ));
        return uploadResult.get("secure_url").toString();
    }
    
    /**
     * Upload MultipartFile to Cloudinary
     * @param file MultipartFile from API
     * @param folder Cloudinary folder
     * @return Cloudinary secure URL
     */
    public String uploadImage(MultipartFile file, String folder) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), 
            ObjectUtils.asMap(
                "folder", folder,
                "resource_type", "image"
            ));
        return uploadResult.get("secure_url").toString();
    }
    
    /**
     * Delete image from Cloudinary
     * @param publicId Cloudinary public ID
     */
    public void deleteImage(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}

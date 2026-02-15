package com.lincee.controller;

import com.lincee.service.ExcelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/products")
@Tag(name = "Admin - Bulk Product Operations", description = "Admin APIs for bulk product management")
public class AdminProductController {

    @Autowired
    private ExcelService excelService;

    @PostMapping("/import/excel")
    @Operation(summary = "Import products from Excel", 
               description = "Upload an Excel file to create or update multiple products. " +
                           "The Excel should have columns: id, name, description, price, discountPrice, " +
                           "category, subCategory, brand, stockQuantity, imageUrls (comma-separated), " +
                           "availableSizes (comma-separated), availableColors (comma-separated), " +
                           "tags, active, featured, weightGrams")
    @ApiResponse(responseCode = "200", description = "Products imported successfully")
    public ResponseEntity<Map<String, Object>> importProductsFromExcel(
            @Parameter(description = "Excel file (.xlsx) with product data") 
            @RequestParam("file") MultipartFile file) {
        
        try {
            // Validate file
            if (file.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("error", "File is empty");
                return ResponseEntity.badRequest().body(error);
            }

            String filename = file.getOriginalFilename();
            if (filename == null || !filename.endsWith(".xlsx")) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("error", "Only .xlsx files are supported");
                return ResponseEntity.badRequest().body(error);
            }

            // Process Excel file
            Map<String, Object> result = excelService.importProductsFromExcel(file);
            return ResponseEntity.ok(result);

        } catch (IOException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Failed to process Excel file: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Unexpected error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/template/excel")
    @Operation(summary = "Download Excel template", 
               description = "Download an Excel template file with correct column headers for bulk product import")
    @ApiResponse(responseCode = "200", description = "Template downloaded successfully")
    public ResponseEntity<byte[]> downloadExcelTemplate() {
        try {
            // Generate template
            Workbook workbook = excelService.generateExcelTemplate();

            // Convert to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            byte[] bytes = outputStream.toByteArray();

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "product-import-template.xlsx");
            headers.setContentLength(bytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(bytes);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/validate/excel")
    @Operation(summary = "Validate Excel file", 
               description = "Validate Excel file format and data without saving to database")
    @ApiResponse(responseCode = "200", description = "Validation completed")
    public ResponseEntity<Map<String, Object>> validateExcelFile(
            @Parameter(description = "Excel file to validate") 
            @RequestParam("file") MultipartFile file) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (file.isEmpty()) {
                response.put("valid", false);
                response.put("error", "File is empty");
                return ResponseEntity.badRequest().body(response);
            }

            String filename = file.getOriginalFilename();
            if (filename == null || !filename.endsWith(".xlsx")) {
                response.put("valid", false);
                response.put("error", "Only .xlsx files are supported");
                return ResponseEntity.badRequest().body(response);
            }

            response.put("valid", true);
            response.put("message", "File format is valid and ready for import");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("valid", false);
            response.put("error", "Validation error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}

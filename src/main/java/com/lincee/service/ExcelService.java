package com.lincee.service;

import com.lincee.entity.Product;
import com.lincee.repository.ProductRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ExcelService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Parse Excel file and update products
     * Expected Excel columns: id, name, description, price, discountPrice, category, subCategory, 
     * brand, stockQuantity, imageUrls (comma-separated), availableSizes (comma-separated), 
     * availableColors (comma-separated), tags, active, featured
     */
    public Map<String, Object> importProductsFromExcel(MultipartFile file) throws IOException {
        List<Product> updatedProducts = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        int successCount = 0;
        int errorCount = 0;

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            
            // Skip header row
            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) {
                rowIterator.next(); // Skip header
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                try {
                    Product product = parseRowToProduct(row);
                    if (product != null) {
                        Product savedProduct = productRepository.save(product);
                        updatedProducts.add(savedProduct);
                        successCount++;
                    }
                } catch (Exception e) {
                    errorCount++;
                    errors.add("Row " + (row.getRowNum() + 1) + ": " + e.getMessage());
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("successCount", successCount);
        result.put("errorCount", errorCount);
        result.put("updatedProducts", updatedProducts);
        if (!errors.isEmpty()) {
            result.put("errors", errors);
        }
        result.put("message", String.format("Successfully processed %d products, %d errors", 
            successCount, errorCount));

        return result;
    }

    private Product parseRowToProduct(Row row) {
        Product product = new Product();
        
        // Column 0: Product ID (if exists, update; if not, create new)
        Cell idCell = row.getCell(0);
        if (idCell != null && idCell.getCellType() == CellType.NUMERIC) {
            Long productId = (long) idCell.getNumericCellValue();
            Optional<Product> existingProduct = productRepository.findById(productId);
            if (existingProduct.isPresent()) {
                product = existingProduct.get();
            } else {
                product.setId(productId);
            }
        }

        // Column 1: Name (required)
        String name = getCellValueAsString(row.getCell(1));
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Product name is required");
        }
        product.setName(name);

        // Column 2: Description
        product.setDescription(getCellValueAsString(row.getCell(2)));

        // Column 3: Price (required)
        Double price = getCellValueAsDouble(row.getCell(3));
        if (price == null || price <= 0) {
            throw new RuntimeException("Valid price is required");
        }
        product.setPrice(BigDecimal.valueOf(price));

        // Column 4: Discount Price
        Double discountPrice = getCellValueAsDouble(row.getCell(4));
        if (discountPrice != null && discountPrice > 0) {
            product.setDiscountPrice(BigDecimal.valueOf(discountPrice));
        }

        // Column 5: Category (required)
        String category = getCellValueAsString(row.getCell(5));
        if (category == null || category.trim().isEmpty()) {
            throw new RuntimeException("Category is required");
        }
        product.setCategory(category);

        // Column 6: Sub Category
        product.setSubCategory(getCellValueAsString(row.getCell(6)));

        // Column 7: Brand (required)
        String brand = getCellValueAsString(row.getCell(7));
        if (brand == null || brand.trim().isEmpty()) {
            throw new RuntimeException("Brand is required");
        }
        product.setBrand(brand);

        // Column 8: Stock Quantity (required)
        Double stockQty = getCellValueAsDouble(row.getCell(8));
        if (stockQty == null) {
            stockQty = 0.0;
        }
        product.setStockQuantity(stockQty.intValue());

        // Column 9: Image URLs (comma-separated)
        String imageUrlsStr = getCellValueAsString(row.getCell(9));
        if (imageUrlsStr != null && !imageUrlsStr.trim().isEmpty()) {
            List<String> imageUrls = Arrays.asList(imageUrlsStr.split(","));
            product.setImageUrls(imageUrls.stream().map(String::trim).toList());
        }

        // Column 10: Available Sizes (comma-separated)
        String sizesStr = getCellValueAsString(row.getCell(10));
        if (sizesStr != null && !sizesStr.trim().isEmpty()) {
            List<String> sizes = Arrays.asList(sizesStr.split(","));
            product.setAvailableSizes(sizes.stream().map(String::trim).toList());
        }

        // Column 11: Available Colors (comma-separated)
        String colorsStr = getCellValueAsString(row.getCell(11));
        if (colorsStr != null && !colorsStr.trim().isEmpty()) {
            List<String> colors = Arrays.asList(colorsStr.split(","));
            product.setAvailableColors(colors.stream().map(String::trim).toList());
        }

        // Column 12: Tags
        product.setTags(getCellValueAsString(row.getCell(12)));

        // Column 13: Active (default true)
        Boolean active = getCellValueAsBoolean(row.getCell(13));
        product.setActive(active != null ? active : true);

        // Column 14: Featured (default false)
        Boolean featured = getCellValueAsBoolean(row.getCell(14));
        product.setFeatured(featured != null ? featured : false);

        // Column 15: Weight in grams
        Double weight = getCellValueAsDouble(row.getCell(15));
        if (weight != null) {
            product.setWeightGrams(weight.intValue());
        }

        return product;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return null;
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    private Double getCellValueAsDouble(Cell cell) {
        if (cell == null) return null;
        
        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return cell.getNumericCellValue();
                case STRING:
                    String strValue = cell.getStringCellValue();
                    return strValue.isEmpty() ? null : Double.parseDouble(strValue);
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Boolean getCellValueAsBoolean(Cell cell) {
        if (cell == null) return null;
        
        try {
            switch (cell.getCellType()) {
                case BOOLEAN:
                    return cell.getBooleanCellValue();
                case STRING:
                    String strValue = cell.getStringCellValue().toLowerCase();
                    return strValue.equals("true") || strValue.equals("yes") || strValue.equals("1");
                case NUMERIC:
                    return cell.getNumericCellValue() != 0;
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Generate Excel template for product import
     */
    public Workbook generateExcelTemplate() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Products");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {
            "id", "name", "description", "price", "discountPrice", "category", 
            "subCategory", "brand", "stockQuantity", "imageUrls", "availableSizes", 
            "availableColors", "tags", "active", "featured", "weightGrams"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            
            // Bold header
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }
}

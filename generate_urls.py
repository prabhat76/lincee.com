#!/usr/bin/env python3
"""Generate updated DataInitService with real Cloudinary URLs"""

import json

# Read the uploaded URLs
with open('/Users/prabhatkumar/Documents/lincee-backend/sweatshirt_urls.json', 'r') as f:
    urls = json.load(f)

# Map folder names to product numbers
products_map = {
    "Sweatshirt 1": 1,
    "Sweatshirt 2": 2,
    "Sweatshirt 3": 3,
    "Sweartshirt 4": 4,  # Note the typo in folder name
    "Sweatshirt 5": 5,
    "Sweatshirt 7": 7,
    "Sweatshirt 9": 9,
    "Sweatshirt 10": 10,
    "Sweatshirt 11": 11,
    "Sweatshirt 13": 13,
    "Sweatshirt 14": 14,
    "Sweatshirt 15": 15,
    "Sweatshirt 16": 16,
    "Sweatshirt 17": 17,
    "Swratshirt 18": 18,  # Note the typo
    "Sweatshirt 19": 19,
    "Sweatshirt 20": 20,
    "Sweatshirt 21": 21,
    "Sweatshirt 22": 22,
    "Sweatshirt 26": 26,
    "Sweatshirt 27": 27,
    "Sweatshirt 29": 29,
    "Sweatshirt 31": 31,
    "Sweatshirt 32": 32,
    "Sweatshirt 33": 33,
    "Sweatshirt 34": 34,
}

# Print update commands for each product
print("# Replacements for DataInitService.java")
print("# Copy and paste these into the file\n")

for folder, product_num in sorted(products_map.items(), key=lambda x: x[1]):
    if folder in urls:
        image_urls = urls[folder]
        if len(image_urls) >= 2:
            print(f"// Sweatshirt {product_num}")
            print(f'sweatshirt{product_num}.setImageUrls(Arrays.asList(')
            print(f'    "{image_urls[0]}",')
            print(f'    "{image_urls[1]}"')
            print('));')
            print()

print("\n# Total products:", len(products_map))

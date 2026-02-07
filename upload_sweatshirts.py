#!/usr/bin/env python3
"""
Cloudinary Image Upload Script
Uploads all sweatshirt mockups from Downloads folder to Cloudinary
"""

import os
import sys
import json
from pathlib import Path
import requests

# Cloudinary credentials
CLOUD_NAME = "dt6pfj9bb"
API_KEY = "862764889694815"
API_SECRET = "qT2uTT3L-svC1situfoL2xGcEec"

# Cloudinary upload URL
UPLOAD_URL = f"https://api.cloudinary.com/v1_1/{CLOUD_NAME}/image/upload"

# Source directory
SOURCE_DIR = "/Users/prabhatkumar/Downloads/Sweatshirt Mockups"

# Output file for URLs
OUTPUT_FILE = "/Users/prabhatkumar/Documents/lincee-backend/sweatshirt_urls.json"

def upload_image(file_path, folder_name):
    """Upload single image to Cloudinary"""
    try:
        with open(file_path, 'rb') as file:
            # Prepare upload parameters
            data = {
                'file': file,
                'upload_preset': 'ml_default',
                'folder': f'products/sweatshirts/{folder_name}',
                'transformation': 'w_800,h_800,c_fill,q_auto'
            }
            
            # Upload to Cloudinary
            response = requests.post(
                UPLOAD_URL,
                files={'file': file},
                data={
                    'folder': f'products/sweatshirts/{folder_name}',
                    'transformation': 'w_800,h_800,c_fill,q_auto'
                },
                auth=(API_KEY, API_SECRET)
            )
            
            if response.status_code == 200:
                result = response.json()
                return result['secure_url']
            else:
                print(f"❌ Failed to upload {file_path}: {response.text}")
                return None
                
    except Exception as e:
        print(f"❌ Error uploading {file_path}: {str(e)}")
        return None

def main():
    """Main upload process"""
    print("🚀 Starting Cloudinary Upload Process...")
    print(f"📁 Source: {SOURCE_DIR}")
    print(f"☁️  Cloud: {CLOUD_NAME}")
    print("-" * 60)
    
    uploaded_images = {}
    total_uploaded = 0
    total_failed = 0
    
    # Iterate through all sweatshirt folders
    for folder in sorted(os.listdir(SOURCE_DIR)):
        folder_path = os.path.join(SOURCE_DIR, folder)
        
        # Skip non-directories and hidden files
        if not os.path.isdir(folder_path) or folder.startswith('.'):
            continue
        
        print(f"\n📦 Processing: {folder}")
        folder_images = []
        
        # Get all image files
        files = [f for f in os.listdir(folder_path) if not f.startswith('.') and f.lower().endswith(('.png', '.jpg', '.jpeg'))]
        
        if not files:
            print(f"  ⚠️  No images found")
            continue
        
        # Upload all images (sorted to get front first, back second)
        files.sort()
        for idx, file in enumerate(files[:2]):  # Only first 2 images
            file_path = os.path.join(folder_path, file)
            image_type = "front" if idx == 0 else "back"
            print(f"  ⬆️  Uploading {image_type}: {file}...")
            url = upload_image(file_path, folder)
            if url:
                folder_images.append(url)
                total_uploaded += 1
                print(f"  ✅ {image_type.capitalize()} uploaded: {url}")
            else:
                total_failed += 1
        
        if folder_images:
            uploaded_images[folder] = folder_images
    
    # Save results to JSON file
    with open(OUTPUT_FILE, 'w') as f:
        json.dump(uploaded_images, f, indent=2)
    
    print("\n" + "=" * 60)
    print(f"✅ Upload Complete!")
    print(f"📊 Total Uploaded: {total_uploaded} images")
    print(f"❌ Total Failed: {total_failed} images")
    print(f"💾 URLs saved to: {OUTPUT_FILE}")
    print("=" * 60)

if __name__ == "__main__":
    main()

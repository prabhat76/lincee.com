#!/usr/bin/env python3
"""
Cloudinary Image Upload Script - Hoodies and T-shirts
Uploads all hoodie and t-shirt mockups to Cloudinary
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

# Source directories
HOODIE_DIR = "/Users/prabhatkumar/Downloads/Hoodie"
TSHIRT_DIR = "/Users/prabhatkumar/Downloads/Tshirt (2)"

# Output files
HOODIE_OUTPUT = "/Users/prabhatkumar/Documents/lincee-backend/hoodie_urls.json"
TSHIRT_OUTPUT = "/Users/prabhatkumar/Documents/lincee-backend/tshirt_urls.json"

def upload_image(file_path, folder_name):
    """Upload single image to Cloudinary"""
    try:
        with open(file_path, 'rb') as file:
            response = requests.post(
                UPLOAD_URL,
                files={'file': file},
                data={
                    'folder': folder_name,
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

def process_hoodies():
    """Process all hoodie images"""
    print("\n" + "="*60)
    print("🔥 PROCESSING HOODIES")
    print("="*60)
    
    uploaded_images = {}
    total_uploaded = 0
    total_failed = 0
    
    # Get all hoodie image files (not model images, just front and back)
    files = [f for f in os.listdir(HOODIE_DIR) 
             if f.lower().endswith('.png') 
             and 'model' not in f.lower()
             and not f.startswith('.')]
    
    # Group by hoodie number
    hoodies = {}
    for file in files:
        # Extract hoodie number (1st, 2nd, 10th, etc.)
        parts = file.lower().split()
        if len(parts) >= 2:
            hoodie_num = parts[0]  # "1st", "2nd", etc.
            if hoodie_num not in hoodies:
                hoodies[hoodie_num] = {'front': None, 'back': None}
            
            if 'front' in file.lower():
                hoodies[hoodie_num]['front'] = file
            elif 'back' in file.lower():
                hoodies[hoodie_num]['back'] = file
    
    # Upload each hoodie
    for hoodie_num in sorted(hoodies.keys(), key=lambda x: int(''.join(filter(str.isdigit, x)) or '0')):
        print(f"\n📦 Processing: Hoodie {hoodie_num}")
        hoodie_images = []
        
        # Upload front
        if hoodies[hoodie_num]['front']:
            front_file = hoodies[hoodie_num]['front']
            front_path = os.path.join(HOODIE_DIR, front_file)
            print(f"  ⬆️  Uploading front: {front_file}...")
            front_url = upload_image(front_path, f'products/hoodies/Hoodie_{hoodie_num}')
            if front_url:
                hoodie_images.append(front_url)
                total_uploaded += 1
                print(f"  ✅ Front uploaded")
            else:
                total_failed += 1
        
        # Upload back
        if hoodies[hoodie_num]['back']:
            back_file = hoodies[hoodie_num]['back']
            back_path = os.path.join(HOODIE_DIR, back_file)
            print(f"  ⬆️  Uploading back: {back_file}...")
            back_url = upload_image(back_path, f'products/hoodies/Hoodie_{hoodie_num}')
            if back_url:
                hoodie_images.append(back_url)
                total_uploaded += 1
                print(f"  ✅ Back uploaded")
            else:
                total_failed += 1
        
        if hoodie_images:
            uploaded_images[f"Hoodie {hoodie_num}"] = hoodie_images
    
    # Save results
    with open(HOODIE_OUTPUT, 'w') as f:
        json.dump(uploaded_images, f, indent=2)
    
    print("\n" + "="*60)
    print(f"✅ Hoodie Upload Complete!")
    print(f"📊 Total Uploaded: {total_uploaded} images")
    print(f"❌ Total Failed: {total_failed} images")
    print(f"💾 URLs saved to: {HOODIE_OUTPUT}")
    print("="*60)
    
    return total_uploaded, total_failed

def process_tshirts():
    """Process all t-shirt images"""
    print("\n" + "="*60)
    print("👕 PROCESSING T-SHIRTS")
    print("="*60)
    
    uploaded_images = {}
    total_uploaded = 0
    total_failed = 0
    
    # Get all t-shirt image files (not model images, just front and back)
    files = [f for f in os.listdir(TSHIRT_DIR) 
             if f.lower().endswith('.png') 
             and 'model' not in f.lower()
             and not f.startswith('.')
             and not f.endswith('.zip')]
    
    # Group by t-shirt number
    tshirts = {}
    for file in files:
        # Extract t-shirt number
        parts = file.lower().replace('thsirt', 'tshirt').split()
        if len(parts) >= 2:
            tshirt_num = parts[0]  # "1st", "2nd", etc.
            if tshirt_num not in tshirts:
                tshirts[tshirt_num] = {'front': None, 'back': None}
            
            if 'front' in file.lower():
                tshirts[tshirt_num]['front'] = file
            elif 'back' in file.lower():
                tshirts[tshirt_num]['back'] = file
    
    # Upload each t-shirt
    for tshirt_num in sorted(tshirts.keys(), key=lambda x: int(''.join(filter(str.isdigit, x)) or '0')):
        print(f"\n📦 Processing: T-shirt {tshirt_num}")
        tshirt_images = []
        
        # Upload front
        if tshirts[tshirt_num]['front']:
            front_file = tshirts[tshirt_num]['front']
            front_path = os.path.join(TSHIRT_DIR, front_file)
            print(f"  ⬆️  Uploading front: {front_file}...")
            front_url = upload_image(front_path, f'products/tshirts/Tshirt_{tshirt_num}')
            if front_url:
                tshirt_images.append(front_url)
                total_uploaded += 1
                print(f"  ✅ Front uploaded")
            else:
                total_failed += 1
        
        # Upload back
        if tshirts[tshirt_num]['back']:
            back_file = tshirts[tshirt_num]['back']
            back_path = os.path.join(TSHIRT_DIR, back_file)
            print(f"  ⬆️  Uploading back: {back_file}...")
            back_url = upload_image(back_path, f'products/tshirts/Tshirt_{tshirt_num}')
            if back_url:
                tshirt_images.append(back_url)
                total_uploaded += 1
                print(f"  ✅ Back uploaded")
            else:
                total_failed += 1
        
        if tshirt_images:
            uploaded_images[f"Tshirt {tshirt_num}"] = tshirt_images
    
    # Save results
    with open(TSHIRT_OUTPUT, 'w') as f:
        json.dump(uploaded_images, f, indent=2)
    
    print("\n" + "="*60)
    print(f"✅ T-shirt Upload Complete!")
    print(f"📊 Total Uploaded: {total_uploaded} images")
    print(f"❌ Total Failed: {total_failed} images")
    print(f"💾 URLs saved to: {TSHIRT_OUTPUT}")
    print("="*60)
    
    return total_uploaded, total_failed

def main():
    """Main upload process"""
    print("\n" + "="*60)
    print("🚀 CLOUDINARY BULK UPLOAD")
    print(f"☁️  Cloud: {CLOUD_NAME}")
    print("="*60)
    
    # Process hoodies
    hoodie_up, hoodie_fail = process_hoodies()
    
    # Process t-shirts
    tshirt_up, tshirt_fail = process_tshirts()
    
    # Grand total
    print("\n" + "="*60)
    print("🎉 GRAND TOTAL")
    print("="*60)
    print(f"Hoodies: {hoodie_up} uploaded, {hoodie_fail} failed")
    print(f"T-shirts: {tshirt_up} uploaded, {tshirt_fail} failed")
    print(f"TOTAL: {hoodie_up + tshirt_up} images uploaded")
    print("="*60)

if __name__ == "__main__":
    main()

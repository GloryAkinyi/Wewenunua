# Jetpack Compose MVVM App with Firebase & Cloudinary

This project demonstrates how to build a modern Android application using **Jetpack Compose** and **MVVM architecture**, integrating **Firebase Authentication** for user registration/login and **Firebase Realtime Database** for product CRUD operations.  
Product images are uploaded to **Cloudinary**, and only the image URL is stored in Firebase alongside the product details.

---

## 🚀 Features

- **Authentication**
  - Register new users
  - Login with Firebase Authentication

- **Product Management (CRUD with Firebase Realtime Database)**
  - Create new products (text + image URL)
  - Read (fetch and display product list)
  - Update product details
  - Delete products

- **Image Handling**
  - Select image from device gallery
  - Upload product image to **Cloudinary**
  - Store only the Cloudinary image URL in Firebase with product data

- **Architecture**
  - MVVM (Model–View–ViewModel) with clean separation of concerns
  - Jetpack Compose for UI
  - Repository pattern for Firebase and Cloudinary operations

---

📱 How It Works

User Registration/Login

Users sign up via Firebase Authentication (Email/Password).

Authenticated users can manage products.

Add Product

User selects an image → uploaded to Cloudinary → get URL.

Product details (name, description, price, imageURL) stored in Firebase Realtime Database.

Product List

Fetch products from Firebase Realtime Database.

Display them in a Compose LazyColumn.

Update Product

Edit product text or image.

If image is changed → upload new one to Cloudinary and update URL in Firebase.

Delete Product

Remove product entry from Firebase.

📸 Screens (Planned)

Login Screen

Register Screen

Product List Screen

Add Product Screen

Edit Product Screen

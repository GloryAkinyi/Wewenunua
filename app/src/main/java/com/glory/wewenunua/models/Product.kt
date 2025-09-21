package com.glory.wewenunua.models



// --- Model ---
data class Product(
    var id: String? = null,           // Unique product ID
    val name: String? = null,         // Product name
    val category: String? = null,     // Category (e.g., electronics, clothing)
    val brand: String? = null,        // Brand/manufacturer
    val price: String? = null,        // Product price (kept as String for Firebase consistency)
    val description: String? = null,  // Short description/details
    val stock: String? = null,        // ✅ Stock quantity
    val imageUrl: String? = null      // Cloudinary image URL
)


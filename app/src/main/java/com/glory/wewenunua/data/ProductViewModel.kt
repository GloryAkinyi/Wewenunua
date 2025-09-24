package com.glory.wewenunua.data

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.glory.wewenunua.models.Product
import com.glory.wewenunua.navigation.ROUT_VIEW_PRODUCTS
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.InputStream

class ProductViewModel : ViewModel() {

    private val cloudinaryUrl = "https://api.cloudinary.com/v1_1/djj5gylbp/image/upload"
    private val uploadPreset = "wewenunua"

    /** ---------------- UPLOAD PRODUCT ---------------- */
    fun uploadProduct(
        imageUri: Uri?,
        name: String,
        category: String,
        price: String,
        description: String,
        stock: String,
        phoneNumber: String,   // ✅ Added phone number
        context: Context,
        navController: NavController
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imageUrl = imageUri?.let { uploadToCloudinary(context, it) }
                val ref = FirebaseDatabase.getInstance().getReference("Products").push()

                val productData = mapOf(
                    "id" to ref.key,
                    "name" to name,
                    "category" to category,
                    "price" to price,
                    "description" to description,
                    "stock" to stock,
                    "phoneNumber" to phoneNumber,   // ✅ Save phone number
                    "imageUrl" to imageUrl
                )

                ref.setValue(productData).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Product saved successfully", Toast.LENGTH_LONG).show()
                    navController.navigate(ROUT_VIEW_PRODUCTS)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Product not saved", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /** ---------------- CLOUDINARY UPLOAD ---------------- */
    private fun uploadToCloudinary(context: Context, uri: Uri): String {
        val contentResolver = context.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val fileBytes = inputStream?.readBytes() ?: throw Exception("Image read failed")

        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(
                "file", "image.jpg",
                RequestBody.create("image/*".toMediaTypeOrNull(), fileBytes)
            )
            .addFormDataPart("upload_preset", uploadPreset)
            .build()

        val request = Request.Builder().url(cloudinaryUrl).post(requestBody).build()
        val response = OkHttpClient().newCall(request).execute()

        if (!response.isSuccessful) throw Exception("Upload failed")
        val responseBody = response.body?.string()

        val secureUrl = Regex("\"secure_url\":\"(.*?)\"")
            .find(responseBody ?: "")?.groupValues?.get(1)

        return secureUrl ?: throw Exception("Failed to get image URL")
    }

    /** ---------------- FETCH PRODUCTS ---------------- */
    private val _products = mutableStateListOf<Product>()
    val products: List<Product> = _products

    fun fetchProducts(context: Context) {
        val ref = FirebaseDatabase.getInstance().getReference("Products")
        ref.get().addOnSuccessListener { snapshot ->
            _products.clear()
            for (child in snapshot.children) {
                val product = child.getValue(Product::class.java)
                product?.let {
                    it.id = child.key
                    _products.add(it)
                }
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to load products", Toast.LENGTH_LONG).show()
        }
    }

    /** ---------------- DELETE PRODUCT ---------------- */
    fun deleteProduct(productId: String, context: Context) {
        val ref = FirebaseDatabase.getInstance()
            .getReference("Products").child(productId)

        ref.removeValue().addOnSuccessListener {
            _products.removeAll { it.id == productId }
        }.addOnFailureListener {
            Toast.makeText(context, "Product not deleted", Toast.LENGTH_LONG).show()
        }
    }

    /** ---------------- UPDATE PRODUCT ---------------- */
    fun updateProduct(
        productId: String,
        imageUri: Uri?,
        name: String,
        category: String,
        price: String,
        description: String,
        stock: String,
        phoneNumber: String,   // ✅ Added phone number
        context: Context,
        navController: NavController
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imageUrl = imageUri?.let { uploadToCloudinary(context, it) }
                val updatedProduct = mapOf(
                    "id" to productId,
                    "name" to name,
                    "category" to category,
                    "price" to price,
                    "description" to description,
                    "stock" to stock,
                    "phoneNumber" to phoneNumber,   // ✅ Update phone number
                    "imageUrl" to imageUrl
                )

                val ref = FirebaseDatabase.getInstance()
                    .getReference("Products").child(productId)

                ref.setValue(updatedProduct).await()
                fetchProducts(context)

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Product updated successfully", Toast.LENGTH_LONG).show()
                    navController.navigate(ROUT_VIEW_PRODUCTS)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Update failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

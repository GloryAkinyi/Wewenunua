package com.glory.wewenunua.ui.screens.products

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.glory.wewenunua.data.ProductViewModel
import com.glory.wewenunua.models.Product
import com.glory.wewenunua.navigation.ROUT_ADD_PRODUCT
import com.glory.wewenunua.navigation.ROUT_VIEW_PRODUCTS
import com.glory.wewenunua.ui.theme.newYellow
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProductScreen(navController: NavController, productId: String) {
    val productViewModel: ProductViewModel = viewModel()

    // Product state from Firebase
    var product by remember { mutableStateOf<Product?>(null) }

    // Fetch a single product from Firebase by its ID (READ operation)
    LaunchedEffect(productId) {
        val ref = FirebaseDatabase.getInstance()
            .getReference("Products").child(productId) // navigate into product node
        val snapshot = ref.get().await() // suspend until data is fetched
        product = snapshot.getValue(Product::class.java)?.apply {
            id = productId // restore the product ID
        }
    }

    // Show loader while waiting for Firebase response
    if (product == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = newYellow)
        }
        return
    }

    // State variables pre-filled with existing product data
    var name by remember { mutableStateOf(product!!.name ?: "") }
    var category by remember { mutableStateOf(product!!.category ?: "") }
    var price by remember { mutableStateOf(product!!.price ?: "") }
    var description by remember { mutableStateOf(product!!.description ?: "") }
    var stock by remember { mutableStateOf(product!!.stock ?: "") }
    var phoneNumber by remember { mutableStateOf(product!!.phoneNumber ?: "") }

    // 🔹 Handle image picking (only replaces image if user selects a new one)
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { uri -> imageUri.value = uri }
    }

    val context = LocalContext.current

    Scaffold(
        // 🔹 Top AppBar
        topBar = {
            TopAppBar(
                title = { Text("Update Product", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { /* future options */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = newYellow,
                    titleContentColor = Color.White
                )
            )
        },

        // 🔹 Bottom navigation
        bottomBar = {
            NavigationBar(containerColor = newYellow) {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(ROUT_VIEW_PRODUCTS) }, // Go back to product list
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(ROUT_ADD_PRODUCT) }, // Add a new product
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                    label = { Text("Add") }
                )
            }
        },

        // 🔹 Main Content
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Product Image (shows existing image or new one if selected)
                Card(
                    modifier = Modifier
                        .size(160.dp)
                        .clickable { launcher.launch("image/*") }, // pick new image
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    AnimatedContent(
                        targetState = imageUri.value,
                        label = "Image Picker Animation"
                    ) {
                        AsyncImage(
                            model = imageUri.value ?: product!!.imageUrl, // show new or old
                            contentDescription = "Product Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Text(
                    text = "Tap image to update",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 8.dp, bottom = 20.dp)
                )

                //Input Fields for updating product info
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Product Name") },
                            placeholder = { Text("e.g., Samsung Galaxy S22") },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = category,
                            onValueChange = { category = it },
                            label = { Text("Category") },
                            placeholder = { Text("e.g., Electronics") },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = price,
                            onValueChange = { price = it },
                            label = { Text("Price") },
                            placeholder = { Text("e.g., 1200") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = stock,
                            onValueChange = { stock = it },
                            label = { Text("Stock Quantity") },
                            placeholder = { Text("e.g., 50") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = { Text("Phone Number") },
                            placeholder = { Text("e.g., +254712345678") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description") },
                            placeholder = { Text("Brief product description") },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).height(100.dp),
                            shape = RoundedCornerShape(12.dp),
                            maxLines = 4
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                //Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Cancel button → just go back
                    Button(
                        onClick = { navController.popBackStack() },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                        modifier = Modifier.width(140.dp)
                    ) {
                        Text("Cancel", color = Color.DarkGray)
                    }

                    // Update button → calls ViewModel (UPDATE operation)
                    Button(
                        onClick = {
                            productViewModel.updateProduct(
                                productId,
                                imageUri.value, // only new image if picked
                                name,
                                category,
                                price,
                                description,
                                stock,
                                phoneNumber,
                                context,
                                navController
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                        modifier = Modifier.width(140.dp)
                    ) {
                        Text("Update", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    )
}

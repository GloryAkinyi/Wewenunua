package com.glory.wewenunua.ui.screens.products

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.glory.wewenunua.data.ProductViewModel
import com.glory.wewenunua.models.Product
import com.glory.wewenunua.navigation.ROUT_ADD_PRODUCT
import com.glory.wewenunua.navigation.ROUT_UPDATE_PRODUCT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(navController: NavController) {
    val productViewModel: ProductViewModel = viewModel()
    val products = productViewModel.products // Live list of products from ViewModel
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    // READ: Fetch products from Firebase when screen opens
    LaunchedEffect(Unit) {
        productViewModel.fetchProducts(context)
    }

    Scaffold(
        topBar = {
            Column {
                // Top bar with app title and menu
                TopAppBar(
                    title = { Text("Products") },
                    actions = {
                        IconButton(onClick = { /* TODO: More menu */ }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFFFC107),
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )

                //  Search Bar for filtering products
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder = { Text("Search products...") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFFC107),
                        unfocusedBorderColor = Color.Gray
                    )
                )
            }
        },
        bottomBar = {
            //  CREATE: Navigate to Add Product screen
            NavigationBar(containerColor = Color(0xFFFFC107)) {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(ROUT_ADD_PRODUCT) }, // ✅ CREATE
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                    label = { Text("Add") }
                )
            }
        }
    ) { paddingValues ->


        //  Filtered products based on search query
        val filteredProducts = products.filter { product ->
            product.name?.contains(searchQuery, ignoreCase = true) == true
        }

        // READ: Display products in grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredProducts) { product ->
                ProductCard(
                    product = product,
                    //  DELETE: Calls ViewModel to remove product
                    onDelete = { productId -> productViewModel.deleteProduct(productId, context) },
                    navController = navController
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(
    product: Product,
    onDelete: (String) -> Unit,
    navController: NavController
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    //  Confirmation dialog for DELETE
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            containerColor = Color.White,
            tonalElevation = 4.dp,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Delete Product?", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Are you sure you want to delete ${product.name}?")
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(onClick = { showBottomSheet = false }) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            showBottomSheet = false
                            product.id?.let { onDelete(it) } //  DELETE
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Delete")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Card showing product details
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //  Product Image
            product.imageUrl?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            //  Product name & price
            Text(product.name ?: "No Name", style = MaterialTheme.typography.titleMedium)
            Text("Price: Ksh${product.price ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))

            // UPDATE & DELETE icons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //  UPDATE: Navigate to Update screen
                IconButton(onClick = {
                    navController.navigate("update_product/${product.id}") //  UPDATE
                }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFFFFC107))
                }
                //  DELETE: Open delete confirmation sheet
                IconButton(onClick = { showBottomSheet = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }

            //  Extra feature: Message seller via SMS
            val context = LocalContext.current
            Button(
                onClick = {
                    val smsIntent = Intent(Intent.ACTION_SENDTO)
                    smsIntent.data = "smsto:${product.phoneNumber}".toUri()
                    smsIntent.putExtra("sms_body", "Hello Seller,...?")
                    context.startActivity(smsIntent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
            ) {
                Text("Message Seller")
            }
        }
    }
}

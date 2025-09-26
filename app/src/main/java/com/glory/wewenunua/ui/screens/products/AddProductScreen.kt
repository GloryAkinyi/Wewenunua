package com.glory.wewenunua.ui.screens.products

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.glory.wewenunua.R
import com.glory.wewenunua.data.ProductViewModel
import com.glory.wewenunua.navigation.ROUT_VIEW_PRODUCTS
import com.glory.wewenunua.ui.theme.newYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(navController: NavController) {


    // ----------------------------- STATE VARIABLES -----------------------------
    // These variables hold user input for creating a new product
    var name by remember { mutableStateOf("") }            // Product name
    var category by remember { mutableStateOf("") }        // Product category
    var price by remember { mutableStateOf("") }           // Product price
    var description by remember { mutableStateOf("") }     // Product description
    var stock by remember { mutableStateOf("") }           // Stock available
    var phoneNumber by remember { mutableStateOf("") }     // Seller phone number

    // Holds the selected image Uri (using rememberSaveable so it survives rotation)
    val imageUri = rememberSaveable { mutableStateOf<Uri?>(null) }



    // Launcher to open gallery and pick an image
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imageUri.value = it } // Save selected image URI
    }


    // ViewModel where CRUD logic lives (Firebase + Cloudinary handling)
    val productViewModel: ProductViewModel = viewModel()
    val context = LocalContext.current

    // For bottom navigation bar
    var selectedIndex by remember { mutableStateOf(0) }

    // ----------------------------- UI SCAFFOLD -----------------------------
    Scaffold(
        // ----------------------------- TOP BAR -----------------------------
        topBar = {
            TopAppBar(
                title = { Text("Add New Product", color = Color.White) }, // Screen title
                navigationIcon = { // Back button to return to previous screen
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = { // Placeholder for extra menu actions
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = newYellow,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },

        // ----------------------------- BOTTOM NAVIGATION -----------------------------
        bottomBar = {
            NavigationBar(containerColor = newYellow) {
                // Home button
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedIndex == 0,
                    onClick = { selectedIndex = 0 }
                )
                // View products button
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Info, contentDescription = "View") },
                    label = { Text("View") },
                    selected = selectedIndex == 1,
                    onClick = { selectedIndex = 1 }
                )
            }
        },

        // ----------------------------- FLOATING ACTION BUTTON -----------------------------
        floatingActionButton = {
            FloatingActionButton(
                onClick = { launcher.launch("image/*") }, // Opens gallery
                containerColor = newYellow
            ) {
                Icon(Icons.Default.Add, contentDescription = "Pick Image")
            }
        },

        // ----------------------------- MAIN CONTENT -----------------------------
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()), // Allow scrolling
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // ----------------------------- IMAGE PICKER -----------------------------
                Card(
                    modifier = Modifier
                        .size(160.dp)
                        .clickable { launcher.launch("image/*") }, // Open gallery when tapped
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    AnimatedContent(
                        targetState = imageUri.value,
                        label = "Image Picker Animation"
                    ) { targetUri ->
                        // Show selected image, or placeholder if none
                        AsyncImage(
                            model = targetUri ?: R.drawable.ic_launcher_foreground,
                            contentDescription = "Product Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Text(
                    text = "Choose an image to upload",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 8.dp, bottom = 20.dp)
                )

                // ----------------------------- PRODUCT FORM -----------------------------
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        // Product Name input
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Product Name") },
                            placeholder = { Text("e.g., Samsung Galaxy S22") },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = {
                                Icon(painter = painterResource(id = R.drawable.name), contentDescription = "")
                            }
                        )

                        // Category dropdown
                        var expanded by remember { mutableStateOf(false) }
                        val options = listOf("Electronics", "Fashion", "Books", "Home", "Toys")
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                        ) {
                            OutlinedTextField(
                                value = category,
                                onValueChange = { },
                                label = { Text("Category") },
                                placeholder = { Text("Select category") },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true, // User must pick from dropdown
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Dropdown arrow",
                                        Modifier.clickable { expanded = !expanded }
                                    )
                                },
                                shape = RoundedCornerShape(12.dp)
                            )

                            // Dropdown list
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                options.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            category = option // Save chosen category
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Price input
                        OutlinedTextField(
                            value = price,
                            onValueChange = { price = it },
                            label = { Text("Price") },
                            placeholder = { Text("e.g., 1200") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = {
                                Icon(painter = painterResource(id = R.drawable.price), contentDescription = "")
                            }
                        )

                        // Stock input
                        OutlinedTextField(
                            value = stock,
                            onValueChange = { stock = it },
                            label = { Text("Stock Quantity") },
                            placeholder = { Text("e.g., 50") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Phone Number input
                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = { Text("Phone Number") },
                            placeholder = { Text("e.g., +254712345678") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = {
                                Icon(painter = painterResource(id = R.drawable.phone), contentDescription = "")
                            }
                        )

                        // Description input
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

                // ----------------------------- ACTION BUTTONS -----------------------------
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Cancel button -> goes back without saving
                    Button(
                        onClick = { navController.popBackStack() },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                        modifier = Modifier.width(140.dp)
                    ) {
                        Text("Cancel", color = Color.DarkGray)
                    }

                    // Save button -> CREATE operation
                    Button(
                        onClick = {
                            productViewModel.uploadProduct(
                                imageUri.value,  // Upload image to Cloudinary
                                name,            // Save product details to Firebase
                                category,
                                price,
                                description,
                                stock,
                                phoneNumber,     // Save seller contact
                                context,
                                navController    // Navigate after save
                            )
                            navController.navigate(ROUT_VIEW_PRODUCTS) //Navigate to view after uploading
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor =newYellow),
                        modifier = Modifier.width(140.dp)
                    ) {
                        Text("Save", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddProductScreenPreview() {
    AddProductScreen(rememberNavController())
}

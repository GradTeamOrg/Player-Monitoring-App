package com.example.playermonitoringapp.ui.theme.screens

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.playermonitoringapp.R
import com.example.playermonitoringapp.navigation.AppRoutes
import com.example.playermonitoringapp.ui.theme.FotGreen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private lateinit var auth: FirebaseAuth

@Composable
fun SigninScreen(navController: NavController) {
    var email by remember { mutableStateOf("") } // Changed from username to email
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    auth = Firebase.auth

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Logo
            Image(
                painter = painterResource(id = R.drawable.player_1), // Replace with your logo resource
                contentDescription = "Logo", modifier = Modifier.size(400.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Login Title
            Text(
                text = "Sign In",
                color = Color.White,
                style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Please sign in to continue",
                color = Color.Gray,
                style = TextStyle(fontSize = 14.sp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(
                    "Email",
                    color = Color.White
                ) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_user), // Replace with your icon resource
                        contentDescription = "Email Icon",
                        tint = FotGreen
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedPlaceholderColor = FotGreen,
                    focusedContainerColor = Color.DarkGray,
                    unfocusedContainerColor = Color.DarkGray,
                    focusedBorderColor = FotGreen,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = FotGreen,
                ),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Password Input
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(
                    "Password" ,
                    color = Color.White
                ) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_password), // Replace with your icon resource
                        contentDescription = "Password Icon",
                        tint = FotGreen

                    )
                },
                trailingIcon = {
                    val icon = if (passwordVisible) R.drawable.view else R.drawable.close_eye
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = "Toggle Password Visibility",
                            tint = FotGreen
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedPlaceholderColor = FotGreen,
                    focusedContainerColor = Color.DarkGray,
                    unfocusedContainerColor = Color.DarkGray,
                    focusedBorderColor = FotGreen,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = FotGreen,
                ),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                ),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Forgot Password
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Forgot Password?",
                    color = FotGreen,
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
                    modifier = Modifier.clickable {
                        // Navigate to forgot password screen (if you have one)
                    }
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Login Button
            Button(
                onClick = {
                    coroutineScope.launch {
                        try {
                            // Sign in with email and password
                            val authResult = auth.signInWithEmailAndPassword(email, password).await()
                            val user = authResult.user

                            // Check if email is verified
                            if (user != null && user.isEmailVerified) {
                                // Navigate to home screen
                                navController.navigate(AppRoutes.HOME_ROUTE)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Please verify your email first.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            Log.w(TAG, "Sign-in error: ${e.message}")
                            Toast.makeText(
                                context,
                                "Sign-in failed: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = FotGreen,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 7.dp
                )
            ) {
                Text(
                    text = "Login",
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Sign Up Link
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don’t have an account?",
                    color = Color.Gray,
                    style = TextStyle(fontSize = 14.sp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Sign Up",
                    color = FotGreen,
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
                    modifier = Modifier.clickable {
                        navController.navigate(AppRoutes.SIGNUP_ROUTE)
                    }
                )
            }
        }
    }
}
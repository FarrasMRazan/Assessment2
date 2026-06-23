package com.farrasmuhammadrazan0100.assement2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.farrasmuhammadrazan0100.assement2.navigation.SetupNavGraph
import com.farrasmuhammadrazan0100.assement2.ui.theme.Assement2Theme
import com.farrasmuhammadrazan0100.assement2.util.SettingsDataStore
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataStore = SettingsDataStore(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            val isDarkMode by dataStore.darkModeFlow.collectAsState(initial = false)

            var userId by remember { mutableStateOf("") }

            LaunchedEffect(Unit) {
                val account = GoogleSignIn.getLastSignedInAccount(this@MainActivity)
                if (account != null) {
                    userId = account.email ?: ""
                }
            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    userId = account.email ?: ""
                } catch (e: ApiException) {
                    Toast.makeText(
                        this@MainActivity,
                        "Login gagal: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            Assement2Theme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupNavGraph(
                        userId = userId,
                        onSignIn = {
                            launcher.launch(googleSignInClient.signInIntent)
                        },
                        onSignOut = {
                            googleSignInClient.signOut().addOnCompleteListener {
                                userId = ""
                            }
                        }
                    )
                }
            }
        }
    }
}
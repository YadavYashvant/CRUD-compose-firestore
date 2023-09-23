package com.example.crud_compose_firestore

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.crud_compose_firestore.presentation.models.User
import com.example.crud_compose_firestore.ui.theme.CRUDcomposefirestoreTheme
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CRUDcomposefirestoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            Text(text = "CRUD Compose Firestore")
                        }
                    ) {
                        firebaseUI(LocalContext.current)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun firebaseUI(context: android.content.Context) {

    val name = remember{
        mutableStateOf("")
    }

    val branch = remember{
        mutableStateOf("")
    }

    val skill = remember{
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .fillMaxWidth()
            .background(Color.White)
    ) {
        TextField(
            value = name.value,
            onValueChange = { name.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Enter your name: ") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = branch.value,
            onValueChange = { branch.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Enter your branch: ") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = skill.value,
            onValueChange = { skill.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Enter your skill: ") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                val user = User(name.value, branch.value, skill.value)
                addToFirebase(name.value,
                    branch.value,
                    skill.value,
                    context)
            }
        ) {
            Text(text = "Add User")
        }
    }


}

fun addToFirebase(
    name: String,
    branch: String,
    skill: String,
    context: Context
) {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    val dbUser: CollectionReference = db.collection("users")
    val users = User(name, branch, skill)

        dbUser.add(users)
        .addOnSuccessListener { documentReference ->
            android.widget.Toast.makeText(context, "User added successfully", android.widget.Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            android.widget.Toast.makeText(context, "Error adding user", android.widget.Toast.LENGTH_SHORT).show()
        }
}

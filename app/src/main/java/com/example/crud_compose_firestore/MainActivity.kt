package com.example.crud_compose_firestore

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.crud_compose_firestore.presentation.models.User
import com.example.crud_compose_firestore.ui.theme.CRUDcomposefirestoreTheme
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

var userList = mutableListOf<User?>()

val spacefamily = FontFamily(
    Font(R.font.spaceregular, FontWeight.Normal),
    Font(R.font.spacebold, FontWeight.Bold),
)

class MainActivity : ComponentActivity() {

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
        CRUDcomposefirestoreTheme {

            /*val context = LocalContext.current
            val view = LocalView.current

            // Set the status bar color to transparent
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, view.rootView).run {
                isAppearanceLightStatusBars = true
                systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                hide(WindowInsetsCompat.Type.statusBars())*/

                Surface(

                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()

                    Scaffold(
                    ) {

                        NavHost(navController = navController, startDestination = "home") {
                            composable("home") {
                                firebaseUI(
                                    context = LocalContext.current,
                                    navController = navController
                                )
                            }

                            readFromFirebase()

                            composable("read") {

                                ReadScreen(navController = navController)

                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun firebaseUI(context:Context, navController: NavController? = null) {

    val name = remember {
        mutableStateOf("")
    }

    val branch = remember {
        mutableStateOf("")
    }

    val skill = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 32.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TextField(
            value = name.value,
            shape = MaterialTheme.shapes.large,
            onValueChange = { name.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .height(70.dp),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            label = { Text("Enter your name ", fontFamily = spacefamily) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = branch.value,
            onValueChange = { branch.value = it },
            shape = MaterialTheme.shapes.large,
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            label = { Text("Enter your branch ", fontFamily = spacefamily) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = skill.value,
            onValueChange = { skill.value = it },
            shape = MaterialTheme.shapes.large,
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            label = { Text("Enter your skills ", fontFamily = spacefamily) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                addToFirebase(
                    name.value,
                    branch.value,
                    skill.value,
                    context
                )
                readFromFirebase()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp, horizontal = 32.dp)
                .height(70.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(
                text = "Add User",
                fontSize = 18.sp,
                fontFamily = spacefamily,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                navController?.navigate("read")
            }, modifier = Modifier
                /*.fillMaxWidth()*/
                /*.padding(vertical = 8.dp, horizontal = 60.dp)*/
                .height(50.dp)
                .width(200.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "See Database",
                fontSize = 18.sp,
                fontFamily = spacefamily,
                fontWeight = FontWeight.Bold
            )
        }



        /*val openDialog = remember { mutableStateOf(false) }

        OutlinedButton(

            onClick = {
                      Dialog()
                *//*deleteFromFirebase(
                userList[index]?.toString()!!,
            )
            userList.removeAt(index)*//*
            },
            modifier = Modifier.wrapContentSize().align(Alignment.CenterHorizontally),

            )

        {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "delete",
                tint = Color.Red
            )
        }*/

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 0.dp, start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            /*.background(Color.White)*/
    ) {
        Button(
            onClick = {
                navController.navigate("home")
            },
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Start),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack, contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .padding(0.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            itemsIndexed(userList) { index, item ->

                OutlinedCard(
                    onClick = { /*TODO*/ },
                    elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                ) {

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .wrapContentHeight()
                    ) {
                        Spacer(modifier = Modifier.width(8.dp))

                        userList[index]?.name?.let {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                                    .align(Alignment.CenterHorizontally),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = it,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp),
                                    fontFamily = spacefamily,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        userList[index]?.branch?.let {
                            Text(
                                text = it,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                                    .align(Alignment.CenterHorizontally),
                                fontFamily = spacefamily,
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        userList[index]?.skill?.let {
                            Text(
                                text = it,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                                    .align(Alignment.CenterHorizontally),
                                fontFamily = spacefamily,
                            )
                        }
                        com.example.crud_compose_firestore.presentation.ui.Dialog()
                    }

                }
            }
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
            android.widget.Toast.makeText(
                context,
                "User added successfully",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
        .addOnFailureListener { e ->
            android.widget.Toast.makeText(
                context,
                "Error adding user",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }

}

fun readFromFirebase() {

    var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    db.collection("users")
        .get()
        .addOnSuccessListener { result ->

            val list = result.documents
            for (d in list) {
                val u = d.toObject(User::class.java)
                userList.add(u)
            }
        }
        .addOnFailureListener { exception ->
            android.util.Log.w("TAG", "Error getting documents.", exception)
        }
}

fun deleteFromFirebase(deletedoc:String) {
     var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    db.collection("users")
        .document(deletedoc)
        .delete().addOnSuccessListener { documentReference ->
            android.util.Log.d("TAG", "DocumentSnapshot successfully deleted!")
        }
        .addOnFailureListener { e ->
            android.util.Log.w("TAG", "Error deleting document", e)
        }
}



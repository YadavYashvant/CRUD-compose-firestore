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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()

                    Scaffold(

                    ) {

                        NavHost(navController = navController, startDestination = "home") {
                            composable("home") {
                                firebaseUI(context = LocalContext.current, navController = navController)
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
fun firebaseUI(context: android.content.Context, navController: NavController? = null) {

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
                .height(70.dp),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            label = { Text("Enter your skill ", fontFamily = spacefamily) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                /*val user = User(name.value, branch.value, skill.value)*/
                addToFirebase(name.value,
                    branch.value,
                    skill.value,
                    context
                )
                readFromFirebase()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp, horizontal = 32.dp)
                .height(70.dp)
        ) {
            Text(text = "Add User", fontSize = 18.sp, fontFamily = spacefamily, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(onClick = {
            navController?.navigate("read")
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 48.dp)
            .height(60.dp)
        ) {
            Text(text = "See Database", fontSize = 18.sp, fontFamily = spacefamily, fontWeight = FontWeight.Bold)
        }

        }

        /*Text(text = "Reading from Firebase",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
        )*/
    }



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 5.dp, horizontal = 25.dp)
            .fillMaxWidth()
            .background(Color.White)
    ) {
        IconButton(onClick = {
            navController.navigate("home")
        },
            modifier = Modifier.wrapContentSize().align(Alignment.Start)
        ) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier
                    .padding(0.dp)
                    .align(Alignment.Start)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ){
            itemsIndexed(userList){ index, item ->

                Card(onClick = { /*TODO*/ },
                    /*elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    ),*/
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                ) {

                    val rememberScrollState = rememberScrollState()

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .wrapContentHeight()
                    ) {
                        Spacer(modifier = Modifier.width(8.dp))

                        userList[index]?.name?.let {
                            Text(text = it,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                                    .align(Alignment.CenterHorizontally),
                                fontFamily = spacefamily,
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        userList[index]?.branch?.let {
                            Text(text = it,
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
                            Text(text = it,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                                    .align(Alignment.CenterHorizontally),
                                fontFamily = spacefamily,
                            )
                        }
                    }

                }
            }
        }

        /*for (user in userList) {
            Text(text = "Name: ${user?.name}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
            )
            Text(text = "Branch: ${user?.branch}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
            )
            Text(text = "Skill: ${user?.skill}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 32.dp),
            )
        }*/

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
            for(d in list){
                val u = d.toObject(User::class.java)
                userList.add(u)
            }

            /*for (document in result) {
                android.util.Log.d("TAG", "${document.id} => ${document.data}")
            }*/
        }
        .addOnFailureListener { exception ->
            android.util.Log.w("TAG", "Error getting documents.", exception)
        }
}
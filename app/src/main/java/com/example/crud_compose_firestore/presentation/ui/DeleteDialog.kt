package com.example.crud_compose_firestore.presentation.ui


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.crud_compose_firestore.R
import com.example.crud_compose_firestore.deleteFromFirebase
import com.example.crud_compose_firestore.presentation.models.User
import com.example.crud_compose_firestore.readFromFirebase


val spacefamily = FontFamily(
    Font(R.font.spaceregular, FontWeight.Normal),
    Font(R.font.spacebold, FontWeight.Bold),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dialog(index: Int, userList: MutableList<User?>, context: Context/*, deletedoc: String*/) {

    val openDialog = remember { mutableStateOf(false) }

    IconButton(onClick = {
        openDialog.value = true
    },
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(8.dp)
    ) {
        Icon(imageVector = Icons.Outlined.Delete, contentDescription = "exiting app")
    }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {

                openDialog.value = false

            },
            title = {
                Text(text = "Delete This Item", fontWeight = FontWeight.Bold, fontFamily = spacefamily)
            },
            text = {
                Text(text = "This is a sample text block being shown inside the dialog box to show how it looks like.Lets see how it looks like.My name is Yashvant Yadav and I am a student of Computer Science.",
                    fontFamily = spacefamily,
                    fontWeight = FontWeight.Normal,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        userList.removeAt(index)
                        openDialog.value = false
                        Toast.makeText(context, "Card successfully deleted", Toast.LENGTH_SHORT).show()
                        /*deleteFromFirebase(deletedoc)*/
                    },
                    Modifier.height(60.dp),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    },
                    Modifier.height(60.dp),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Text("Dismiss")
                }
            }
        )
    }

    //Below code is an implementation of similar dialog box:)(had some errors)
    /*
    val openDialog = remember{mutableStateOf(true)}

    if (openDialog.value) {
       AlertDialog(onDismissRequest = {

                                      openDialog.value = false

       }, confirmButton = {// })
           Surface(
               modifier = Modifier
                   .wrapContentWidth()
                   .wrapContentHeight(),
               shape = MaterialTheme.shapes.large,
               tonalElevation = AlertDialogDefaults.TonalElevation
           ) {
               Column(modifier = Modifier.padding(16.dp)) {
                   Text(text = "Just enjoying the extensive help Material3 is doing for developers!")
                   Spacer(modifier = Modifier.height(24.dp))
                   TextButton(onClick = {
                       openDialog.value = false
                   },
                       modifier = Modifier.align(Alignment.End)
                       ) {
                       Text(text = "Confirm")
                   }
               }
           }
       }

     */

}
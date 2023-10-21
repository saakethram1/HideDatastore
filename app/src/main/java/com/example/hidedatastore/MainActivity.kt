package com.example.hidedatastore

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.dataStore
import com.example.hidedatastore.ui.theme.CryptoManager
import com.example.hidedatastore.ui.theme.HideDatastoreTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

private val Context.datastore by dataStore(
    fileName="user-settings.json",
    serializer= UserSettingsSerializer(CryptoManager())
)


    @OptIn(ExperimentalMaterial3Api::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HideDatastoreTheme {
               var username by remember {
                   mutableStateOf("")
               }
                var password by remember{
                    mutableStateOf("")
                }
                var settings by remember {
                    mutableStateOf(UserSettings())
                }
                val scope= rememberCoroutineScope()
                Column(modifier= Modifier
                    .fillMaxSize()
                    .padding(32.dp)) {
                      TextField(value = username, onValueChange = {username=it}, placeholder = { Text(
                          text = "Username"
                      )})
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(modifier= Modifier
                        .fillMaxSize()
                        .padding(32.dp)) {
                        TextField(value = password, onValueChange = {password=it}, placeholder = { Text(
                            text = "Password"
                        )})
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Button(onClick =
                            { scope.launch {
                                datastore.updateData {
                                    UserSettings(
                                        username = username,
                                        password = password
                                    )
                                }
                            }
                            }
                            ) {
                                Text(text = "Save")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Button(onClick = {scope.launch {
                                settings=datastore.data.first()
                            }}) {
                                Text(text = "Load")
                            }
                            Text(text = settings.toString())
                        }

                }
            }
        }
    }}}



package com.example.burgers.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.burgers.domain.model.AssetsBurger
import com.example.burgers.domain.model.RemoteBurger
import com.example.burgers.ui.theme.AndroidTaskTheme
import com.example.burgers.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllBurgersActivity : ComponentActivity() {
    private val viewModel: AllBurgersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AndroidTaskTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = Color.Gray) {
                    val burgers = viewModel.state.collectAsState().value
                    if (burgers == null) {
                        Text(text = "Loading")
                        return@Surface
                    }

                    if (burgers.isEmpty()) {
                        Text(text = "No burgers")
                        return@Surface
                    }
                    BurgersList(burgers = burgers)
                }
            }
        }
    }
}

@Composable
fun BurgersList(burgers: List<Any>) {
    val context = LocalContext.current
    LazyColumn(contentPadding = PaddingValues(8.dp)) {
        items(burgers) { burger ->
            val name = if(burger is AssetsBurger) burger.name else if(burger is RemoteBurger) burger.name else ""
            val restaurant = if(burger is AssetsBurger) burger.restaurant else if(burger is RemoteBurger) "NoRestaurant" else ""
            val link = if(burger is AssetsBurger) burger.link else if(burger is RemoteBurger) burger.link else ""
            val description = if(burger is AssetsBurger) burger.description else if(burger is RemoteBurger) burger.desc else ""
            val tag = if(burger is AssetsBurger) "#Database" else if(burger is RemoteBurger) "#API" else ""
            Log.d("[BurgersList]",link)
            Box(modifier = Modifier.padding(8.dp)) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            Utils.openHttpsUrl(context,link)
                        }
                ) {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .wrapContentSize()) {
                        Text(
                            text = name ?: "",
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.h5,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row {
                            Text(
                                text = "by: $restaurant",
                                textAlign = TextAlign.Start,
                                style = MaterialTheme.typography.caption,
                                modifier = Modifier.weight(1.0f)
                            )
                            Text(
                                text = tag,
                                textAlign = TextAlign.Start,
                                style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Bold),
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = description ?: "",
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
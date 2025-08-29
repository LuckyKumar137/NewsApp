package com.example.api

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.size.Scale

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun News () {


    val context = LocalContext.current

    var articles by remember { mutableStateOf<List<NewsData>>(emptyList()) }
    var isDataLoaded by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("Loading...") }

    LaunchedEffect(Unit) {
        RetrofitInstance.api.getData().enqueue(object : Callback<NewsResponse> {
            override fun onResponse(
                call: Call<NewsResponse>,
                response: Response<NewsResponse>
            ) {
                if (response.isSuccessful) {
                    val newslist = response.body()?.articles ?: emptyList()
                    articles = newslist




                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Toast.makeText(context, "Error fetching data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "News") })
        }
    ) { innerPadding ->

        if (isDataLoaded) {
            if (articles.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    items(articles) { article ->
                        NewsCard(article)
                    }
                }
            } else {
                Toast.makeText(context, "Data Not Found", Toast.LENGTH_SHORT).show()

            }


        }
    }


    // ✅ News Card UI

}

@Composable
fun NewsCard(article: NewsData) {

    Card (modifier = Modifier.fillMaxWidth().
    padding(8.dp),
        elevation = CardDefaults.cardElevation(10.dp)

    ){

        Column{

            if (article.urlToImage.isNotEmpty()){

                Image(

                    painter = rememberAsyncImagePainter(article.urlToImage),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().
                    height(180.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
            Column(modifier = Modifier.padding(12.dp)){

          Text(
              text = article.title,
              style = MaterialTheme.typography.headlineMedium,
              maxLines = 2,
              overflow = TextOverflow.Ellipsis
          )
                Spacer(modifier = Modifier.height(4.dp))
                 val description = if (article.description.isNullOrBlank()){
                     "No Description Available"
                 }else{

                     article.description
                 }
                Text(
                    text =  description,
                    maxLines = 3,
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis
                )


            }

        }



    }

}
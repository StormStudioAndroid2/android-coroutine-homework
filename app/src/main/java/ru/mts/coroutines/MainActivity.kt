package ru.mts.coroutines

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ru.ermolnik.news.NewsScreen
import ru.ermolnik.news.NewsViewModel
import ru.mts.coroutines.ui.theme.CoroutinesTheme
import ru.mts.data.news.db.NewsLocalDataSource
import ru.mts.data.news.remote.NewsRemoteDataSource
import ru.mts.data.news.repository.News
import ru.mts.data.news.repository.NewsRepository
import ru.mts.data.news.usecase.SaveDataToDBUseCase

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = NewsViewModel(
            NewsRepository(
                NewsLocalDataSource(applicationContext),
                NewsRemoteDataSource(),
            ),
            SaveDataToDBUseCase(context = applicationContext)
        )

        setContent {
            CoroutinesTheme {
                // A surface container using the 'background' color from the theme

                Surface {
                    var refreshing by remember { mutableStateOf(false) }

                    SwipeRefresh(
                        state = rememberSwipeRefreshState(isRefreshing = refreshing),
                        onRefresh = {
                            refreshing = true
                            viewModel.refresh()
                            refreshing = false
                        },
                    ) {
                        LazyColumn (modifier = Modifier.fillMaxSize()) {
                            items(count = 1) {
                                NewsScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CoroutinesTheme {
        Greeting("Android")
    }
}
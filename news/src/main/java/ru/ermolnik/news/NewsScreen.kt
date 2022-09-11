package ru.ermolnik.news

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.net.UnknownHostException

@Composable
fun NewsScreen(viewModel: NewsViewModel) {
    val state = viewModel.state.collectAsState()
    Spacer(modifier = Modifier.padding(24.dp))

    Box(modifier = Modifier.fillMaxSize()) {
        when (val stateValue =  state.value) {
            is NewsState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center)
                )
            }
            is NewsState.Error -> {
                Text(
                    text = stateValue.throwable.toString(),
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
            is NewsState.Content -> {
                Text(
                    text = (state.value as NewsState.Content).id.toString(),
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

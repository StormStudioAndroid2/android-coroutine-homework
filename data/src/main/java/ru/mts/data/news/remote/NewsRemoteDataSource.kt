package ru.mts.data.news.remote

import kotlinx.coroutines.delay
import ru.mts.data.main.NetworkClient
import ru.mts.data.utils.runOperationCatching
import ru.mts.data.utils.Result
import java.net.UnknownHostException

class NewsRemoteDataSource {
    suspend fun getNews(): Result<NewsDto.Response, Throwable> {
        return runOperationCatching {
            delay(3000L)
            val rnds = (0..10).random()
            if (rnds > 5) {
                throw UnknownHostException("bla bla bla")

            }
            NetworkClient.create().getSampleData(NewsDto.Request(1))
        }
    }
}
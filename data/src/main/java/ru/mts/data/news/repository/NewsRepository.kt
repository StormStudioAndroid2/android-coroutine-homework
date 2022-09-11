package ru.mts.data.news.repository

import kotlinx.coroutines.flow.*
import ru.mts.data.news.db.NewsLocalDataSource
import ru.mts.data.news.db.toDomain
import ru.mts.data.news.remote.NewsRemoteDataSource
import ru.mts.data.news.remote.toDomain
import ru.mts.data.utils.*
import java.lang.RuntimeException

class NewsRepository(
    private val newsLocalDataSource: NewsLocalDataSource,
    private val newsRemoteDataSource: NewsRemoteDataSource
) {

    val newsFlow = MutableSharedFlow<Result<News, Throwable>>()
    suspend fun getNews() {
        newsLocalDataSource.getNews().doOnSuccess {
            newsFlow.emit(Result.Success(it.toDomain()))
        }.doOnError {
            newsFlow.emit(newsRemoteDataSource.getNews().mapSuccess { it.toDomain() }
                .mapError {
                    return@mapError RuntimeException("При загрузке данных через сеть произошла ошибка. Пожалуйста, проверьте соединение с сетью")
                })
        }
    }

    suspend fun getRemoteNews() {
        newsFlow.emit(
            newsRemoteDataSource.getNews().mapSuccess { it.toDomain() }
                .mapError {
                    return@mapError RuntimeException("При загрузке данных через сеть произошла ошибка. Пожалуйста, проверьте соединение с сетью")
                }
        )
    }
}

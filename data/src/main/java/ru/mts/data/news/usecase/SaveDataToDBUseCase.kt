package ru.mts.data.news.usecase

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mts.data.main.AppDatabase
import ru.mts.data.news.db.NewsEntity
import ru.mts.data.news.repository.News

class SaveDataToDBUseCase(private val context: Context) {
    suspend fun execute(newsState: News) {
        withContext(Dispatchers.IO) {
            if (AppDatabase.getDatabase(context).newsDao().getById(newsState.id.toLong()) == null) {
                AppDatabase.getDatabase(context).newsDao().insert(NewsEntity(newsState.id))

            }
        }
    }
}
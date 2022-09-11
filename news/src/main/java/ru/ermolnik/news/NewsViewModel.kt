package ru.ermolnik.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.mts.data.news.repository.NewsRepository
import ru.mts.data.news.usecase.SaveDataToDBUseCase
import ru.mts.data.utils.doOnError
import ru.mts.data.utils.doOnSuccess

class NewsViewModel(val repository: NewsRepository, saveDataToDBUseCase: SaveDataToDBUseCase) : ViewModel() {
    private val _state: MutableStateFlow<NewsState> = MutableStateFlow(NewsState.Loading)
    val state = _state.asStateFlow()

    init {
        repository.newsFlow.onEach {
            it.doOnError { error ->
                _state.emit(NewsState.Error(error))
            }.doOnSuccess { news ->
                saveDataToDBUseCase.execute(news)
                _state.emit(NewsState.Content(news.id))
            }
        }.launchIn(viewModelScope)
        viewModelScope.launch {
            repository.getNews()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _state.emit(NewsState.Loading)
            repository.getRemoteNews()
        }
    }
}

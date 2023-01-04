package non.mametich.newsapp.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import non.mametich.newsapp.data.api.NewsRepository
import non.mametich.newsapp.models.NewsResponse
import non.mametich.newsapp.utils.Resourses
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: NewsRepository)
    :ViewModel()
{
    val searchNewsLiveData: MutableLiveData<Resourses<NewsResponse>> = MutableLiveData()
    var searchPage = 1

    init {
        getSearchNews("")
    }

    fun getSearchNews(query: String) =
        viewModelScope.launch {
            searchNewsLiveData.postValue(Resourses.Loading())
            val response = repository.getSearchNews(query = query, pageNumber = searchPage)
            if (response.isSuccessful) {
                response.body().let { res ->
                    searchNewsLiveData.postValue(Resourses.Success(res))
                }
            } else {
                searchNewsLiveData.postValue(Resourses.Error(message = response.message()))
            }
        }
}
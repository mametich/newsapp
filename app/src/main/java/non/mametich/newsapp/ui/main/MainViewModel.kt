package non.mametich.newsapp.ui.main


import androidx.lifecycle.LiveData
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
class MainViewModel @Inject constructor(private val repository: NewsRepository) : ViewModel(){

    val newsLiveData: MutableLiveData<Resourses<NewsResponse>> = MutableLiveData()
    var newPage = 1

    init {
        getNews("ru")
    }

    private fun getNews(countryCode: String) =
        viewModelScope.launch {
            newsLiveData.postValue(Resourses.Loading())
            val response = repository.getNews(countryCode = countryCode, pageNumber = newPage)
            if (response.isSuccessful) {
                response.body().let { res ->
                    newsLiveData.postValue(Resourses.Success(res))
                }
            } else {
                    newsLiveData.postValue(Resourses.Error(message = response.message()))
                }
            }
        }

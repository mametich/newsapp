package non.mametich.newsapp.ui.search

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import non.mametich.newsapp.R
import non.mametich.newsapp.databinding.FragmentSearchBinding
import non.mametich.newsapp.ui.adapters.NewsAdapter
import non.mametich.newsapp.utils.Resourses

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    lateinit var newsAdapter: NewsAdapter
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        var job: Job? = null
        binding.editSearch.addTextChangedListener { text: Editable? ->
            job?.cancel()
            job = MainScope().launch {
                delay(5000)
                text?.let {
                    if (it.toString().isNotEmpty()){
                        viewModel.getSearchNews(query = it.toString())
                    }
                }
            }
        }
        viewModel.searchNewsLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resourses.Success -> {
                    binding.pagProgressBarSearch.visibility = View.INVISIBLE
                    response.data.let {
                        if (it != null) {
                            newsAdapter.differ.submitList(it.articles)
                        }
                    }
                }
                is Resourses.Error -> {
                    binding.pagProgressBarSearch.visibility = View.INVISIBLE
                    response.data.let {
                        Log.e("check data", "Main fragment: error ${it}")
                    }
                }
                is Resourses.Loading -> {
                    binding.pagProgressBarSearch.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initAdapter() {
        newsAdapter = NewsAdapter()
        binding.recVSearch.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}
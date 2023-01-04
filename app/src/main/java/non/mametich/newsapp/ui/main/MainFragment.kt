package non.mametich.newsapp.ui.main

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import non.mametich.newsapp.R
import non.mametich.newsapp.databinding.FragmentMainBinding
import non.mametich.newsapp.ui.adapters.NewsAdapter
import non.mametich.newsapp.utils.Resourses


@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()
    lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

        newsAdapter.setOnItemClickListener {
            val bundle = bundleOf("article" to it)
            view.findNavController().navigate(
                R.id.action_navigation_main_to_navigation_details,
                bundle
            )
        }


        viewModel.newsLiveData.observe(viewLifecycleOwner) { response ->
            when(response){
                is Resourses.Success -> {
                    binding.pagProgressBar.visibility = View.INVISIBLE
                    response.data.let {
                        if (it != null) {
                            newsAdapter.differ.submitList(it.articles)
                        }
                    }
                }
                is Resourses.Error -> {
                    binding.pagProgressBar.visibility = View.INVISIBLE
                    response.data.let{
                        Log.e("check data", "Main fragment: error ${it}")
                    }
                }
                is Resourses.Loading -> {
                    binding.pagProgressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initAdapter() {
        newsAdapter = NewsAdapter()
        binding.newsAdapter.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}
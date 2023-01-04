package non.mametich.newsapp.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import non.mametich.newsapp.databinding.FragmentDetailsBinding


@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: DetailsViewModel by viewModels()
    private val bundleArgs: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val articleArg = bundleArgs.article

        articleArg.let { article ->
            article.urlToImage.let {
                Glide.with(this).load(article.urlToImage).into(binding.headerImage)
            }
            binding.apply {
                headerImage.clipToOutline = true
                articleDetailTitle.text = article.title
                articleDetailDescriptionText.text = article.description

                articleDetailButton.setOnClickListener {
                    try {
                        Intent()
                            .setAction(Intent.ACTION_VIEW)
                            .addCategory(Intent.CATEGORY_BROWSABLE)
                            .setData(Uri.parse(takeIf { URLUtil.isValidUrl(article.url) }
                                ?.let {
                                article.url
                                } ?: "https://google.com"))
                                .let {
                                    ContextCompat.startActivity(requireContext(),it,null)
                                }
                    } catch (e: Exception){
                        Toast.makeText(context, "The device does not have any browser", Toast.LENGTH_SHORT).show()
                    }
                }
                iconFavorite.setOnClickListener {
                    viewModel.savedFavotiteArticle(article)
                }
                iconBack.setOnClickListener {
                    val action = DetailsFragmentDirections.actionNavigationDetailsToNavigationMain()
                    findNavController().navigate(action)
                }
            }
        }
    }
}
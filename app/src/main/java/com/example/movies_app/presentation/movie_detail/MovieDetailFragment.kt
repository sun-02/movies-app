package com.example.movies_app.presentation.movie_detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.movies_app.R
import com.example.movies_app.databinding.FragmentDetailBinding
import com.example.movies_app.presentation.core.extensions.addTopViewInsets
import com.example.movies_app.presentation.core.extensions.navigateBack
import com.example.movies_app.presentation.core.extensions.toast
import com.example.movies_app.presentation.movies.MoviesFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private var _adapter: MovieActorsAdapter? = null
    private val adapter get() = _adapter!!

    private var id: Int = 0

    private val viewModel: MovieDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = requireArguments().getInt(MOVIE_ID)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _adapter = MovieActorsAdapter(::onListItemEvent)
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addTopViewInsets(binding.topSpacer)

        bindViews()
        setupStateCollectors()
        viewModel.getMovieDetail(id)
    }

    private fun bindViews() {
        with(binding) {
            back.setOnClickListener {
                navigateBack()
            }

            tryAgain.setOnClickListener {
                viewModel.getMovieDetail(id)
            }

            isFavourite.setOnClickListener {
                viewModel.onItemLiked(id)
            }

            playTrailer.setOnClickListener {
                viewModel.state.value.movieDetail?.let {
                    openBrowser(it.trailerUrl)
                } ?: requireContext().toast("No trailer URL")
            }

            val dividerDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.rv_divider_horizontal)!!
            val itemDivider = DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL)
                .apply { setDrawable(dividerDrawable) }
            movieActorsList.let {
                it.addItemDecoration(itemDivider)
                it.adapter = adapter
            }
        }
    }

    private fun setupStateCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    val detail = state.movieDetail
                    val error = state.movieDetailError
                    with (binding) {
                        if (detail != null) {
                            errorGroup.isVisible = false
                            progress.isVisible = false
                            isFavourite.isChecked = state.isFavourite
                            moviePreview.load(detail.posterUrl)
                            movieTitle.text = detail.title
                            playTrailer.isEnabled = detail.trailerUrl.isNotEmpty()
                            movieDescription.originalText = detail.description
                            movieGenre.text = detail.genres
                            movieReleaseDate.text = detail.releaseDate.toString()
                            movieImdbRating.text = detail.imdbRating.toString()
                            movieCountry.text = detail.countries
                            adapter.submitList(detail.actors)
                            detailLayout.isVisible = true
                            isFavourite.isVisible = true
                        } else if (error != null) {
                            detailLayout.isVisible = false
                            isFavourite.isVisible = false
                            progress.isVisible = false
                            errorText.text = error
                            errorGroup.isVisible = true
                        } else {
                            detailLayout.isVisible = false
                            isFavourite.isVisible = false
                            errorGroup.isVisible = false
                            progress.isVisible = true

                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
    }

    private fun onListItemEvent(name: ActorName) {
        val nameForSearch = name.value.replace(' ', '+')
        openBrowser(KINOPOISK_SEARCH_PROMPT_URI + nameForSearch)
    }

    override fun onDetach() {
        super.onDetach()
        setFragmentResult(
            MoviesFragment.FRAGMENT_KEY,
            bundleOf(MoviesFragment.FAVORITES_FRAGMENT_DETACHED to true)
        )
    }

    private fun openBrowser(uri: String) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(uri)
        )
        requireContext().startActivity(intent)
    }

    companion object {
        const val MOVIE_ID = "MovieId"
        private const val KINOPOISK_SEARCH_PROMPT_URI = "https://www.kinopoisk.ru/index.php?kp_query="
    }
}
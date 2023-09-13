package com.example.movies_app.presentation.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies_app.R
import com.example.movies_app.databinding.FragmentFavouritesBinding
import com.example.movies_app.presentation.core.MoviesAdapter
import com.example.movies_app.presentation.core.MoviesListItemEvent
import com.example.movies_app.presentation.core.extensions.addTopViewInsets
import com.example.movies_app.presentation.core.extensions.hideSoftKeyboard
import com.example.movies_app.presentation.core.extensions.navigateBack
import com.example.movies_app.presentation.core.extensions.navigateTo
import com.example.movies_app.presentation.movie_detail.MovieDetailFragment
import com.example.movies_app.presentation.movies.MoviesFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class FavouritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    private var _adapter: MoviesAdapter? = null
    private val adapter get() = _adapter!!

    private val viewModel: FavouritesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _adapter = MoviesAdapter(::onListItemEvent)
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addTopViewInsets(binding.topSpacer)

        bindViews()
        setupStateCollectors()
    }

    private fun bindViews() {
        with (binding) {
            back.setOnClickListener {
                navigateBack()
            }

            searchField.doAfterTextChanged {
                viewModel.onSearchQueryChanged(it.toString())
            }

            clearQuery.setOnClickListener {
                searchField.setText("")
            }

            val dividerDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.rv_divider_vertical)!!
            val itemDivider = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
                .apply { setDrawable(dividerDrawable) }
            moviesList.let {
                it.addItemDecoration(itemDivider)
                it.adapter = adapter
                it.addOnScrollListener(getHideKeyboardWhenScrolledListener())
            }

            clearFavouritesList.setOnClickListener {
                viewModel.onClearFavourites()
            }
        }
    }

    private fun getHideKeyboardWhenScrolledListener(): RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            private var isDragging = false

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                isDragging = when (newState) {
                    RecyclerView.SCROLL_STATE_DRAGGING -> true
                    else -> false
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 10 || dy < 10 && isDragging) {
                    requireContext().hideSoftKeyboard(binding.root)
                }
            }
        }

    private fun setupStateCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    adapter.submitList(state.movies)
                    binding.notFound.isVisible = state.isNotFoundStubVisible
                    binding.clearQuery.isVisible = state.isClearQueryButtonVisible
                    if (state.isFavouritesCleared) {
                        binding.clearFavouritesList.isVisible = false
                        binding.emptyList.isVisible = true
                        withContext(Dispatchers.Main) {
                            delay(1000L)
                            navigateBack()
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

    private fun onListItemEvent(event: MoviesListItemEvent) {
        requireContext().hideSoftKeyboard(binding.root)
        when (event) {
            is MoviesListItemEvent.OnItemClick -> {
                val args = Bundle().apply {
                    putInt(MovieDetailFragment.MOVIE_ID, event.id.value)
                }
                navigateTo<MovieDetailFragment>(args)
            }
            is MoviesListItemEvent.OnLikeClick -> {
                viewModel.onItemUnliked(event.movie)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        setFragmentResult(
            MoviesFragment.FRAGMENT_KEY,
            bundleOf(MoviesFragment.FAVORITES_FRAGMENT_DETACHED to true)
        )
    }
}
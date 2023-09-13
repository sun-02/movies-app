package com.example.movies_app.presentation.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies_app.R
import com.example.movies_app.databinding.FragmentMoviesBinding
import com.example.movies_app.presentation.core.MoviesAdapter
import com.example.movies_app.presentation.core.MoviesListItemEvent
import com.example.movies_app.presentation.core.MoviesListMenuEvent
import com.example.movies_app.presentation.core.extensions.addTopViewInsets
import com.example.movies_app.presentation.core.extensions.hideSoftKeyboard
import com.example.movies_app.presentation.core.extensions.navigateTo
import com.example.movies_app.presentation.favourites.FavouritesFragment
import com.example.movies_app.presentation.movie_detail.MovieDetailFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private var _adapter: MoviesAdapter? = null
    private val adapter get() = _adapter!!

    private val viewModel: MoviesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(FRAGMENT_KEY) { _, bundle ->
            val moviesListChanged = bundle.getBoolean(FAVORITES_FRAGMENT_DETACHED)
            if (moviesListChanged) viewModel.onMoviesListChanged()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _adapter = MoviesAdapter(::onListItemEvent)
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
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
            searchField.doAfterTextChanged {
                viewModel.onSearchQueryChanged(it.toString())
            }

            clearQuery.setOnClickListener {
                searchField.setText("")
            }

            val menu = setupPopupMenu(sortList)
            sortList.setOnClickListener {
                menu.show()
            }

            val dividerDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.rv_divider_vertical)!!
            val itemDivider = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
                .apply { setDrawable(dividerDrawable) }
            moviesList.let {
                it.addItemDecoration(itemDivider)
                it.adapter = adapter
                it.addOnScrollListener(getHideKeyboardWhenScrolledListener())
            }

            navigateToFavourites.setOnClickListener {
                navigateTo<FavouritesFragment>()
            }

//            lifecycleScope.launch {
//                delay(300L)
//                withContext(Dispatchers.Main) {
//                    root.isVisible = true
//                }
//            }
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
                    binding.navigateToFavourites.isVisible = state.isFavouritesButtonVisible
                    binding.clearQuery.isVisible = state.isClearQueryButtonVisible
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
                viewModel.onItemLiked(event.movie)
            }
        }
    }

    private fun setupPopupMenu(v: View): PopupMenu {
        val popupMenu = PopupMenu(requireContext(), v)
        popupMenu.inflate(R.menu.movies_list_sort_menu)
        popupMenu
            .setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.sort_by_default -> {
                        viewModel.onMenuEvent(MoviesListMenuEvent.SortByDefault)
                        true
                    }
                    R.id.sort_by_name -> {
                        viewModel.onMenuEvent(MoviesListMenuEvent.SortByImbdRating)
                        true
                    }
                    R.id.sort_by_imbd_rating -> {
                        viewModel.onMenuEvent(MoviesListMenuEvent.SortByImbdVotes)
                        true
                    }
                    R.id.sort_by_imbd_votes -> {
                        viewModel.onMenuEvent(MoviesListMenuEvent.SortByName)
                        true
                    }
                    else -> false
                }
            }
        return popupMenu
    }

    companion object {
        const val FAVORITES_FRAGMENT_DETACHED = "FavoritesFragmentDetached"
        const val FRAGMENT_KEY = "MoviesFragment"
    }
}
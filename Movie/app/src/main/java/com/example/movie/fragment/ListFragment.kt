package com.example.movie.fragment
//grid view işmeini listviewmodele taşıııı
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.BundleKey
import com.example.movie.MovieService
import com.example.movie.R
import com.example.movie.data.Movie
import com.example.movie.viewModel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list), ListAdapter.OnItemClickListener {
    @Inject
    lateinit var movieService: MovieService
    private val viewModel by viewModels<ListViewModel>()//by ile çağırdık
    private var listAdapter: ListAdapter? = null
    private lateinit var recyclerView: RecyclerView
    private var isGridLayoutManager = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycler)
        initView(view)
        observeViewModel()
        setHasOptionsMenu(true)
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                if (!viewModel.isScrollRequestInProgress) {
                    if (dy > 0 && lastVisibleItem == totalItemCount - 1) {

                        viewModel.loadNextPage()
                    } else if (dy < 0 && firstVisibleItem == 0) {

                        viewModel.loadPreviousPage()
                    }
                }
            }
        }
        recyclerView.addOnScrollListener(scrollListener)
    }


    private fun initView(view: View) {

        recyclerView = view.findViewById(R.id.recycler)

        val layoutManager = if (isGridLayoutManager) {
            GridLayoutManager(requireContext(), 3) // Grid layout manager
        } else {
            LinearLayoutManager(requireContext()) // List layout manager
        }
        recyclerView.layoutManager = layoutManager

        listAdapter = ListAdapter(isGridLayoutManager)
        recyclerView.adapter = listAdapter

        listAdapter?.setOnItemClickListener(this)
        viewModel.display()
    }

    override fun onItemClick(movie: Movie, position: Int) {

        val bundle = Bundle().apply {
            putString(BundleKey.movie_url_Id, movie.id.toString())

        }
        bundle.getString("id_info")?.let { Log.e("url gözüktü", it) }
        val destination = DetailFragment()
        destination.arguments = bundle

        val action =
            ListFragmentDirections.actionListFragmentToDetailFragment(movie.id.toString()).actionId
        findNavController().navigate(action, bundle)

    }

    private fun observeViewModel() {
        viewModel.liveDataMovieList.observe(viewLifecycleOwner) { movieList ->
            movieList?.let {

                listAdapter?.setMovieList(it.movieList)// Yeni veriler gelince adapter'a set et
                viewModel.updateMovieVisibility(it.movieList)
            }
        }
        viewModel.liveDataSearchResults.observe(viewLifecycleOwner) { searchResults ->
            searchResults?.let {
                listAdapter?.setMovieList(it)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val listViewItem = menu.findItem(R.id.menu_list_view)
        val gridViewItem = menu.findItem(R.id.menu_grid_view)
        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem.actionView as SearchView

        // Set up the SearchView listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle the query when the user presses the search button
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Leave this empty to prevent search while typing
                return false
            }
        })


        listViewItem.setOnMenuItemClickListener {
            switchToListView()
            true
        }

        gridViewItem.setOnMenuItemClickListener {
            switchToGridView()
            true
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        setHasOptionsMenu(true) // Add this line to enable the options menu
        return view
    }

    private fun performSearch(query: String?) {
        viewModel.searchMovies(query)
    }

    private fun switchToListView() {
        if (isGridLayoutManager) {
            isGridLayoutManager = false
            initView(requireView()) // Reinitialize the view with the new layout
        }
    }

    private fun switchToGridView() {
        if (!isGridLayoutManager) {
            isGridLayoutManager = true
            initView(requireView()) // Reinitialize the view with the new layout
        }
    }
}


package com.mml.foody.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mml.foody.R
import com.mml.foody.adapters.RecipesAdapter
import com.mml.foody.databinding.FragmentRecipesBinding
import com.mml.foody.util.NetworkListener
import com.mml.foody.util.NetworkResult
import com.mml.foody.util.observeOnce
import com.mml.foody.viewmodels.MainViewModel
import com.mml.foody.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val TAG = "RecipesFragment"

@AndroidEntryPoint
class RecipesFragment : Fragment(), androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<RecipesFragmentArgs>()

    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel

    private lateinit var networkListener: NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        binding.recipesFAB.setOnClickListener {
            if (recipesViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            } else {
                recipesViewModel.showNetworkStatus()
            }
        }

        setHasOptionsMenu(true)

        setUpRecyclerView()

        recipesViewModel.readBackOnline.observe(viewLifecycleOwner, {
            recipesViewModel.backOnline = it
        })

        networkListener = NetworkListener()

        lifecycleScope.launchWhenStarted {
            networkListener.checkNetworkAvailability(requireContext()).collect { status ->
                Log.d(TAG, "networkListener $status")
                recipesViewModel.networkStatus = status
                recipesViewModel.showNetworkStatus()
                readDatabase()
            }
        }

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as androidx.appcompat.widget.SearchView

        searchView.isSubmitButtonEnabled = true

        searchView.setOnQueryTextListener(this)
    }

    private fun setUpRecyclerView() {
        _binding!!.apply {
            recyclerView.apply {
                adapter = mAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
        showShimmerEffect()
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            if (_binding != null) {
                mainViewModel.readRecipes.observeOnce(viewLifecycleOwner, { database ->
                    if (database.isNotEmpty() && !args.backFromBottomSheet) {
                        Log.d(TAG, "readDatabase: called")
                        mAdapter.setData(database[0].foodRecipe)
                        hideShimmerEffect()
                    } else {
                        requestApiData()
                    }
                })
            }
        }
    }

    private fun requestApiData() {
        Log.d(TAG, "requestApiData: called")
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, { response ->

            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let {
                        mAdapter.setData(it)
                    }
                }

                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e(TAG, response.data.toString())
                    Log.e(TAG, response.message.toString())
                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }

        })

    }

    private fun searchApiData(searchQuery: String) {
        showShimmerEffect()
        mainViewModel.searchRecipes(recipesViewModel.applySearchQuery(searchQuery))

        mainViewModel.searchedRecipesResponse.observe(viewLifecycleOwner, { response ->

            when (response) {

                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val foodRecipe = response.data
                    foodRecipe?.let {
                        mAdapter.setData(it)
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }

            }


        })
    }


    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner, { database ->

                if (database.isNotEmpty()) {
                    mAdapter.setData(database[0].foodRecipe)
                }

            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun showShimmerEffect() {
        _binding?.recyclerView!!.showShimmer()
    }

    private fun hideShimmerEffect() {
        _binding?.recyclerView!!.hideShimmer()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

}
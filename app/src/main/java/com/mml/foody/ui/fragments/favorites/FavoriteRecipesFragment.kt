package com.mml.foody.ui.fragments.favorites

import android.os.Bundle
import android.os.Message
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mml.foody.R
import com.mml.foody.adapters.FavoriteRecipesAdapter
import com.mml.foody.databinding.FavoriteRecipesRowBinding
import com.mml.foody.databinding.FragmentFavoriteRecipesBinding
import com.mml.foody.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()
    private var _binding: FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!
    private val mAdapter: FavoriteRecipesAdapter by lazy {
        FavoriteRecipesAdapter(
            requireActivity(),
            mainViewModel
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteRecipesBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)


        binding.apply {
            adapter = mAdapter
            favoritesRecipesRecyclerView.adapter = mAdapter
            favoritesRecipesRecyclerView.layoutManager = LinearLayoutManager(context)
            lifecycleOwner = this@FavoriteRecipesFragment
            mainViewModel = this@FavoriteRecipesFragment.mainViewModel
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mAdapter.clearContextualActionMode()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_recipes_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteAllFavoriteRecipes){
            mainViewModel.deleteAllFavoriteRecipes()
            Snackbar.make(binding.root,"All recipes deleted",Snackbar.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }


}
package com.mml.foody.ui.fragments.ingredients

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mml.foody.R
import com.mml.foody.adapters.IngredientsAdapter
import com.mml.foody.databinding.FragmentIngredientsBinding
import com.mml.foody.models.Result
import com.mml.foody.util.Constants.Companion.RECIPE_RESULT_KEY

class IngredientsFragment : Fragment() {

    private val mAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }
    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!
    private  var myBundle: Result?=null

    companion object{
        private const val TAG = "IngredientsFragment"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentIngredientsBinding.inflate(inflater,container,false)

        val args = arguments
        
        if (args != null) {
            myBundle = args.getParcelable(RECIPE_RESULT_KEY)
            Log.d(TAG, "args: ${myBundle?.extendedIngredients}")
            
        }
  

        binding.apply {
            ingredientsRecyclerView.adapter = mAdapter
            ingredientsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            Log.d(TAG, "ingredientsRecyclerView: called")

        }
        myBundle?.extendedIngredients?.let {
            mAdapter.setData(it)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
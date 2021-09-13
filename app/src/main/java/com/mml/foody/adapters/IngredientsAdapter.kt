package com.mml.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mml.foody.R
import com.mml.foody.databinding.IngredientsRowLayoutBinding
import com.mml.foody.models.ExtendedIngredient
import com.mml.foody.util.Constants.Companion.BASE_IMAGE_URL
import com.mml.foody.util.RecipesDiffUtil

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private var ingredientsList = emptyList<ExtendedIngredient>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            IngredientsRowLayoutBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = ingredientsList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    inner class MyViewHolder(private val binding: IngredientsRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(extendedIngredient: ExtendedIngredient){
                binding.apply {
                    ingredientsImageView.load(BASE_IMAGE_URL + extendedIngredient.image){
                        crossfade(600)
                        error(R.drawable.ic_error_placeholder)
                    }
                    ingredientsName.text = extendedIngredient.name.capitalize()
                    ingredientsAmount.text = extendedIngredient.amount.toString()
                    ingredientsUnit.text = extendedIngredient.unit
                    ingredientConsistency.text = extendedIngredient.consistency
                    ingredientOriginal.text = extendedIngredient.original
                }
            }

    }

    fun setData(newIngredients: List<ExtendedIngredient>){

        val ingredientsDiffUtil = RecipesDiffUtil(oldList = ingredientsList, newIngredients )

        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUtil)

        ingredientsList = newIngredients

        diffUtilResult.dispatchUpdatesTo(this)


    }
}
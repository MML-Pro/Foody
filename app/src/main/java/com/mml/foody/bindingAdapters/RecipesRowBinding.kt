package com.mml.foody.bindingAdapters

import android.accounts.AuthenticatorDescription
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.mml.foody.R
import com.mml.foody.models.Result
import com.mml.foody.ui.fragments.recipes.RecipesFragmentDirections
import org.jsoup.Jsoup

class RecipesRowBinding {

    companion object {

        private const val TAG = "RecipesRowBinding"

        @BindingAdapter("onRecipeClickListener")
        @JvmStatic
        fun onRecipeClickListener(recipesRowLayout:ConstraintLayout, result: Result?){

            Log.d(TAG, "onRecipeClickListener: called")

            recipesRowLayout.setOnClickListener {
                try {
                    val action = result?.let { it1 ->
                        RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(
                            it1
                        )
                    }
                    if (action != null) {
                        recipesRowLayout.findNavController().navigate(action)
                    }
                }catch (ex:Exception){
                    Log.e(TAG, "onRecipeClickListener: $ex", )
                }
            }

        }

        @BindingAdapter("loadImageFromURL")
        @JvmStatic
        fun loadImageFromURL(imageView: ImageView, imageURL: String) {
            imageView.load(imageURL) {
                crossfade(600)
                    .error(R.drawable.ic_error_placeholder)
            }
        }


        @BindingAdapter("applyVeganColor")
        @JvmStatic
        fun applyVeganColor(view: View, vegan: Boolean) {
            if (vegan) {
                when (view) {
                    is TextView -> {
                        view.setTextColor(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                    is ImageView -> {
                        view.setColorFilter(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                }
            }
        }

        @BindingAdapter("parseHTML")
        @JvmStatic
        fun parseDesc(textView: TextView, description: String?){
            if (description != null){
                val desc = Jsoup.parse(description).text()
                textView.text = desc
            }
        }
    }
}
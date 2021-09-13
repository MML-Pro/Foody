package com.mml.foody.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.navigation.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.mml.foody.R
import com.mml.foody.adapters.PagerAdapter
import com.mml.foody.data.database.entities.FavoritesEntity
import com.mml.foody.databinding.ActivityDetailsBinding
import com.mml.foody.util.Constants.Companion.RECIPE_RESULT_KEY
import com.mml.foody.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "DetailsActivity"
    }

    private lateinit var binding: ActivityDetailsBinding

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var menuItem:MenuItem

    private var recipeSaved = false
    private var savedRecipeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        try {

            val resultBundle = Bundle()
            resultBundle.putParcelable(RECIPE_RESULT_KEY, args.result)

            val pagerAdapter = PagerAdapter(resultBundle, supportFragmentManager, lifecycle)

            binding.apply {
                viewPager.adapter = pagerAdapter

            }
            val tabTitles = arrayOf("OverView", "Ingredients", "Instructions")

            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = tabTitles[0]
                    1 -> tab.text = tabTitles[1]
                    2 -> tab.text = tabTitles[2]
                }

            }.attach()
        }catch (ex:Exception){
            Log.e(TAG, "onCreate: ${ex.message}" )
            Log.e(TAG, "onCreate: ${ex.cause}", )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        menuItem = menu!!.findItem(R.id.save_to_favorite_menu)
        checkSavedRecipes(menuItem)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_to_favorite_menu && !recipeSaved) {
            saveToFavorites(item)
        } else if (item.itemId == R.id.save_to_favorite_menu && recipeSaved){
            removeFromFavorites(item)
        }
            return super.onOptionsItemSelected(item)
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this, { favoritesEntity ->

            try {
                for (savedRecipes in favoritesEntity) {
                    if (savedRecipes.result.recipeId == args.result.recipeId) {
                        changeMenuItemColor(menuItem, R.color.yellow)
                        savedRecipeId = savedRecipes.id
                        recipeSaved = true
                    }
                }
            } catch (ex: Exception) {
                Log.e(TAG, "checkSavedRecipes: ${ex.message}")
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        changeMenuItemColor(menuItem,R.color.white)
    }

    private fun saveToFavorites(item: MenuItem) {
        val favoritesEntity = FavoritesEntity(0, args.result)
        mainViewModel.insertFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipe saved")
        recipeSaved = true

    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.root, message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun removeFromFavorites(item: MenuItem) {
        val favoritesEntity = FavoritesEntity(savedRecipeId, args.result)
        mainViewModel.deleteFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Removed from favorites")
        recipeSaved = false
    }

    private fun changeMenuItemColor(item: MenuItem, yellow: Int) {
        item.icon.setTint(ContextCompat.getColor(this, yellow))
    }
}
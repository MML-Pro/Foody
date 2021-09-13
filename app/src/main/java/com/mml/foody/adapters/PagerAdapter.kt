package com.mml.foody.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mml.foody.ui.fragments.ingredients.IngredientsFragment
import com.mml.foody.ui.fragments.instructions.InstructionsFragment
import com.mml.foody.ui.fragments.overview.OverviewFragment

class PagerAdapter(
    private val resultBundle: Bundle,
    fm: FragmentManager,
    lifeCycle: Lifecycle
) : FragmentStateAdapter(fm, lifeCycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val overviewFragment = OverviewFragment()
                overviewFragment.arguments = resultBundle
                overviewFragment
            }
            1 -> {
                val ingredientsFragment = IngredientsFragment()
                ingredientsFragment.arguments = resultBundle
                ingredientsFragment
            }
            2 -> {
                val instructionsFragment = InstructionsFragment()
                instructionsFragment.arguments = resultBundle
                instructionsFragment

            }
            else -> OverviewFragment()
        }


    }

}
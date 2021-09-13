package com.mml.foody.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mml.foody.R
import kotlinx.coroutines.delay


class SplashScreenFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        (activity as AppCompatActivity?)!!.supportActionBar?.hide()
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            goToRecipesFragment()
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    private suspend fun goToRecipesFragment() {
        delay(3000)
        findNavController()
            .navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToRecipesFragment())
    }
}
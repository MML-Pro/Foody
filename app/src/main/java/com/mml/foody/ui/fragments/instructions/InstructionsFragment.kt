package com.mml.foody.ui.fragments.instructions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.mml.foody.databinding.FragmentInstructionsBinding
import com.mml.foody.models.Result
import com.mml.foody.util.Constants


class InstructionsFragment : Fragment() {

    private var _binding:FragmentInstructionsBinding?=null
    private val binding get() = _binding!!

    private  var myBundle: Result?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentInstructionsBinding.inflate(inflater, container, false)

        val args = arguments
        if (args != null) {
            myBundle = args.getParcelable(Constants.RECIPE_RESULT_KEY)
        }

        binding.apply {
            instructionsWebView.webViewClient = object : WebViewClient() {

            }
            val websiteURL = myBundle!!.sourceUrl
            instructionsWebView.loadUrl(websiteURL)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
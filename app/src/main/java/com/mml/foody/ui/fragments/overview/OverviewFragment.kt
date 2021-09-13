package com.mml.foody.ui.fragments.overview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import com.mml.foody.R
import com.mml.foody.databinding.FragmentOverviewBinding
import com.mml.foody.models.Result
import com.mml.foody.util.Constants.Companion.RECIPE_RESULT_KEY
import org.jsoup.Jsoup


class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val TAG = "OverviewFragment"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        try {
            val args = arguments
            if (args != null) {
                val myBundle: Result? = args.getParcelable(RECIPE_RESULT_KEY)


                binding.apply {
                    mainImageView.load(myBundle?.image)
                    titleTV.text = myBundle?.title
                    likesTV.text = myBundle?.aggregateLikes.toString()
                    timeTV.text = myBundle?.readyInMinutes.toString()
                    myBundle!!.summary.let {
                        val summery = Jsoup.parse(it).text()
                        summeryTV.text = summery
                    }

                    Log.d(TAG, "onCreateView: binding called")

                    if (myBundle.vegetarian) {
                        vegetarianIV.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                        vegetarianTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                    }

                    if (myBundle.vegan) {
                        veganIV.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                        veganTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                    }

                    if (myBundle.glutenFree == true) {
                        glutenFreeIV.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                        glutenFreeTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                    }

                    if (myBundle.dairyFree) {
                        dairyFreeIV.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                        dairyFreeTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                    }

                    if (myBundle.veryHealthy) {
                        healthyIV.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                        healthyTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                    }

                    if (myBundle.cheap) {
                        cheapIV.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                        cheapTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                    }

                }
            }

        } catch (ex: Exception) {
            Log.e(TAG, "onCreateView: ${ex.message}")
            Log.e(TAG, "onCreateView: ${ex.cause}")
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
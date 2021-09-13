package com.mml.foody.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mml.foody.R
import com.mml.foody.databinding.RecipesBottomSheetBinding
import com.mml.foody.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.mml.foody.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.mml.foody.viewmodels.RecipesViewModel
import java.util.*

private const val TAG = "RecipesBottomSheet"

class RecipesBottomSheet : BottomSheetDialogFragment() {

    private lateinit var recipesViewModel: RecipesViewModel
    private var _binding: RecipesBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipID = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = RecipesBottomSheetBinding.inflate(inflater, container, false)

        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, { value ->
            mealTypeChip = value.selectedMealType
            dietTypeChip = value.selectedDietType

            updateChip(value.selectedMealTypeID, binding.mealTypeChipGroup)
            updateChip(value.selectedDietTypeID, binding.dietTypeChipGroup)
        })

        binding.mealTypeChipGroup.setOnCheckedChangeListener { group, selectedChipID ->
            val chip = group.findViewById<Chip>(selectedChipID)
            val selectedMealType = chip.text.toString().lowercase(Locale.ROOT)
            mealTypeChip = selectedMealType
            mealTypeChipID = selectedChipID
        }

        binding.dietTypeChipGroup.setOnCheckedChangeListener { group, selectedChipID ->
            val chip = group.findViewById<Chip>(selectedChipID)
            val selectedDietType = chip.text.toString().lowercase(Locale.ROOT)
            dietTypeChip = selectedDietType
            dietTypeChipId = selectedChipID
        }

        binding.applyBtn.setOnClickListener {
            recipesViewModel.saveMealAndDietType(
                mealTypeChip,
                mealTypeChipID,
                dietTypeChip,
                dietTypeChipId
            )
            val action =
                RecipesBottomSheetDirections
                    .actionRecipesBottomSheetToRecipesFragment(true)
            findNavController().navigate(action)
        }



        return binding.root
    }

    private fun updateChip(chipID: Int, chipGroup: ChipGroup) {
        if (chipID != 0) {
            try {
                chipGroup.findViewById<Chip>(chipID).isChecked = true
            } catch (ex: Exception) {
                Log.e(TAG, "updateChip: ${ex.message.toString()}")
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.myapplication.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.myapplication.databinding.FragmentSettingsBinding
import com.example.myapplication.utils.Constants


class SettingsFragment :
    BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            binding.switchAdult.isChecked =
                it.getPreferences(Context.MODE_PRIVATE).getBoolean(Constants.IS_ADULT_KEY, true)
        }

        binding.switchAdult.setOnClickListener{
            activity?.getPreferences(Context.MODE_PRIVATE)?.edit()
                ?.putBoolean(Constants.IS_ADULT_KEY, binding.switchAdult.isChecked)
                ?.apply()
        }
    }

}
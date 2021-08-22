package com.example.materialdesignvoipcall.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.materialdesignvoipcall.R
import com.example.materialdesignvoipcall.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_register,
            container,
            false
        )
        val view = binding.root
        binding.lifecycleOwner = this
        binding.login.setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }

}
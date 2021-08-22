package com.example.materialdesignvoipcall.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.materialdesignvoipcall.R
import com.example.materialdesignvoipcall.databinding.FragmentIncomingCallBinding


class IncomingCallFragment : Fragment() {

    lateinit var binding:FragmentIncomingCallBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_incoming_call,
            container,
            false
        )
        val view = binding.root
        binding.lifecycleOwner = this

        return view
    }

}
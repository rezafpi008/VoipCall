package com.example.materialdesignvoipcall.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.materialdesignvoipcall.R
import com.example.materialdesignvoipcall.databinding.FragmentWelcomeBinding
import com.example.materialdesignvoipcall.service.LinphoneService
import com.example.materialdesignvoipcall.util.AppConstants.Companion.SIP_CONNECT
import com.example.materialdesignvoipcall.util.SharedPreferenceUtil

class WelcomeFragment : Fragment() {

    lateinit var binding: FragmentWelcomeBinding

    companion object {
        @JvmStatic
        fun newInstance(): WelcomeFragment {
            return WelcomeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_welcome,
            container,
            false
        )
        val view = binding.root
        binding.lifecycleOwner = this
        initialData()
        return view
    }

    private fun initialData() {
        binding.retryConnection.setOnClickListener {
            binding.connectionLayout.visibility = View.VISIBLE
            binding.errorLayout.visibility = View.GONE
            /*coreContext.stop(coreListener)
            coreContext.startLogin(coreListener)*/
        }

        binding.login.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }
    }

    private fun startLogin() {
        if (SharedPreferenceUtil.getBooleanValue(SIP_CONNECT)) {
            /*ensureCoreExists(requireContext(), coreListener)*/
        } else findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
    }

    override fun onStart() {
        super.onStart()

        // Check whether the Service is already running
        if (LinphoneService.isReady()) {
            onServiceReady()
        } else {
            // If it's not, let's start it
            val intent = Intent(context, LinphoneService::class.java)
            requireActivity().startService(intent)
            // And wait for it to be ready, so we can safely use it afterwards
            waitForService()
        }
    }

    private fun onServiceReady() {
        val defaultAccount = LinphoneService.getCore().defaultAccount
        if (defaultAccount != null) {
           findNavController().navigate(R.id.action_welcomeFragment_to_mainActivity)
            requireActivity().finish()
        } else {
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }
    }

    private fun waitForService(){
        val mHandler = android.os.Handler()
        val myThread: Thread = object : Thread() {
            override fun run() {
                while (!LinphoneService.isReady()) {
                    try {
                        sleep(30)
                    } catch (e: InterruptedException) {
                        throw RuntimeException("waiting thread sleep() has been interrupted")
                    }
                }
                // As we're in a thread, we can't do UI stuff in it, must post a runnable in UI thread
                mHandler.post { onServiceReady()  }
            }
        }
        myThread.start()
    }
}
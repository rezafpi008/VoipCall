package com.example.materialdesignvoipcall.mainFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.materialdesignvoipcall.R
import com.example.materialdesignvoipcall.databinding.FragmentDialerBinding
import com.example.materialdesignvoipcall.service.LinphoneService
import com.example.materialdesignvoipcall.util.AppConstants.Companion.CONNECTED
import com.example.materialdesignvoipcall.util.AppConstants.Companion.CONNECTION_FAILED
import com.example.materialdesignvoipcall.util.AppConstants.Companion.CONNECTION_IN_PROGRESS
import com.example.materialdesignvoipcall.util.AppConstants.Companion.CONNECTION_NONE
import com.example.materialdesignvoipcall.util.LinPhoneUtil
import com.example.materialdesignvoipcall.viewModel.DialerViewModel
import org.linphone.core.*

class DialerFragment : Fragment(),View.OnClickListener {

    lateinit var binding: FragmentDialerBinding
    lateinit var viewModel: DialerViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dialer,
            container,
            false
        )
        val view = binding.root
        viewModel = DialerViewModel()
        binding.callViewModel = viewModel
        binding.lifecycleOwner = this
        binding.call.setOnClickListener {
            if (binding.remoteAddress.text.toString() == "") {
                binding.remoteAddress.error = getString(R.string.fill_this_field)
            } else {
                LinPhoneUtil.outgoingCall(binding.remoteAddress.text.toString())
            }
        }
        initialNumpad()
        subscription()
        return view
    }

    private fun subscription() {
        viewModel.connectionStatus.observe(viewLifecycleOwner, Observer { status ->
            when (status) {
                RegistrationState.Ok -> {
                    binding.connectionStatus.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.green
                        )
                    )
                    viewModel.setConnectionStatusTitle(CONNECTED)
                }
                RegistrationState.None, RegistrationState.Cleared -> {
                    binding.connectionStatus.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray
                        )
                    )
                    viewModel.setConnectionStatusTitle(CONNECTION_NONE)
                }
                RegistrationState.Failed -> {
                    binding.connectionStatus.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )
                    viewModel.setConnectionStatusTitle(CONNECTION_FAILED)
                }
                RegistrationState.Progress -> {
                    binding.connectionStatus.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.orange
                        )
                    )
                    viewModel.setConnectionStatusTitle(CONNECTION_IN_PROGRESS)
                }
            }
        })
    }


    // Monitors the registration state of our account(s) and update the LED accordingly
    private val coreListener =
        object : CoreListenerStub() {
            override fun onAccountRegistrationStateChanged(
                core: Core,
                account: Account,
                state: RegistrationState,
                message: String
            ) {
                viewModel.setConnectionStatus(account.state)
            }
        }

    override fun onResume() {
        super.onResume()
        // The best way to use Core listeners in Activities is to add them in onResume
        // and to remove them in onPause
        LinphoneService.getCore().addListener(coreListener)
        val account: Account = LinphoneService.getCore().defaultAccount!!
        if (account != null) {
            viewModel.setConnectionStatus(account.state)
        } else {
            // No account configured, we display the configuration activity
        }
    }

    override fun onPause() {
        super.onPause()
        LinphoneService.getCore().removeListener(coreListener)
    }

    private fun initialNumpad() {
        binding.num0.setOnClickListener(this)
        binding.num1.setOnClickListener(this)
        binding.num2.setOnClickListener(this)
        binding.num3.setOnClickListener(this)
        binding.num4.setOnClickListener(this)
        binding.num5.setOnClickListener(this)
        binding.num6.setOnClickListener(this)
        binding.num7.setOnClickListener(this)
        binding.num8.setOnClickListener(this)
        binding.num9.setOnClickListener(this)
        binding.star.setOnClickListener(this)
        binding.sharp.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        var char = ""
        when (v?.id) {
            R.id.num0 -> {
                char = "0"
            }
            R.id.num1 -> {
                char = "1"
            }
            R.id.num2 -> {
                char = "2"
            }
            R.id.num3 -> {
                char = "3"
            }
            R.id.num4 -> {
                char = "4"
            }
            R.id.num5 -> {
                char = "5"
            }
            R.id.num6 -> {
                char = "6"
            }
            R.id.num7 -> {
                char = "7"
            }
            R.id.num8 -> {
                char = "8"
            }
            R.id.num9 -> {
                char = "9"
            }
            R.id.star -> {
                char = "*"
            }
            R.id.sharp -> {
                char = "#"
            }
        }
        binding.remoteAddress.setText(binding.remoteAddress.text.toString() + char)
    }
}
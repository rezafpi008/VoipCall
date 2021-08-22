package com.example.materialdesignvoipcall.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.materialdesignvoipcall.R
import com.example.materialdesignvoipcall.databinding.FragmentLoginBinding
import com.example.materialdesignvoipcall.service.LinphoneService
import com.example.materialdesignvoipcall.util.AppConstants.Companion.SIP_DOMAIN
import com.example.materialdesignvoipcall.util.AppConstants.Companion.SIP_PASSWORD
import com.example.materialdesignvoipcall.util.AppConstants.Companion.SIP_TRANSFER_TYPE
import com.example.materialdesignvoipcall.util.AppConstants.Companion.SIP_USERNAME
import com.example.materialdesignvoipcall.util.AppConstants.Companion.TCP
import com.example.materialdesignvoipcall.util.AppConstants.Companion.UDP
import com.example.materialdesignvoipcall.util.SharedPreferenceUtil
import com.example.materialdesignvoipcall.util.ToastUtil
import org.linphone.core.*

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    private var transportType: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            activity?.finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )
        val view = binding.root
        binding.lifecycleOwner = this
        binding.register.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }


        binding.tcp.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                transportType = TCP
            }
        }

        binding.udp.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                transportType = UDP
            }
        }

        binding.login.setOnClickListener {
            if (checkLogin()) {
                loginProgress(true)
                configureAccount()
            }
        }

        return view
    }


    private fun configureAccount() {
        val username = binding.username.text.toString()
        val password = binding.password.text.toString()
        val domain = binding.domain.text.toString()

        val authInfo = Factory.instance().createAuthInfo(
            username, null, password,
            null, null, domain, null
        )
        val accountParams: AccountParams = LinphoneService.getCore().createAccountParams()
        val identity = Factory.instance().createAddress(
            "sip:" +
                    username
                    + "@" +
                    domain
        )
        accountParams.identityAddress = identity
        val address = Factory.instance().createAddress("sip:$domain")
        address!!.transport = TransportType.fromInt(transportType!!)
        accountParams.serverAddress = address
        accountParams.registerEnabled = true
        val account: Account = LinphoneService.getCore().createAccount(accountParams)
        LinphoneService.getCore().addAuthInfo(authInfo)
        LinphoneService.getCore().addAccount(account)
        LinphoneService.getCore().defaultAccount = account

    }

    private fun checkLogin(): Boolean {
        var check = true
        if (transportType == null) {
            check = false
            ToastUtil.showToast(R.string.select_transfer_type)
        }
        if (binding.username.text.toString() == "") {
            check = false
            binding.username.error = getString(R.string.fill_this_field)
        }
        if (binding.password.text.toString() == "") {
            check = false
            binding.password.error = getString(R.string.fill_this_field)
        }
        if (binding.domain.text.toString() == "") {
            check = false
            binding.domain.error = getString(R.string.fill_this_field)
        }
        if(check){
            SharedPreferenceUtil.saveStringValue(SIP_USERNAME, binding.username.text.toString())
            SharedPreferenceUtil.saveStringValue(SIP_PASSWORD, binding.password.text.toString())
            SharedPreferenceUtil.saveStringValue(SIP_DOMAIN, binding.domain.text.toString())
            SharedPreferenceUtil.saveIntValue(SIP_TRANSFER_TYPE, transportType!!)
        }
        return check
    }

    private val mCoreListener =
        object : CoreListenerStub() {
            override fun onAccountRegistrationStateChanged(
                core: Core,
                account: Account,
                state: RegistrationState,
                message: String
            ) {
                super.onAccountRegistrationStateChanged(core, account, state, message)
                if (state == RegistrationState.Ok) {
                    loginProgress(false)
                    findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
                    requireActivity().finish()
                } else if (state == RegistrationState.Failed) {
                    loginProgress(false)
                    ToastUtil.showToast("Failure")
                }
            }
        }

    override fun onResume() {
        super.onResume()
        LinphoneService.getCore().addListener(mCoreListener)
    }

    override fun onPause() {
        LinphoneService.getCore().removeListener(mCoreListener)
        super.onPause()
    }

    fun loginProgress(boolean: Boolean) {
        if (boolean) {
            binding.loginProgress.visibility = View.VISIBLE
            binding.login.visibility = View.GONE
        } else {
            binding.loginProgress.visibility = View.GONE
            binding.login.visibility = View.VISIBLE
        }

    }

}
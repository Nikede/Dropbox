package com.nikede.dropbox.fragments

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.android.Auth
import com.dropbox.core.v2.DbxClientV2
import com.nikede.dropbox.databinding.FragmentLoginBinding
import com.nikede.dropbox.R


class LoginFragment : Fragment() {
    var v: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentLoginBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )

        binding.login.setOnClickListener {
            v = it
            Auth.startOAuth2Authentication(activity, getString(R.string.app_key));
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (Auth.getOAuth2Token() != null) {
            try {
                val sp = PreferenceManager.getDefaultSharedPreferences(activity)
                sp.edit().putString("Token", Auth.getOAuth2Token()).apply()
                v?.findNavController()?.navigate(R.id.action_loginFragment_to_listFragment)

            } catch (e: IllegalStateException) {
                Toast.makeText(
                    activity,
                    getString(R.string.authentication_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

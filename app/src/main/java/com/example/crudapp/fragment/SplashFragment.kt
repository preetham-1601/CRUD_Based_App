package com.example.crudapp.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.crudapp.R
import com.example.crudapp.databinding.FragmentSplashBinding
import com.example.crudapp.util.SessionManager

class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        val view = binding.root


        sessionManager = SessionManager(activity as Context)




        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (sessionManager.isLoggedIn()) {
            Handler().postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            }, 2000)
            onDestroy()


        } else {

            Handler().postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }, 2000)

                //findNavController().navigate(R.id.action_mainScreenFragment_to_loginFragment)
                onDestroy()
            }


        }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
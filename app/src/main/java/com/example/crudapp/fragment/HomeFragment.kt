package com.example.crudapp.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.crudapp.R
import com.example.crudapp.databinding.FragmentHomeBinding
import com.example.crudapp.util.SessionManager
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    /*Variables used in managing the login session*/
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        sessionManager = SessionManager(activity as Context)

        if(sessionManager.isLoggedIn()){
            activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity!!.finish()
                }
            })
        }else{

        }


        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.toolbar.toolbar.inflateMenu(R.menu.logout_menu)
        binding.toolbar.toolbar.title = "Home"

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addPostFragment)

        }

        binding.toolbar.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_logout ->{
                    auth = FirebaseAuth.getInstance()
                    val builder = AlertDialog.Builder(context as Activity)
                    builder.setTitle("Logout")
                        .setMessage("Do you want to Logout?")
                        .setPositiveButton("Logout") { _, _ ->
                            auth.signOut()
                            sessionManager.setLogin(false)
                            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                        }
                        .setNegativeButton("No") { _, _ ->

                        }
                        .create()
                        .show()
                    true
                }
                else -> false
            }

        }

    }

}
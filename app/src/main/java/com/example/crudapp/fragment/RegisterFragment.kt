package com.example.crudapp.fragment

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.crudapp.R
import com.example.crudapp.databinding.FragmentRegisterBinding
import com.example.crudapp.util.ConnectionManager
import com.example.crudapp.util.SessionManager
import com.example.crudapp.util.Validations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.json.JSONObject


class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(context as Activity)

        sharedPreferences = (activity as FragmentActivity).getSharedPreferences(
            sessionManager.PREF_NAME,
            sessionManager.PRIVATE_MODE
        )


        auth = Firebase.auth

        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.toolbar.toolbar.title = "Register"
        binding.toolbar.toolbar.setNavigationOnClickListener { view ->
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        })

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPass1.text.toString()

            if (Validations.validateEmail(email)) {

                if (Validations.validatePasswordLength(password)) {

                    if (ConnectionManager().isNetworkAvailable(context as Activity)) {

                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(requireActivity()) { task ->
                                if (task.isSuccessful) {
                                    val uid = auth.currentUser?.uid!!


                                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

                                } else {
                                    Toast.makeText(
                                        activity as Context,
                                        "Singed Up Failed!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                val queue = Volley.newRequestQueue(context as Activity)
                                val url = "http://fastapi-rishikumar.herokuapp.com/users/"

                                val jsonParams = JSONObject()
                                jsonParams.put("email", email)
                                jsonParams.put("password", password)

                                val jsonObjectRequest = object : JsonObjectRequest(
                                    Method.POST,
                                    url,
                                    jsonParams,
                                    Response.Listener {

                                        Toast.makeText(
                                            context as Activity,
                                            "registered successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()


                                    },
                                    Response.ErrorListener {
                                        Toast.makeText(
                                            context as Activity,
                                            it.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                ) {
                                    override fun getHeaders(): MutableMap<String, String> {
                                        val headers = HashMap<String, String>()
                                        headers["Accept"] = "application/json"
                                        headers["Content-Type"] = "application/json"

                                        return headers
                                    }
                                }
                                queue.add(jsonObjectRequest)
                            }


                    } else {
                        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Password should be more than or equal 4 digits",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


        }
        return binding.root


    }
}

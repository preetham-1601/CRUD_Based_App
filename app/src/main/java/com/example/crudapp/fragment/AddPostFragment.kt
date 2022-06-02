package com.example.crudapp.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.crudapp.MainActivity
import com.example.crudapp.Post
import com.example.crudapp.PostRVModel
import com.example.crudapp.R
import com.example.crudapp.databinding.FragmentAddPostBinding
import com.example.crudapp.util.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*


class AddPostFragment : Fragment() {
    private var _binding: FragmentAddPostBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var postId: String
    private lateinit var postRVModel: Post


    private var filePath: Uri? = null
    lateinit var imagePreview: ImageView

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
        _binding = FragmentAddPostBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        sessionManager = SessionManager(activity as Context)


        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("Posts")

        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.toolbar.toolbar.title = "Create Post"
        binding.toolbar.toolbar.setNavigationOnClickListener { view ->
            findNavController().navigate(R.id.action_addPostFragment_to_homeFragment)
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_addPostFragment_to_homeFragment)

            }
        })


        binding.btnUp.setOnClickListener {
            selectImage()
        }
        binding.reset.setOnClickListener {
            binding.outlinedTextField.editText?.text?.clear()
            binding.outlinedTextField2.editText?.text?.clear()
        }


        binding.save.setOnClickListener {
            val postImg = filePath
            val postTitle = binding.outlinedTextField.editText?.text.toString()
            val postDescription = binding.outlinedTextField2.editText?.text.toString()
            postId = postTitle
            postRVModel = Post(postId,postTitle,postImg.toString(),postDescription)

            databaseReference.child(postId).setValue(postRVModel)
            // displaying a toast message.
            Toast.makeText(context, "Post Added..", Toast.LENGTH_SHORT)
                .show()
            // starting a main activity.
            findNavController().navigate(R.id.action_addPostFragment_to_homeFragment)

            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // on below line we are setting data in our firebase database.
                    //databaseReference.child(postId).setValue(postRVModel)
                    // displaying a toast message.
                    //Toast.makeText(context, "Post Added..", Toast.LENGTH_SHORT)
                     //   .show()
                    // starting a main activity.
                    //findNavController().navigate(R.id.action_addPostFragment_to_homeFragment)

                }

                override fun onCancelled(error: DatabaseError) {
                    // displaying a failure message on below line.
                    Toast.makeText(
                        context,
                        "Fail to add Post..",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

            //Toast.makeText(context, "$filePath", Toast.LENGTH_SHORT).show()

        }
        return binding.root

    }

    fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent,100)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            filePath = data?.data!!
            binding.imgUpld.setImageURI(filePath)

        }
    }

}
package com.example.crudapp.fragment

import android.app.Activity
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
import androidx.core.net.toUri
import androidx.core.text.set
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.crudapp.MainActivity
import com.example.crudapp.R
import com.example.crudapp.databinding.FragmentAddPostBinding
import com.example.crudapp.databinding.FragmentEditPostBinding
import com.example.crudapp.util.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class EditPostFragment : Fragment() {
    private var _binding: FragmentEditPostBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var postId: String

    private var filePath: Uri? = null
    lateinit var imagePreview: ImageView

    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditPostBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        sessionManager = SessionManager(activity as Context)
        firebaseDatabase = FirebaseDatabase.getInstance()


        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.toolbar.toolbar.title = "Edit Post"
        binding.toolbar.toolbar.setNavigationOnClickListener { view ->
            findNavController().navigate(R.id.action_editPostFragment_to_homeFragment)
        }
        val sun = arguments?.getString("image_url")?.toUri()
        val bun = arguments?.getString("title")
        val gun = arguments?.getString("desc")
        val kus = arguments?.getString("id")
        if(sun == null){

        }else{

            binding.imgUpld.setImageURI(sun)
        }
        binding.outlinedTextField.editText?.setText(bun)
        binding.outlinedTextField2.editText?.setText(gun)
        postId = kus.toString()

        databaseReference = firebaseDatabase.getReference("Posts").child(postId)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_editPostFragment_to_homeFragment)

            }
        })

        binding.btnUp.setOnClickListener {
            selectImage()
        }

        binding.update.setOnClickListener {

            val postImage = filePath.toString()
            val postTitle = binding.outlinedTextField.editText?.text.toString()
            val postDescription = binding.outlinedTextField2.editText?.text.toString()
            postId = kus.toString()

            val map: MutableMap<String, Any> = HashMap()
            map["postImage"] = postImage
            map["postTitle"] = postTitle
            map["postDescription"] = postDescription
            map["postId"] = postId

            databaseReference.updateChildren(map)
            Toast.makeText(context, "Post Updated..", Toast.LENGTH_SHORT)
                .show()
            findNavController().navigate(R.id.action_editPostFragment_to_homeFragment)



        }
        binding.delete.setOnClickListener {
            deletePost()

        }



        return binding.root
    }

    private fun deletePost() {
        databaseReference.removeValue()
        Toast.makeText(context, "Post Deleted", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_editPostFragment_to_homeFragment)

    }
    fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent,100)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            filePath = data?.data!!
            binding.imgUpld.setImageURI(filePath)

        }
    }

}
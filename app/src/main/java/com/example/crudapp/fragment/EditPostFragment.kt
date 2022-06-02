package com.example.crudapp.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
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

    lateinit var postId: String

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
        databaseReference = firebaseDatabase.getReference("Posts").child(postId)

        binding.update.setOnClickListener {

            val postImg = filePath.toString()
            val postTitle = binding.outlinedTextField.editText?.text.toString()
            val postDescription = binding.outlinedTextField2.editText?.text.toString()

            val map: MutableMap<String, Any> = HashMap()
            map["postImg"] = postImg
            map["postTitle"] = postTitle
            map["postDescription"] = postDescription
            map["postId"] = postId

            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    // adding a map to our database.
                    databaseReference.updateChildren(map)
                    // on below line we are displaying a toast message.
                    Toast.makeText(context, "Post Updated..", Toast.LENGTH_SHORT)
                        .show()
                    // opening a new activity after updating our coarse.
                    findNavController().navigate(R.id.action_editPostFragment_to_homeFragment)

                }

                override fun onCancelled(error: DatabaseError) {
                    // displaying a failure message on toast.
                    Toast.makeText(
                        context,
                        "Fail to update course..",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
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

}
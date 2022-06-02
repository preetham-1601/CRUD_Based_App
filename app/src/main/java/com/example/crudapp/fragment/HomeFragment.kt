package com.example.crudapp.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crudapp.Post
import com.example.crudapp.PostRVAdapter
import com.example.crudapp.R
import com.example.crudapp.databinding.FragmentHomeBinding
import com.example.crudapp.util.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(){
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var sessionManager: SessionManager
    private lateinit var postRecyclerView: RecyclerView
    private lateinit var postArrayList:ArrayList<Post>

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

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
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("Posts")

        postRecyclerView = binding.recyclerView
        postRecyclerView.layoutManager = LinearLayoutManager(context)
        postArrayList = arrayListOf<Post>()
        getPostData()

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity!!.finish()
            }
        })


        //var postList = arrayListOf(post)
        //recyclerView = binding.recyclerView
        //Toast.makeText(context, "$post", Toast.LENGTH_SHORT).show()
        //postAdapter = PostRVAdapter(context as Activity,postList)
        //recyclerView.layoutManager = LinearLayoutManager(context)
        //recyclerView.adapter = postAdapter

        //getPosts()

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
    /*private fun getPosts() {
        // on below line clearing our list.
        postList.clear()
        // on below line we are calling add child event listener method to read the data.
        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(
                @NonNull snapshot: DataSnapshot,
                @Nullable previousChildName: String?
            ) {
                // adding snapshot to our array list on below line.
                snapshot.getValue(Post::class.java)?.let { postList.add(it) }
                // notifying our adapter that data has changed.
                postRVAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(
                @NonNull snapshot: DataSnapshot,
                @Nullable previousChildName: String?
            ) {
                // this method is called when new child is added
                // we are notifying our adapter and making progress bar
                // visibility as gone.
                postRVAdapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(@NonNull snapshot: DataSnapshot) {
                // notifying our adapter when child is removed.
                postRVAdapter.notifyDataSetChanged()
            }

            override fun onChildMoved(
                @NonNull snapshot: DataSnapshot,
                @Nullable previousChildName: String?
            ) {
                // notifying our adapter when child is moved.
                postRVAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(@NonNull error: DatabaseError) {}
        })
    }*/


    private fun getPostData() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Posts")

        databaseReference.addValueEventListener(object :ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    for(postSnapshot in snapshot.children){

                        val post = postSnapshot.getValue(Post::class.java)
                        postArrayList.add(post!!)
                    }
                    postRecyclerView.adapter = PostRVAdapter(context as Activity,postArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}
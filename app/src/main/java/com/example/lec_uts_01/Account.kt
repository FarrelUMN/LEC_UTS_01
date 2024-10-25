package com.example.lec_uts_01

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Account.newInstance] factory method to
 * create an instance of this fragment.
 */
class Account : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize FirebaseAuth and Firebase Realtime Database reference
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userEmailTextView = view.findViewById<TextView>(R.id.account_email_text)
        val userNameTextView = view.findViewById<TextView>(R.id.account_name_text)
        val userNimTextView = view.findViewById<TextView>(R.id.sync_account)
        val logoutButton = view.findViewById<Button>(R.id.logout_button)

        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid

            // Fetch user data from Firebase Realtime Database
            database.child("users").child(userId).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val name = snapshot.child("name").getValue(String::class.java)
                        val sync = snapshot.child("sync").getValue(String::class.java)
                        val email = currentUser.email

                        userEmailTextView.text = "Email: $email"
                        userNameTextView.text = "Username: $name"
                        userNimTextView.text = "sync: $sync"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("AccountFragment", "Failed to retrieve user data", error.toException())
                }
            })
        } else {
            findNavController().navigate(R.id.login)
        }

        logoutButton.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.login)
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
        }
    }
}
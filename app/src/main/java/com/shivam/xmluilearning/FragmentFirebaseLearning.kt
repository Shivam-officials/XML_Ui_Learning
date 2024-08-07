package com.shivam.xmluilearning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.shivam.xmluilearning.databinding.FragmentFirebaseLearningBinding
import com.shivam.xmluilearning.model.School


class FragmentFirebaseLearning : Fragment() {


    private var _binding: FragmentFirebaseLearningBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseRef: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_firebase_learning, container, false)
        _binding = FragmentFirebaseLearningBinding.inflate(inflater,container,false)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseRef = FirebaseDatabase.getInstance().getReference("test")

        binding.idUploadButton.setOnClickListener{
            val name = binding.idDrivingSchoolName.text.toString()
            val address = binding.idSchoolAddress.text.toString()
            val ratings = binding.idRatings.text.toString()
            val ratingCount = binding.idRatingCount.text.toString()
            val school = School(name,address,ratings,ratingCount)
            firebaseRef.setValue(school)
                .addOnCompleteListener {
                    Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show()
                }
        }

    }

}
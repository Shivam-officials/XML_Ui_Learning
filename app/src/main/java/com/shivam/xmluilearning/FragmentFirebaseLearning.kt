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

        firebaseRef = FirebaseDatabase.getInstance().getReference("schools list")

        binding.idUploadButton.setOnClickListener{
            saveData()
        }

    }

    private fun saveData() {
        val name = binding.idDrivingSchoolName.text.toString()
        val address = binding.idSchoolAddress.text.toString()
        val ratings = binding.idRatings.text.toString()
        val ratingCount = binding.idRatingCount.text.toString()

        // make a unique key for every data entry
        val schoolId = firebaseRef.push().key!!

        // make the data class that u want to insert with the key
        val school = School(schoolId, name, address, ratings, ratingCount)

        // check if the fields are not empty
        if (name.isNotBlank() && address.isNotBlank() && ratings.isNotBlank() && ratingCount.isNotBlank()) {

            // insert the data into the database
            firebaseRef.child(schoolId).setValue(school)
                // if the data is inserted successfully
                .addOnCompleteListener {
                    Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show()
                    binding.idDrivingSchoolName.text = null
                    binding.idSchoolAddress.text = null
                    binding.idRatings.text = null
                    binding.idRatingCount.text = null
                }
                // if the data is not inserted successfully
                .addOnCanceledListener {
                    Toast.makeText(activity, "failed", Toast.LENGTH_SHORT).show()
                }

        } else
        // if the fields are empty
        {
            binding.idDrivingSchoolName.error = "fill this field"
            binding.idSchoolAddress.error = "fill this field"
            binding.idRatings.error = "fill this field"
            binding.idRatingCount.error = "fill this field"

            Toast.makeText(activity, "plz fill all the fields", Toast.LENGTH_LONG).show()
        }
    }

}
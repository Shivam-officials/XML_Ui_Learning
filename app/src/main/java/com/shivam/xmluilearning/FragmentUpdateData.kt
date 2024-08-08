package com.shivam.xmluilearning

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shivam.xmluilearning.databinding.FragmentUpdateDataBinding
import com.shivam.xmluilearning.model.School


/**
 * A simple [Fragment] subclass.
 * Use the [FragmentUpdateData.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentUpdateData : Fragment() {

    private var  firebaseRef  = FirebaseDatabase.getInstance().getReference("schools list")
     private  val args : FragmentUpdateDataArgs by navArgs()


    private var _binding: FragmentUpdateDataBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_update_data, container, false)

        _binding = FragmentUpdateDataBinding.inflate(inflater,container,false)
        return binding.root
    }

    /**
     * Called immediately after [.onCreateView]
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     * @param view The View returned by [.onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.idUpdateButton.setOnClickListener {
            updateData(args.id)
        }
        fillFieldsFromIdFromFirebase()

    }

    private fun updateData( id: String ) {
//        TODO("Not yet implemented")
        val name = binding.idDrivingSchoolName.text.toString()
        val address = binding.idSchoolAddress.text.toString()
        val ratings = binding.idRatings.text.toString()
        val ratingCount = binding.idRatingCount.text.toString()

        val school = School(id,name,address,ratings,ratingCount)

        firebaseRef.child(id).setValue(school)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "data updated", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
            .addOnCanceledListener {
                Toast.makeText(context, "data not updated", Toast.LENGTH_SHORT).show()
            }

    }

    // get the data from the firebase
    private fun fillFieldsFromIdFromFirebase(id: String = args.id) {


        val schoolData =
            firebaseRef.orderByKey().equalTo(id).addListenerForSingleValueEvent(object : ValueEventListener {
                /**
                 * This method will be called with a snapshot of the data at this location. It will also be called
                 * each time that data changes.
                 *
                 * @param snapshot The current data at the location
                 */
                override fun onDataChange(snapshot: DataSnapshot) {
    //                    TODO("Not yet implemented")
                    val dataReceived: School
                    if (snapshot.exists()) {
                        Log.d("UpdateDataFragment", "onDataChange: $snapshot")
                        dataReceived = snapshot.children.first().getValue(School::class.java)!!
                        with(binding) {
                            idDrivingSchoolName.setText(dataReceived.name)
                            idSchoolAddress.setText(dataReceived.address)
                            idRatings.setText(dataReceived.ratings)
                            idRatingCount.setText(dataReceived.ratingCount)
                        }
                    }


                }

                /**
                 * This method will be triggered in the event that this listener either failed at the server, or
                 * is removed as a result of the security and Firebase Database rules. For more information on
                 * securing your data, see: [ Security
     * Quickstart](https://firebase.google.com/docs/database/security/quickstart)
                 *
                 * @param error A description of the error that occurred
                 */
                override fun onCancelled(error: DatabaseError) {
    //                    TODO("Not yet implemented")
                    Toast.makeText(
                        context,
                        "error in fetching the data from firebase",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })



//        binding.apply {
//            idDrivingSchoolName.setText(id)
//        }
    }
}
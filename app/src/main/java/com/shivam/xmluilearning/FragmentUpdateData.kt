package com.shivam.xmluilearning

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.PackageManagerCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.common.wrappers.PackageManagerWrapper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.shivam.xmluilearning.databinding.FragmentUpdateDataBinding
import com.shivam.xmluilearning.model.School


/**
 * A simple [Fragment] subclass.
 * Use the [FragmentUpdateData.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentUpdateData : Fragment() {

    private var  firebaseRef  = FirebaseDatabase.getInstance().getReference("schools list")
    private var storageRef = FirebaseStorage.getInstance().getReference("images")
     private  val args : FragmentUpdateDataArgs by navArgs()

    private var _binding: FragmentUpdateDataBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageUri: Uri


    /**
     * Called to do initial creation of a fragment.  This is called after
     * [.onAttach] and before
     * [.onCreateView].
     *
     *
     * Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, add a [androidx.lifecycle.LifecycleObserver] on the
     * activity's Lifecycle, removing it when it receives the
     * [Lifecycle.State.CREATED] callback.
     *
     *
     * Any restored child fragments will be created before the base
     * `Fragment.onCreate` method returns.
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */

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

        // update the data in the firebase
        binding.idUpdateButton.setOnClickListener {
            updateData(args.id)
            findNavController().navigateUp()
        }

        // pick image from the gallery and set it to the image view
        val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()){
            if (it != null){
                imageUri = it
                binding.idSchoolImage.setImageURI(it)
            }else{
                Toast.makeText(activity, "select an image please", Toast.LENGTH_SHORT).show()
            }

        }
        binding.idSchoolImage.setOnClickListener {
            imagePicker.launch("image/*")
        }


        binding.idDeleteBtn.setOnClickListener {
            deleteData(args.id)
        }

        // get the data from the firebase to populate the fields
        fillFieldsFromIdFromFirebase()


    }



    fun deleteData(id: String) {
//        TODO("Not yet implemented")


        // delete the data from the firebase
        firebaseRef.child(id).removeValue()
            .addOnCompleteListener{
                if (it.isSuccessful) {
                    Toast.makeText(context, "data deleted", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }.addOnCanceledListener {
                Toast.makeText(context, "data not deleted", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateData( id: String ) {
            val name = binding.idDrivingSchoolName.text.toString()
            val address = binding.idSchoolAddress.text.toString()
            val ratings = binding.idRatings.text.toString()
            val ratingCount = binding.idRatingCount.text.toString()

            // make a unique key for every data entry
            val schoolId = firebaseRef.push().key!!

            // make the data class that u want to insert with the key
            var school :School

            // check if the fields are not empty
            if (name.isNotBlank() && address.isNotBlank() && ratings.isNotBlank() && ratingCount.isNotBlank() && imageUri != null) {
                val storageId =
                    storageRef.child(schoolId).putFile(imageUri!!)
                        .addOnCompleteListener{
                            it.result.metadata?.reference?.downloadUrl?.addOnSuccessListener {uri->
                                Toast.makeText(context, "image saved", Toast.LENGTH_SHORT).show()


                                school = School(schoolId, name, address, ratings, ratingCount, uri.toString())
                                // insert the data into the database
                                firebaseRef.child(schoolId).setValue(school)
                                    // if the data is inserted successfully
                                    .addOnCompleteListener {
                                        Toast.makeText(context, "data saved to RLDB", Toast.LENGTH_SHORT).show()
                                        binding.idDrivingSchoolName.text = null
                                        binding.idSchoolAddress.text = null
                                        binding.idRatings.text = null
                                        binding.idRatingCount.text = null
                                        findNavController().navigateUp()
                                    }
                                    // if the data is not inserted successfully
                                    .addOnCanceledListener {
                                        Toast.makeText(context, "data saving failed for RLDB", Toast.LENGTH_SHORT).show()
                                    }
                            }

                        }
                        .addOnCanceledListener {
                            Toast.makeText(context, "Image saved failed", Toast.LENGTH_SHORT).show()
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

            /**
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
            findNavController().navigateUp()
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
             */

       /**
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

        */
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
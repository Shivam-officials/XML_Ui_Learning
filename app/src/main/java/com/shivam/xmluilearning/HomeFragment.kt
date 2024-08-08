package com.shivam.xmluilearning

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shivam.xmluilearning.adaptor.SchoolCardAdaptor
import com.shivam.xmluilearning.databinding.FragmentHomeBinding
import com.shivam.xmluilearning.model.School

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    //list of schools
    private var schoolList = ArrayList<School>()

    //realtime database reference
    private val firebaseRef = FirebaseDatabase.getInstance().getReference("schools list")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false)
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
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

        binding.idAddSchool.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_fragment_firebase_learning)
        }


        binding.idBtnGoToScreen5.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_screen5)
        }

        // set up the recycler view or initialising the recyclerView
        binding.idSchoolsListView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
        fetchData()

    }

    private fun fetchData() {
        firebaseRef.addValueEventListener(object : ValueEventListener {
            /**
             * This method will be called with a snapshot of the data at this location. It will also be called
             * each time that data changes.
             *
             * @param snapshot The current data at the location
             */
            override fun onDataChange(snapshot: DataSnapshot) {
//                TODO("Not yet implemented")

                // clear the list before adding new data
                schoolList.clear()
                // loop through the data and add it to the list
                if (snapshot.exists()){
                    Log.d("HomeFragment", "onDataChange: ${snapshot.children.elementAt(4)}")
                   for (school in snapshot.children){
                      val schoolData = school.getValue(School::class.java)
                       schoolList.add(schoolData!!)
                   }
                }

                val schoolListAdaptor = SchoolCardAdaptor(schoolList)
                binding.idSchoolsListView.adapter = schoolListAdaptor
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
//                TODO("Not yet implemented")
                Toast.makeText(context, "error $error", Toast.LENGTH_SHORT).show()
            }

        })
    }
}
package com.shivam.xmluilearning

import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shivam.xmluilearning.databinding.FragmentScreen5Binding
import com.shivam.xmluilearning.databinding.LayoutBenefitsBinding
import com.shivam.xmluilearning.databinding.LayoutLessonCardBinding
import com.shivam.xmluilearning.databinding.LayoutLessonListBinding
import com.shivam.xmluilearning.databinding.LayoutServicesListBinding


/**
 * A simple [Fragment] subclass.
 * Use the [screen5.newInstance] factory method to
 * create an instance of this fragment.
 */
class screen5 : Fragment() {


     private var _binding: FragmentScreen5Binding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_screen5, container, false)
        _binding = FragmentScreen5Binding.inflate(inflater,container,false)

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
        binding.idLayoutLessonList.lessonListCard2.lessonDay.text = "day 2"
        binding.idLayoutLessonList.lessonListCard3.lessonDay.text = "day 3"
        binding.idLayoutLessonList.lessonListCard4.lessonDay.text = "day 4"
        binding.idLayoutLessonList.lessonListCard5.lessonDay.text = "day 5"


    }
}
package com.shivam.xmluilearning.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shivam.xmluilearning.R

class Screen5 : Fragment(R.layout.fragment_screen5) {

    companion object {
        fun newInstance() = Screen5()
    }

    private val viewModel: Screen5ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_screen5, container, false)
    }
}
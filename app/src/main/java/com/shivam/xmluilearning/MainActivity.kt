package com.shivam.xmluilearning

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.shivam.xmluilearning.databinding.ActivityMainBinding
import com.shivam.xmluilearning.databinding.LayoutBenefitsBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Check if the fragment container is null to avoid overlapping fragments
        if (savedInstanceState == null) {
            // Create an instance of the fragment
            val fragmentScreen5 = screen5()
            val firebaseLearningFragment = FragmentFirebaseLearning()

            // Attach the fragment to the activity
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,firebaseLearningFragment )
                .commit()
        }

    }
}
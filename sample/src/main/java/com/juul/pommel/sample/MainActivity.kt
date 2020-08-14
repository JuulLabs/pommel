package com.juul.pommel.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.juul.pommel.sample.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var greeter: Greeter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.hello.text = greeter.greet()
        binding.subGreeting.text = greeter.subGreeting()
    }
}

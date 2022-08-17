package com.example.understandingannotation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.understandingannotation.demo.SomeInnerState
import com.example.understandingannotation.demo.SomeState
import com.example.understandingannotation.demo.toAnalyticsParamsMap

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

fun main() {
    val state = SomeState(
        firstName = "Kshitij",
        secondName = "Kumar",
        location = SomeInnerState(
            city = "sdfgs",
            country = "sdfsd",
            pin = 123
        )
    )

    println("state map: ${state.toAnalyticsParamsMap()}")
}
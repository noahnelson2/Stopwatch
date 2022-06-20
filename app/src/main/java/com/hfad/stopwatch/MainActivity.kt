package com.hfad.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hfad.stopwatch.databinding.ActivityMainBinding
import android.os.PersistableBundle

import android.os.SystemClock

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var running = false
    var offset: Long = 0


    fun setBaseTime() {
        binding.stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    fun saveOffset(){
        offset = SystemClock.elapsedRealtime() - binding.stopwatch.base
    }

    //Keys
    val OFFSET_KEY = "offset"
    val RUNNING_KEY = "running"
    val BASE_KEY = "base"




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        //Restoration
        if (savedInstanceState != null) {
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if (running) {
                binding.stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                binding.stopwatch.start()
            } else setBaseTime()
        }

        binding.startButton.setOnClickListener {
            if (!running) {
                setBaseTime()
                binding.stopwatch.start()
                running = true
            }
        }

        binding.pauseButton.setOnClickListener{
            if (running){
                saveOffset()
                binding.stopwatch.stop()
                running = false
            }
        }

        binding.resetButton.setOnClickListener{
            offset = 0
            setBaseTime()
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putLong(OFFSET_KEY, offset)
        savedInstanceState.putBoolean(RUNNING_KEY, running)
        savedInstanceState.putLong(BASE_KEY, binding.stopwatch.base)
        super.onSaveInstanceState(savedInstanceState)
    }


    override fun onPause() {
        super.onPause()
        if (running) {
            saveOffset()
            binding.stopwatch.stop()
        }
    }

    override fun onResume(){
        super.onResume()
        if(running){
            setBaseTime()
            binding.stopwatch.start()
            offset = 0
        }
    }
}
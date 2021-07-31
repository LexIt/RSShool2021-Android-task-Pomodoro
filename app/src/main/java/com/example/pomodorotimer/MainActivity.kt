package com.example.pomodorotimer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pomodorotimer.databinding.ActivityMainBinding
import androidx.lifecycle.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), PomodoroListener {

    private lateinit var binding: ActivityMainBinding
    private var startTime = 0L

    private val timerAdapter = TimerAdapter(this)
    private val timers = mutableListOf<TimerData>()
    private var nextId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startTime = System.currentTimeMillis()

//        lifecycleScope.launch(Dispatchers.Main){
//            while(true){
//                binding.timerView.text = (System.currentTimeMillis() - startTime).displayTime()
//                delay(INTERVAL)
//            }
//        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = timerAdapter
        }

        binding.addNewTimerButton.setOnClickListener {
            timers.add(TimerData(nextId++, 0, false))
            timerAdapter.submitList(timers.toList())
        }
    }

    override fun start(id: Int){
        changeTimer(id, null, true)
    }

    override fun stop(id: Int, currentMs: Long){
        changeTimer(id, currentMs, false)
    }

    override fun reset(id: Int){
        changeTimer(id, 0L, false)
    }

    override fun delete(id: Int){
        timers.remove(timers.find { it.id == id })
        timerAdapter.submitList(timers.toList())
    }

    private fun changeTimer(id: Int, currentMs: Long?, isStarted: Boolean){
        val newTimers = mutableListOf<TimerData>()
        timers.forEach {
            if (it.id == id) {
                newTimers.add(TimerData(it.id, currentMs ?: it.currentMs, isStarted))
            } else {
                newTimers.add(it)
            }
        }
        timerAdapter.submitList(newTimers)
        timers.clear()
        timers.addAll(newTimers)
    }

    private companion object{
        private const val INTERVAL = 10L
    }
}
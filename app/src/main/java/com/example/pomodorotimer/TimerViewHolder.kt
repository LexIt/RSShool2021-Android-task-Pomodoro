package com.example.pomodorotimer

import android.content.res.Resources
import android.graphics.drawable.AnimationDrawable
import android.os.CountDownTimer
import androidx.recyclerview.widget.RecyclerView
import com.example.pomodorotimer.databinding.TimerItemBinding
import androidx.core.view.isInvisible

class TimerViewHolder(
    private val binding: TimerItemBinding,
    private val listener: PomodoroListener,
    private val resources: Resources
    ): RecyclerView.ViewHolder(binding.root) {

    private var timer: CountDownTimer? = null

    fun bind(timerData: TimerData) {
        binding.stopwatchTimer.text = timerData.currentMs.displayTime()

        if (timerData.isStarted) {
            startTimer(timerData)
        } else {
            stopTimer(timerData)
        }

        initButtonsListeners(timerData)
    }

    private fun initButtonsListeners(timerData: TimerData) {
        binding.startPauseButton.setOnClickListener {
            if (timerData.isStarted) {
                listener.stop(timerData.id, timerData.currentMs)
            } else {
                listener.start(timerData.id)
            }
        }

        binding.restartButton.setOnClickListener { listener.reset(timerData.id) }

        binding.deleteButton.setOnClickListener { listener.delete(timerData.id) }
    }

    private fun startTimer(timerData: TimerData) {
        //val drawable = resources.getDrawable(R.drawable.ic_baseline_pause_24)
        val drawable = resources.getDrawable(R.drawable.ic_baseline_pause_24)
        binding.startPauseButton.setImageDrawable(drawable)

        timer?.cancel()
        timer = getCountDownTimer(timerData)
        timer?.start()

        binding.blinkingIndicator.isInvisible = false
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
    }

    private fun stopTimer(timerData: TimerData) {
        val drawable = resources.getDrawable(R.drawable.ic_baseline_play_arrow_24)
        binding.startPauseButton.setImageDrawable(drawable)

        timer?.cancel()

        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
    }

    private fun getCountDownTimer(timerData: TimerData): CountDownTimer {
        return object : CountDownTimer(PERIOD, UNIT_TEN_MS) {
            val interval = UNIT_TEN_MS

            override fun onTick(millisUntilFinished: Long) {
                timerData.currentMs += interval
                binding.stopwatchTimer.text = timerData.currentMs.displayTime()
            }

            override fun onFinish() {
                binding.stopwatchTimer.text = timerData.currentMs.displayTime()
            }
        }
    }

    private fun Long.displayTime(): String {
        if (this <= 0L) {
            return START_TIME
        }
        val h = this / 1000 / 3600
        val m = this / 1000 % 3600 / 60
        val s = this / 1000 % 60
        val ms = this % 1000 / 10

        return "${displaySlot(h)}:${displaySlot(m)}:${displaySlot(s)}:${displaySlot(ms)}"
    }

    private fun displaySlot(count: Long): String {
        return if (count / 10L > 0) {
            "$count"
        } else {
            "0$count"
        }
    }

    private companion object {

        private const val START_TIME = "00:00:00:00"
        private const val UNIT_TEN_MS = 10L
        private const val PERIOD  = 1000L * 60L * 60L * 24L // Day
    }
}
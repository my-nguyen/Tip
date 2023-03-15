package com.rahulpandey.tip

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.content.ContextCompat
import com.rahulpandey.tip.databinding.ActivityMainBinding

private const val TAG = "MainActivity-Truong"
private const val INITIAL_PERCENT = 15

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.seekBar.progress = INITIAL_PERCENT
        binding.percent.text = "$INITIAL_PERCENT%"
        updateDescription(INITIAL_PERCENT)

        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d(TAG, "onProgressChanged $progress")
                binding.percent.text = "$progress%"
                computeTipAndTotal()
                updateDescription(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        binding.base.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.d(TAG, "afterTextChanged $p0")
                computeTipAndTotal()
            }
        })
    }

    private fun updateDescription(percent: Int) {
        binding.description.text = when (percent) {
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }

        // update the description color based on the tip percent
        binding.description.setTextColor(
            ArgbEvaluator().evaluate(
                percent.toFloat() / binding.seekBar.max,
                ContextCompat.getColor(this, R.color.worst_tip),
                ContextCompat.getColor(this, R.color.best_tip)
            ) as Int
        )
    }

    private fun computeTipAndTotal() {
        if (binding.base.text.isEmpty()) {
            binding.tip.text = ""
            binding.total.text = ""
        } else {
            // get the value of the base and tip percent
            val base = binding.base.text.toString().toDouble()
            val percent = binding.seekBar.progress

            // compute the tip and total
            val tip = base * percent / 100
            val total = base + tip

            // update the UI
            binding.tip.text = "%.2f".format(tip)
            binding.total.text = "%.2f".format(total)
        }
    }
}
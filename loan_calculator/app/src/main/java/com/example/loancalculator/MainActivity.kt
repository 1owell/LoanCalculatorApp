package com.example.loancalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.NumberFormatException
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    // variables used when saving/restoring state
    private var loanAmount   = 0.00
    private var interestRate = 0

    companion object {
        private val LOAN_AMOUNT   = "LOAN_AMOUNT"
        private val INTEREST_RATE = "INTEREST_RATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // check if app just started or is being restored from memory
        if (savedInstanceState != null) {
            // the app is being restored
            loanAmount   = savedInstanceState.getDouble(LOAN_AMOUNT)
            interestRate = savedInstanceState.getInt(INTEREST_RATE)
        }
        loanAmountEditText.addTextChangedListener(loanAmountTextWatcher)

        // get the seek bar value when it changes
        interestRateSeekBar.setOnSeekBarChangeListener(customSeekBarListener)
    }

    private fun updateValues(rate: Int, principal: Double) {
        val years: IntArray = intArrayOf(5, 10, 15, 20, 25, 30)
        for (year in years) {
            val r: Double = rate / 1200.0
            val n: Double = year * 12.0
            var emi = 0.0
            if (((1 + r).pow(n) - 1) != 0.0) {
                emi = ( principal * r * (1 + r).pow(n)) / ((1 + r).pow(n) - 1)
            }
            when (year) {
                5  -> {
                    fiveYearEMIText.setText(String.format("%.02f", emi))
                    fiveYearTotalText.setText(String.format("%.2f", emi * n))
                }
                10 -> {
                    tenYearEMIText.setText(String.format("%.02f", emi))
                    tenYearTotalText.setText(String.format("%.02f", emi * n))
                }
                15 -> {
                    fifteenYearEMIText.setText(String.format("%.02f", emi))
                    fifteenYearTotalText.setText(String.format("%.02f", emi * n))
                }
                20 -> {
                    twentyYearEMIText.setText(String.format("%.02f", emi))
                    twentyYearTotalText.setText(String.format("%.02f", emi * n))
                }
                25 -> {
                    twentyFiveYearEMIText.setText(String.format("%.02f", emi))
                    twentyFiveYearTotalText.setText(String.format("%.02f", emi * n))
                }
                30 -> {
                    thirtyYearEMIText.setText(String.format("%.02f", emi))
                    thirtyYearTotalText.setText(String.format("%.02f", emi * n))
                }
                else -> return
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putDouble(LOAN_AMOUNT, loanAmountEditText.text.toString().toDouble())
        outState.putInt(INTEREST_RATE, interestRateSeekBar.progress)
    }

    // seek bar change listener
    private val customSeekBarListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            interestRateTextView.text = progress.toString() + "%"
            try {
                updateValues(rate = progress, principal = loanAmountEditText.text.toString().toDouble())
            }
            catch (e: NumberFormatException) {
                updateValues(rate = progress, principal = 0.00)
            }

        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    }

    private val loanAmountTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            try {
                updateValues(rate = interestRateSeekBar.progress, principal = s.toString().toDouble())
            }
            catch (e: NumberFormatException) {
                updateValues(rate = interestRateSeekBar.progress, principal = 1.00)
            }
        }

    }
}
package com.example.tintint_jw.Matching

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.tintint_jw.R
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import kotlinx.android.synthetic.main.activity_filter.*

class FilterActivity : AppCompatActivity() {
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        back.setOnClickListener(){
            finish()
        }

        genderSegmentedGroup.setTintColor(resources.getColor(R.color.tingtingMain),resources.getColor(R.color.white))
        memberSegmentedGroup.setTintColor(resources.getColor(R.color.tingtingMain), resources.getColor(R.color.white))

        val ageRange: RangeSeekBar = findViewById(R.id.ageRange)
        ageRange.invalidate()
        ageRange.leftSeekBar.setIndicatorText("20세")
        ageRange.rightSeekBar.setIndicatorText("29세")
        ageRange.setProgress(20F, 29F)

        ageRange.setRange(20F, 29F)
        ageRange.setOnRangeChangedListener(object: OnRangeChangedListener{
            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
                ageMin.setText("")
                ageMax.setText("")
        }

            override fun onRangeChanged(
                view: RangeSeekBar?,
                leftValue: Float,
                rightValue: Float,
                isFromUser: Boolean
            ) {
                ageRange.leftSeekBar.setIndicatorText(leftValue.toInt().toString()+"세")
                ageRange.rightSeekBar.setIndicatorText(rightValue.toInt().toString()+"세")
                /*ageMin.setText(leftValue.toInt().toString())
                ageMax.setText(rightValue.toInt().toString())*/
            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

                ageMin.setText("")
                ageMax.setText("")
            }

        })

    }
}
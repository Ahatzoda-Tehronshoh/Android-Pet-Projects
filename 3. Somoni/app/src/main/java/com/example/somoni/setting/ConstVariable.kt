package com.example.somoni.setting

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import com.example.somoni.R
import com.google.android.material.card.MaterialCardView
import java.math.BigDecimal

object ConstVariable {
    fun isChecked(cardView: MaterialCardView) {
        cardView.apply {
            strokeWidth = 5
            strokeColor = resources.getColor(R.color.selectedOBS)
        }
    }

    fun isUnChecked(cardView: MaterialCardView) {
        cardView.apply {
            strokeWidth = 2
            strokeColor = resources.getColor(R.color.notSelectedOBS)
        }
    }

    fun getGradient(firstColor: String, secondColor: String): GradientDrawable {
        val grad = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                Color.parseColor(firstColor),
                Color.parseColor(secondColor)
            )
        )
        grad.gradientType = GradientDrawable.LINEAR_GRADIENT
        return grad
    }


    fun chiselFormat(input: String): String {
        val result = StringBuilder("")

        var count = input.length - 1

        if(input.contains('.') && input[count] == '0') {
            while (input[count] == '0') {
                count--
            }
        }

        if(input[count] == '.')
            count--

        for(i in 0 .. count)
            result.append(input[i])

        if(result.toString() == "0")
            result.append(".0")

        return result.toString()
    }
}

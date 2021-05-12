package com.example.watch

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.*

class Watch @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
): View(context, attributeSet, defStyle) {

    private val hourArrowLengthDef = 300
    private val minuteArrowLengthDef = 300
    private val secondArrowLengthDef = 300
    private val circle: Paint
    private val rect: Paint
    private val hourArrow: Paint
    private val minuteArrow: Paint
    private val secondArrow: Paint
    private val hourArrowColor: Int
    private val minuteArrowColor: Int
    private val secondArrowColor: Int
    private val circleColor: Int
    private val partsColor: Int
    private val hourArrowSize: Float
    private val minuteArrowSize: Float
    private val secondArrowSize: Float

    init {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.Watch, 0, 0).apply {
            try {
                hourArrowColor = getColor(R.styleable.Watch_hour_arrow_color, Color.BLACK)
                minuteArrowColor = getColor(R.styleable.Watch_minute_arrow_color, Color.BLUE)
                secondArrowColor = getColor(R.styleable.Watch_second_arrow_color, Color.GREEN)
                circleColor = getColor(R.styleable.Watch_circle_color, Color.BLACK)
                partsColor = getColor(R.styleable.Watch_parts_color, Color.BLACK)
                hourArrowSize = getDimensionPixelSize(R.styleable.Watch_hour_arrow_size, hourArrowLengthDef).toFloat()
                minuteArrowSize = getDimensionPixelSize(R.styleable.Watch_minute_arrow_size, minuteArrowLengthDef).toFloat()
                secondArrowSize = getDimensionPixelSize(R.styleable.Watch_second_arrow_size, secondArrowLengthDef).toFloat()
            }
            finally {
                recycle()
            }
        }

        circle = Paint().apply {
            color = circleColor
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 15f
        }

        rect = Paint().apply {
            color = partsColor
            isAntiAlias = true
        }

        hourArrow = Paint().apply {
            color = hourArrowColor
            isAntiAlias = true
        }

        minuteArrow = Paint().apply {
            color = minuteArrowColor
            isAntiAlias = true
        }

        secondArrow = Paint().apply {
            color = secondArrowColor
            isAntiAlias = true
        }
    }

    fun setHourArrowColor(colorId: Int) {
       hourArrow.color = colorId
        invalidate()
    }

    fun setMinuteArrowColor(colorId: Int) {
        minuteArrow.color = colorId
        invalidate()
    }

    fun setSecondArrowColor(colorId: Int) {
        secondArrow.color = colorId
        invalidate()
    }

    private fun drawArrows(canvas: Canvas, centerX: Float, centerY: Float) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        val hourAngle = (hour + minute.toFloat() / 60) * 360 / 12
        val minuteAngle = (minute + second.toFloat() / 60) * 360 / 60
        val secondAngle = second * 360 / 60

        canvas.save()
        canvas.rotate(hourAngle, centerX, centerY)

        canvas.drawRect(centerX - 15f / 2, centerY - hourArrowSize / 2, centerX + 15f / 2, centerY + hourArrowSize / 6, hourArrow)
        canvas.restore()

        canvas.save()
        canvas.rotate(minuteAngle, centerX, centerY)

        canvas.drawRect(centerX - 10f / 2, centerY - minuteArrowSize * 3.5f / 5, centerX + 10f / 2, centerY + minuteArrowSize / 6, minuteArrow)
        canvas.restore()

        canvas.save()
        canvas.rotate(secondAngle.toFloat(), centerX, centerY)

        canvas.drawRect(centerX - 5f / 2, centerY - secondArrowSize + 40, centerX + 5f / 2, centerY + secondArrowSize / 6, secondArrow)
        canvas.restore()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerWidth = (width / 2).toFloat()
        val centerHeight = (height / 2).toFloat()

        canvas.drawCircle(
            centerWidth,
            centerHeight,
            300f,
            circle
        )

        val radius = 360f / 12f

        for(i in 1..12) {
            canvas.rotate(radius, centerWidth, centerHeight)
            val top = centerHeight + 10f
            val bottom = centerHeight - 10f
            val left = centerWidth + 300f - 35f
            val right = centerWidth + 300f
            canvas.drawRect(left, top, right, bottom, rect)
        }

        drawArrows(canvas, centerWidth, centerHeight)

        postInvalidateDelayed(1000)
    }
}
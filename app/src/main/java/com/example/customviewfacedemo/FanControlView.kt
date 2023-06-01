package com.example.customviewfacedemo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


class FanControlView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val SELECTION_COUNT = 4 // Total number of selections.

    private var mWidth = 0// Custom view width.

    private var mHeight = 0 // Custom view height.

    private val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG) // For text in the view.

    private val mDialPaint = Paint(Paint.ANTI_ALIAS_FLAG) // For dial circle in the view.

    private var mRadius = 0f // Radius of the circle.

    private var mActiveSelection = 0// The active selection.

    // String buffer for dial labels and float for ComputeXY result.
    private val mTempLabel = StringBuffer(8)
    private val mTempResult = FloatArray(2)

    private var mFanOffColor = Color.LTGRAY
    private var mFanOnColor = Color.LTGRAY

    init {
        mTextPaint.color = Color.BLACK
        mTextPaint.style = Paint.Style.FILL_AND_STROKE
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.textSize = 40f

        mDialPaint.color = mFanOffColor
        mActiveSelection = 0

        setOnClickListener { // Rotate selection to the next valid choice.
            mActiveSelection = (mActiveSelection + 1) % SELECTION_COUNT
            // Set dial background color to green if selection is >= 1.
            if (mActiveSelection >= 1) {
                mDialPaint.color = mFanOnColor
            } else {
                mDialPaint.color = mFanOffColor
            }
            // Redraw the view.
            invalidate()
        }

        setupAttributes(attrs)
    }

    private fun setupAttributes(attrs: AttributeSet) {
        // Obtain a typed array of attributes
        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.FanControlView,
            0, 0
        )

        // Extract custom attributes into member variables
        mFanOffColor = typedArray.getColor(
            R.styleable.FanControlView_fanOffColor,
            mFanOffColor
        )
        mFanOnColor = typedArray.getColor(
            R.styleable.FanControlView_fanOnColor,
            mFanOnColor
        )

        // TypedArray objects are shared and must be recycled.
        typedArray.recycle()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        mRadius = ((min(mWidth, mHeight) / 2 * 0.8).toFloat())
    }

    private fun computeXYForPosition(pos: Int, radius: Float): FloatArray {
        val result = mTempResult
        val startAngle = Math.PI * (9 / 8.0) // Angles are in radians.
        val angle = startAngle + pos * (Math.PI / 4)
        result[0] = (radius * cos(angle)).toFloat() + mWidth / 2
        result[1] = (radius * sin(angle)).toFloat() + mHeight / 2
        return result
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw the dial.
        canvas.drawCircle((mWidth / 2).toFloat(), (mHeight / 2).toFloat(), mRadius, mDialPaint)

        // Draw the text labels.
        val labelRadius = mRadius + 20
        val label = mTempLabel
        for (i in 0 until SELECTION_COUNT) {
            val xyData = computeXYForPosition(i, labelRadius)
            val x = xyData[0]
            val y = xyData[1]
            label.setLength(0)
            label.append(i)
            canvas.drawText(label, 0, label.length, x, y, mTextPaint)
        }

        // Draw the indicator mark.
        val markerRadius = mRadius - 35
        val xyData = computeXYForPosition(
            mActiveSelection,
            markerRadius
        )
        val x = xyData[0]
        val y = xyData[1]
        canvas.drawCircle(x, y, 20f, mTextPaint)
    }

}
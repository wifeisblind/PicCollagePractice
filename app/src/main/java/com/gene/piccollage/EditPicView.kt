package com.gene.piccollage

import android.content.Context
import android.graphics.*
import android.media.Image
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.times

class EditPicView @JvmOverloads constructor(
    context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var bitmap: Bitmap
    private val curMatrix: Matrix = Matrix()

    private var croppingRect: Rect? = null
    private val rect: Rect = Rect()
    private val outlinePaint: Paint = Paint().apply {
        strokeWidth = 2f
        color = Color.BLACK
        style = Paint.Style.STROKE
    }

    private val eraser: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {

        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        color = Color.WHITE
    }

    fun load(uri: Uri, scale: Float = 1f, position: Point? = null, degree: Int = 0, cropping: Rect? = null) {
        bitmap = getImageBitmap(uri)
        val fitX = measuredWidth.toFloat() / bitmap.width
        val fitY = measuredHeight.toFloat() / bitmap.height
        if (fitX > fitY) {
            curMatrix.setScale(fitX * scale, fitX * scale)
        } else {
            curMatrix.setScale(fitY * scale, fitY * scale)
        }

        curMatrix.postRotate(degree.toFloat(), 0f, 0f)

        position?.let {
            curMatrix.postTranslate(it.x.toFloat(), it.y.toFloat())
        }

        croppingRect = cropping

        invalidate()
    }

    private fun getImageBitmap(uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.createSource(context.contentResolver, uri).let { source ->
                ImageDecoder.decodeBitmap(source)
            }
        } else {
            TODO()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        rect.set(0, 0, measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (this::bitmap.isInitialized) {
            canvas.save()
            croppingRect?.let {
                canvas.clipRect(it)
            }
            canvas.drawBitmap(bitmap, curMatrix, null)
            canvas.restore()
            canvas.drawRect(rect, outlinePaint)
        }
    }
}
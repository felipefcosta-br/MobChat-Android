package br.felipefcosta.mobchat.ui.components

import android.content.Context
import android.view.ViewOutlineProvider
import androidx.appcompat.widget.AppCompatImageView
import br.felipefcosta.mobchat.R

class CircleImageView(context: Context): AppCompatImageView(context) {

    init {
        outlineProvider = ViewOutlineProvider.BACKGROUND
        clipToOutline = true
        setBackgroundResource(R.drawable.image_circle_background)
        scaleType = ScaleType.CENTER_CROP
    }
}
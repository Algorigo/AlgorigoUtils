package com.algorigo.library

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView

/**
 * AnimationUtil
 * Created by judh on 2018. 3. 13..
 */
object AnimationUtil {
    fun expand(view: View, duration: Int, targetHeight: Int) {
        val prevHeight = view.height
        view.visibility = View.VISIBLE
        val valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight)
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.addUpdateListener { animation ->
            view.layoutParams.height = animation.animatedValue as Int
            view.requestLayout()
        }
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.duration = duration.toLong()
        valueAnimator.start()
    }

    fun collapse(view: View, duration: Int, targetHeight: Int) {
        val prevHeight = view.height
        val valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight)
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.addUpdateListener { animation ->
            view.layoutParams.height = animation.animatedValue as Int
            view.requestLayout()
        }
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                view.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.duration = duration.toLong()
        valueAnimator.start()
    }

    fun imageViewAnimatedChange(context: Context, imageView: ImageView, newImage: Bitmap) {
        val animOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
        val animIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        animOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                animIn.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}

                    override fun onAnimationEnd(animation: Animation) {}

                    override fun onAnimationRepeat(animation: Animation) {}
                })
                imageView.setImageBitmap(newImage)
                imageView.startAnimation(animIn)
            }
        })
        imageView.startAnimation(animOut)
    }
}
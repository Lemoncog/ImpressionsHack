package net.rossharper.impressionsexperiment.impressions.ui

import android.graphics.Rect
import android.view.View

class HalfVisibleItemVisibilityStrategy :
    ItemVisibilityStrategy {

    override fun isVisible(view: View): Boolean = view.isAtLeastHalfVisible


    private val View.isAtLeastHalfVisible: Boolean
        get() {
            return visibleArea > area.half
        }

    private val View.visibleArea : Int
        get() {
            return Rect().let {
                getGlobalVisibleRect(it)
                it.area
            }
        }

    private val Rect.area : Int
        get() {
            return this.width() * this.height()
        }

    private val View.area : Int
        get() {
            return width * height
        }

    private val Int.half : Float
        get() {
            return this.toFloat() / 2
        }
}
package uk.co.bbc.impressionshack

import android.app.Activity
import android.graphics.Rect
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver

import java.util.ArrayList
import java.util.WeakHashMap

class ImpressionDetectionOLD(activity: Activity) {
    var visibilityTrackerListener: VisibilityTrackerListener? = null
    private val mTrackedViews = WeakHashMap<View, TrackingInfo>()
    private var mOnPreDrawListener: ViewTreeObserver.OnPreDrawListener? = null
    private var mIsVisibilityCheckScheduled: Boolean = false
    private val mVisibilityChecker: VisibilityChecker
    private val mVisibilityHandler: Handler
    private val mVisibilityRunnable: Runnable


    interface VisibilityTrackerListener {
        fun onVisibilityChanged(visibleViews: List<View>, invisibleViews: List<View>)
    }

    internal class TrackingInfo {
        var mRootView: View? = null
        var mMinVisiblePercent: Int = 0
        var firstSeen: Long = 0
    }

    init {
        val rootView = activity.window.decorView
        val viewTreeObserver = rootView.viewTreeObserver

        mVisibilityHandler = Handler()
        mVisibilityChecker = VisibilityChecker()
        mVisibilityRunnable = VisibilityRunnable()

        if (viewTreeObserver.isAlive) {
            mOnPreDrawListener = ViewTreeObserver.OnPreDrawListener {
                scheduleVisibilityCheck()
                true
            }
            viewTreeObserver.addOnPreDrawListener(mOnPreDrawListener)
        } else {
            Log.d(
                ImpressionDetectionOLD::class.java.simpleName,
                "Visibility tracker root view is not alive"
            )
        }
    }

    fun trackViewForImpression(view: View, minVisiblePercentageViewed: Int) {

        var trackingInfo = mTrackedViews[view]
        if (trackingInfo == null) {
            // view is not yet being tracked
            trackingInfo = TrackingInfo()
            mTrackedViews[view] = trackingInfo
            scheduleVisibilityCheck()
        }


        trackingInfo.mRootView = view
        trackingInfo.mMinVisiblePercent = minVisiblePercentageViewed
    }


    private fun scheduleVisibilityCheck() {
        if (mIsVisibilityCheckScheduled) {
            return
        }
        mIsVisibilityCheckScheduled = true
        mVisibilityHandler.postDelayed(mVisibilityRunnable, VISIBILITY_CHECK_DELAY_MILLIS)
    }


    internal class VisibilityChecker {
        private val mClipRect = Rect()


        fun isVisible(view: View?, minPercentageViewed: Int): Boolean {
            if (view == null || view.visibility != View.VISIBLE || view.parent == null) {
                return false
            }

            if (!view.getGlobalVisibleRect(mClipRect)) {
                return false
            }

            val visibleArea = mClipRect.height().toLong() * mClipRect.width()
            val totalViewArea = view.height.toLong() * view.width

            return totalViewArea > 0 && 100 * visibleArea >= minPercentageViewed * totalViewArea

        }


    }

    internal inner class VisibilityRunnable : Runnable {
        private var visibleViews: MutableList<View>
        private var invisibleViews: MutableList<View>

        init {
            visibleViews = ArrayList()
            invisibleViews = ArrayList()
        }

        override fun run() {
            Log.d("VIS_LOG", "run")
            val currentVisibleViews = mutableListOf<View>()
            val currentInvisibleViews = mutableListOf<View>()

            mIsVisibilityCheckScheduled = false
            for ((view, value) in mTrackedViews) {
                val minPercentageViewed = value.mMinVisiblePercent

                if (mVisibilityChecker.isVisible(view, minPercentageViewed) && visibleForThreshold(value)) {
                    currentVisibleViews.add(view)
                } else
                    currentInvisibleViews.add(view)
            }

            if (visibilityTrackerListener != null) {
                visibilityTrackerListener!!.onVisibilityChanged(currentVisibleViews, currentInvisibleViews)
            }

            visibleViews = currentVisibleViews
            invisibleViews = currentInvisibleViews
        }

        private val TIME_TILL_IMPRESSION = 1000 * 5

        private fun visibleForThreshold(value: TrackingInfo) : Boolean {
            return if(value.firstSeen == 0.toLong()) {
                value.firstSeen = System.currentTimeMillis()
                return false
            } else {
                return System.currentTimeMillis() - value.firstSeen > TIME_TILL_IMPRESSION
            }
        }
    }

    companion object {
        private val VISIBILITY_CHECK_DELAY_MILLIS: Long = 100
    }

}
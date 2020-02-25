package uk.co.bbc.impressionshack.theAPI

import android.view.View

class TrackedView() {
    var view: View? = null
    var timeSeen: Long? = null
    var position: Int? = null
}

class ImpressionViewModel {

    val trackedViews = mutableMapOf<View, TrackedView>()
}
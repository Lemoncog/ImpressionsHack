package net.rossharper.impressionsexperiment.impressions.ui

import android.view.View

interface ItemVisibilityStrategy {
    fun isVisible(view: View): Boolean
}
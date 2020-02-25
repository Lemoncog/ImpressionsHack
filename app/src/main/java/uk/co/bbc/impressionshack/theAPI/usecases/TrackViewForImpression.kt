package uk.co.bbc.impressionshack.theAPI.usecases

import android.view.View
import uk.co.bbc.impressionshack.theAPI.ImpressionViewModel
import uk.co.bbc.impressionshack.theAPI.TrackedView


class TrackViewForImpression(val impressionViewModel: ImpressionViewModel, val periodicallyCheckViewsForImpression: PeriodicallyCheckViewsForImpression) {

    fun execute(view: View, position: Int) {
        var trackedView = impressionViewModel.trackedViews[view]

        //This view might already be tracked due to recyling views (if not, create a new one)
        if(trackedView == null) {
            trackedView = TrackedView()
            impressionViewModel.trackedViews[view] = trackedView
            periodicallyCheckViewsForImpression.execute()
        }

        //Update view and position.
        trackedView.view = view
        trackedView.position = position
    }
}
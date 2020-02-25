package uk.co.bbc.impressionshack.theAPI

import android.view.View
import net.rossharper.impressionsexperiment.impressions.ui.HalfVisibleItemVisibilityStrategy
import net.rossharper.impressionsexperiment.impressions.ui.ItemImpressionObserver
import uk.co.bbc.impressionshack.theAPI.usecases.PeriodicallyCheckViewsForImpression
import uk.co.bbc.impressionshack.theAPI.usecases.TrackViewForImpression
import uk.co.bbc.impressionshack.theAPI.usecases.ViewImpression

class ImpressionDetection {
    val viewModel = ImpressionViewModel()
    val itemImpressionObserver = object: ItemImpressionObserver<ViewImpression> {
        override fun onItemImpression(itemDescriptor: ViewImpression) {
            //Its an impression!
        }
    }

    private val trackViewForImpression = TrackViewForImpression(viewModel, PeriodicallyCheckViewsForImpression(viewModel, HalfVisibleItemVisibilityStrategy(), 1000, itemImpressionObserver))

    fun trackViewForImpression(view: View, position: Int) {
        trackViewForImpression.execute(view, position)
    }
}
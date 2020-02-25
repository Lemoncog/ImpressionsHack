package uk.co.bbc.impressionshack.theAPI.usecases

import android.view.View
import net.rossharper.impressionsexperiment.impressions.ui.ItemImpressionObserver
import net.rossharper.impressionsexperiment.impressions.ui.ItemVisibilityStrategy
import uk.co.bbc.impressionshack.theAPI.ImpressionViewModel

class ViewImpression(val view: View, val position: Int)

class PeriodicallyCheckViewsForImpression(private val viewModel: ImpressionViewModel,
                                          private val itemVisibilityStrategy : ItemVisibilityStrategy,
                                          private val impressionDurationThresholdMillis: Long,
                                          private val itemImpressionObserver: ItemImpressionObserver<ViewImpression>) {
    fun execute() {
        //If already scheduled wait
        //TODO
        //else schedule
        //TODO

        //onSchedule hit
        viewModel.trackedViews.forEach {
            it.value.let {trackedView ->
                trackedView.position?.let { position ->
                    trackedView.timeSeen?.let { timestamp ->
                        trackedView.view?.let { view ->


                            if (hasBeenVisibleForLongEnough(timestamp) && itemVisibilityStrategy.isVisible(view)) {
                                itemImpressionObserver.onItemImpression(ViewImpression(view, position))
                            }


                        }
                    }
                }
            }
        }
    }

    private fun hasBeenVisibleForLongEnough(timestamp: Long) =
        elapsedSince(timestamp) > impressionDurationThresholdMillis

    private fun elapsedSince(timestamp: Long) = System.currentTimeMillis() - timestamp
}
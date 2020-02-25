package uk.co.bbc.impressionshack

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.a_list_item.view.*
import uk.co.bbc.impressionshack.theAPI.ImpressionDetection

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

class MyAdapter(activity: Activity) : RecyclerView.Adapter<MyViewHolder>() {
    var dataSet = listOf<ListItemModel>()
    private val impressionDetection =
        ImpressionDetection()

    private val visibilityTracker =
        ImpressionDetectionOLD(activity)

    init {
        visibilityTracker.visibilityTrackerListener = object:
            ImpressionDetectionOLD.VisibilityTrackerListener {
            override fun onVisibilityChanged(visibleViews: List<View>, invisibleViews: List<View>) {
            }

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.a_list_item, viewGroup, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val itemModel = dataSet[position]

        holder.view.impression.setBackgroundColor(if(itemModel.impressed) Color.GREEN else Color.RED)
        holder.view.subtitle.text = itemModel.text

        impressionDetection.trackViewForImpression(holder.view, position)
    }

    override fun getItemId(position: Int): Long {
        return dataSet[position].id
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}
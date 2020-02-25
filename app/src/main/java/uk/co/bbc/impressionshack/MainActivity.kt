package uk.co.bbc.impressionshack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.app.Activity
import android.graphics.Color
import kotlinx.android.synthetic.main.a_list_item.view.*
import uk.co.bbc.impressionshack.theAPI.ImpressionDetection
import uk.co.bbc.impressionshack.theAPI.usecases.TrackViewForImpression


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        viewManager = LinearLayoutManager(this)
        val viewAdapter = ImpressionAdapter(this)
        viewAdapter.setHasStableIds(true)
        viewAdapter.dataSet = (0..400).map { ListItemModel(it.toLong(),false, "item " + it) }
        viewAdapter.notifyDataSetChanged()


        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
    }
}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

data class ListItemModel(val id: Long, var impressed: Boolean, val text: String)

class ImpressionAdapter(activity: Activity) : RecyclerView.Adapter<MyViewHolder>() {
    var dataSet = listOf<ListItemModel>()
    private val impressionDetection = ImpressionDetection()

    private val visibilityTracker = ImpressionDetectionOLD(activity)

    init {
        visibilityTracker.visibilityTrackerListener = object: ImpressionDetectionOLD.VisibilityTrackerListener {
            override fun onVisibilityChanged(visibleViews: List<View>, invisibleViews: List<View>) {
            }

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.a_list_item, viewGroup, false)
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
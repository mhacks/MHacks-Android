package com.mhacks.android.ui.events

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.vipulasri.timelineview.TimelineView
import com.mhacks.android.data.model.Events
import org.mhacks.android.R
import kotlin.collections.ArrayList

class AnnouncementAdapter(var mContext: Context, var eventsList: ArrayList<Events>):
        RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AnnouncementViewHolder {
        // Create the view for this row
        val row = LayoutInflater.from(mContext)
                .inflate(R.layout.events_list_item, viewGroup, false)

        // Create a new viewHolder which caches all the views that needs to be saved
        val viewHolder = AnnouncementViewHolder(row)

        return viewHolder
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: AnnouncementViewHolder, i: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        // Get the current announcement item
        val announcement = eventsList[i]

        // Set this item's views based off of the announcement data
        viewHolder.titleView.text = announcement.title
        viewHolder.descriptionView.text = announcement.info

        if (!announcement.title.equals("")) {
            viewHolder.timeView.setText("8:00")
        }

        val category = announcement.category
        var current = 1
        for (a in 0..4) {
            current = 1 shl a
            if (category and current != 0) break
        }
        when (current) {
            1 -> {
            }
            2 -> {
            }
            4 -> {
            }
            8 -> {
            }
            16 -> {
            }
            32 -> {
            }
        }
        //                    viewHolder.colorView.setBackgroundColor(getResources().getColor(R.color.event_red));
        //                    viewHolder.colorView.setBackgroundColor(getResources().getColor(R.color.event_blue));
        //                    viewHolder.colorView.setBackgroundColor(getResources().getColor(R.color.event_yellow));
        //                    viewHolder.colorView.setBackgroundColor(getResources().getColor(R.color.event_green));
        //                    viewHolder.colorView.setBackgroundColor(getResources().getColor(R.color.event_purple));
        //                    viewHolder.colorView.setBackgroundColor(getResources().getColor(R.color.md_brown_500));

        // Get the date from this announcement and set it as a relative date
//        val date = Date(announcement.broadcastAt)
//            val relativeDate = DateUtils.getRelativeTimeSpanString(date.time)
//            viewHolder.dateView.text = relativeDate
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return eventsList.size
    }

    // Simple class that holds all the views that need to be reused
    // Default constructor, itemView holds all the views that need to be saved
    inner class AnnouncementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView = itemView.findViewById<View>(R.id.announcements_item_title) as TextView
        val timeView = itemView.findViewById<View>(R.id.announcements_time_text) as TextView
        val descriptionView = itemView.findViewById<View>(R.id.announcements_item_description) as TextView
        val timelineView = itemView.findViewById<View>(R.id.announcements_info_time_marker) as TimelineView
        //            val timelineView = itemView.findViewById<View>(R.id.info_time_marker) as TimelineView
//        init {
//            timelineView.initLine(viewType)
//        }


    }
}


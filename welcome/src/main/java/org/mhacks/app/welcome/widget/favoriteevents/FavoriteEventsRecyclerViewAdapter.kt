package org.mhacks.app.welcome.widget.favoriteevents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView

import org.mhacks.app.eventlibrary.data.model.Event
import org.mhacks.app.eventlibrary.data.model.EventSectionModel
import org.mhacks.app.eventlibrary.EventWithDay
import org.mhacks.app.eventlibrary.widget.EventTimeLineItem
import org.mhacks.app.welcome.R
import org.mhacks.app.R as mainR
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class FavoriteEventsRecyclerViewAdapter(
        eventsMap: Map<String, List<EventWithDay>>,
        private val eventsCallback: ((
                event: Event,
                isChecked: Boolean) -> Unit)?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dateFormatter by lazy {
        DateTimeFormatter.ofPattern("h:mm a")
    }

    private val eventFlatList by lazy {

        val eventFlatArrayList = ArrayList<EventFlatList>()
        eventsMap.forEach {
            eventFlatArrayList.add(
                    EventFlatList(
                            it.key,
                            null,
                            EVENT_HEADER_VIEW_TYPE
                    )
            )
            val groupedEvent = it.value
                    .asSequence()
                    .map { eventWithDay ->
                        eventWithDay.event
                    }.groupBy { event ->
                        event.startDateTs
                    }

            groupedEvent.let { groupEvent ->
                groupEvent.asSequence().forEachIndexed { i, group ->
                    val (time, value) = group
                    eventFlatArrayList.add(
                            EventFlatList(
                                    null,
                                    EventSectionModel(time, ArrayList(value)),
                                    TimelineView.getTimeLineViewType(i, groupEvent.size)
                            )
                    )
                }
            }
        }
        eventFlatArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : RecyclerView.ViewHolder{
        val viewHolder: RecyclerView.ViewHolder
        if (viewType == EVENT_HEADER_VIEW_TYPE) {
            val eventHeaderTextView =
                    LayoutInflater.from(parent.context)
                            .inflate(mainR.layout.events_header_item, parent, false)
            viewHolder = EventHeaderViewHolder(eventHeaderTextView)
        } else {
            viewHolder = EventViewHolder(EventTimeLineItem(parent.context))
            viewHolder.eventTimeLineItem.timeLineType = viewType
            viewHolder.eventTimeLineItem.onEventClicked = eventsCallback
        }
        return viewHolder
    }

    override fun onBindViewHolder(
            viewHolder: RecyclerView.ViewHolder,
            position: Int
    ) {
        val eventFlatListItem = eventFlatList[position]

        (viewHolder as? EventHeaderViewHolder)?.let { eventHeaderViewHolder ->
            eventFlatListItem.dayOfWeek?.let {
                eventHeaderViewHolder.bind(it)
            }
        }
        (viewHolder as? EventViewHolder)?.let { eventHeaderViewHolder ->
            eventFlatListItem.eventSectionModel?.let { eventSectionModel ->
                val localTime =
                        Instant.ofEpochMilli(eventSectionModel.time)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()

                eventHeaderViewHolder.eventTimeLineItem.addEventGroup(eventSectionModel)
                eventHeaderViewHolder.eventTimeLineItem.timeText = dateFormatter.format(localTime)
            }
        }
    }

    override fun getItemCount() =
            eventFlatList.size

    override fun getItemViewType(position: Int)=
            eventFlatList[position].timeLineType

    class EventViewHolder(val eventTimeLineItem: EventTimeLineItem)
        : RecyclerView.ViewHolder(eventTimeLineItem)

    class EventHeaderViewHolder(view: View)
        : RecyclerView.ViewHolder(view) {

        fun bind(weekDay: String) {
            (itemView as TextView).text = weekDay
        }
    }

    data class EventFlatList(
            val dayOfWeek: String?,
            val eventSectionModel: EventSectionModel?,
            val timeLineType: Int)

    companion object {

        private const val EVENT_HEADER_VIEW_TYPE = -999

    }
}
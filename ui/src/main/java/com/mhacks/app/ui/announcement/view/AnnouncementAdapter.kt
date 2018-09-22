package com.mhacks.app.ui.announcement.view

import android.content.Context
import android.support.v7.recyclerview.extensions.ListAdapter
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mhacks.app.data.models.Announcement
import org.mhacks.mhacksui.R
import java.util.*

/**
 * Created by jeffreychang on 9/15/17.
 */
class AnnouncementsAdapter(private val context: Context,
                           private val announcementList: ArrayList<Announcement>):
        ListAdapter<Announcement, AnnouncementViewHolder>(AnnouncementDiffCallback()) {

    override fun getItemCount() = announcementList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.announcements_item, parent, false)
        return AnnouncementViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) =
            bindItems(holder, announcementList[position])

    fun updateList(announcementList: ArrayList<Announcement>) {
        this.announcementList.clear()
        this.announcementList.addAll(announcementList)
        notifyDataSetChanged()
    }

    private fun bindItems(holder: AnnouncementViewHolder, announcement: Announcement) {
        val relativeTime = DateUtils.getRelativeTimeSpanString(
                announcement.createdAtTs,
                Date().time, DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_NO_NOON)
        holder.announcementsHeader.text = announcement.title
        holder.announcementDate.text = relativeTime
        holder.announcementEventDetail.text = announcement.body
    }
}
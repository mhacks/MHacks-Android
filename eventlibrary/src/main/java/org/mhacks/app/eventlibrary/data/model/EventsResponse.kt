package org.mhacks.app.eventlibrary.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

/**
 * Model for events
 */
data class EventsResponse(
	@Json(name = "status") var status: Boolean,
	@Json(name = "events") var events: List<Event>
)

@Entity(tableName = "event")
data class Event(
	@PrimaryKey
	@Json(name = "id") var id: String,
	@Json(name = "updatedAt") var updatedAt: String?,
	@Json(name = "createdAt") var createdAt: String?,
	@Json(name = "name") var name: String?,
	@Json(name = "desc") var desc: String?,
	@Json(name = "startDate") var startDate: String?,
	@Json(name = "endDate") var endDate: String?,
	@Json(name = "location") var location: String?,
	@Json(name = "category") var category: String?,
	@Json(name = "deleted") var deleted: Boolean?,
	@field:Json(name = "createdAt_ts") var createdAtTs: Long,
	@field:Json(name = "updatedAt_ts") var updatedAtTs: Long,
	@field:Json(name = "startDate_ts") var startDateTs: Long,
	@field:Json(name = "endDate_ts") var endDateTs: Long,
    var favorited: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?:  "",
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readLong(),
            parcel.readLong(),
            parcel.readLong(),
            parcel.readLong())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(updatedAt)
        parcel.writeString(createdAt)
        parcel.writeString(name)
        parcel.writeString(desc)
        parcel.writeString(startDate)
        parcel.writeString(endDate)
        parcel.writeString(location)
        parcel.writeString(category)
        parcel.writeValue(deleted)
        parcel.writeLong(createdAtTs)
        parcel.writeLong(updatedAtTs)
        parcel.writeLong(startDateTs)
        parcel.writeLong(endDateTs)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Event> {
        override fun createFromParcel(parcel: Parcel): Event {
            return Event(parcel)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }
    }

}
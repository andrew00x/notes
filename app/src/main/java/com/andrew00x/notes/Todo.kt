package com.andrew00x.notes

import android.os.Parcel
import android.os.Parcelable
import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import java.util.*

enum class Period {
    NONE() {
        override fun calculateNext(date: Date): Date {
            TODO("nothing to calculate for NONE period")
        }
    },
    DAY() {
        override fun calculateNext(date: Date): Date {
            val c = Calendar.getInstance()
            c.time = date
            c.add(Calendar.DATE, 1)
            return c.time
        }
    },
    WEEK() {
        override fun calculateNext(date: Date): Date {
            val c = Calendar.getInstance()
            c.time = date
            c.add(Calendar.DATE, 7)
            return c.time
        }
    },
    MONTH() {
        override fun calculateNext(date: Date): Date {
            val c = Calendar.getInstance()
            c.time = date
            c.add(Calendar.MONTH, 1)
            return c.time
        }
    };

    abstract fun calculateNext(date: Date): Date
}

@Table(name = "todo")
data class Todo(
        @Column(name = "title", index = true) var title: String = "",
        @Column(name = "timestamp") var timestamp: Date = Date(),
        @Column(name = "period") var period: Period = Period.NONE
) : Model(), Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            Date(parcel.readLong()),
            Period.values()[parcel.readInt()]
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeLong(timestamp.time)
        parcel.writeInt(period.ordinal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Todo> {
        override fun createFromParcel(parcel: Parcel): Todo {
            return Todo(parcel)
        }

        override fun newArray(size: Int): Array<Todo?> {
            return arrayOfNulls(size)
        }
    }
}

package com.andrew00x.notes

import android.os.Parcel
import android.os.Parcelable
import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table

@Table(name = "purchase")
data class Purchase(
        @Column(name = "title") var title: String = "",
        @Column(name = "details") var details: String = "",
        @Column(name = "done") var done: Boolean = false
) : Model(), Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt() == 1
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeString(details)
        dest.writeInt(if (done) 1 else 0)
    }

    companion object CREATOR : Parcelable.Creator<Purchase> {
        override fun createFromParcel(parcel: Parcel): Purchase {
            return Purchase(parcel)
        }

        override fun newArray(size: Int): Array<Purchase?> {
            return arrayOfNulls(size)
        }
    }
}

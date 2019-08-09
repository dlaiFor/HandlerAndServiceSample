package com.example.aidlserverdemo

import android.os.Parcel
import android.os.Parcelable


data class WebSite(val id: Int, val name: String?, val url: String?) : Parcelable {

    constructor(source: Parcel) : this(
        source.readInt(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(name)
        writeString(url)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<WebSite> = object : Parcelable.Creator<WebSite> {
            override fun createFromParcel(source: Parcel): WebSite = WebSite(source)
            override fun newArray(size: Int): Array<WebSite?> = arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "siteName:->$name siteUrl:->$url siteId->$id"
    }
}


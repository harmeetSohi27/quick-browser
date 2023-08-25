package com.ultivic.quickbrowser.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tabData")
data class TabData(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo var title: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var icon: ByteArray? = null,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var screenShot:ByteArray?=null,
    @ColumnInfo var lastUrl: String="www.google.com",
    val createdTime: Long = System.currentTimeMillis(),
    var updatedTIme: Long = System.currentTimeMillis()
):Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TabData

        if (icon != null) {
            if (other.icon == null) return false
            if (!icon.contentEquals(other.icon)) return false
        } else if (other.icon != null) return false

        return true
    }

    override fun hashCode(): Int {
        return icon?.contentHashCode() ?: 0
    }
}

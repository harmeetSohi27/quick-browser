package com.ultivic.quickbrowser.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabHistory")
data class TabHistory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tabId: Long = 0,
    val url : String,
    val originalUrl :String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var icon: ByteArray? = null,
    val updatedTime:Long = System.currentTimeMillis(),
    var title : String
    ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TabHistory

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

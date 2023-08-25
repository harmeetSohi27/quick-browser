package com.ultivic.quickbrowser.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ultivic.quickbrowser.models.TabData
import com.ultivic.quickbrowser.models.TabHistory

@Database(entities = [TabData::class, TabHistory::class], version = 1)
abstract class MyDataBase : RoomDatabase(){
    abstract fun daoImpl(): AppDao
}
package com.ultivic.quickbrowser.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ultivic.quickbrowser.models.TabData
import com.ultivic.quickbrowser.models.TabHistory

@Dao
interface AppDao {

    @Insert
    fun insert(tabData: TabData): Long

    @Query("UPDATE tabData SET title = :title,screenShot = :screenShot , icon = :icon , lastUrl = :lastUrl,updatedTime = :updatedTime WHERE id = :id")
    fun updateTab(id: Long, title: String, icon: ByteArray?, lastUrl: String, updatedTime: Long?,screenShot:ByteArray?)

    @Insert
    fun insertHistory(history: TabHistory)

    @Query("DELETE FROM tabHistory")
    fun deleteAll()

    @Query("Select * From tabData")
    fun getTabs(): LiveData<List<TabData>>

    @Query("Select * From tabHistory order by updatedTIme DESC")
    fun getHistory(): LiveData<List<TabHistory>>

    @Query("DELETE FROM tabData WHERE id = :id")
    fun deleteById(id: Long?)

    @Query("DELETE FROM TabHistory WHERE tabId = :tabId")
    fun deleteTabHistory(tabId: Long?)

    @Query("DELETE FROM TabHistory WHERE url = :url")
    fun deleteHistory(url: String?)

    @Query("Select * from tabData order by updatedTIme DESC limit 1")
    fun getLastTab(): TabData?

    @Query("Select * from tabHistory WHERE tabId = :id order by updatedTIme DESC limit 1 ")
    fun getLastUrl(id: Long?): TabHistory?

    @Query("SELECT COUNT(*) FROM tabData")
    fun getTabsCount(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM tabHistory Where tabId =:id")
    fun getCurrentTabHistoryCount(id: Long?): LiveData<Int>

    @Query("Select * from tabData where id = :id")
    fun getData(id: Long): TabData
}
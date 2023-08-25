package com.ultivic.quickbrowser.dao

import androidx.lifecycle.LiveData
import com.ultivic.quickbrowser.models.TabData
import com.ultivic.quickbrowser.models.TabHistory
import javax.inject.Inject

class AppDaoImpl @Inject constructor(private val appDao: AppDao) : AppDao {
    override fun insert(tabData: TabData): Long {
        return appDao.insert(tabData)
    }

    override fun updateTab(id: Long, title: String, icon: ByteArray?, lastUrl: String, updatedTime: Long?, screenShot: ByteArray?) {
        appDao.updateTab(id,title,icon,lastUrl,updatedTime,screenShot)
    }

    override fun insertHistory(history: TabHistory) {
        return appDao.insertHistory(history)
    }

    override fun deleteAll() {
        appDao.deleteAll()
    }


    override fun getTabs(): LiveData<List<TabData>> {
        return appDao.getTabs()
    }

    override fun getHistory(): LiveData<List<TabHistory>> {
        return appDao.getHistory()
    }





    override fun deleteById(id: Long?) {
        return appDao.deleteById(id)
    }

    override fun deleteTabHistory(tabId: Long?) {
        appDao.deleteTabHistory(tabId)
    }


    override fun deleteHistory(url: String?) {
        return appDao.deleteHistory(url)
    }

    override fun getLastTab(): TabData? {
        return appDao.getLastTab()
    }

    override fun getLastUrl(id: Long?): TabHistory? {
        return appDao.getLastUrl(id = id)
    }

    override fun getTabsCount(): LiveData<Int> {
        return appDao.getTabsCount()
    }

    override fun getCurrentTabHistoryCount(id: Long?): LiveData<Int> {
        return appDao.getCurrentTabHistoryCount(id)
    }

    override fun getData(id: Long): TabData {
        return appDao.getData(id)
    }
}
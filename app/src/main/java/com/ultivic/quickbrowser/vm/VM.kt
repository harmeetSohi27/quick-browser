package com.ultivic.quickbrowser.vm

import android.app.Application
import android.graphics.BitmapFactory
import android.view.View
import android.webkit.WebBackForwardList
import androidx.core.view.drawToBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ultivic.quickbrowser.R
import com.ultivic.quickbrowser.dao.AppDaoImpl
import com.ultivic.quickbrowser.models.TabData
import com.ultivic.quickbrowser.models.TabHistory
import com.ultivic.quickbrowser.utils.toByteArray
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "VMDATA"

@HiltViewModel
class VM @Inject constructor(private val app: Application, private val repo: AppDaoImpl) : AndroidViewModel(app) {
    val tabsList get() = repo.getTabs()

    val historyList get() = repo.getHistory()

    val lastTab get() = repo.getLastTab()

    fun d(id: Long) = repo.getData(id)

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAll()
        }
    }

    fun lastUrl(id: Long?) = repo.getLastUrl(id)

    fun historyCount(id: Long?) = repo.getCurrentTabHistoryCount(id)

    val getTabsCount get() = repo.getTabsCount()

    private val _newTab = MutableLiveData<TabData?>()

    val newTab get() = _newTab

    fun insert(list: WebBackForwardList?) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = list?.currentItem
            val d = data?.favicon?.toByteArray
            repo.insert(TabData(title = data?.title ?: "newTab", icon = d))
        }
    }

    fun insertHistory(list: WebBackForwardList? = null, tabData: TabData, binding: View) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = list?.currentItem
            val d = data?.favicon?.toByteArray
            if (lastUrl(tabData.id)?.url == list?.currentItem?.url) return@launch
            repo.insertHistory(
                TabHistory(
                    tabId = tabData.id,
                    url = list?.currentItem?.url ?: "https://www.google.com/",
                    originalUrl = list?.currentItem?.originalUrl ?: "https://www.google.com/",
                    icon = d,
                    title = list?.currentItem?.title.toString()
                )
            )
            repo.updateTab(
                id = tabData.id,
                updatedTime = System.currentTimeMillis(),
                title = data?.title.toString(),
                lastUrl = data?.url.toString(),
                icon = data?.favicon?.toByteArray,
                screenShot = binding.drawToBitmap().toByteArray
            )
        }
    }


    val insertDefault: Unit
        get() {
            val data = TabData(title = "Google", icon = BitmapFactory.decodeResource(app.resources, R.drawable.google).toByteArray)
            viewModelScope.launch(Dispatchers.IO) {
                val d = repo.insert(data)
                withContext(Dispatchers.Main) {
                    if (d >= 0)
                        _newTab.postValue(data.apply { id = d })
                }
            }
        }

    var delete: Long = 0
        set(value) {
            viewModelScope.launch(Dispatchers.IO) {
                repo.deleteById(value)
            }
        }
    var deleteTabHistory: Long = 0
        set(value) {
            viewModelScope.launch(Dispatchers.IO) {
                repo.deleteTabHistory(value)
            }
        }

    fun deleteHistory(value: TabHistory?, binding: View) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteHistory(value?.url ?: "")
            repo.updateTab(
                id = value?.tabId ?: 0,
                updatedTime = System.currentTimeMillis(),
                title = value?.title.toString(),
                lastUrl = value?.url.toString(),
                icon = value?.icon,
                screenShot = binding.drawToBitmap().toByteArray
            )
        }
    }

}
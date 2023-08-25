package com.ultivic.quickbrowser.dagger_hilt

import android.content.Context
import androidx.room.Room
import com.ultivic.quickbrowser.dao.AppDao
import com.ultivic.quickbrowser.dao.AppDaoImpl
import com.ultivic.quickbrowser.dao.MyDataBase
import com.ultivic.quickbrowser.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Provides
    @Singleton
    fun getDataBase(@ApplicationContext context: Context): MyDataBase {
        return Room.databaseBuilder(context, MyDataBase::class.java, Constants.DATABASE).build()
    }

    @Provides
    @Singleton
    fun getDao(myDataBase: MyDataBase): AppDao = AppDaoImpl(myDataBase.daoImpl())

}

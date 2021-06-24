package com.brian.weightLess.data

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeightDaoModule {

    @Provides
    @Singleton
    fun provideWeightDao(@ApplicationContext context: Context) : WeightDao {
        return AppDatabase(context).weightDao()
    }
}

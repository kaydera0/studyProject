package com.example.task7tracker.di


import android.content.Context
import com.example.task7tracker.utils.DataBaseUtils
import com.example.task7tracker.utils.FirebaseUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HiltDI {

    @Provides
    @Singleton
    fun provideDataBaseUtils(@ApplicationContext context: Context): DataBaseUtils {
        return DataBaseUtils(context = context)
    }

    @Provides
    @Singleton
    fun provideFirebaseUtils(): FirebaseUtils {
        return FirebaseUtils()
    }

}

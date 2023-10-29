package br.com.udesc.eso.tcc.studytalk.core.data.di

import android.content.Context
import androidx.room.Room
import br.com.udesc.eso.tcc.studytalk.core.data.dataBase.StudyTalkRoomDatabase
import br.com.udesc.eso.tcc.studytalk.core.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoreModule {

    @Provides
    @Singleton
    fun provideStudyTalkRoomDatabase(@ApplicationContext context: Context): StudyTalkRoomDatabase {
        return Room.databaseBuilder(context, StudyTalkRoomDatabase::class.java, "studytalk_db")
            .build()
    }

    @Provides
    @Singleton
    fun provideRestApi(): Retrofit {
        return Retrofit.Builder().baseUrl(Constants.RETROFIT_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}
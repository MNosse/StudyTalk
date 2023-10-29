package br.com.udesc.eso.tcc.studytalk.featureAnswer.data.di

import br.com.udesc.eso.tcc.studytalk.core.data.dataBase.StudyTalkRoomDatabase
import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.dataSource.LocalAnswerDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AnswerModule {

    @Provides
    @Singleton
    fun provideLocalAnswerDataSource(db: StudyTalkRoomDatabase): LocalAnswerDataSource {
        return db.getLocalAnswerDataSource()
    }
}
package br.com.udesc.eso.tcc.studytalk.featureQuestion.data.di

import br.com.udesc.eso.tcc.studytalk.core.data.dataBase.StudyTalkRoomDatabase
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.dataSource.LocalQuestionDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class QuestionModule {

    @Provides
    @Singleton
    fun provideLocalQuestionDataSource(db: StudyTalkRoomDatabase): LocalQuestionDataSource {
        return db.getLocalQuestionDataSource()
    }
}
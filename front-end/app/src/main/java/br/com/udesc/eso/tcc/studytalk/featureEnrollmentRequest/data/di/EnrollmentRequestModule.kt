package br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.di

import br.com.udesc.eso.tcc.studytalk.core.data.dataBase.StudyTalkRoomDatabase
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.dataSource.LocalEnrollmentRequestDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class EnrollmentRequestModule {

    @Provides
    @Singleton
    fun provideLocalEnrollmentRequestDataSource(db: StudyTalkRoomDatabase): LocalEnrollmentRequestDataSource {
        return db.getLocalEnrollmentRequestDataSource()
    }
}
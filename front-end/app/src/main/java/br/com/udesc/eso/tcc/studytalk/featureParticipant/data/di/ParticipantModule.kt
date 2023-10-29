package br.com.udesc.eso.tcc.studytalk.featureParticipant.data.di

import br.com.udesc.eso.tcc.studytalk.core.data.dataBase.StudyTalkRoomDatabase
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.dataSource.LocalParticipantDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ParticipantModule {

    @Provides
    @Singleton
    fun provideLocalParticipantDataSource(db: StudyTalkRoomDatabase): LocalParticipantDataSource {
        return db.getLocalParticipantDataSource()
    }
}
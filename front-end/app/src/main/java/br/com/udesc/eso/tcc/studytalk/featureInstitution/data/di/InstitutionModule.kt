package br.com.udesc.eso.tcc.studytalk.featureInstitution.data.di

import br.com.udesc.eso.tcc.studytalk.core.data.dataBase.StudyTalkRoomDatabase
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.dataSource.LocalInstitutionDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class InstitutionModule {

    @Provides
    @Singleton
    fun provideLocalInstitutionDataSource(db: StudyTalkRoomDatabase): LocalInstitutionDataSource {
        return db.getLocalInstitutionDataSource()
    }
}
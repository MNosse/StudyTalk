package br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.di

import br.com.udesc.eso.tcc.studytalk.core.data.dataBase.StudyTalkRoomDatabase
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.dataSource.LocalAdministratorDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AdministratorModule {

    @Provides
    @Singleton
    fun provideLocalAdministratorDataSource(db: StudyTalkRoomDatabase): LocalAdministratorDataSource {
        return db.getLocalAdministratorDataSource()
    }
}
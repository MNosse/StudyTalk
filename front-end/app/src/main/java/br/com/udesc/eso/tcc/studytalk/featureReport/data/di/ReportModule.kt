package br.com.udesc.eso.tcc.studytalk.featureReport.data.di

import br.com.udesc.eso.tcc.studytalk.core.data.dataBase.StudyTalkRoomDatabase
import br.com.udesc.eso.tcc.studytalk.featureReport.data.dataSource.LocalReportDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ReportModule {

    @Provides
    @Singleton
    fun provideLocalReportDataSource(db: StudyTalkRoomDatabase): LocalReportDataSource {
        return db.getLocalReportDataSource()
    }
}
package br.com.udesc.eso.tcc.studytalk.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import br.com.udesc.eso.tcc.studytalk.core.data.dataBase.StudyTalkRoomDatabase
import br.com.udesc.eso.tcc.studytalk.core.utils.Constants
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.dataSource.LocalAdministratorDataSource
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.dataSource.RemoteAdministratorDataSource
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.repository.AdministratorRepositoryImpl
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.repository.AdministratorRepository
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.useCase.AdministratorUseCases
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.useCase.CreateUseCase
import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.dataSource.LocalAnswerDataSource
import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.dataSource.RemoteAnswerDataSource
import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.repository.AnswerRepositoryImpl
import br.com.udesc.eso.tcc.studytalk.featureAnswer.domain.repository.AnswerRepository
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.dataSource.LocalEnrollmentRequestDataSource
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.dataSource.RemoteEnrollmentRequestDataSource
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.repository.EnrollmentRequestRepositoryImpl
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.domain.repository.EnrollmentRequestRepository
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.dataSource.LocalInstitutionDataSource
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.dataSource.RemoteInstitutionDataSource
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.repository.InstitutionRepositoryImpl
import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.repository.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.dataSource.LocalParticipantDataSource
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.dataSource.RemoteParticipantDataSource
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.repository.ParticipantRepositoryImpl
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.repository.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.ParticipantUseCases
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.dataSource.LocalQuestionDataSource
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.dataSource.RemoteQuestionDataSource
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.repository.QuestionRepositoryImpl
import br.com.udesc.eso.tcc.studytalk.featureQuestion.domain.repository.QuestionRepository
import br.com.udesc.eso.tcc.studytalk.featureReport.data.dataSource.LocalReportDataSource
import br.com.udesc.eso.tcc.studytalk.featureReport.data.dataSource.RemoteReportDataSource
import br.com.udesc.eso.tcc.studytalk.featureReport.data.repository.ReportRepositoryImpl
import br.com.udesc.eso.tcc.studytalk.featureReport.domain.repository.ReportRepository
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
class AppModule {

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

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("StudyTalkSharedPreferences", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideLocalAdministratorDataSource(db: StudyTalkRoomDatabase): LocalAdministratorDataSource {
        return db.getLocalAdministratorDataSource()
    }

    @Provides
    @Singleton
    fun provideLocalAnswerDataSource(db: StudyTalkRoomDatabase): LocalAnswerDataSource {
        return db.getLocalAnswerDataSource()
    }

    @Provides
    @Singleton
    fun provideLocalEnrollmentRequestDataSource(db: StudyTalkRoomDatabase): LocalEnrollmentRequestDataSource {
        return db.getLocalEnrollmentRequestDataSource()
    }

    @Provides
    @Singleton
    fun provideLocalInstitutionDataSource(db: StudyTalkRoomDatabase): LocalInstitutionDataSource {
        return db.getLocalInstitutionDataSource()
    }

    @Provides
    @Singleton
    fun provideLocalParticipantDataSource(db: StudyTalkRoomDatabase): LocalParticipantDataSource {
        return db.getLocalParticipantDataSource()
    }

    @Provides
    @Singleton
    fun provideLocalQuestionDataSource(db: StudyTalkRoomDatabase): LocalQuestionDataSource {
        return db.getLocalQuestionDataSource()
    }

    @Provides
    @Singleton
    fun provideLocalReportDataSource(db: StudyTalkRoomDatabase): LocalReportDataSource {
        return db.getLocalReportDataSource()
    }

    @Provides
    @Singleton
    fun provideRemoteAdministratorDataSource(retrofit: Retrofit): RemoteAdministratorDataSource {
        return retrofit.create(RemoteAdministratorDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteAnswerDataSource(retrofit: Retrofit): RemoteAnswerDataSource {
        return retrofit.create(RemoteAnswerDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteEnrollmentRequestDataSource(retrofit: Retrofit): RemoteEnrollmentRequestDataSource {
        return retrofit.create(RemoteEnrollmentRequestDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteInstitutionDataSource(retrofit: Retrofit): RemoteInstitutionDataSource {
        return retrofit.create(RemoteInstitutionDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteParticipantDataSource(retrofit: Retrofit): RemoteParticipantDataSource {
        return retrofit.create(RemoteParticipantDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteQuestionDataSource(retrofit: Retrofit): RemoteQuestionDataSource {
        return retrofit.create(RemoteQuestionDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteReportDataSource(retrofit: Retrofit): RemoteReportDataSource {
        return retrofit.create(RemoteReportDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideAdministratorRepository(
        localAdministratorDataSource: LocalAdministratorDataSource,
        remoteAdministratorDataSource: RemoteAdministratorDataSource
    ): AdministratorRepository {
        return AdministratorRepositoryImpl(
            localAdministratorDataSource = localAdministratorDataSource,
            remoteAdministratorDataSource = remoteAdministratorDataSource
        )
    }

    @Provides
    @Singleton
    fun provideAnswerRepository(
        localAnswerDataSource: LocalAnswerDataSource,
        localParticipantDataSource: LocalParticipantDataSource,
        localQuestionDataSource: LocalQuestionDataSource,
        remoteAnswerDataSource: RemoteAnswerDataSource
    ): AnswerRepository {
        return AnswerRepositoryImpl(
            localAnswerDataSource = localAnswerDataSource,
            localParticipantDataSource = localParticipantDataSource,
            localQuestionDataSource = localQuestionDataSource,
            remoteAnswerDataSource = remoteAnswerDataSource
        )
    }

    @Provides
    @Singleton
    fun provideEnrollmentRequestRepository(
        localEnrollmentRequestDataSource: LocalEnrollmentRequestDataSource,
        localInstitutionDataSource: LocalInstitutionDataSource,
        localParticipantDataSource: LocalParticipantDataSource,
        remoteEnrollmentRequestDataSource: RemoteEnrollmentRequestDataSource
    ): EnrollmentRequestRepository {
        return EnrollmentRequestRepositoryImpl(
            localEnrollmentRequestDataSource = localEnrollmentRequestDataSource,
            localInstitutionDataSource = localInstitutionDataSource,
            localParticipantDataSource = localParticipantDataSource,
            remoteEnrollmentRequestDataSource = remoteEnrollmentRequestDataSource
        )
    }

    @Provides
    @Singleton
    fun provideInstitutionRepository(
        localInstitutionDataSource: LocalInstitutionDataSource,
        remoteInstitutionDataSource: RemoteInstitutionDataSource
    ): InstitutionRepository {
        return InstitutionRepositoryImpl(
            localInstitutionDataSource = localInstitutionDataSource,
            remoteInstitutionDataSource = remoteInstitutionDataSource
        )
    }

    @Provides
    @Singleton
    fun provideParticipantRepository(
        localAnswerDataSource: LocalAnswerDataSource,
        localInstitutionDataSource: LocalInstitutionDataSource,
        localParticipantDataSource: LocalParticipantDataSource,
        localQuestionDataSource: LocalQuestionDataSource,
        remoteParticipantDataSource: RemoteParticipantDataSource
    ): ParticipantRepository {
        return ParticipantRepositoryImpl(
            localAnswerDataSource = localAnswerDataSource,
            localInstitutionDataSource = localInstitutionDataSource,
            localParticipantDataSource = localParticipantDataSource,
            localQuestionDataSource = localQuestionDataSource,
            remoteParticipantDataSource = remoteParticipantDataSource
        )
    }

    @Provides
    @Singleton
    fun provideQuestionRepository(
        localInstitutionDataSource: LocalInstitutionDataSource,
        localParticipantDataSource: LocalParticipantDataSource,
        localQuestionDataSource: LocalQuestionDataSource,
        remoteQuestionDataSource: RemoteQuestionDataSource
    ): QuestionRepository {
        return QuestionRepositoryImpl(
            localInstitutionDataSource = localInstitutionDataSource,
            localParticipantDataSource = localParticipantDataSource,
            localQuestionDataSource = localQuestionDataSource,
            remoteQuestionDataSource = remoteQuestionDataSource
        )
    }

    @Provides
    @Singleton
    fun provideReportRepository(
        localInstitutionDataSource: LocalInstitutionDataSource,
        localReportDataSource: LocalReportDataSource,
        remoteReportDataSource: RemoteReportDataSource
    ): ReportRepository {
        return ReportRepositoryImpl(
            localInstitutionDataSource = localInstitutionDataSource,
            localReportDataSource = localReportDataSource,
            remoteReportDataSource = remoteReportDataSource
        )
    }

    @Provides
    @Singleton
    fun provideAdministratorUseCases(repository: AdministratorRepository): AdministratorUseCases {
        return AdministratorUseCases(
            createUseCase = CreateUseCase(repository),
            getByUidUseCase = br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.useCase.GetByUidUseCase(
                repository
            )
        )

    }

    @Provides
    @Singleton
    fun provideParticipantUseCases(repository: ParticipantRepository): ParticipantUseCases {
        return ParticipantUseCases(
            getByUidUseCase = br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.GetByUidUseCase(
                repository
            )
        )

    }

}
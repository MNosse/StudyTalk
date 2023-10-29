package br.com.udesc.eso.tcc.studytalk.core.data.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.dataSource.LocalAdministratorDataSource
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.entity.AdministratorRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.dataSource.LocalAnswerDataSource
import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.entity.AnswerRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.dataSource.LocalEnrollmentRequestDataSource
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.entity.EnrollmentRequestRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.dataSource.LocalInstitutionDataSource
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.entity.InstitutionRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.dataSource.LocalParticipantDataSource
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.entity.ParticipantRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.dataSource.LocalQuestionDataSource
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.entity.QuestionRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureReport.data.dataSource.LocalReportDataSource
import br.com.udesc.eso.tcc.studytalk.featureReport.data.entity.ReportRoomEntity

@Database(
    entities = [
        AdministratorRoomEntity::class,
        AnswerRoomEntity::class,
        EnrollmentRequestRoomEntity::class,
        InstitutionRoomEntity::class,
        ParticipantRoomEntity::class,
        QuestionRoomEntity::class,
        ReportRoomEntity::class],
    version = 1
)
abstract class StudyTalkRoomDatabase : RoomDatabase() {
    abstract fun getLocalAdministratorDataSource(): LocalAdministratorDataSource
    abstract fun getLocalAnswerDataSource(): LocalAnswerDataSource
    abstract fun getLocalEnrollmentRequestDataSource(): LocalEnrollmentRequestDataSource
    abstract fun getLocalInstitutionDataSource(): LocalInstitutionDataSource
    abstract fun getLocalParticipantDataSource(): LocalParticipantDataSource
    abstract fun getLocalQuestionDataSource(): LocalQuestionDataSource
    abstract fun getLocalReportDataSource(): LocalReportDataSource
}
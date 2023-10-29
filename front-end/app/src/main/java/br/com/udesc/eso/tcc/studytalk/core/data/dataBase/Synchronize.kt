package br.com.udesc.eso.tcc.studytalk.core.data.dataBase

import br.com.udesc.eso.tcc.studytalk.core.domain.model.Privilege
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.dataSource.LocalAdministratorDataSource
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.dataSource.RemoteAdministratorDataSource
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.model.AdministratorApiModel
import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.dataSource.LocalAnswerDataSource
import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.dataSource.RemoteAnswerDataSource
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.dataSource.LocalEnrollmentRequestDataSource
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.dataSource.RemoteEnrollmentRequestDataSource
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.dataSource.LocalInstitutionDataSource
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.dataSource.RemoteInstitutionDataSource
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.dataSource.LocalParticipantDataSource
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.dataSource.RemoteParticipantDataSource
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.model.ParticipantApiModel
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.dataSource.LocalQuestionDataSource
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.dataSource.RemoteQuestionDataSource
import br.com.udesc.eso.tcc.studytalk.featureReport.data.dataSource.LocalReportDataSource
import br.com.udesc.eso.tcc.studytalk.featureReport.data.dataSource.RemoteReportDataSource
import javax.inject.Inject

class Synchronize @Inject constructor(
    private val localAdministratorDataSource: LocalAdministratorDataSource,
    private val localAnswerDataSource: LocalAnswerDataSource,
    private val localEnrollmentRequestDataSource: LocalEnrollmentRequestDataSource,
    private val localInstitutionDataSource: LocalInstitutionDataSource,
    private val localParticipantDataSource: LocalParticipantDataSource,
    private val localQuestionDataSource: LocalQuestionDataSource,
    private val localReportDataSource: LocalReportDataSource,
    private val remoteAdministratorDataSource: RemoteAdministratorDataSource,
    private val remoteAnswerDataSource: RemoteAnswerDataSource,
    private val remoteEnrollmentRequestDataSource: RemoteEnrollmentRequestDataSource,
    private val remoteInstitutionDataSource: RemoteInstitutionDataSource,
    private val remoteParticipantDataSource: RemoteParticipantDataSource,
    private val remoteQuestionDataSource: RemoteQuestionDataSource,
    private val remoteReportDataSource: RemoteReportDataSource
) {
    private lateinit var administratorsIds: MutableList<Long>
    private lateinit var answersIds: MutableList<Long>
    private lateinit var enrollmentRequestiesIds: MutableList<Long>
    private lateinit var institutionsIds: MutableList<Long>
    private lateinit var participantsIds: MutableList<Long>
    private lateinit var questionsIds: MutableList<Long>
    private lateinit var reportsIds: MutableList<Long>

    suspend fun syncData(requestingUid: String, isAdministrator: Boolean) {
        backupIds()
        if (isAdministrator) {
            syncDataForAdministrator(requestingUid)
        } else {
            syncDataForParticipant(requestingUid)
        }
        clearRemainingData()
    }

    private suspend fun backupIds() {
        administratorsIds = localAdministratorDataSource.getAllIds()
        answersIds = localAnswerDataSource.getAllIds()
        enrollmentRequestiesIds = localEnrollmentRequestDataSource.getAllIds()
        institutionsIds = localInstitutionDataSource.getAllIds()
        participantsIds = localParticipantDataSource.getAllIds()
        questionsIds = localQuestionDataSource.getAllIds()
        reportsIds = localReportDataSource.getAllIds()
    }

    private suspend fun syncDataForAdministrator(administratorUid: String) {
        lateinit var administrator: AdministratorApiModel

        //Sync Administrator
        val administratorReponse = remoteAdministratorDataSource.getByUid(administratorUid)
        if (administratorReponse.isSuccessful) {
            administrator = administratorReponse.body()!!
            localAdministratorDataSource.create(
                br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.converter.convertToRoomEntity(
                    administrator
                )
            )
            administratorsIds.remove(administrator.id)

            //Sync Institutions
            val institutionsResponse = remoteInstitutionDataSource.getAll(administratorUid)
            if (institutionsResponse.isSuccessful) {
                institutionsResponse.body()!!.institutions.let {
                    for (institution in it) {
                        localInstitutionDataSource.create(
                            br.com.udesc.eso.tcc.studytalk.featureInstitution.data.converter.convertToRoomEntity(
                                institution
                            )
                        )
                        institutionsIds.remove(institution.id)
                    }
                }

                //Sync Participants
                val participantsResponse = remoteParticipantDataSource.getAll(administratorUid)
                if (participantsResponse.isSuccessful) {
                    participantsResponse.body()!!.participants.let {
                        for (participant in it) {
                            localParticipantDataSource.create(
                                br.com.udesc.eso.tcc.studytalk.featureParticipant.data.converter.convertToRoomEntity(
                                    participant
                                )
                            )
                            participantsIds.remove(participant.id)
                        }
                    }
                }
            }
        }
    }

    private suspend fun syncDataForParticipant(participantUid: String) {
        lateinit var participant: ParticipantApiModel

        //Sync Participant
        val reponse = remoteParticipantDataSource.getByUid(participantUid, participantUid, false)
        if (reponse.isSuccessful) {
            participant = reponse.body()!!
            localParticipantDataSource.create(
                br.com.udesc.eso.tcc.studytalk.featureParticipant.data.converter.convertToRoomEntity(
                    participant
                )
            )
            participantsIds.remove(participant.id)

            if (participant.institution != null) {
                //Sync Institution
                val response = remoteInstitutionDataSource.getById(
                    participant.institution!!.id,
                    participant.uid,
                    false
                )
                if (response.isSuccessful) {
                    response.body()!!.let {
                        localInstitutionDataSource.create(
                            br.com.udesc.eso.tcc.studytalk.featureInstitution.data.converter.convertToRoomEntity(
                                it
                            )
                        )
                        institutionsIds.remove(it.id)
                    }

                    //Sync Participants
                    if (participant.privilege == Privilege.PRINCIPAL) {
                        val participantsResponse = remoteParticipantDataSource.getAllByInstitution(
                            participant.institution!!.id,
                            participantUid,
                            false
                        )
                        if (participantsResponse.isSuccessful) {
                            participantsResponse.body()!!.participants.let {
                                for (participantFromResponse in it) {
                                    localParticipantDataSource.create(
                                        br.com.udesc.eso.tcc.studytalk.featureParticipant.data.converter.convertToRoomEntity(
                                            participantFromResponse
                                        )
                                    )
                                    participantsIds.remove(participantFromResponse.id)
                                }
                            }
                        }
                    }

                    //Sync EnrollmentRequesties
                    if (participant.privilege == Privilege.PRINCIPAL || participant.privilege == Privilege.TEACHER) {
                        val enrollmentRequestiesReponse =
                            remoteEnrollmentRequestDataSource.getAllByInstitution(
                                participant.institution!!.id,
                                participantUid
                            )
                        if (enrollmentRequestiesReponse.isSuccessful) {
                            enrollmentRequestiesReponse.body()!!.enrollmentRequesties.let {
                                for (enrollmentRequest in it) {
                                    localEnrollmentRequestDataSource.create(
                                        br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.converter.convertToRoomEntity(
                                            enrollmentRequest
                                        )
                                    )
                                    enrollmentRequestiesIds.remove(enrollmentRequest.id)
                                }
                            }
                        }
                    }

                    //Sync Questions
                    val questionsResponse = remoteQuestionDataSource.getAllByInstitution(
                        participant.institution!!.id,
                        participantUid
                    )
                    if (questionsResponse.isSuccessful) {
                        questionsResponse.body()!!.questions.let {
                            for (question in it) {
                                localQuestionDataSource.create(
                                    br.com.udesc.eso.tcc.studytalk.featureQuestion.data.converter.convertToRoomEntity(
                                        question
                                    )
                                )
                                questionsIds.remove(question.id)
                                localParticipantDataSource.create(
                                    br.com.udesc.eso.tcc.studytalk.featureParticipant.data.converter.convertToRoomEntity(
                                        question.participant
                                    )
                                )
                                participantsIds.remove(question.participant.id)

                                //Sync Answers
                                val answersResponse = remoteAnswerDataSource.getAllByQuestion(
                                    question.id,
                                    participantUid
                                )
                                if (answersResponse.isSuccessful) {
                                    answersResponse.body()!!.answers.let {
                                        for (answer in it) {
                                            localAnswerDataSource.create(
                                                br.com.udesc.eso.tcc.studytalk.featureAnswer.data.converter.convertToRoomEntity(
                                                    answer
                                                )
                                            )
                                            answersIds.remove(answer.id)
                                        }
                                    }
                                }
                            }
                        }

                        //Sync Reports
                        if (participant.privilege == Privilege.PRINCIPAL || participant.privilege == Privilege.TEACHER) {
                            val reportsResponse = remoteReportDataSource.getAllByInstitution(
                                participant.institution!!.id,
                                participantUid
                            )
                            if (reportsResponse.isSuccessful) {
                                reportsResponse.body()!!.reports.let {
                                    for (report in it) {
                                        localReportDataSource.create(
                                            br.com.udesc.eso.tcc.studytalk.featureReport.data.converter.convertToRoomEntity(
                                                report
                                            )
                                        )
                                        reportsIds.remove(report.id)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun clearRemainingData() {
        for (id in administratorsIds) {
            localAdministratorDataSource.deleteById(id)
        }
        for (id in answersIds) {
            localAnswerDataSource.deleteById(id)
        }
        for (id in enrollmentRequestiesIds) {
            localEnrollmentRequestDataSource.deleteById(id)
        }
        for (id in institutionsIds) {
            localInstitutionDataSource.deleteById(id)
        }
        for (id in participantsIds) {
            localParticipantDataSource.deleteById(id)
        }
        for (id in questionsIds) {
            localQuestionDataSource.deleteById(id)
        }
        for (id in reportsIds) {
            localReportDataSource.deleteById(id)
        }
    }
}
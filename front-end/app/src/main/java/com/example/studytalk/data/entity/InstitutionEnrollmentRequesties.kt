package com.example.studytalk.data.entity

data class InstitutionEnrollmentRequesties(
    private val ID: Long,
    private val INSTITUTION : Institution,
    private val ENROLLMENT_REQUESTIES: MutableList<EnrollmentRequest> = mutableListOf()
) {
    fun addEnrollmentRequest(enrollmentRequest: EnrollmentRequest) {
        ENROLLMENT_REQUESTIES.add(enrollmentRequest)
    }

    fun removeEnrollmentRequest(enrollmentRequest: EnrollmentRequest) {
        ENROLLMENT_REQUESTIES.remove(enrollmentRequest)
    }
}
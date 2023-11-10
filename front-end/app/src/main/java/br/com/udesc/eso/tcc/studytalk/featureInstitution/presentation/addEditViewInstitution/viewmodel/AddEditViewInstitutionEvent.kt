package br.com.udesc.eso.tcc.studytalk.featureInstitution.presentation.addEditViewInstitution.viewmodel

sealed class AddEditViewInstitutionEvent {
    data class ChangeEditMode(val value: Boolean) : AddEditViewInstitutionEvent()
    object Delete : AddEditViewInstitutionEvent()
    data class EnteredName(val value: String) : AddEditViewInstitutionEvent()
    object Save : AddEditViewInstitutionEvent()
}
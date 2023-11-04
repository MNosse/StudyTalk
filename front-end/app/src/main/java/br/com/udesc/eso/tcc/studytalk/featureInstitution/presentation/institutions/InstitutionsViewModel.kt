package br.com.udesc.eso.tcc.studytalk.featureInstitution.presentation.institutions

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InstitutionsViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

}
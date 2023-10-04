package br.com.udesc.eso.tcc.studytalk.entity.question.gateway

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.entity.question.model.Question

interface QuestionGateway {
    fun delete(id: Long)
    fun getAllByInstitutionId(id: Long): MutableList<Question>
    fun getById(id: Long): Question?
    fun update(id: Long, title: String?, description: String?, subjects: MutableList<Subject>?)
}
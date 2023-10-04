package br.com.udesc.eso.tcc.studytalk.entity.answer.gateway

import br.com.udesc.eso.tcc.studytalk.entity.answer.model.Answer

interface AnswerGateway {
    fun delete(id: Long)
    fun getAllByQuestionId(id: Long): MutableList<Answer>
    fun getById(id: Long): Answer?
    fun update(id: Long, description: String)
}
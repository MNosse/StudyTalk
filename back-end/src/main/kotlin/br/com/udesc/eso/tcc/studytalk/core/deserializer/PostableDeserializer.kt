package br.com.udesc.eso.tcc.studytalk.core.deserializer

import br.com.udesc.eso.tcc.studytalk.core.interfaces.Postable
import br.com.udesc.eso.tcc.studytalk.entity.answer.model.Answer
import br.com.udesc.eso.tcc.studytalk.entity.question.model.Question
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

class PostableDeserializer : JsonDeserializer<Postable>() {
    override fun deserialize(parser: JsonParser?, context: DeserializationContext?): Postable? {
        val objectMapper = parser?.codec as ObjectMapper
        val node: JsonNode = objectMapper.readTree(parser)
        return if (node.has("title")) {
            objectMapper.treeToValue(node, Question::class.java)
        } else {
            objectMapper.treeToValue(node, Answer::class.java)
        }
    }
}

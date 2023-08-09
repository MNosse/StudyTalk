package br.com.udesc.eso.tcc.studytalk

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StudytalkApplication

fun main(args: Array<String>) {
	runApplication<StudytalkApplication>(*args)
}

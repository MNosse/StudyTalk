package br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.administrator

import jakarta.persistence.*

@Entity
@Table(name = "administrator")
data class AdministratorSchema(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Column(nullable = false, unique = true, updatable = false)
    val uid: String
)
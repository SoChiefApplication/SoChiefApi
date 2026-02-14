package fr.vlegall.sochief.model.recipe

import fr.vlegall.sochief.model.common.AuditableEntity
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(
    name = "UTENSIL",
    uniqueConstraints = [UniqueConstraint(columnNames = ["name"])]
)
class Utensil(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @field:NotBlank(message = "The name cannot be blank.")
    @Column(nullable = false)
    var name: String? = null,
) : AuditableEntity()
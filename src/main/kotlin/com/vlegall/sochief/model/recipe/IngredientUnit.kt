package com.vlegall.sochief.model.recipe

import com.vlegall.sochief.model.common.AuditableEntity
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "INGREDIENT_UNIT")
class IngredientUnit(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @field:NotBlank(message = "The name cannot be blank.")
    @Column(nullable = false)
    var name: String? = null,
) : AuditableEntity()
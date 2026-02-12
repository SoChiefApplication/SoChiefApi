package com.vlegall.sochief.model.recipe

import com.fasterxml.jackson.annotation.JsonIgnore
import com.vlegall.sochief.model.common.AuditableEntity
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

@Entity
@Table(
    name = "RECIPE_UTENSIL",
    uniqueConstraints = [UniqueConstraint(columnNames = ["recipe_id", "utensil_id"])]
)
class RecipeUtensil(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @field:NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id", nullable = false)
    @JsonIgnore
    var recipe: Recipe? = null,

    @field:NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utensil_id", nullable = false)
    var utensil: Utensil? = null,
) : AuditableEntity()
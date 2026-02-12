package com.vlegall.sochief.model.recipe

import com.fasterxml.jackson.annotation.JsonIgnore
import com.vlegall.sochief.model.common.AuditableEntity
import jakarta.persistence.*
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.time.Duration

@Entity
@Table(name = "RECIPE_STEP")
class RecipeStep(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @field:NotNull(message = "Recipe cannot be null.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id", nullable = false)
    @JsonIgnore
    var recipe: Recipe? = null,

    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @field:NotNull(message = "Duration cannot be null.")
    @Column(nullable = false, name = "duration")
    var duration: Duration? = null,

    @field:NotNull(message = "Position cannot be null.")
    @field:Positive
    @Column(nullable = false, name = "position")
    var position: Int? = null,
) : AuditableEntity() {
    @AssertTrue(message = "Duration must be positive.")
    fun isDurationPositive(): Boolean = duration!! > Duration.ZERO
}
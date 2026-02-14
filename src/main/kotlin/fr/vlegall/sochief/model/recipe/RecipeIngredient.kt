package fr.vlegall.sochief.model.recipe

import com.fasterxml.jackson.annotation.JsonIgnore
import fr.vlegall.sochief.model.common.AuditableEntity
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

@Entity
@Table(
    name = "RECIPE_INGREDIENT",
    uniqueConstraints = [UniqueConstraint(columnNames = ["recipe_id", "ingredient_id"])]
)
class RecipeIngredient(
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
    @JoinColumn(name = "ingredient_id", nullable = false)
    var ingredient: Ingredient? = null,

    @field:NotNull
    @Column(nullable = false)
    @field:Positive
    var quantity: Long? = null,

    @field:NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unit_id", nullable = false)
    var unit: IngredientUnit? = null,
) : AuditableEntity()
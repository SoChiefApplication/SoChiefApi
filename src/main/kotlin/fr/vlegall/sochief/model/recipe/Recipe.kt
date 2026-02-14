package fr.vlegall.sochief.model.recipe

import fr.vlegall.sochief.model.common.AuditableEntity
import jakarta.persistence.*
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.time.Duration

@Entity
@Table(name = "RECIPE")
class Recipe(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @field:NotBlank(message = "The title name cannot be blank.")
    @Column(nullable = false)
    var title: String? = null,

    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    var category: RecipeCategory? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "difficulty_id", nullable = false)
    var difficulty: RecipeDifficulty? = null,

    @field:NotNull(message = "Portions cannot be null.")
    @Column(nullable = false, name = "initial_portions")
    @field:Positive
    var initialPortions: Int? = null,

    @field:NotNull(message = "Preparation time cannot be null.")
    @Column(nullable = false, name = "preparation_time")
    var preparationTime: Duration? = null,

    @field:NotNull(message = "Cooking time cannot be null.")
    @Column(nullable = false, name = "cooking_time")
    var cookingTime: Duration? = null,

    @OneToMany(
        mappedBy = "recipe",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    var ingredients: MutableSet<RecipeIngredient> = mutableSetOf(),

    @OneToMany(
        mappedBy = "recipe",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @OrderBy("position ASC")
    var steps: MutableList<RecipeStep> = mutableListOf(),

    @OneToMany(
        mappedBy = "recipe",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    var utensils: MutableSet<RecipeUtensil> = mutableSetOf(),

    @OneToMany(
        mappedBy = "recipe",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    var tags: MutableSet<RecipeTag> = mutableSetOf()
) : AuditableEntity() {
    fun addTag(tag: Tag) {
        if (tags.any { it.tag?.id == tag.id }) return
        tags.add(RecipeTag(recipe = this, tag = tag))
    }

    fun addUtensil(utensil: Utensil) {
        if (utensils.any { it.utensil?.id == utensil.id }) return
        utensils.add(RecipeUtensil(recipe = this, utensil = utensil))
    }

    fun addStep(description: String?, duration: Duration) {
        val next = (steps.maxOfOrNull { it.position ?: 0 } ?: 0) + 1
        steps.add(
            RecipeStep(
                recipe = this,
                description = description,
                duration = duration,
                position = next
            )
        )
    }

    @AssertTrue(message = "Preparation time must be positive.")
    fun isPreparationTimePositive(): Boolean = preparationTime!! > Duration.ZERO

    @AssertTrue(message = "Cooking time must be positive.")
    fun isCookingTimePositive(): Boolean = cookingTime!! > Duration.ZERO

}

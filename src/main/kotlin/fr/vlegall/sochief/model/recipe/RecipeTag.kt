package fr.vlegall.sochief.model.recipe

import com.fasterxml.jackson.annotation.JsonIgnore
import fr.vlegall.sochief.model.common.AuditableEntity
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

@Entity
@Table(
    name = "RECIPE_TAG",
    uniqueConstraints = [UniqueConstraint(columnNames = ["recipe_id", "tag_id"])]
)
class RecipeTag(
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
    @JoinColumn(name = "tag_id", nullable = false)
    var tag: Tag? = null,
) : AuditableEntity()
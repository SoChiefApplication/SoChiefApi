package fr.vlegall.sochief.model.recipe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.vlegall.sochief.model.common.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "RECIPE_UTENSIL",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"recipe_id", "utensil_id"})}
)
public class RecipeUtensil extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id", nullable = false)
    @JsonIgnore
    private Recipe recipe;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utensil_id", nullable = false)
    private Utensil utensil;
}


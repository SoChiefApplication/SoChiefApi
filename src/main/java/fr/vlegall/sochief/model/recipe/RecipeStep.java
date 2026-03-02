package fr.vlegall.sochief.model.recipe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.vlegall.sochief.model.common.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "RECIPE_STEP")
public class RecipeStep extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Recipe cannot be null.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id", nullable = false)
    @JsonIgnore
    private Recipe recipe;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Duration cannot be null.")
    @Column(nullable = false, name = "duration")
    private Duration duration;

    @NotNull(message = "Position cannot be null.")
    @Positive
    @Column(nullable = false, name = "position")
    private Integer position;

    @AssertTrue(message = "Duration must be positive.")
    public boolean isDurationPositive() {
        return duration != null && duration.compareTo(Duration.ZERO) > 0;
    }
}


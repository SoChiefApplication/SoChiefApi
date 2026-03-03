package fr.vlegall.sochief.model.recipe;

import fr.vlegall.sochief.model.common.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "RECIPE")
public class Recipe extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The title name cannot be blank.")
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private RecipeCategory category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "difficulty_id", nullable = false)
    private RecipeDifficulty difficulty;

    @NotNull(message = "Portions cannot be null.")
    @Positive
    @Column(nullable = false, name = "initial_portions")
    private Integer initialPortions;

    @NotNull(message = "Preparation time cannot be null.")
    @Column(nullable = false, name = "preparation_time")
    private Duration preparationTime;

    @NotNull(message = "Cooking time cannot be null.")
    @Column(nullable = false, name = "cooking_time")
    private Duration cookingTime;

    @OneToMany(
            mappedBy = "recipe",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<RecipeIngredient> ingredients = new HashSet<>();

    @OneToMany(
            mappedBy = "recipe",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy("position ASC")
    private List<RecipeStep> steps = new ArrayList<>();

    @OneToMany(
            mappedBy = "recipe",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<RecipeUtensil> utensils = new HashSet<>();

    @OneToMany(
            mappedBy = "recipe",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<RecipeTag> tags = new HashSet<>();

    @Column(name = "minio_object_key_img")
    private String minioObjectKeyImg;

    public void addTag(Tag tag) {
        boolean exists = tags.stream().anyMatch(t -> t.getTag() != null && t.getTag().getId().equals(tag.getId()));
        if (exists) return;
        RecipeTag rt = new RecipeTag();
        rt.setRecipe(this);
        rt.setTag(tag);
        tags.add(rt);
    }

    public void addUtensil(Utensil utensil) {
        boolean exists = utensils.stream().anyMatch(u -> u.getUtensil() != null && u.getUtensil().getId().equals(utensil.getId()));
        if (exists) return;
        RecipeUtensil ru = new RecipeUtensil();
        ru.setRecipe(this);
        ru.setUtensil(utensil);
        utensils.add(ru);
    }

    public void addStep(String description, Duration duration) {
        int next = steps.stream().map(s -> s.getPosition() != null ? s.getPosition() : 0).max(Integer::compareTo).orElse(0) + 1;
        RecipeStep step = new RecipeStep();
        step.setRecipe(this);
        step.setDescription(description);
        step.setDuration(duration);
        step.setPosition(next);
        steps.add(step);
    }

    @AssertTrue(message = "Preparation time must be positive.")
    public boolean isPreparationTimePositive() {
        return preparationTime != null && preparationTime.compareTo(Duration.ZERO) > 0;
    }

    @AssertTrue(message = "Cooking time must be positive.")
    public boolean isCookingTimePositive() {
        return cookingTime != null && cookingTime.compareTo(Duration.ZERO) > 0;
    }
}


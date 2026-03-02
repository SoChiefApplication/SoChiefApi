package fr.vlegall.sochief.repository;

import fr.vlegall.sochief.model.recipe.Utensil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtensilRepository extends JpaRepository<Utensil, Long> {
    Utensil findByNameIgnoreCase(String name);
}


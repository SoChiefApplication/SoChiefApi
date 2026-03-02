package fr.vlegall.sochief.repository;

import fr.vlegall.sochief.model.recipe.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByNameIgnoreCase(String name);
}


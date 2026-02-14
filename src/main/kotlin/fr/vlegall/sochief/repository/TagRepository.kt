package fr.vlegall.sochief.repository

import fr.vlegall.sochief.model.recipe.Tag
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : JpaRepository<Tag, Long> {
    fun findByNameIgnoreCase(name: String): Tag?
}

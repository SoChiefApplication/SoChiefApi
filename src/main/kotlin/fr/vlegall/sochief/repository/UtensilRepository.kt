package fr.vlegall.sochief.repository

import fr.vlegall.sochief.model.recipe.Utensil
import org.springframework.data.jpa.repository.JpaRepository

interface UtensilRepository : JpaRepository<Utensil, Long> {
    fun findByNameIgnoreCase(name: String): Utensil?
}

package fr.vlegall.sochief.repository

import fr.vlegall.sochief.model.recipe.Recipe
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface RecipeRepository : JpaRepository<Recipe, Long> {

    @Query(
        value = """
            select distinct r
            from Recipe r
            left join r.ingredients ri
            left join ri.ingredient i
            left join r.tags rt
            left join rt.tag t
            where (:categoryId is null or r.category.id = :categoryId)
              and (
                   :q is null or :q = ''
                   or lower(r.title) like lower(concat('%', :q, '%'))
                   or lower(i.name) like lower(concat('%', :q, '%'))
                   or lower(t.name) like lower(concat('%', :q, '%'))
              )
        """,
        countQuery = """
            select count(distinct r.id)
            from Recipe r
            left join r.ingredients ri
            left join ri.ingredient i
            left join r.tags rt
            left join rt.tag t
            where (:categoryId is null or r.category.id = :categoryId)
              and (
                   :q is null or :q = ''
                   or lower(r.title) like lower(concat('%', :q, '%'))
                   or lower(i.name) like lower(concat('%', :q, '%'))
                   or lower(t.name) like lower(concat('%', :q, '%'))
              )
        """
    )
    fun search(
        @Param("q") q: String?,
        @Param("categoryId") categoryId: Long?,
        pageable: Pageable
    ): Page<Recipe>

    /**
     * Pour GET /recipes/{id} (détail complet)
     * EntityGraph évite les N+1 et les LazyInitializationException.
     */
    @EntityGraph(
        attributePaths = [
            "category",
            "difficulty",
            "ingredients",
            "ingredients.ingredient",
            "ingredients.unit",
            "steps",
            "utensils",
            "utensils.utensil",
            "tags",
            "tags.tag"
        ]
    )
    fun findDetailedById(id: Long): Recipe?
}

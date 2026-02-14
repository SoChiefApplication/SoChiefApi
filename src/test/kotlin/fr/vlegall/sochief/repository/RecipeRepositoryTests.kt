package fr.vlegall.sochief.repository

import org.junit.jupiter.api.Test
import org.mockito.Mockito

class RecipeRepositoryTests {
    @Test
    fun can_invoke_findDetailedById() {
        val repo = Mockito.mock(RecipeRepository::class.java)
        Mockito.`when`(repo.findDetailedById(1L)).thenReturn(null)
        repo.findDetailedById(1L)
    }
}

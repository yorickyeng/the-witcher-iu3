package com.fk.thewitcheriu3

import com.fk.thewitcheriu3.domain.entities.Cell
import com.fk.thewitcheriu3.domain.entities.characters.units.witchers.CatSchoolWitcher
import com.fk.thewitcheriu3.domain.penalty
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun penalty_isCorrect() {
        val cell = mockk<Cell>()
        val character = mockk<CatSchoolWitcher>()

        every { cell.type } returns "forest"

        val result = penalty(cell, character)

        assertEquals(0, result)
    }
}
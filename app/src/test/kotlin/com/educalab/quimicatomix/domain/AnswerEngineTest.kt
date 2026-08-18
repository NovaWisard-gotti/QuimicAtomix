package com.educalab.quimicatomix.domain

import com.educalab.quimicatomix.data.local.entity.InteractionType
import com.educalab.quimicatomix.domain.logic.AnswerEngine
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AnswerEngineTest {

    @Test
    fun `ORDENAR correct when sequence matches exactly`() {
        val outcome = AnswerEngine.evaluate(InteractionType.ORDENAR, "hielo,agua,vapor", "hielo,agua,vapor")
        assertTrue(outcome.isCorrect)
    }

    @Test
    fun `ORDENAR incorrect when sequence order differs`() {
        val outcome = AnswerEngine.evaluate(InteractionType.ORDENAR, "hielo,agua,vapor", "agua,hielo,vapor")
        assertFalse(outcome.isCorrect)
    }

    @Test
    fun `CLASIFICAR correct when groups match regardless of item order within group`() {
        val outcome = AnswerEngine.evaluate(InteractionType.CLASIFICAR, "a,b|c,d", "b,a|d,c")
        assertTrue(outcome.isCorrect)
    }

    @Test
    fun `CLASIFICAR incorrect when an item is in the wrong group`() {
        val outcome = AnswerEngine.evaluate(InteractionType.CLASIFICAR, "a,b|c,d", "a,c|b,d")
        assertFalse(outcome.isCorrect)
    }

    @Test
    fun `CLASIFICAR marks partial credit when most items are correctly grouped`() {
        val outcome = AnswerEngine.evaluate(InteractionType.CLASIFICAR, "a,b,c|d", "a,b,d|c")
        assertFalse(outcome.isCorrect)
        assertTrue(outcome.isPartial)
    }

    @Test
    fun `CLASIFICAR with wrong group count is incorrect`() {
        val outcome = AnswerEngine.evaluate(InteractionType.CLASIFICAR, "a,b|c,d", "a,b,c,d")
        assertFalse(outcome.isCorrect)
    }

    @Test
    fun `CONECTAR correct when all pairs match as a set`() {
        val outcome = AnswerEngine.evaluate(InteractionType.CONECTAR, "H-Hidrogeno,O-Oxigeno", "O-Oxigeno,H-Hidrogeno")
        assertTrue(outcome.isCorrect)
    }

    @Test
    fun `CONECTAR incorrect when a pair is wrong`() {
        val outcome = AnswerEngine.evaluate(InteractionType.CONECTAR, "H-Hidrogeno,O-Oxigeno", "H-Oxigeno,O-Hidrogeno")
        assertFalse(outcome.isCorrect)
    }

    @Test
    fun `default set comparison correct for single value match`() {
        val outcome = AnswerEngine.evaluate(InteractionType.PREDECIR, "se_derrite", "se_derrite")
        assertTrue(outcome.isCorrect)
    }

    @Test
    fun `default set comparison incorrect for wrong single value`() {
        val outcome = AnswerEngine.evaluate(InteractionType.PREDECIR, "se_derrite", "se_endurece")
        assertFalse(outcome.isCorrect)
    }

    @Test
    fun `default set comparison supports multi-value answers like build`() {
        val outcome = AnswerEngine.evaluate(InteractionType.CONSTRUIR, "N,H", "H,N")
        assertTrue(outcome.isCorrect)
    }

    @Test
    fun `blank correctAnswerCsv is always incorrect (defensive)`() {
        val outcome = AnswerEngine.evaluate(InteractionType.OPCION_MULTIPLE, "", "anything")
        assertFalse(outcome.isCorrect)
    }
}

package com.educalab.quimicatomix.ui.components

/**
 * Catálogo cerrado de ilustraciones vectoriales dibujadas por Canvas. Cada miembro es una
 * "pieza" visual reutilizable (matraz, átomo, insignia...) que se colorea y combina de
 * forma distinta según el contexto (sustancia, insignia, tema...), cumpliendo el mínimo de
 * 8-15 ilustraciones temáticas reutilizables exigido para apps infantiles sin depender de
 * imágenes externas.
 */
enum class IllustrationKind {
    BEAKER, FLASK, TEST_TUBE, DROPLET, MAGNET, FUNNEL, THERMOMETER, MICROSCOPE,
    GLOVES, GOGGLES, LAB_COAT, ATOM, MOLECULE, CRYSTAL_GRAIN, LEAF, MEDAL,
    SHIELD, STAR_BADGE, TROPHY, WARNING_TRIANGLE, FLAME, SKULL, CORROSIVE, RECYCLE,
    FIRST_AID, BOOK, MAP, ICE_CUBE, STEAM_CLOUD, BUBBLE_JAR, PERIODIC_CARD
}

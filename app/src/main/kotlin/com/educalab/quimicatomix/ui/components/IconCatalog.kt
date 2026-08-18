package com.educalab.quimicatomix.ui.components

import androidx.compose.ui.graphics.Color
import com.educalab.quimicatomix.ui.theme.LabBlue500
import com.educalab.quimicatomix.ui.theme.LabCoral500
import com.educalab.quimicatomix.ui.theme.LabGold500
import com.educalab.quimicatomix.ui.theme.LabLime500
import com.educalab.quimicatomix.ui.theme.LabTeal500
import com.educalab.quimicatomix.ui.theme.LabViolet500

/**
 * Traduce las claves de icono del contenido semilla (texto legible como "sub_agua" o
 * "badge_leyenda") a una pieza real del catálogo de ilustraciones [IllustrationKind].
 * Se usa coincidencia de subcadenas para que cualquier clave nueva de contenido futuro siga
 * resolviendo a una ilustración razonable sin tener que listar cada caso uno por uno.
 */
object IconCatalog {

    private val paletteFallback = listOf(LabTeal500, LabBlue500, LabViolet500, LabCoral500, LabLime500, LabGold500)

    fun resolve(iconKey: String): IllustrationKind {
        val k = iconKey.lowercase()
        return when {
            "vaso" in k || "beaker" in k -> IllustrationKind.BEAKER
            "matraz" in k || "flask" in k -> IllustrationKind.FLASK
            "tubo" in k -> IllustrationKind.TEST_TUBE
            "hierro" in k || "magnet" in k || "iman" in k -> IllustrationKind.MAGNET
            "embudo" in k || "filtra" in k || "funnel" in k -> IllustrationKind.FUNNEL
            "termometro" in k || "temperatura" in k -> IllustrationKind.THERMOMETER
            "microscopio" in k -> IllustrationKind.MICROSCOPE
            "guante" in k -> IllustrationKind.GLOVES
            "gafas" in k || "goggles" in k -> IllustrationKind.GOGGLES
            "bata" in k -> IllustrationKind.LAB_COAT
            "atomo" in k || "atom" == k || k.startsWith("h") && k.length <= 2 -> IllustrationKind.ATOM
            "mole" in k || "molecula" in k -> IllustrationKind.MOLECULE
            "sal" in k || "azucar" in k || "cristal" in k || "crystal" in k -> IllustrationKind.CRYSTAL_GRAIN
            "planta" in k || "hoja" in k || "leaf" in k || "repollo" in k -> IllustrationKind.LEAF
            "medalla" in k || "medal" in k -> IllustrationKind.MEDAL
            "guardian" in k || "shield" in k || "escudo" in k -> IllustrationKind.SHIELD
            "estrella" in k || "star" in k -> IllustrationKind.STAR_BADGE
            "trofeo" in k || "trophy" in k || "cientifico" in k || "leyenda" in k || "arquitecto" in k -> IllustrationKind.TROPHY
            "warning" in k || "triangulo" in k || "etq" in k -> IllustrationKind.WARNING_TRIANGLE
            "llama" in k || "flame" in k || "fuego" in k -> IllustrationKind.FLAME
            "calavera" in k || "skull" in k -> IllustrationKind.SKULL
            "corrosiv" in k -> IllustrationKind.CORROSIVE
            "reciclaje" in k || "recycle" in k -> IllustrationKind.RECYCLE
            "aux" in k || "auxilio" in k -> IllustrationKind.FIRST_AID
            "academia" in k || "book" in k || "libro" in k -> IllustrationKind.BOOK
            "mapa" in k || "map" in k || "progreso" in k -> IllustrationKind.MAP
            "hielo" in k || "ice" in k -> IllustrationKind.ICE_CUBE
            "vapor" in k || "steam" in k -> IllustrationKind.STEAM_CLOUD
            "mezcla" in k || "burbuja" in k || "bubble" in k || "jabon" in k -> IllustrationKind.BUBBLE_JAR
            "tabla" in k || "periodic" in k -> IllustrationKind.PERIODIC_CARD
            "gota" in k || "aceite" in k || "colorante" in k || "vinagre" in k || "droplet" in k -> IllustrationKind.DROPLET
            else -> IllustrationKind.ATOM
        }
    }

    /** Color determinista (mismo texto -> mismo color) para claves sin colorHex explícito. */
    fun colorFor(key: String): Color {
        val idx = key.sumOf { it.code } % paletteFallback.size
        return paletteFallback[idx]
    }

    fun parseHexOrFallback(hex: String?, fallbackKey: String): Color {
        if (hex.isNullOrBlank()) return colorFor(fallbackKey)
        return runCatching {
            val clean = hex.removePrefix("#")
            val colorLong = clean.toLong(16)
            if (clean.length == 6) Color((0xFF000000 or colorLong)) else Color(colorLong)
        }.getOrElse { colorFor(fallbackKey) }
    }
}

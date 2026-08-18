package com.educalab.quimicatomix.data.seed

import com.educalab.quimicatomix.data.local.entity.SubstanceProperty

/**
 * Propiedades observables de algunas sustancias clave, usadas en las tarjetas de
 * propiedades del módulo de Mezclas (no exhaustivo: se prioriza calidad sobre relleno).
 */
object SeedSubstanceProperties {
    val list: List<SubstanceProperty> = listOf(
        SubstanceProperty(substanceId = "agua", propertyKey = "Color", propertyValue = "Transparente", iconKey = "prop_color"),
        SubstanceProperty(substanceId = "agua", propertyKey = "Solubilidad", propertyValue = "Es el disolvente universal", iconKey = "prop_solubilidad"),
        SubstanceProperty(substanceId = "aceite", propertyKey = "Densidad", propertyValue = "Menos denso que el agua: flota", iconKey = "prop_densidad"),
        SubstanceProperty(substanceId = "aceite", propertyKey = "Solubilidad", propertyValue = "No se disuelve en agua", iconKey = "prop_solubilidad"),
        SubstanceProperty(substanceId = "sal", propertyKey = "Solubilidad", propertyValue = "Se disuelve por completo en agua", iconKey = "prop_solubilidad"),
        SubstanceProperty(substanceId = "sal", propertyKey = "Aspecto", propertyValue = "Cristales pequeños y blancos", iconKey = "prop_aspecto"),
        SubstanceProperty(substanceId = "limaduras_hierro", propertyKey = "Magnetismo", propertyValue = "Un imán las atrae con fuerza", iconKey = "prop_magnetismo"),
        SubstanceProperty(substanceId = "limaduras_hierro", propertyKey = "Solubilidad", propertyValue = "No se disuelve en agua", iconKey = "prop_solubilidad"),
        SubstanceProperty(substanceId = "arena", propertyKey = "Densidad", propertyValue = "Más densa que el agua: se hunde", iconKey = "prop_densidad"),
        SubstanceProperty(substanceId = "corcho", propertyKey = "Densidad", propertyValue = "Muy liviano: flota fácilmente", iconKey = "prop_densidad"),
        SubstanceProperty(substanceId = "azucar", propertyKey = "Solubilidad", propertyValue = "Se disuelve por completo en agua", iconKey = "prop_solubilidad"),
        SubstanceProperty(substanceId = "vinagre", propertyKey = "Reactividad", propertyValue = "Reacciona con el bicarbonato liberando gas", iconKey = "prop_reactividad")
    )
}

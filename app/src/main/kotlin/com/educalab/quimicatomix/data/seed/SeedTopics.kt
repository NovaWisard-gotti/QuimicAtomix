// ============================================================================
// ARCHIVO GENERADO POR tools/generate_seed.py — NO EDITAR A MANO.
// Para cambiar contenido, edita el script generador y vuelve a ejecutarlo.
// ============================================================================

package com.educalab.quimicatomix.data.seed

import com.educalab.quimicatomix.data.local.entity.ChemicalTopic

object SeedTopics {
    val list: List<ChemicalTopic> = listOf(
        ChemicalTopic(id="estados", title="Materia y Estados", shortDescription="Sólidos, líquidos y gases en acción", narrativeIntro="El Laboratorio necesita tu ayuda para clasificar y transformar la materia.", iconKey="topic_estados", colorHex="#2E7DFF", orderIndex=1, minLevelToUnlock=1),
        ChemicalTopic(id="mezclas", title="Mezclas", shortDescription="Combina sustancias virtuales y descubre qué pasa", narrativeIntro="Quimi guarda decenas de frascos mezclados. ¡Ayúdale a identificarlos!", iconKey="topic_mezclas", colorHex="#14C7B4", orderIndex=2, minLevelToUnlock=1),
        ChemicalTopic(id="separacion", title="Separación", shortDescription="Filtra, decanta y evapora para recuperar sustancias", narrativeIntro="Algunas mezclas escondidas deben separarse antes de guardarse en la estantería.", iconKey="topic_separacion", colorHex="#7ED957", orderIndex=3, minLevelToUnlock=2),
        ChemicalTopic(id="atomos", title="Átomos", shortDescription="Los bloques diminutos que forman todo", narrativeIntro="Un mapa gigante de átomos apareció en la pared del laboratorio.", iconKey="topic_atomos", colorHex="#8B5CF6", orderIndex=4, minLevelToUnlock=2),
        ChemicalTopic(id="moleculas", title="Constructor Molecular", shortDescription="Une átomos y construye moléculas reales", narrativeIntro="La mesa de construcción molecular de Quimi espera tus creaciones.", iconKey="topic_moleculas", colorHex="#FF6B4A", orderIndex=5, minLevelToUnlock=3),
        ChemicalTopic(id="reacciones", title="Reacciones Virtuales", shortDescription="Observa cambios seguros y predice resultados", narrativeIntro="Algo burbujea en la cámara de reacciones. ¿Puedes predecir qué ocurrirá?", iconKey="topic_reacciones", colorHex="#FFC145", orderIndex=6, minLevelToUnlock=3),
    )
}
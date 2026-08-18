// ============================================================================
// ARCHIVO GENERADO POR tools/generate_seed.py — NO EDITAR A MANO.
// Para cambiar contenido, edita el script generador y vuelve a ejecutarlo.
// ============================================================================

package com.educalab.quimicatomix.data.seed

import com.educalab.quimicatomix.data.local.entity.Atom
import com.educalab.quimicatomix.data.local.entity.AtomCategory

object SeedAtoms {
    val list: List<Atom> = listOf(
        Atom(id="H", symbol="H", name="Hidrógeno", protons=1, electronsShellsCsv="1", category=AtomCategory.NO_METAL, colorHex="#90CAF9", funFact="Es el átomo más ligero y el más abundante del universo.", commonValence=1),
        Atom(id="He", symbol="He", name="Helio", protons=2, electronsShellsCsv="2", category=AtomCategory.GAS_NOBLE, colorHex="#B39DDB", funFact="Es tan liviano que hace flotar los globos de fiesta.", commonValence=0),
        Atom(id="C", symbol="C", name="Carbono", protons=6, electronsShellsCsv="2,4", category=AtomCategory.NO_METAL, colorHex="#616161", funFact="Forma parte de todos los seres vivos, incluido tú.", commonValence=4),
        Atom(id="N", symbol="N", name="Nitrógeno", protons=7, electronsShellsCsv="2,5", category=AtomCategory.NO_METAL, colorHex="#64B5F6", funFact="Compone casi el 78% del aire que respiramos.", commonValence=3),
        Atom(id="O", symbol="O", name="Oxígeno", protons=8, electronsShellsCsv="2,6", category=AtomCategory.NO_METAL, colorHex="#EF5350", funFact="Lo necesitamos para respirar y para que el fuego arda.", commonValence=2),
        Atom(id="F", symbol="F", name="Flúor", protons=9, electronsShellsCsv="2,7", category=AtomCategory.HALOGENO, colorHex="#AED581", funFact="Ayuda a proteger los dientes en las pastas dentales.", commonValence=1),
        Atom(id="Ne", symbol="Ne", name="Neón", protons=10, electronsShellsCsv="2,8", category=AtomCategory.GAS_NOBLE, colorHex="#F06292", funFact="Brilla de color naranja-rojizo en los carteles luminosos.", commonValence=0),
        Atom(id="Na", symbol="Na", name="Sodio", protons=11, electronsShellsCsv="2,8,1", category=AtomCategory.METAL, colorHex="#FFD54F", funFact="Junto al cloro forma la sal de mesa que usamos al cocinar.", commonValence=1),
        Atom(id="Mg", symbol="Mg", name="Magnesio", protons=12, electronsShellsCsv="2,8,2", category=AtomCategory.METAL, colorHex="#CE93D8", funFact="Le da a las plantas su color verde dentro de la clorofila.", commonValence=2),
        Atom(id="Al", symbol="Al", name="Aluminio", protons=13, electronsShellsCsv="2,8,3", category=AtomCategory.METAL, colorHex="#B0BEC5", funFact="Es un metal liviano usado en latas y aviones.", commonValence=3),
        Atom(id="Si", symbol="Si", name="Silicio", protons=14, electronsShellsCsv="2,8,4", category=AtomCategory.METALOIDE, colorHex="#A1887F", funFact="Es el ingrediente principal de la arena y del vidrio.", commonValence=4),
        Atom(id="Cl", symbol="Cl", name="Cloro", protons=17, electronsShellsCsv="2,8,7", category=AtomCategory.HALOGENO, colorHex="#AED581", funFact="Se combina con el sodio para formar la sal común.", commonValence=1),
        Atom(id="K", symbol="K", name="Potasio", protons=19, electronsShellsCsv="2,8,8,1", category=AtomCategory.METAL, colorHex="#FFB74D", funFact="Es esencial para que tus músculos funcionen bien.", commonValence=1),
        Atom(id="Ca", symbol="Ca", name="Calcio", protons=20, electronsShellsCsv="2,8,8,2", category=AtomCategory.METAL, colorHex="#FFF176", funFact="Forma parte de tus huesos y tus dientes.", commonValence=2),
        Atom(id="Fe", symbol="Fe", name="Hierro", protons=26, electronsShellsCsv="2,8,14,2", category=AtomCategory.METAL, colorHex="#8D6E63", funFact="Un imán puede atraerlo; también está en tu sangre.", commonValence=2),
        Atom(id="Ar", symbol="Ar", name="Argón", protons=18, electronsShellsCsv="2,8,8", category=AtomCategory.GAS_NOBLE, colorHex="#4DD0E1", funFact="Es un gas noble que casi nunca reacciona con nada.", commonValence=0),
    )
}
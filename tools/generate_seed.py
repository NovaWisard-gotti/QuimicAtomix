#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Generador de contenido semilla de QuimicAtomix.

Este script NO se ejecuta en la app (la app no tiene Python). Es una herramienta de
autoría: escribe el contenido educativo real (temas, sustancias, 55 prácticas, átomos,
moléculas, 35 escenarios de seguridad, insignias y equipamiento) en Python -donde es
más manejable mantener listas largas- y emite archivos .kt válidos con objetos Kotlin
inmutables (List<Entity>) listos para que DatabaseSeeder los inserte con Room.

Ejecutar: python3 tools/generate_seed.py
Salida:   app/src/main/kotlin/com/educalab/quimicatomix/data/seed/Seed*.kt
"""
import os

OUT_DIR = os.path.join(
    os.path.dirname(__file__), "..",
    "app", "src", "main", "kotlin", "com", "educalab", "quimicatomix", "data", "seed"
)
PKG = "com.educalab.quimicatomix.data.seed"

def esc(s: str) -> str:
    return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", " ").strip()

def kt_str(s: str) -> str:
    return f"\"{esc(s)}\""

HEADER = """// ============================================================================
// ARCHIVO GENERADO POR tools/generate_seed.py — NO EDITAR A MANO.
// Para cambiar contenido, edita el script generador y vuelve a ejecutarlo.
// ============================================================================
"""

# ---------------------------------------------------------------------------
# 1) TEMAS (ChemicalTopic)
# ---------------------------------------------------------------------------
TOPICS = [
    dict(id="estados", title="Materia y Estados", short="Sólidos, líquidos y gases en acción",
         narrative="El Laboratorio necesita tu ayuda para clasificar y transformar la materia.",
         icon="topic_estados", color="#2E7DFF", order=1, min_level=1),
    dict(id="mezclas", title="Mezclas", short="Combina sustancias virtuales y descubre qué pasa",
         narrative="Quimi guarda decenas de frascos mezclados. ¡Ayúdale a identificarlos!",
         icon="topic_mezclas", color="#14C7B4", order=2, min_level=1),
    dict(id="separacion", title="Separación", short="Filtra, decanta y evapora para recuperar sustancias",
         narrative="Algunas mezclas escondidas deben separarse antes de guardarse en la estantería.",
         icon="topic_separacion", color="#7ED957", order=3, min_level=2),
    dict(id="atomos", title="Átomos", short="Los bloques diminutos que forman todo",
         narrative="Un mapa gigante de átomos apareció en la pared del laboratorio.",
         icon="topic_atomos", color="#8B5CF6", order=4, min_level=2),
    dict(id="moleculas", title="Constructor Molecular", short="Une átomos y construye moléculas reales",
         narrative="La mesa de construcción molecular de Quimi espera tus creaciones.",
         icon="topic_moleculas", color="#FF6B4A", order=5, min_level=3),
    dict(id="reacciones", title="Reacciones Virtuales", short="Observa cambios seguros y predice resultados",
         narrative="Algo burbujea en la cámara de reacciones. ¿Puedes predecir qué ocurrirá?",
         icon="topic_reacciones", color="#FFC145", order=6, min_level=3),
]

# ---------------------------------------------------------------------------
# 2) SUSTANCIAS VIRTUALES (VirtualSubstance) — 100% conceptuales y seguras
# ---------------------------------------------------------------------------
# (id, topic, name, formula, state, color, desc, icon, miscible, magnetic, density_tier)
SUBSTANCES = [
    ("agua", "estados", "Agua", "H2O", "LIQUIDO", "#4FC3F7",
     "Líquido transparente esencial para la vida. Cambia de estado con la temperatura.", "sub_agua", True, False, 2),
    ("hielo", "estados", "Hielo", "H2O (sólido)", "SOLIDO", "#B3E5FC",
     "Agua en estado sólido. Sus partículas están muy ordenadas y casi no se mueven.", "sub_hielo", False, False, 2),
    ("vapor_agua", "estados", "Vapor de agua", "H2O (gas)", "GASEOSO", "#ECEFF1",
     "Agua en estado gaseoso. Sus partículas se mueven libremente y ocupan más espacio.", "sub_vapor", False, False, 1),
    ("mantequilla", "estados", "Mantequilla", "Mezcla de grasas", "SOLIDO", "#FFE082",
     "Sólido blando a temperatura ambiente que se derrite fácilmente con el calor.", "sub_mantequilla", False, False, 2),
    ("chocolate", "estados", "Chocolate", "Mezcla de cacao", "SOLIDO", "#8D6E63",
     "Sólido que cambia a líquido al calentarse y vuelve a solidificar al enfriarse.", "sub_chocolate", False, False, 2),
    ("cera_vela", "estados", "Cera", "Hidrocarburos sólidos", "SOLIDO", "#FFF9C4",
     "Sólido que se ablanda y derrite con el calor; al enfriarse, vuelve a endurecerse.", "sub_cera", False, False, 2),
    ("arena", "mezclas", "Arena", "SiO2 (principalmente)", "SOLIDO", "#D7B98E",
     "Granos sólidos diminutos. No se disuelve en agua.", "sub_arena", False, False, 3),
    ("aceite", "mezclas", "Aceite vegetal", "Triglicéridos", "LIQUIDO", "#FFD54F",
     "Líquido que no se mezcla con el agua y flota sobre ella.", "sub_aceite", False, False, 1),
    ("sal", "mezclas", "Sal de mesa", "NaCl", "SOLIDO", "#F5F5F5",
     "Sólido cristalino que se disuelve por completo en agua.", "sub_sal", True, False, 3),
    ("azucar", "mezclas", "Azúcar", "Sacarosa", "SOLIDO", "#FFF3E0",
     "Sólido dulce que se disuelve fácilmente en agua.", "sub_azucar", True, False, 3),
    ("limaduras_hierro", "mezclas", "Limaduras de hierro", "Fe", "SOLIDO", "#9E9E9E",
     "Trocitos diminutos de hierro. No se disuelven, pero un imán los atrae.", "sub_hierro", False, True, 3),
    ("corcho", "mezclas", "Corcho", "Material vegetal", "SOLIDO", "#D2A679",
     "Sólido muy liviano que flota fácilmente en el agua.", "sub_corcho", False, False, 1),
    ("piedritas", "mezclas", "Piedritas", "Minerales variados", "SOLIDO", "#8E8E8E",
     "Sólidos duros y pesados que se hunden en el agua.", "sub_piedritas", False, False, 3),
    ("colorante", "mezclas", "Colorante alimentario", "Pigmento soluble", "LIQUIDO", "#E91E63",
     "Líquido de color intenso que se mezcla por completo con el agua.", "sub_colorante", True, False, 2),
    ("harina", "mezclas", "Harina", "Almidón y proteínas", "SOLIDO", "#FFFDE7",
     "Polvo fino que forma una mezcla turbia y espesa con el agua, sin disolverse del todo.", "sub_harina", False, False, 2),
    ("jabon_liquido", "mezclas", "Jabón líquido", "Tensioactivos", "LIQUIDO", "#B2EBF2",
     "Líquido resbaladizo que se mezcla por completo con el agua y hace espuma.", "sub_jabon", True, False, 2),
    ("vinagre", "reacciones", "Vinagre", "Ácido acético diluido", "LIQUIDO", "#FFF59D",
     "Líquido de olor fuerte que reacciona con el bicarbonato produciendo burbujas.", "sub_vinagre", True, False, 2),
    ("bicarbonato", "reacciones", "Bicarbonato de sodio", "NaHCO3", "SOLIDO", "#FAFAFA",
     "Polvo blanco que, al mezclarse con vinagre, libera burbujas de gas de forma segura.", "sub_bicarbonato", True, False, 3),
]

# combos: (substanceA, substanceB, isHomogeneous, technique, resultDescription)
COMBOS = [
    ("agua", "arena", False, "filtracion", "La arena no se disuelve: queda suspendida y se puede filtrar."),
    ("agua", "piedritas", False, "filtracion", "Las piedritas se hunden enteras; un colador las separa fácilmente."),
    ("agua", "aceite", False, "decantacion", "El aceite flota formando una capa separada sobre el agua."),
    ("agua", "corcho", False, "decantacion", "El corcho flota; se puede retirar con cuidado desde arriba."),
    ("agua", "harina", False, "filtracion", "La harina forma una mezcla turbia que un filtro fino puede retener."),
    ("agua", "sal", True, "evaporacion", "La sal se disuelve por completo; al evaporar el agua, la sal reaparece."),
    ("agua", "azucar", True, "evaporacion", "El azúcar se disuelve por completo, formando una mezcla homogénea."),
    ("agua", "colorante", True, "evaporacion", "El colorante se reparte de manera uniforme en toda el agua."),
    ("agua", "jabon_liquido", True, "evaporacion", "El jabón se integra por completo formando una mezcla homogénea con espuma."),
    ("aceite", "arena", False, "filtracion", "La arena se hunde en el aceite y puede retenerse con un filtro."),
]

# ---------------------------------------------------------------------------
# 3) ÁTOMOS (Atom) — set educativo simplificado, sin isótopos
# ---------------------------------------------------------------------------
# (symbol, name, protons, shells_csv, category, color, fun_fact, valence)
ATOMS = [
    ("H", "Hidrógeno", 1, "1", "NO_METAL", "#90CAF9", "Es el átomo más ligero y el más abundante del universo.", 1),
    ("He", "Helio", 2, "2", "GAS_NOBLE", "#B39DDB", "Es tan liviano que hace flotar los globos de fiesta.", 0),
    ("C", "Carbono", 6, "2,4", "NO_METAL", "#616161", "Forma parte de todos los seres vivos, incluido tú.", 4),
    ("N", "Nitrógeno", 7, "2,5", "NO_METAL", "#64B5F6", "Compone casi el 78% del aire que respiramos.", 3),
    ("O", "Oxígeno", 8, "2,6", "NO_METAL", "#EF5350", "Lo necesitamos para respirar y para que el fuego arda.", 2),
    ("F", "Flúor", 9, "2,7", "HALOGENO", "#AED581", "Ayuda a proteger los dientes en las pastas dentales.", 1),
    ("Ne", "Neón", 10, "2,8", "GAS_NOBLE", "#F06292", "Brilla de color naranja-rojizo en los carteles luminosos.", 0),
    ("Na", "Sodio", 11, "2,8,1", "METAL", "#FFD54F", "Junto al cloro forma la sal de mesa que usamos al cocinar.", 1),
    ("Mg", "Magnesio", 12, "2,8,2", "METAL", "#CE93D8", "Le da a las plantas su color verde dentro de la clorofila.", 2),
    ("Al", "Aluminio", 13, "2,8,3", "METAL", "#B0BEC5", "Es un metal liviano usado en latas y aviones.", 3),
    ("Si", "Silicio", 14, "2,8,4", "METALOIDE", "#A1887F", "Es el ingrediente principal de la arena y del vidrio.", 4),
    ("Cl", "Cloro", 17, "2,8,7", "HALOGENO", "#AED581", "Se combina con el sodio para formar la sal común.", 1),
    ("K", "Potasio", 19, "2,8,8,1", "METAL", "#FFB74D", "Es esencial para que tus músculos funcionen bien.", 1),
    ("Ca", "Calcio", 20, "2,8,8,2", "METAL", "#FFF176", "Forma parte de tus huesos y tus dientes.", 2),
    ("Fe", "Hierro", 26, "2,8,14,2", "METAL", "#8D6E63", "Un imán puede atraerlo; también está en tu sangre.", 2),
    ("Ar", "Argón", 18, "2,8,8", "GAS_NOBLE", "#4DD0E1", "Es un gas noble que casi nunca reacciona con nada.", 0),
]

# ---------------------------------------------------------------------------
# 4) MOLÉCULAS (MoleculeChallenge)
# ---------------------------------------------------------------------------
MOLECULES = [
    ("mol_h2o", "H2O", "Agua", "La molécula más importante para la vida.", "H:2,O:1", 1, 25, 3,
     "Una sola molécula de agua tiene 2 átomos de hidrógeno y 1 de oxígeno.", "mole_h2o"),
    ("mol_o2", "O2", "Oxígeno", "El gas que respiramos, formado por dos átomos de oxígeno unidos.", "O:2", 1, 20, 3,
     "El aire que respiras contiene oxígeno en esta forma exacta.", "mole_o2"),
    ("mol_n2", "N2", "Nitrógeno", "El gas más abundante del aire.", "N:2", 1, 20, 3,
     "Aunque respiramos nitrógeno todo el tiempo, nuestro cuerpo no lo usa directamente.", "mole_n2"),
    ("mol_co2", "CO2", "Dióxido de carbono", "El gas que exhalamos y que las plantas usan para crecer.", "C:1,O:2", 2, 30, 4,
     "Las plantas transforman el CO2 en oxígeno gracias a la luz del sol.", "mole_co2"),
    ("mol_h2", "H2", "Hidrógeno", "El gas más simple, formado por dos átomos de hidrógeno.", "H:2", 1, 20, 3,
     "Es el elemento más abundante de todo el universo.", "mole_h2"),
    ("mol_ch4", "CH4", "Metano", "Un gas formado por carbono rodeado de hidrógeno.", "C:1,H:4", 2, 35, 4,
     "Es el gas principal del biogás y del gas natural.", "mole_ch4"),
    ("mol_nh3", "NH3", "Amoníaco", "Una molécula con nitrógeno rodeado de hidrógeno.", "N:1,H:3", 2, 35, 4,
     "Se usa en muchos productos de limpieza del hogar.", "mole_nh3"),
    ("mol_nacl", "NaCl", "Cloruro de sodio (sal)", "La sal de mesa que usamos para cocinar.", "Na:1,Cl:1", 1, 25, 3,
     "Se forma con un átomo de sodio y uno de cloro perfectamente unidos.", "mole_nacl"),
    ("mol_caco3", "CaCO3", "Carbonato de calcio", "El compuesto principal de la tiza y las conchas marinas.", "Ca:1,C:1,O:3", 3, 45, 5,
     "Las conchas de mar y la tiza escolar están hechas principalmente de esto.", "mole_caco3"),
    ("mol_sio2", "SiO2", "Dióxido de silicio", "El compuesto principal de la arena y el vidrio.", "Si:1,O:2", 3, 45, 5,
     "Cuando se funde a temperaturas altísimas, se convierte en vidrio.", "mole_sio2"),
    ("mol_o3", "O3", "Ozono", "Una forma especial de oxígeno con tres átomos.", "O:3", 2, 30, 4,
     "En las capas altas de la atmósfera nos protege de los rayos del sol.", "mole_o3"),
    ("mol_c", "C", "Carbono puro", "Un solo átomo de carbono, la base de la química de la vida.", "C:1", 1, 15, 1,
     "El diamante y el grafito de tu lápiz están hechos solo de carbono.", "mole_c"),
]

def main():
    os.makedirs(OUT_DIR, exist_ok=True)
    print("Generador listo. Estructuras cargadas:")
    print(f"  Temas: {len(TOPICS)}  Sustancias: {len(SUBSTANCES)}  Combos: {len(COMBOS)}")
    print(f"  Atomos: {len(ATOMS)}  Moleculas: {len(MOLECULES)}")

if __name__ == "__main__":
    main()

# ---------------------------------------------------------------------------
# 5) EXPERIMENTOS (Experiment + ExperimentStep) — 55 prácticas totales
# ---------------------------------------------------------------------------
# interaction shorthand -> InteractionType Kotlin enum name
I = {
    "drag": "ARRASTRAR_SOLTAR", "order": "ORDENAR", "build": "CONSTRUIR", "connect": "CONECTAR",
    "classify": "CLASIFICAR", "config": "CONFIGURAR", "predict": "PREDECIR", "observe": "OBSERVAR",
    "imgselect": "SELECCION_IMAGEN", "mcq": "OPCION_MULTIPLE",
}

def step(instruction, kind, options, correct, exp_ok, exp_bad):
    return dict(instruction=instruction, kind=I[kind], options=options, correct=correct,
                exp_ok=exp_ok, exp_bad=exp_bad)

EXPERIMENTS = []

def add_exp(code, topic, etype, title, hook, desc, diff, primary, xp, order, level, icon, steps):
    EXPERIMENTS.append(dict(
        id=f"exp_{code.lower().replace('-','_')}", code=code, topic=topic, etype=etype, title=title,
        hook=hook, desc=desc, diff=diff, primary=I[primary], xp=xp, order=order, level=level,
        icon=icon, steps=steps
    ))

# --- A) ESTADOS (10) ---------------------------------------------------------
add_exp("EST-001", "estados", "ESTADOS", "Sólido, líquido o gas",
    "Quimi encontró tres frascos sin etiqueta: ayúdale a identificar el estado de cada uno.",
    "Observa tres muestras de agua en distintos estados y clasifícalas correctamente.", 1, "imgselect", 15, 1, 1,
    "exp_clasificar_estados",
    [step("Arrastra cada imagen al estado que le corresponde: hielo, agua líquida y vapor.",
          "imgselect", "hielo,agua_liquida,vapor", "solido,liquido,gaseoso",
          "¡Exacto! El hielo es sólido, el agua líquida fluye y el vapor es un gas.",
          "Repasa: el hielo mantiene su forma, el agua líquida se adapta al recipiente y el vapor se expande.")])

add_exp("EST-002", "estados", "ESTADOS", "El viaje del hielo",
    "El cubito de hielo de Quimi quiere convertirse en vapor. ¿En qué orden ocurre?",
    "Ordena las etapas por las que pasa el agua al calentarse desde hielo hasta vapor.", 1, "order", 15, 2, 1,
    "exp_viaje_hielo",
    [step("Ordena las etapas de calentamiento del agua.",
          "order", "vapor,agua_liquida,hielo", "hielo,agua_liquida,vapor",
          "¡Correcto! El hielo se derrite (fusión) y luego se evapora (evaporación).",
          "El orden correcto es: primero sólido, luego líquido y finalmente gas.")])

add_exp("EST-003", "estados", "ESTADOS", "Mantequilla al sol",
    "Quimi dejó la mantequilla fuera de la nevera. ¿Qué crees que pasará?",
    "Predice qué le ocurre a la mantequilla sólida cuando aumenta la temperatura.", 1, "predict", 15, 3, 1,
    "exp_mantequilla_sol",
    [step("¿Qué le pasará a la mantequilla si sube la temperatura?",
          "predict", "se_derrite,se_endurece,desaparece", "se_derrite",
          "¡Bien predicho! El calor hace que la mantequilla pase de sólido a líquido.",
          "El calor no hace desaparecer la materia: la mantequilla se derrite, no se esfuma.")])

add_exp("EST-004", "estados", "ESTADOS", "Chocolate derretido",
    "Observa la animación: el chocolate sólido se calienta poco a poco en el laboratorio virtual.",
    "Observa el cambio de estado del chocolate y describe lo que ves.", 1, "observe", 15, 4, 1,
    "exp_chocolate_derretido",
    [step("Después de observar, indica qué cambio de estado ocurrió.",
          "observe", "fusion,evaporacion,condensacion", "fusion",
          "¡Correcto! El chocolate pasó de sólido a líquido: eso es fusión.",
          "El chocolate se derritió: ese cambio de sólido a líquido se llama fusión.")])

add_exp("EST-005", "estados", "ESTADOS", "El armario de los estados",
    "Quimi necesita ordenar su armario de sustancias según su estado de la materia.",
    "Clasifica seis sustancias cotidianas en sólido, líquido o gaseoso.", 2, "classify", 20, 5, 1,
    "exp_armario_estados",
    [step("Arrastra cada sustancia a la caja de su estado correcto: hielo, agua, vapor, mantequilla, chocolate, cera.",
          "classify", "hielo,agua,vapor_agua,mantequilla,chocolate,cera_vela",
          "hielo,mantequilla,chocolate,cera_vela|agua|vapor_agua",
          "¡Gran trabajo clasificando! La mayoría eran sólidos, uno líquido y uno gaseoso.",
          "Recuerda: a temperatura ambiente el hielo, la mantequilla, el chocolate y la cera son sólidos.")])

add_exp("EST-006", "estados", "ESTADOS", "La vela se enfría",
    "La cera líquida de una vela virtual empieza a enfriarse. ¿Qué pasos sigue hasta endurecerse?",
    "Ordena el proceso de solidificación de la cera al enfriarse.", 2, "order", 20, 6, 1,
    "exp_vela_enfria",
    [step("Ordena las etapas de la cera al enfriarse.",
          "order", "cera_solida,cera_tibia,cera_liquida", "cera_liquida,cera_tibia,cera_solida",
          "¡Perfecto! La cera pasa de líquida a tibia y finalmente se solidifica del todo.",
          "El orden correcto es: líquida primero, después tibia, y sólida al final.")])

add_exp("EST-007", "estados", "ESTADOS", "El termómetro del laboratorio",
    "Mueve el control de temperatura y observa cuándo el agua se congela.",
    "Configura la temperatura conceptual hasta encontrar el punto de congelación del agua.", 2, "config", 20, 7, 2,
    "exp_termometro",
    [step("Mueve el control hasta la zona donde el agua se congela.",
          "config", "caliente,templado,frio_congelante", "frio_congelante",
          "¡Así es! A temperaturas muy bajas, el agua líquida se convierte en hielo.",
          "Debes bajar mucho la temperatura: el agua se congela en la zona más fría.")])

add_exp("EST-008", "estados", "ESTADOS", "El vapor que vuelve",
    "El vapor de la olla virtual choca con una tapa fría. ¿Qué crees que pasará?",
    "Predice qué sucede cuando el vapor de agua se enfría de repente.", 2, "predict", 20, 8, 2,
    "exp_vapor_vuelve",
    [step("¿Qué pasa cuando el vapor toca una superficie fría?",
          "predict", "se_condensa,se_congela,desaparece", "se_condensa",
          "¡Correcto! El vapor se enfría y vuelve a ser gotitas de agua líquida: condensación.",
          "El vapor no desaparece: al enfriarse forma gotitas de agua líquida, eso es condensación.")])

add_exp("EST-009", "estados", "ESTADOS", "Propiedades conectadas",
    "Cada estado de la materia tiene una propiedad especial. ¿Puedes unirlas?",
    "Conecta cada estado de la materia con la propiedad que lo describe mejor.", 2, "connect", 20, 9, 2,
    "exp_propiedades_conectadas",
    [step("Conecta: sólido, líquido y gaseoso con su propiedad correspondiente.",
          "connect", "solido-forma_fija,liquido-toma_forma_recipiente,gaseoso-se_expande_todo",
          "solido-forma_fija,liquido-toma_forma_recipiente,gaseoso-se_expande_todo",
          "¡Excelente conexión! Cada estado se comporta de forma distinta según sus partículas.",
          "Piensa cómo se comportan las partículas: muy juntas y ordenadas, algo libres, o muy separadas.")])

add_exp("EST-010", "estados", "ESTADOS", "Repaso: el gran clasificador",
    "Última prueba de la Academia de Estados: clasifica todo lo que has aprendido.",
    "Repasa clasificando seis sustancias nuevas según su estado de la materia.", 2, "classify", 25, 10, 2,
    "exp_repaso_estados",
    [step("Clasifica: jugo helado, aire, roca, aceite, nube de vapor y una moneda.",
          "classify", "jugo_helado,aire,roca,aceite,nube_vapor,moneda",
          "jugo_helado,roca,moneda|aceite|aire,nube_vapor",
          "¡Repaso superado! Distinguiste correctamente sólidos, líquidos y gases variados.",
          "Recuerda: los sólidos mantienen forma propia, los líquidos fluyen y los gases se expanden.")])

# --- B) MEZCLAS (10, una por combinación) -----------------------------------
mezcla_titles = {
    ("agua","arena"): ("Arena en el vaso", "Quimi vertió arena en agua. ¿Se mezclará de verdad?"),
    ("agua","piedritas"): ("Piedritas al fondo", "Unas piedritas cayeron en el vaso de agua de Quimi."),
    ("agua","aceite"): ("Agua y aceite", "El aceite parece flotar sobre el agua. ¿Por qué pasará eso?"),
    ("agua","corcho"): ("El corcho flotante", "Un trocito de corcho cayó al vaso de agua."),
    ("agua","harina"): ("Nube de harina", "La harina forma una nube blanca dentro del agua."),
    ("agua","sal"): ("La sal desaparece", "Quimi disolvió sal en agua y ya no puede verla."),
    ("agua","azucar"): ("Agua dulce", "El azúcar se disolvió por completo en el vaso de Quimi."),
    ("agua","colorante"): ("Agua de colores", "Una gota de colorante se esparce en el agua."),
    ("agua","jabon_liquido"): ("Burbujas de jabón", "El jabón líquido se mezcló con agua y generó espuma."),
    ("aceite","arena"): ("Arena en el aceite", "Esta vez la arena cae dentro de un vaso de aceite."),
}
order_i = 0
for (a, b, homog, tech, result) in COMBOS:
    order_i += 1
    title, hook = mezcla_titles[(a, b)]
    diff = 1 if order_i <= 4 else 2
    add_exp(f"MEZ-{order_i:03d}", "mezclas", "MEZCLA", title, hook,
        f"Combina {a.replace('_',' ')} y {b.replace('_',' ')} para descubrir si forman una mezcla homogénea o heterogénea.",
        diff, "drag", 15 + diff * 5, order_i, 1, f"exp_mezcla_{a}_{b}",
        [step(f"Arrastra {a} y {b} al vaso de mezcla y observa el resultado.",
              "drag", f"{a},{b}", "homogenea" if homog else "heterogenea",
              ("¡Correcto! Es una mezcla homogénea: no se distinguen las partes por separado." if homog else
               "¡Correcto! Es una mezcla heterogénea: se pueden distinguir sus partes a simple vista."),
              ("Fíjate bien: si no puedes distinguir las partes, la mezcla es homogénea." if homog else
               "Fíjate bien: si puedes distinguir las partes por separado, la mezcla es heterogénea."))])

# --- C) SEPARACION (8) -------------------------------------------------------
separacion_pairs = [("agua","arena","filtracion"), ("agua","piedritas","filtracion"),
                     ("agua","aceite","decantacion"), ("agua","corcho","decantacion"),
                     ("agua","harina","filtracion"), ("aceite","arena","filtracion")]
sep_titles = {
    ("agua","arena"): "Rescata el agua limpia",
    ("agua","piedritas"): "Cuela las piedritas",
    ("agua","aceite"): "Separa las capas",
    ("agua","corcho"): "Recoge el corcho",
    ("agua","harina"): "Filtra la nube blanca",
    ("aceite","arena"): "Limpia el aceite",
}
order_i = 0
for (a, b, tech) in separacion_pairs:
    order_i += 1
    tech_name = {"filtracion": "filtración", "decantacion": "decantación", "evaporacion": "evaporación"}[tech]
    add_exp(f"SEP-{order_i:03d}", "separacion", "SEPARACION", sep_titles[(a,b)],
        f"Esta mezcla de {a} y {b} debe separarse antes de guardarse en la estantería.",
        f"Elige la técnica correcta para separar {a} y {b}: filtración, decantación o evaporación.",
        2, "imgselect", 25, order_i, 2, f"exp_separar_{a}_{b}",
        [step("Selecciona la técnica correcta de separación para esta mezcla.",
              "imgselect", "filtracion,decantacion,evaporacion", tech,
              f"¡Correcto! La {tech_name} es la técnica ideal para este caso.",
              f"Piensa en las propiedades de la mezcla: aquí la técnica correcta era la {tech_name}.")])

order_i += 1
add_exp(f"SEP-{order_i:03d}", "separacion", "SEPARACION", "Pasos de la evaporación",
    "Quimi quiere recuperar la sal disuelta en agua. ¿En qué orden se hace?",
    "Ordena correctamente los pasos del proceso de evaporación conceptual.", 2, "order", 25, order_i, 2,
    "exp_pasos_evaporacion",
    [step("Ordena los pasos: calentar suavemente, esperar a que el agua se evapore, recoger el sólido restante.",
          "order", "recoger_solido,calentar_suavemente,esperar_evaporacion",
          "calentar_suavemente,esperar_evaporacion,recoger_solido",
          "¡Excelente secuencia! Así se recupera el sólido disuelto en una mezcla homogénea.",
          "El orden correcto es: calentar, esperar a que el agua se vaya, y recoger lo que queda.")])

order_i += 1
add_exp(f"SEP-{order_i:03d}", "separacion", "SEPARACION", "Pasos de la filtración",
    "Una mezcla turbia espera en la mesa de Quimi. ¿Cómo se filtra correctamente?",
    "Ordena correctamente los pasos del proceso de filtración conceptual.", 1, "order", 20, order_i, 2,
    "exp_pasos_filtracion",
    [step("Ordena los pasos: verter la mezcla sobre el filtro, colocar el filtro en el embudo, recoger el líquido limpio.",
          "order", "recoger_liquido,colocar_filtro,verter_mezcla",
          "colocar_filtro,verter_mezcla,recoger_liquido",
          "¡Muy bien! Primero se prepara el filtro, luego se vierte, y al final se recoge el resultado.",
          "El orden correcto es: colocar el filtro, verter la mezcla, y recoger el líquido limpio.")])

# --- D) ATOMOS (8) ------------------------------------------------------------
add_exp("ATO-001", "atomos", "ATOMO", "El mapa de los átomos",
    "Un mapa gigante de átomos apareció en la pared del laboratorio de Quimi.",
    "Clasifica seis átomos según su categoría: metal, no metal o gas noble.", 1, "classify", 20, 1, 2,
    "exp_mapa_atomos",
    [step("Clasifica: H, O, Na, He, Fe, Ne según su categoría.",
          "classify", "H,O,Na,He,Fe,Ne", "H,O|Na,Fe|He,Ne",
          "¡Bien hecho! Distinguiste no metales, metales y gases nobles correctamente.",
          "Recuerda: H y O son no metales, Na y Fe son metales, He y Ne son gases nobles.")])

add_exp("ATO-002", "atomos", "ATOMO", "Símbolo y nombre",
    "Cada átomo tiene un símbolo especial. ¿Puedes emparejarlos con su nombre?",
    "Conecta el símbolo químico de cada átomo con su nombre correcto.", 1, "connect", 20, 2, 2,
    "exp_simbolo_nombre",
    [step("Conecta: H-Hidrógeno, O-Oxígeno, C-Carbono, N-Nitrógeno.",
          "connect", "H-Hidrogeno,O-Oxigeno,C-Carbono,N-Nitrogeno",
          "H-Hidrogeno,O-Oxigeno,C-Carbono,N-Nitrogeno",
          "¡Perfecto emparejamiento! Ya reconoces los símbolos más comunes.",
          "Revisa con calma: cada símbolo químico corresponde a un único nombre.")])

add_exp("ATO-003", "atomos", "ATOMO", "Orden de protones",
    "Quimi mezcló las tarjetas de átomos. ¿Puedes ordenarlas por número de protones?",
    "Ordena cuatro átomos de menor a mayor número de protones.", 2, "order", 25, 3, 2,
    "exp_orden_protones",
    [step("Ordena de menor a mayor número de protones: O, H, C, N.",
          "order", "O,C,N,H", "H,C,N,O",
          "¡Correcto! H tiene 1 protón, C tiene 6, N tiene 7 y O tiene 8.",
          "Recuerda el número de protones: H=1, C=6, N=7, O=8.")])

add_exp("ATO-004", "atomos", "ATOMO", "Capas de electrones",
    "Observa cómo los electrones del sodio se acomodan en capas alrededor del núcleo.",
    "Observa la animación de capas electrónicas y responde cuántas capas tiene el átomo.", 2, "observe", 25, 4, 2,
    "exp_capas_electrones",
    [step("¿Cuántas capas de electrones observaste en el átomo de sodio (Na)?",
          "observe", "1,2,3", "3",
          "¡Correcto! El sodio tiene tres capas: 2, 8 y 1 electrón respectivamente.",
          "Cuenta de nuevo: el sodio distribuye sus electrones en tres capas.")])

add_exp("ATO-005", "atomos", "ATOMO", "Encuentra el átomo",
    "Quimi describe un átomo con pistas. ¿Puedes encontrarlo en la mesa periódica virtual?",
    "Selecciona el átomo correcto a partir de una pista visual sobre sus propiedades.", 2, "imgselect", 25, 5, 2,
    "exp_encuentra_atomo",
    [step("Pista: soy un gas noble que brilla de color naranja en los carteles luminosos. ¿Quién soy?",
          "imgselect", "He,Ne,Ar", "Ne",
          "¡Correcto! El neón es famoso por su brillo naranja-rojizo en los letreros.",
          "Piensa en los carteles luminosos de neón: ese gas noble es el que buscas.")])

add_exp("ATO-006", "atomos", "ATOMO", "La última capa",
    "Quimi quiere saber cuántos electrones tiene la capa más externa de un átomo.",
    "Predice cuántos electrones hay en la última capa del átomo de oxígeno.", 3, "predict", 30, 6, 3,
    "exp_ultima_capa",
    [step("El oxígeno tiene capas 2,6. ¿Cuántos electrones hay en su última capa?",
          "predict", "2,6,8", "6",
          "¡Exacto! La última capa del oxígeno tiene 6 electrones.",
          "Observa la secuencia 2,6: el último número es el de la capa más externa.")])

add_exp("ATO-007", "atomos", "ATOMO", "Metal o no metal",
    "Quimi separa sus tarjetas de átomos en dos grupos: metales y no metales.",
    "Clasifica cuatro átomos comparando sus propiedades como metal o no metal.", 2, "classify", 25, 7, 2,
    "exp_metal_no_metal",
    [step("Clasifica: Fe, Ca, O, N en metales o no metales.",
          "classify", "Fe,Ca,O,N", "Fe,Ca|O,N",
          "¡Bien clasificado! Fe y Ca son metales; O y N son no metales.",
          "Recuerda: los metales suelen ser brillantes y conducir electricidad; O y N no lo son.")])

add_exp("ATO-008", "atomos", "ATOMO", "Repaso: gran examen atómico",
    "Última prueba de la Academia de Átomos antes de pasar al constructor molecular.",
    "Responde preguntas de repaso sobre los átomos aprendidos.", 2, "mcq", 25, 8, 3,
    "exp_repaso_atomos",
    [step("¿Cuál de estos NO es un gas noble?",
          "mcq", "He,Ne,Na", "Na",
          "¡Correcto! El sodio (Na) es un metal, no un gas noble.",
          "Repasa: He y Ne son gases nobles; Na es un metal.")])

# --- E) MOLECULAS - retos introductorios (8) ---------------------------------
add_exp("MOL-001", "moleculas", "MOLECULA", "Fórmula y nombre",
    "Antes de construir, Quimi quiere que reconozcas las fórmulas más comunes.",
    "Conecta la fórmula química de cada molécula con su nombre común.", 1, "connect", 20, 1, 3,
    "exp_formula_nombre",
    [step("Conecta: H2O-Agua, CO2-Dióxido de carbono, O2-Oxígeno, NaCl-Sal.",
          "connect", "H2O-Agua,CO2-Dioxido_de_carbono,O2-Oxigeno,NaCl-Sal",
          "H2O-Agua,CO2-Dioxido_de_carbono,O2-Oxigeno,NaCl-Sal",
          "¡Excelente! Ya reconoces las fórmulas de moléculas muy comunes.",
          "Piensa en su uso diario: una es para respirar, otra para cocinar, otra es agua.")])

add_exp("MOL-002", "moleculas", "MOLECULA", "Cuenta los átomos",
    "Quimi te muestra una fórmula. ¿Cuántos átomos totales tiene?",
    "Predice cuántos átomos en total forman la molécula de metano (CH4).", 2, "predict", 25, 2, 3,
    "exp_cuenta_atomos",
    [step("La fórmula CH4 tiene 1 átomo de carbono y 4 de hidrógeno. ¿Cuántos átomos en total?",
          "predict", "4,5,6", "5",
          "¡Correcto! 1 carbono + 4 hidrógenos = 5 átomos en total.",
          "Suma con cuidado: 1 átomo de carbono más 4 de hidrógeno.")])

add_exp("MOL-003", "moleculas", "MOLECULA", "¿Tiene carbono?",
    "Quimi separa las moléculas orgánicas de las que no lo son.",
    "Clasifica moléculas según si contienen o no átomos de carbono.", 2, "classify", 25, 3, 3,
    "exp_tiene_carbono",
    [step("Clasifica: CO2, H2O, CH4, O2 según si contienen carbono.",
          "classify", "CO2,H2O,CH4,O2", "CO2,CH4|H2O,O2",
          "¡Correcto! CO2 y CH4 tienen carbono; H2O y O2 no lo tienen.",
          "Fíjate en la letra C dentro de la fórmula: indica presencia de carbono.")])

add_exp("MOL-004", "moleculas", "MOLECULA", "De menor a mayor",
    "Quimi mezcló tarjetas de moléculas. ¿Puedes ordenarlas por cantidad de átomos?",
    "Ordena tres moléculas de menor a mayor cantidad total de átomos.", 2, "order", 25, 4, 3,
    "exp_menor_mayor",
    [step("Ordena de menor a mayor cantidad de átomos: H2O (3), O2 (2), CH4 (5).",
          "order", "H2O,CH4,O2", "O2,H2O,CH4",
          "¡Correcto! O2 tiene 2 átomos, H2O tiene 3 y CH4 tiene 5.",
          "Cuenta átomo por átomo en cada fórmula antes de ordenar.")])

add_exp("MOL-005", "moleculas", "MOLECULA", "Encuentra la molécula",
    "Quimi describe una molécula con una pista visual. ¿Puedes reconocerla?",
    "Selecciona la molécula correcta a partir de una pista sobre su función.", 1, "imgselect", 20, 5, 3,
    "exp_encuentra_molecula",
    [step("Pista: las plantas la transforman en oxígeno gracias a la luz del sol. ¿Cuál es?",
          "imgselect", "O2,CO2,N2", "CO2",
          "¡Correcto! El dióxido de carbono es transformado en oxígeno por las plantas.",
          "Piensa en la fotosíntesis: las plantas absorben este gas para producir oxígeno.")])

add_exp("MOL-006", "moleculas", "MOLECULA", "Observa la estructura",
    "Observa cómo se representan los átomos unidos dentro de una molécula de agua.",
    "Observa la animación de la estructura del agua y responde una pregunta.", 1, "observe", 20, 6, 3,
    "exp_observa_estructura",
    [step("¿Cuántos átomos de hidrógeno viste unidos al oxígeno?",
          "observe", "1,2,3", "2",
          "¡Correcto! El agua tiene 2 átomos de hidrógeno unidos a 1 de oxígeno.",
          "Vuelve a observar: cuenta los átomos pequeños unidos al átomo central.")])

add_exp("MOL-007", "moleculas", "MOLECULA", "Piezas necesarias",
    "Antes de construir en la mesa grande, arma mentalmente la lista de piezas.",
    "Identifica qué átomos necesitarás para construir una molécula de amoníaco (NH3).", 2, "build", 30, 7, 3,
    "exp_piezas_necesarias",
    [step("Selecciona los TIPOS de átomos necesarios para construir NH3 (nitrógeno e hidrógeno).",
          "build", "N,H,O,C", "N,H",
          "¡Perfecto! NH3 se construye combinando átomos de nitrógeno e hidrógeno.",
          "Revisa la fórmula NH3: la N indica nitrógeno y la H indica hidrógeno.")])

add_exp("MOL-008", "moleculas", "MOLECULA", "Repaso molecular",
    "Última prueba antes de pasar a la mesa de construcción molecular completa.",
    "Responde una pregunta de repaso general sobre moléculas.", 2, "mcq", 25, 8, 3,
    "exp_repaso_moleculas",
    [step("¿Cuál de estas fórmulas corresponde al agua?",
          "mcq", "CO2,H2O,O2", "H2O",
          "¡Correcto! H2O es la fórmula del agua.",
          "Repasa: la fórmula del agua combina hidrógeno y oxígeno como H2O.")])

# --- F) REACCIONES (11) -------------------------------------------------------
add_exp("REA-001", "reacciones", "REACCION", "Vinagre y bicarbonato",
    "Quimi va a combinar vinagre y bicarbonato en la cámara de reacciones. ¿Qué pasará?",
    "Predice qué ocurre al combinar vinagre y bicarbonato de forma segura y virtual.", 1, "predict", 20, 1, 3,
    "exp_vinagre_bicarbonato",
    [step("¿Qué crees que pasará al combinar vinagre y bicarbonato?",
          "predict", "burbujas,nada,se_congela", "burbujas",
          "¡Correcto! Se forman burbujas: es una reacción que libera gas de forma segura.",
          "Piensa en experimentos caseros clásicos: esta combinación siempre burbujea.")])

add_exp("REA-002", "reacciones", "REACCION", "Observa la efervescencia",
    "Observa con atención la animación de la reacción entre vinagre y bicarbonato.",
    "Observa la efervescencia y responde qué señal indica que ocurrió una reacción.", 1, "observe", 20, 2, 3,
    "exp_observa_efervescencia",
    [step("¿Qué señal viste que indica que ocurrió una reacción química?",
          "observe", "burbujas,cambio_color,ningun_cambio", "burbujas",
          "¡Correcto! Las burbujas son una señal clásica de reacción química.",
          "Vuelve a observar con calma: algo se liberó en forma de pequeñas burbujas.")])

add_exp("REA-003", "reacciones", "REACCION", "Más o menos bicarbonato",
    "Mueve el control de cantidad de bicarbonato y observa cómo cambia la reacción.",
    "Configura la cantidad de bicarbonato y observa el efecto sobre la efervescencia.", 2, "config", 25, 3, 3,
    "exp_cantidad_bicarbonato",
    [step("Mueve el control hasta encontrar la cantidad que produce más burbujas.",
          "config", "poca,media,mucha", "mucha",
          "¡Correcto! A mayor cantidad de bicarbonato, más burbujas se producen.",
          "Prueba con más cantidad: la reacción es más intensa cuando hay más bicarbonato.")])

add_exp("REA-004", "reacciones", "REACCION", "El hierro se oxida",
    "Con el paso del tiempo, un clavo de hierro virtual cambia lentamente de color.",
    "Ordena las etapas de la oxidación lenta del hierro al estar en contacto con el aire.", 2, "order", 25, 4, 3,
    "exp_hierro_oxida",
    [step("Ordena las etapas: hierro cubierto de óxido, hierro brillante, hierro con manchas rojizas.",
          "order", "hierro_con_manchas,hierro_cubierto_oxido,hierro_brillante",
          "hierro_brillante,hierro_con_manchas,hierro_cubierto_oxido",
          "¡Correcto! Con el tiempo el hierro brillante desarrolla manchas y luego se oxida por completo.",
          "El orden correcto va del hierro brillante y nuevo hasta quedar completamente oxidado.")])

add_exp("REA-005", "reacciones", "REACCION", "Lo que necesita una planta",
    "Quimi riega una planta virtual y quiere saber qué necesita para producir su alimento.",
    "Predice qué elementos necesita una planta para realizar la fotosíntesis.", 2, "predict", 25, 5, 3,
    "exp_planta_fotosintesis",
    [step("¿Qué necesita la planta, además de agua, para hacer fotosíntesis?",
          "predict", "luz_solar,oscuridad,frio_extremo", "luz_solar",
          "¡Correcto! La luz solar es esencial para que la planta produzca su alimento.",
          "Piensa en dónde crecen mejor las plantas: necesitan luz solar para fotosintetizar.")])

add_exp("REA-006", "reacciones", "REACCION", "Rápida o lenta",
    "Algunas reacciones ocurren en segundos, otras tardan años. Clasifícalas.",
    "Clasifica reacciones cotidianas según sean rápidas o lentas.", 2, "classify", 25, 6, 3,
    "exp_rapida_lenta",
    [step("Clasifica: vinagre+bicarbonato, oxidación del hierro, quema de un fósforo, maduración de una fruta.",
          "classify", "vinagre_bicarbonato,oxidacion_hierro,quema_fosforo,maduracion_fruta",
          "vinagre_bicarbonato,quema_fosforo|oxidacion_hierro,maduracion_fruta",
          "¡Correcto! Unas reacciones son casi inmediatas y otras tardan días o años.",
          "Piensa en el tiempo que tardan: algunas son instantáneas, otras muy graduales.")])

add_exp("REA-007", "reacciones", "REACCION", "Reactivos y productos",
    "Toda reacción tiene ingredientes iniciales y resultados finales. Conéctalos.",
    "Conecta cada combinación de reactivos con el producto que genera.", 2, "connect", 25, 7, 3,
    "exp_reactivos_productos",
    [step("Conecta: vinagre+bicarbonato-burbujas_de_gas, hierro+aire_humedo-oxido.",
          "connect", "vinagre_bicarbonato-burbujas_de_gas,hierro_aire_humedo-oxido",
          "vinagre_bicarbonato-burbujas_de_gas,hierro_aire_humedo-oxido",
          "¡Correcto! Cada combinación de reactivos produce un resultado característico.",
          "Piensa en lo que observaste en experimentos anteriores sobre estas combinaciones.")])

add_exp("REA-008", "reacciones", "REACCION", "El repollo mágico",
    "El jugo de repollo morado cambia de color según lo que le agregues: es un indicador natural.",
    "Observa cómo el jugo de repollo morado cambia de color al mezclarse con vinagre.", 2, "observe", 25, 8, 4,
    "exp_repollo_magico",
    [step("¿A qué color cambió el jugo de repollo morado al mezclarlo con vinagre?",
          "observe", "rosado,verde,igual", "rosado",
          "¡Correcto! El jugo de repollo morado se vuelve rosado en contacto con sustancias ácidas.",
          "Vuelve a observar la animación: el color cambia hacia tonos rosados o rojizos.")])

add_exp("REA-009", "reacciones", "REACCION", "Combinar dos gases",
    "Quimi combina conceptualmente hidrógeno y oxígeno en la cámara segura. ¿Qué se forma?",
    "Predice qué molécula se puede formar al combinar hidrógeno y oxígeno.", 3, "predict", 30, 9, 4,
    "exp_combinar_gases",
    [step("Al combinar hidrógeno y oxígeno de forma equilibrada, ¿qué molécula se forma?",
          "predict", "agua,sal,azucar", "agua",
          "¡Correcto! Hidrógeno y oxígeno combinados forman agua (H2O).",
          "Recuerda la fórmula del agua: está hecha justamente de estos dos elementos.")])

add_exp("REA-010", "reacciones", "REACCION", "Señales de reacción",
    "Quimi te muestra distintas imágenes. ¿Cuál demuestra que ocurrió una reacción química?",
    "Selecciona la imagen que mejor representa una señal de reacción química.", 2, "imgselect", 25, 10, 4,
    "exp_senales_reaccion",
    [step("Selecciona la señal que indica que ocurrió una reacción química.",
          "imgselect", "burbujeo,agua_quieta,piedra_inmovil", "burbujeo",
          "¡Correcto! El burbujeo es una señal clara de que se liberó un gas por reacción.",
          "Busca cambios visibles: burbujas, cambio de color o de temperatura indican reacción.")])

add_exp("REA-011", "reacciones", "REACCION", "Repaso: gran examen de reacciones",
    "Última prueba de la cámara de reacciones antes de graduarte del laboratorio.",
    "Responde una pregunta de repaso general sobre reacciones virtuales.", 2, "mcq", 25, 11, 4,
    "exp_repaso_reacciones",
    [step("¿Cuál de estas NO es una señal típica de reacción química?",
          "mcq", "burbujas,cambio_color,silencio_total", "silencio_total",
          "¡Correcto! El silencio total no indica ningún cambio químico observable.",
          "Repasa: burbujas y cambios de color sí son señales de reacción; la ausencia de cambio no lo es.")])

print(f"Total experimentos generados: {len(EXPERIMENTS)}")
assert len(EXPERIMENTS) == 55, f"Se esperaban 55 experimentos, hay {len(EXPERIMENTS)}"

# ---------------------------------------------------------------------------
# 6) ESCENARIOS DE SEGURIDAD (SafetyScenario) — 35 totales, 7 por categoría
# ---------------------------------------------------------------------------
# (id, category, title, situation, correct, distractor1, distractor2, explanation, icon)
SAFETY = [
    # EN_CASA (7)
    ("saf_casa_01","EN_CASA","Frasco sin etiqueta","Encuentras un frasco de limpieza sin etiqueta bajo el fregadero.",
     "No tocarlo y avisar a un adulto","Olerlo para adivinar qué es","Probar un poco con el dedo",
     "Nunca debes tocar ni probar sustancias desconocidas: un adulto sabe identificarlas de forma segura.","saf_casa_01"),
    ("saf_casa_02","EN_CASA","Hermanito curioso","Ves a tu hermanito pequeño jugando cerca de productos de limpieza.",
     "Alejarlo y avisar a un adulto","Dejarlo seguir jugando","Quitarle el envase y guardarlo tú mismo",
     "Los productos de limpieza deben estar fuera del alcance de los más pequeños; un adulto debe encargarse.","saf_casa_02"),
    ("saf_casa_03","EN_CASA","Tapa mal cerrada","Un envase de detergente tiene la tapa mal cerrada y podría derramarse.",
     "Avisar a un adulto para que lo cierre bien","Cerrarlo tú mismo apretando fuerte","Ignorarlo, no es importante",
     "Aunque parezca sencillo, es mejor que un adulto manipule envases de productos químicos del hogar.","saf_casa_03"),
    ("saf_casa_04","EN_CASA","Humo en el enchufe","Ves humo saliendo de un enchufe de la pared.",
     "Alejarte y avisar a un adulto de inmediato","Tocar el enchufe para ver qué pasa","Echarle agua",
     "El humo eléctrico es peligroso: aléjate y pide ayuda a un adulto enseguida; nunca uses agua cerca de electricidad.","saf_casa_04"),
    ("saf_casa_05","EN_CASA","Limpieza en la cocina","Un adulto está usando un producto de limpieza fuerte en la cocina.",
     "Salir o mantenerte a una distancia segura","Acercarte a oler el producto","Tocar el envase mientras el adulto lo usa",
     "Al usar productos fuertes, es mejor ventilar el espacio y mantener distancia, incluso si un adulto los maneja.","saf_casa_05"),
    ("saf_casa_06","EN_CASA","Pastillas parecidas a caramelos","Encuentras unas pastillas sueltas que se parecen a caramelos.",
     "No tocarlas y avisar a un adulto","Comer una para probar","Guardarlas en tu bolsillo",
     "Algunos medicamentos parecen caramelos pero no lo son: nunca los pruebes, avisa siempre a un adulto.","saf_casa_06"),
    ("saf_casa_07","EN_CASA","Olor a gas","Sientes un olor extraño parecido a gas en la cocina.",
     "Avisar a un adulto de inmediato y salir del lugar","Encender un fósforo para ver mejor","Ignorarlo y seguir jugando",
     "Ante un olor a gas, nunca enciendas nada: sal del lugar y avisa a un adulto enseguida.","saf_casa_07"),
    # EN_ESCUELA (7)
    ("saf_esc_01","EN_ESCUELA","Símbolo de peligro","En el laboratorio del cole ves un frasco con un símbolo de peligro.",
     "No tocarlo y decírselo al profesor","Abrirlo para ver qué contiene","Moverlo a otra mesa tú mismo",
     "Los símbolos de peligro indican que solo un adulto responsable debe manipular ese frasco.","saf_esc_01"),
    ("saf_esc_02","EN_ESCUELA","Mezcla por curiosidad","Un compañero quiere mezclar productos de limpieza del laboratorio 'por curiosidad'.",
     "Decirle que no lo haga y avisar al profesor","Ayudarlo a mezclar","Grabarlo en video sin decir nada",
     "Mezclar sustancias sin supervisión puede ser peligroso; siempre debe avisarse a un adulto responsable.","saf_esc_02"),
    ("saf_esc_03","EN_ESCUELA","Derrame en el suelo","Se derrama un líquido desconocido en el suelo del laboratorio.",
     "Avisar al profesor y alejarte del área","Limpiarlo tú mismo con papel","Pisarlo para ver qué es",
     "Ante cualquier derrame desconocido, la persona adulta a cargo debe encargarse de limpiarlo con seguridad.","saf_esc_03"),
    ("saf_esc_04","EN_ESCUELA","Gafas protectoras","La profesora reparte gafas protectoras antes de un experimento.",
     "Ponértelas correctamente antes de empezar","Dejarlas a un lado, no son necesarias","Usarlas solo si te acuerdas",
     "Las gafas protectoras evitan que salpicaduras lleguen a los ojos: siempre deben usarse cuando se indican.","saf_esc_04"),
    ("saf_esc_05","EN_ESCUELA","Oler directamente","Un compañero prueba a oler directamente un frasco de sustancia química.",
     "Decirle que aleje el frasco de la nariz y avise al profesor","Reírte y no decir nada","Oler tú también para comprobar",
     "Nunca se debe oler una sustancia directamente y de cerca; los vapores pueden ser irritantes.","saf_esc_05"),
    ("saf_esc_06","EN_ESCUELA","Alarma de incendios","Suena la alarma de incendios durante una clase de ciencias.",
     "Salir en orden siguiendo las indicaciones del profesor","Quedarte a terminar el experimento","Correr sin avisar a nadie",
     "Ante cualquier alarma, lo correcto es salir en orden y seguir siempre las indicaciones de un adulto.","saf_esc_06"),
    ("saf_esc_07","EN_ESCUELA","Correr en el laboratorio","Ves a un compañero corriendo dentro del laboratorio escolar.",
     "Decirle que camine con calma por seguridad","Correr también","No decir nada",
     "Correr cerca de mesas con materiales de laboratorio aumenta el riesgo de accidentes y derrames.","saf_esc_07"),
    # EN_LABORATORIO_VIRTUAL (7) - hábitos generales de seguridad transferibles a la vida real
    ("saf_lab_01","EN_LABORATORIO_VIRTUAL","Antes de empezar","Antes de cualquier experimento real de ciencias, ¿qué debes hacer primero?",
     "Leer las instrucciones y pedir permiso a un adulto","Empezar directamente sin leer nada","Improvisar tus propios pasos",
     "Leer las instrucciones y contar con la supervisión de un adulto es el primer paso de cualquier experimento real.","saf_lab_01"),
    ("saf_lab_02","EN_LABORATORIO_VIRTUAL","Guardar materiales","Terminaste tu experimento real en clase: ¿qué haces con los materiales usados?",
     "Guardarlos como indique el profesor","Dejarlos donde sea","Llevártelos a casa sin permiso",
     "Guardar correctamente los materiales, siguiendo las indicaciones del adulto responsable, es parte de la seguridad.","saf_lab_02"),
    ("saf_lab_03","EN_LABORATORIO_VIRTUAL","Fuego o calor","Un experimento del libro requiere usar fuego o una fuente de calor real.",
     "Hacerlo solo con un adulto presente","Intentarlo a solas con cuidado","Usar velas de tu casa sin avisar",
     "Cualquier actividad con fuego o calor real necesita siempre la supervisión directa de un adulto.","saf_lab_03"),
    ("saf_lab_04","EN_LABORATORIO_VIRTUAL","Algo desconocido","Un experimento pide usar un material que no reconoces.",
     "Preguntar a un adulto qué es antes de usarlo","Usarlo igual, seguro no pasa nada","Sustituirlo por otra cosa sin avisar",
     "Si no reconoces un material, siempre pregunta antes de usarlo: la curiosidad segura empieza por preguntar.","saf_lab_04"),
    ("saf_lab_05","EN_LABORATORIO_VIRTUAL","Manos sucias","Te ensuciaste las manos con una sustancia durante un experimento real.",
     "Lavarte las manos con agua y jabón","Limpiarte en la ropa","Seguir tocando otras cosas sin lavarte",
     "Lavarse las manos después de manipular materiales de laboratorio es un hábito de seguridad básico.","saf_lab_05"),
    ("saf_lab_06","EN_LABORATORIO_VIRTUAL","Solo con un adulto","El libro de experimentos dice 'realizar solo con un adulto presente'.",
     "Esperar a tener un adulto contigo","Hacerlo de todas formas, solo esta vez","Pedirle a un amigo que haga de adulto",
     "Esa indicación existe para tu seguridad: siempre debe respetarse tal como está escrita.","saf_lab_06"),
    ("saf_lab_07","EN_LABORATORIO_VIRTUAL","Guardar herramientas","Terminaste de usar unas tijeras y pinzas de laboratorio en clase real.",
     "Guardarlas en su lugar con cuidado","Dejarlas sobre la mesa de cualquier forma","Llevártelas en el bolsillo",
     "Guardar bien las herramientas evita accidentes y ayuda a que el siguiente grupo las encuentre en orden.","saf_lab_07"),
    # PRIMEROS_AUXILIOS_BASICOS (7) - siempre orientado a avisar a un adulto, sin procedimientos médicos
    ("saf_aux_01","PRIMEROS_AUXILIOS_BASICOS","Salpicadura en los ojos","Un compañero se salpicó los ojos con algo por accidente.",
     "Avisar de inmediato a un adulto responsable","Decirle que se frote los ojos","Esperar a ver si se le pasa solo",
     "Ante cualquier salpicadura en los ojos, se debe avisar inmediatamente a un adulto; nunca frotar.","saf_aux_01"),
    ("saf_aux_02","PRIMEROS_AUXILIOS_BASICOS","Quemadura leve","Alguien se quemó ligeramente la mano al tocar algo caliente.",
     "Avisar a un adulto para que lo atienda","Aplicar hielo directamente y con fuerza","No decir nada, ya se le pasará",
     "Las quemaduras, incluso leves, deben ser atendidas por un adulto responsable.","saf_aux_02"),
    ("saf_aux_03","PRIMEROS_AUXILIOS_BASICOS","Mareo repentino","Un compañero se sintió mareado durante una actividad.",
     "Avisar a un adulto y ayudarlo a sentarse","Dejarlo solo hasta que se le pase","Darle algo de comer o beber sin avisar",
     "Ante un mareo, lo correcto es avisar a un adulto y acompañar a la persona mientras llega ayuda.","saf_aux_03"),
    ("saf_aux_04","PRIMEROS_AUXILIOS_BASICOS","Algo tragado por accidente","Alguien tragó accidentalmente algo que no debía.",
     "Avisar a un adulto de inmediato","Darle de beber algo para que se le pase","Esperar a ver si se siente mal",
     "En estos casos hay que avisar a un adulto inmediatamente; solo un adulto o un profesional debe decidir qué hacer.","saf_aux_04"),
    ("saf_aux_05","PRIMEROS_AUXILIOS_BASICOS","Corte leve","Un compañero se cortó levemente con un material del laboratorio.",
     "Avisar a un adulto para que lo revise","Ignorarlo si no sangra mucho","Cubrirlo con cualquier cosa sin avisar",
     "Incluso los cortes leves deben ser revisados por un adulto responsable para curarlos bien.","saf_aux_05"),
    ("saf_aux_06","PRIMEROS_AUXILIOS_BASICOS","Tos por un olor fuerte","Alguien empezó a toser mucho después de oler algo fuerte.",
     "Alejarlo del lugar y avisar a un adulto","Decirle que aguante la respiración","No darle importancia",
     "Ante tos o molestia por un olor fuerte, aléjate del área y busca ayuda de un adulto.","saf_aux_06"),
    ("saf_aux_07","PRIMEROS_AUXILIOS_BASICOS","Piel manchada","Un compañero se manchó la piel con una sustancia desconocida.",
     "Avisar a un adulto para que lo revise","Frotar fuerte para quitarlo","Esperar a que se seque solo",
     "Ante contacto con una sustancia desconocida en la piel, siempre debe intervenir un adulto responsable.","saf_aux_07"),
    # ETIQUETAS_Y_SIMBOLOS (7)
    ("saf_etq_01","ETIQUETAS_Y_SIMBOLOS","Símbolo de calavera","Ves un símbolo con una calavera en un envase.",
     "Entender que significa peligro y no tocarlo","Pensar que es solo una decoración","Abrirlo para comprobar",
     "El símbolo de calavera indica peligro: nunca debe tocarse ni abrirse sin un adulto.","saf_etq_01"),
    ("saf_etq_02","ETIQUETAS_Y_SIMBOLOS","Símbolo de llama","Ves un símbolo de llama en una etiqueta.",
     "Saber que el contenido puede ser inflamable","Pensar que sirve para encender fuego","Acercarlo a una fuente de calor",
     "El símbolo de llama avisa que el producto puede arder fácilmente: debe alejarse del calor y del fuego.","saf_etq_02"),
    ("saf_etq_03","ETIQUETAS_Y_SIMBOLOS","Símbolo corrosivo","Ves un símbolo con una mano y un líquido goteando sobre ella.",
     "Entender que el producto puede dañar la piel y evitarlo","Tocarlo para comprobar si es cierto","Ignorarlo por completo",
     "Ese símbolo indica que el producto es corrosivo y puede dañar la piel: nunca debe tocarse directamente.","saf_etq_03"),
    ("saf_etq_04","ETIQUETAS_Y_SIMBOLOS","Símbolo de reciclaje","Ves el símbolo de reciclaje en un envase vacío.",
     "Depositarlo en el contenedor correcto","Tirarlo en cualquier lugar","Quemarlo",
     "El símbolo de reciclaje indica que el envase, una vez vacío y limpio, puede reciclarse correctamente.","saf_etq_04"),
    ("saf_etq_05","ETIQUETAS_Y_SIMBOLOS","Signo de exclamación","Ves un signo de exclamación en una etiqueta de producto.",
     "Tener cuidado extra y leer las instrucciones","Ignorarlo, no parece grave","Usar el doble de producto",
     "El signo de exclamación pide precaución: conviene leer bien las instrucciones antes de usar el producto.","saf_etq_05"),
    ("saf_etq_06","ETIQUETAS_Y_SIMBOLOS","Envase sin etiqueta","Ves un envase de líquido que no tiene ninguna etiqueta.",
     "No usarlo y avisar a un adulto","Adivinar qué es por el color","Probarlo con cuidado",
     "Un envase sin etiqueta es un misterio: nunca se debe usar ni probar, siempre avisar a un adulto.","saf_etq_06"),
    ("saf_etq_07","ETIQUETAS_Y_SIMBOLOS","Símbolo no comestible","Ves un símbolo de 'no comestible' en un producto casero.",
     "Recordar que nunca se debe comer ni probar","Pensar que es solo para adultos","Probarlo si tiene buen color",
     "El símbolo de 'no comestible' aplica para todos, niños y adultos: nunca debe llevarse a la boca.","saf_etq_07"),
]
assert len(SAFETY) == 35, f"Se esperaban 35 escenarios de seguridad, hay {len(SAFETY)}"

# ---------------------------------------------------------------------------
# 7) INSIGNIAS (Badge) — 10
# ---------------------------------------------------------------------------
# (id, name, description, category, icon, criteria_type, criteria_value, order)
BADGES = [
    ("badge_primeros_pasos","Primeros Pasos","Completaste tu primer experimento en QuimicAtomix.","PROGRESO","badge_primeros_pasos","EXPERIMENTOS_COMPLETADOS",1,1),
    ("badge_curioso","Explorador Curioso","Completaste 10 experimentos del laboratorio.","PROGRESO","badge_curioso","EXPERIMENTOS_COMPLETADOS",10,2),
    ("badge_maestro_mezclas","Maestro de las Mezclas","Dominaste por completo un tema del laboratorio.","DOMINIO","badge_maestro_mezclas","TEMA_DOMINADO",1,3),
    ("badge_racha","Racha de Genio","Encadenaste 5 aciertos seguidos sin fallar.","ESPECIAL","badge_racha","RACHA_ACIERTOS",5,4),
    ("badge_guardian","Guardián de la Seguridad","Completaste 10 escenarios de seguridad del laboratorio.","SEGURIDAD","badge_guardian","ESCENARIOS_SEGURIDAD",10,5),
    ("badge_arquitecto","Arquitecto Molecular","Construiste 5 moléculas distintas correctamente.","COLECCION","badge_arquitecto","MOLECULAS_CONSTRUIDAS",5,6),
    ("badge_nivel5","Explorador Nivel 5","Alcanzaste el nivel 5 en tu perfil de laboratorio.","PROGRESO","badge_nivel5","NIVEL_ALCANZADO",5,7),
    ("badge_coleccionista","Coleccionista de Estrellas","Acumulaste 50 estrellas en total.","COLECCION","badge_coleccionista","ESTRELLAS_TOTALES",50,8),
    ("badge_cientifico","Científico Completo","Completaste 40 experimentos del laboratorio.","DOMINIO","badge_cientifico","EXPERIMENTOS_COMPLETADOS",40,9),
    ("badge_leyenda","Leyenda del Laboratorio","Dominaste por completo los seis temas de QuimicAtomix.","ESPECIAL","badge_leyenda","TEMA_DOMINADO",6,10),
]

# ---------------------------------------------------------------------------
# 8) EQUIPAMIENTO DE LABORATORIO (LabEquipment) — 10 coleccionables
# ---------------------------------------------------------------------------
EQUIPMENT = [
    ("equip_bata","Bata de laboratorio","La primera prenda de todo científico responsable.","equip_bata","COMUN","EXPERIMENTOS_COMPLETADOS",1,1),
    ("equip_gafas","Gafas protectoras","Protegen los ojos durante cualquier experimento.","equip_gafas","COMUN","EXPERIMENTOS_COMPLETADOS",5,2),
    ("equip_guantes","Guantes de laboratorio","Ideales para manipular materiales con cuidado.","equip_guantes","COMUN","ESCENARIOS_SEGURIDAD",5,3),
    ("equip_vaso","Vaso de precipitados","Un clásico para medir y observar líquidos.","equip_vaso","POCO_COMUN","EXPERIMENTOS_COMPLETADOS",10,4),
    ("equip_matraz","Matraz Erlenmeyer","Perfecto para mezclas que necesitan agitarse con cuidado.","equip_matraz","POCO_COMUN","TEMA_DOMINADO",1,5),
    ("equip_mechero","Mechero virtual","Una fuente de calor conceptual, siempre segura y simulada.","equip_mechero","POCO_COMUN","EXPERIMENTOS_COMPLETADOS",20,6),
    ("equip_microscopio","Microscopio","Te permite imaginar el mundo diminuto de los átomos.","equip_microscopio","RARO","NIVEL_ALCANZADO",4,7),
    ("equip_tabla","Tabla periódica de bolsillo","Un mapa de todos los átomos que has conocido.","equip_tabla","RARO","MOLECULAS_CONSTRUIDAS",8,8),
    ("equip_iman","Kit de imanes","Ideal para descubrir qué materiales son magnéticos.","equip_iman","RARO","RACHA_ACIERTOS",8,9),
    ("equip_medalla","Medalla dorada del laboratorio","El máximo reconocimiento de QuimicAtomix.","equip_medalla","LEGENDARIO","TEMA_DOMINADO",6,10),
]
print(f"Seguridad: {len(SAFETY)}  Insignias: {len(BADGES)}  Equipamiento: {len(EQUIPMENT)}")

# ---------------------------------------------------------------------------
# EMISIÓN DE ARCHIVOS KOTLIN
# ---------------------------------------------------------------------------

def write(path, content):
    with open(path, "w", encoding="utf-8") as f:
        f.write(content)
    print(f"  escrito: {path} ({len(content.splitlines())} lineas)")

def emit_topics():
    lines = [HEADER, f"package {PKG}\n",
             "import com.educalab.quimicatomix.data.local.entity.ChemicalTopic\n",
             "object SeedTopics {", "    val list: List<ChemicalTopic> = listOf("]
    for t in TOPICS:
        lines.append(
            f'        ChemicalTopic(id={kt_str(t["id"])}, title={kt_str(t["title"])}, '
            f'shortDescription={kt_str(t["short"])}, narrativeIntro={kt_str(t["narrative"])}, '
            f'iconKey={kt_str(t["icon"])}, colorHex={kt_str(t["color"])}, orderIndex={t["order"]}, '
            f'minLevelToUnlock={t["min_level"]}),'
        )
    lines.append("    )\n}")
    write(os.path.join(OUT_DIR, "SeedTopics.kt"), "\n".join(lines))

def emit_substances():
    lines = [HEADER, f"package {PKG}\n",
             "import com.educalab.quimicatomix.data.local.entity.MatterState",
             "import com.educalab.quimicatomix.data.local.entity.VirtualSubstance\n",
             "object SeedSubstances {", "    val list: List<VirtualSubstance> = listOf("]
    for (sid, topic, name, formula, state, color, desc, icon, misc, mag, dens) in SUBSTANCES:
        lines.append(
            f'        VirtualSubstance(id={kt_str(sid)}, topicId={kt_str(topic)}, name={kt_str(name)}, '
            f'symbolOrFormula={kt_str(formula)}, state=MatterState.{state}, colorHex={kt_str(color)}, '
            f'description={kt_str(desc)}, iconKey={kt_str(icon)}, isMiscible={str(misc).lower()}, '
            f'isMagneticConceptual={str(mag).lower()}, densityTier={dens}),'
        )
    lines.append("    )\n}")
    write(os.path.join(OUT_DIR, "SeedSubstances.kt"), "\n".join(lines))

def emit_atoms():
    lines = [HEADER, f"package {PKG}\n",
             "import com.educalab.quimicatomix.data.local.entity.Atom",
             "import com.educalab.quimicatomix.data.local.entity.AtomCategory\n",
             "object SeedAtoms {", "    val list: List<Atom> = listOf("]
    for (sym, name, protons, shells, cat, color, fact, val_) in ATOMS:
        lines.append(
            f'        Atom(id={kt_str(sym)}, symbol={kt_str(sym)}, name={kt_str(name)}, protons={protons}, '
            f'electronsShellsCsv={kt_str(shells)}, category=AtomCategory.{cat}, colorHex={kt_str(color)}, '
            f'funFact={kt_str(fact)}, commonValence={val_}),'
        )
    lines.append("    )\n}")
    write(os.path.join(OUT_DIR, "SeedAtoms.kt"), "\n".join(lines))

def emit_molecules():
    lines = [HEADER, f"package {PKG}\n",
             "import com.educalab.quimicatomix.data.local.entity.MoleculeChallenge\n",
             "object SeedMolecules {", "    val list: List<MoleculeChallenge> = listOf("]
    for (mid, formula, name, desc, comp, diff, xp, lvl, fact, icon) in MOLECULES:
        lines.append(
            f'        MoleculeChallenge(id={kt_str(mid)}, formula={kt_str(formula)}, commonName={kt_str(name)}, '
            f'description={kt_str(desc)}, compositionCsv={kt_str(comp)}, difficulty={diff}, xpReward={xp}, '
            f'unlockLevel={lvl}, funFact={kt_str(fact)}, iconKey={kt_str(icon)}),'
        )
    lines.append("    )\n}")
    write(os.path.join(OUT_DIR, "SeedMolecules.kt"), "\n".join(lines))

def emit_safety():
    lines = [HEADER, f"package {PKG}\n",
             "import com.educalab.quimicatomix.data.local.entity.SafetyCategory",
             "import com.educalab.quimicatomix.data.local.entity.SafetyScenario\n",
             "object SeedSafetyScenarios {", "    val list: List<SafetyScenario> = listOf("]
    for i, (sid, cat, title, situ, correct, d1, d2, expl, icon) in enumerate(SAFETY, start=1):
        distractors = f"{d1}|{d2}"
        lines.append(
            f'        SafetyScenario(id={kt_str(sid)}, category=SafetyCategory.{cat}, title={kt_str(title)}, '
            f'situationText={kt_str(situ)}, correctActionText={kt_str(correct)}, '
            f'distractorActionCsv={kt_str(distractors)}, explanation={kt_str(expl)}, iconKey={kt_str(icon)}, '
            f'orderIndex={i}),'
        )
    lines.append("    )\n}")
    write(os.path.join(OUT_DIR, "SeedSafetyScenarios.kt"), "\n".join(lines))

def emit_badges_equipment():
    lines = [HEADER, f"package {PKG}\n",
             "import com.educalab.quimicatomix.data.local.entity.Badge",
             "import com.educalab.quimicatomix.data.local.entity.BadgeCategory",
             "import com.educalab.quimicatomix.data.local.entity.CriteriaType",
             "import com.educalab.quimicatomix.data.local.entity.EquipmentRarity",
             "import com.educalab.quimicatomix.data.local.entity.LabEquipment\n",
             "object SeedBadgesAndEquipment {",
             "    val badges: List<Badge> = listOf("]
    for (bid, name, desc, cat, icon, crit, val_, order) in BADGES:
        lines.append(
            f'        Badge(id={kt_str(bid)}, name={kt_str(name)}, description={kt_str(desc)}, '
            f'category=BadgeCategory.{cat}, iconKey={kt_str(icon)}, criteriaType=CriteriaType.{crit}, '
            f'criteriaValue={val_}, orderIndex={order}),'
        )
    lines.append("    )\n")
    lines.append("    val equipment: List<LabEquipment> = listOf(")
    for (eid, name, desc, icon, rarity, crit, val_, order) in EQUIPMENT:
        lines.append(
            f'        LabEquipment(id={kt_str(eid)}, name={kt_str(name)}, description={kt_str(desc)}, '
            f'iconKey={kt_str(icon)}, rarity=EquipmentRarity.{rarity}, unlockCriteriaType=CriteriaType.{crit}, '
            f'unlockCriteriaValue={val_}, orderIndex={order}),'
        )
    lines.append("    )\n}")
    write(os.path.join(OUT_DIR, "SeedBadgesAndEquipment.kt"), "\n".join(lines))

def emit_experiments():
    # Vincula combinaciones a los experimentos de MEZ-*** (orden = índice en COMBOS)
    # y a los primeros 6 SEP-*** (orden = índice en separacion_pairs).
    mez_exps = [e for e in EXPERIMENTS if e["code"].startswith("MEZ-")]
    sep_exps = [e for e in EXPERIMENTS if e["code"].startswith("SEP-")]

    lines = [HEADER, f"package {PKG}\n",
             "import com.educalab.quimicatomix.data.local.entity.Experiment",
             "import com.educalab.quimicatomix.data.local.entity.ExperimentCombination",
             "import com.educalab.quimicatomix.data.local.entity.ExperimentStep",
             "import com.educalab.quimicatomix.data.local.entity.ExperimentType",
             "import com.educalab.quimicatomix.data.local.entity.InteractionType\n",
             "/** 55 practicas semilla, sus pasos y las combinaciones de sustancias que usan. */",
             "object SeedExperiments {",
             "    val list: List<Experiment> = listOf("]
    for e in EXPERIMENTS:
        lines.append(
            f'        Experiment(id={kt_str(e["id"])}, code={kt_str(e["code"])}, topicId={kt_str(e["topic"])}, '
            f'type=ExperimentType.{e["etype"]}, title={kt_str(e["title"])}, narrativeHook={kt_str(e["hook"])}, '
            f'description={kt_str(e["desc"])}, difficulty={e["diff"]}, primaryInteraction=InteractionType.{e["primary"]}, '
            f'xpReward={e["xp"]}, orderIndex={e["order"]}, requiredLevel={e["level"]}, iconKey={kt_str(e["icon"])}),'
        )
    lines.append("    )\n")

    lines.append("    val steps: List<ExperimentStep> = listOf(")
    for e in EXPERIMENTS:
        for idx, s in enumerate(e["steps"], start=1):
            lines.append(
                f'        ExperimentStep(experimentId={kt_str(e["id"])}, stepIndex={idx}, '
                f'instruction={kt_str(s["instruction"])}, interactionType=InteractionType.{s["kind"]}, '
                f'optionsCsv={kt_str(s["options"])}, correctAnswerCsv={kt_str(s["correct"])}, '
                f'explanationCorrect={kt_str(s["exp_ok"])}, explanationIncorrect={kt_str(s["exp_bad"])}),'
            )
    lines.append("    )\n")

    lines.append("    val combinations: List<ExperimentCombination> = listOf(")
    for exp, combo in zip(mez_exps, COMBOS):
        a, b, homog, tech, result = combo
        lines.append(
            f'        ExperimentCombination(experimentId={kt_str(exp["id"])}, substanceAId={kt_str(a)}, '
            f'substanceBId={kt_str(b)}, isHomogeneous={str(homog).lower()}, '
            f'recommendedSeparationTechnique={kt_str(tech)}, resultDescription={kt_str(result)}),'
        )
    for exp, (a, b, tech) in zip(sep_exps, separacion_pairs):
        homog = False
        result = dict((c[0], c) for c in [(x[0], x[1]) for x in []])  # no-op, keep structure simple
        lines.append(
            f'        ExperimentCombination(experimentId={kt_str(exp["id"])}, substanceAId={kt_str(a)}, '
            f'substanceBId={kt_str(b)}, isHomogeneous=false, '
            f'recommendedSeparationTechnique={kt_str(tech)}, resultDescription={kt_str("Mezcla heterogenea que debe separarse con " + tech + ".")}),'
        )
    lines.append("    )\n}")
    write(os.path.join(OUT_DIR, "SeedExperiments.kt"), "\n".join(lines))

print("\nEmitiendo archivos Kotlin...")
emit_topics()
emit_substances()
emit_atoms()
emit_molecules()
emit_safety()
emit_badges_equipment()
emit_experiments()
print("\nListo. Total experimentos:", len(EXPERIMENTS), " Total pasos:", sum(len(e["steps"]) for e in EXPERIMENTS))
print("Total sustancias:", len(SUBSTANCES), " Total combinaciones mezclas:", len(COMBOS))
print("Total atomos:", len(ATOMS), " Total moleculas:", len(MOLECULES))
print("Total seguridad:", len(SAFETY), " Total insignias:", len(BADGES), " Total equipamiento:", len(EQUIPMENT))

# ---------------------------------------------------------------------------
# EMISIÓN DE database/sample_data.sql (misma fuente de datos, formato SQL)
# ---------------------------------------------------------------------------
def sql_str(s):
    return "'" + str(s).replace("'", "''") + "'"

def emit_sql():
    out_path = os.path.join(os.path.dirname(__file__), "..", "database", "sample_data.sql")
    lines = [
        "-- ============================================================================",
        "-- QuimicAtomix -- Datos de ejemplo / contenido semilla (generado automaticamente)",
        "-- Fuente: tools/generate_seed.py -- misma fuente que usa DatabaseSeeder.kt en runtime",
        "-- ============================================================================", ""
    ]

    lines.append("-- Temas")
    for t in TOPICS:
        lines.append(
            f"INSERT INTO chemical_topic (id, title, shortDescription, narrativeIntro, iconKey, colorHex, orderIndex, minLevelToUnlock) VALUES "
            f"({sql_str(t['id'])}, {sql_str(t['title'])}, {sql_str(t['short'])}, {sql_str(t['narrative'])}, {sql_str(t['icon'])}, {sql_str(t['color'])}, {t['order']}, {t['min_level']});"
        )
    lines.append("")

    lines.append("-- Sustancias virtuales")
    for (sid, topic, name, formula, state, color, desc, icon, misc, mag, dens) in SUBSTANCES:
        lines.append(
            f"INSERT INTO virtual_substance (id, topicId, name, symbolOrFormula, state, colorHex, description, iconKey, isMiscible, isMagneticConceptual, densityTier) VALUES "
            f"({sql_str(sid)}, {sql_str(topic)}, {sql_str(name)}, {sql_str(formula)}, {sql_str(state)}, {sql_str(color)}, {sql_str(desc)}, {sql_str(icon)}, {1 if misc else 0}, {1 if mag else 0}, {dens});"
        )
    lines.append("")

    lines.append("-- Atomos")
    for (sym, name, protons, shells, cat, color, fact, val_) in ATOMS:
        lines.append(
            f"INSERT INTO atom (id, symbol, name, protons, electronsShellsCsv, category, colorHex, funFact, commonValence) VALUES "
            f"({sql_str(sym)}, {sql_str(sym)}, {sql_str(name)}, {protons}, {sql_str(shells)}, {sql_str(cat)}, {sql_str(color)}, {sql_str(fact)}, {val_});"
        )
    lines.append("")

    lines.append("-- Moleculas")
    for (mid, formula, name, desc, comp, diff, xp, lvl, fact, icon) in MOLECULES:
        lines.append(
            f"INSERT INTO molecule_challenge (id, formula, commonName, description, compositionCsv, difficulty, xpReward, unlockLevel, funFact, iconKey) VALUES "
            f"({sql_str(mid)}, {sql_str(formula)}, {sql_str(name)}, {sql_str(desc)}, {sql_str(comp)}, {diff}, {xp}, {lvl}, {sql_str(fact)}, {sql_str(icon)});"
        )
    lines.append("")

    lines.append("-- Experimentos (55 practicas)")
    for e in EXPERIMENTS:
        lines.append(
            f"INSERT INTO experiment (id, code, topicId, type, title, narrativeHook, description, difficulty, primaryInteraction, xpReward, orderIndex, requiredLevel, iconKey) VALUES "
            f"({sql_str(e['id'])}, {sql_str(e['code'])}, {sql_str(e['topic'])}, {sql_str(e['etype'])}, {sql_str(e['title'])}, {sql_str(e['hook'])}, {sql_str(e['desc'])}, {e['diff']}, {sql_str(e['primary'])}, {e['xp']}, {e['order']}, {e['level']}, {sql_str(e['icon'])});"
        )
    lines.append("")

    lines.append("-- Pasos de experimento")
    for e in EXPERIMENTS:
        for idx, s in enumerate(e["steps"], start=1):
            lines.append(
                "INSERT INTO experiment_step (experimentId, stepIndex, instruction, interactionType, optionsCsv, correctAnswerCsv, explanationCorrect, explanationIncorrect) VALUES "
                f"({sql_str(e['id'])}, {idx}, {sql_str(s['instruction'])}, {sql_str(s['kind'])}, {sql_str(s['options'])}, {sql_str(s['correct'])}, {sql_str(s['exp_ok'])}, {sql_str(s['exp_bad'])});"
            )
    lines.append("")

    lines.append("-- Combinaciones de mezclas")
    mez_exps = [e for e in EXPERIMENTS if e["code"].startswith("MEZ-")]
    for exp, combo in zip(mez_exps, COMBOS):
        a, b, homog, tech, result = combo
        lines.append(
            "INSERT INTO experiment_combination (experimentId, substanceAId, substanceBId, isHomogeneous, recommendedSeparationTechnique, resultDescription) VALUES "
            f"({sql_str(exp['id'])}, {sql_str(a)}, {sql_str(b)}, {1 if homog else 0}, {sql_str(tech)}, {sql_str(result)});"
        )
    sep_exps = [e for e in EXPERIMENTS if e["code"].startswith("SEP-")]
    for exp, (a, b, tech) in zip(sep_exps, separacion_pairs):
        lines.append(
            "INSERT INTO experiment_combination (experimentId, substanceAId, substanceBId, isHomogeneous, recommendedSeparationTechnique, resultDescription) VALUES "
            f"({sql_str(exp['id'])}, {sql_str(a)}, {sql_str(b)}, 0, {sql_str(tech)}, {sql_str('Mezcla heterogenea que debe separarse con ' + tech + '.')});"
        )
    lines.append("")

    lines.append("-- Escenarios de seguridad (35)")
    for i, (sid, cat, title, situ, correct, d1, d2, expl, icon) in enumerate(SAFETY, start=1):
        lines.append(
            "INSERT INTO safety_scenario (id, category, title, situationText, correctActionText, distractorActionCsv, explanation, iconKey, orderIndex) VALUES "
            f"({sql_str(sid)}, {sql_str(cat)}, {sql_str(title)}, {sql_str(situ)}, {sql_str(correct)}, {sql_str(d1+'|'+d2)}, {sql_str(expl)}, {sql_str(icon)}, {i});"
        )
    lines.append("")

    lines.append("-- Insignias")
    for (bid, name, desc, cat, icon, crit, val_, order) in BADGES:
        lines.append(
            "INSERT INTO badge (id, name, description, category, iconKey, criteriaType, criteriaValue, orderIndex) VALUES "
            f"({sql_str(bid)}, {sql_str(name)}, {sql_str(desc)}, {sql_str(cat)}, {sql_str(icon)}, {sql_str(crit)}, {val_}, {order});"
        )
    lines.append("")

    lines.append("-- Equipamiento de laboratorio")
    for (eid, name, desc, icon, rarity, crit, val_, order) in EQUIPMENT:
        lines.append(
            "INSERT INTO lab_equipment (id, name, description, iconKey, rarity, unlockCriteriaType, unlockCriteriaValue, orderIndex) VALUES "
            f"({sql_str(eid)}, {sql_str(name)}, {sql_str(desc)}, {sql_str(icon)}, {sql_str(rarity)}, {sql_str(crit)}, {val_}, {order});"
        )
    lines.append("")

    lines.append("-- Ejemplo de perfil y progreso de un jugador de muestra (no forma parte del seed real de la app)")
    lines.append("INSERT INTO user_profile (id, alias, avatarId, createdAt, lastActiveAt, totalXp, level) VALUES (1, 'Explorador', 0, 1700000000000, 1700000000000, 120, 2);")
    for t in TOPICS:
        lines.append(f"INSERT INTO progress (userId, topicId, experimentsCompleted, experimentsTotal, starsTotal, mastery) VALUES (1, {sql_str(t['id'])}, 0, 0, 0, 'DISPONIBLE');")

    write(out_path, "\n".join(lines))

emit_sql()

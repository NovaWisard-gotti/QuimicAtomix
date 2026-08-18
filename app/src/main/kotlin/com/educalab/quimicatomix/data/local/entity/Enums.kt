package com.educalab.quimicatomix.data.local.entity

/** Estado de la materia (módulo 3). */
enum class MatterState { SOLIDO, LIQUIDO, GASEOSO }

/** Tipo de práctica/experimento virtual (módulos 3-8). */
enum class ExperimentType { ESTADOS, MEZCLA, SEPARACION, ATOMO, MOLECULA, REACCION }

/** Tipo de interacción pedagógica de un paso de experimento (regla: <50% opción múltiple). */
enum class InteractionType {
    ARRASTRAR_SOLTAR,   // drag & drop de sustancias/objetos
    ORDENAR,            // ordenar pasos de un proceso
    CONSTRUIR,          // construir molécula/estructura
    CONECTAR,           // conectar átomos/conceptos
    CLASIFICAR,         // clasificar por categoría (arrastre a contenedores)
    CONFIGURAR,         // mover un slider/control (p.ej. temperatura conceptual)
    PREDECIR,           // predecir resultado antes de observar
    OBSERVAR,           // observar animación y responder sobre lo observado
    SELECCION_IMAGEN,   // seleccionar sobre representación visual (no texto)
    OPCION_MULTIPLE     // opción múltiple clásica (uso minoritario, <50% del total)
}

/** Resultado de un intento. */
enum class OutcomeType { EXITO, PARCIAL, REINTENTAR }

/** Estado visual de progreso de un módulo/tema (regla: no solo color). */
enum class MasteryState { BLOQUEADO, DISPONIBLE, INICIADO, COMPLETADO, DOMINADO }

/** Categoría simplificada de átomo para el constructor molecular educativo. */
enum class AtomCategory { NO_METAL, METAL, GAS_NOBLE, METALOIDE, HALOGENO }

/** Rareza de una pieza de equipamiento coleccionable. */
enum class EquipmentRarity { COMUN, POCO_COMUN, RARO, LEGENDARIO }

/** Qué tipo de acción desbloquea equipamiento/insignias. */
enum class CriteriaType {
    EXPERIMENTOS_COMPLETADOS,
    TEMA_DOMINADO,
    RACHA_ACIERTOS,
    ESCENARIOS_SEGURIDAD,
    MOLECULAS_CONSTRUIDAS,
    NIVEL_ALCANZADO,
    ESTRELLAS_TOTALES
}

/** Categoría de insignia. */
enum class BadgeCategory { PROGRESO, DOMINIO, SEGURIDAD, COLECCION, ESPECIAL }

/** Categoría de escenario de seguridad. */
enum class SafetyCategory { EN_CASA, EN_ESCUELA, EN_LABORATORIO_VIRTUAL, PRIMEROS_AUXILIOS_BASICOS, ETIQUETAS_Y_SIMBOLOS }

// ============================================================================
// ARCHIVO GENERADO POR tools/generate_seed.py — NO EDITAR A MANO.
// Para cambiar contenido, edita el script generador y vuelve a ejecutarlo.
// ============================================================================

package com.educalab.quimicatomix.data.seed

import com.educalab.quimicatomix.data.local.entity.Badge
import com.educalab.quimicatomix.data.local.entity.BadgeCategory
import com.educalab.quimicatomix.data.local.entity.CriteriaType
import com.educalab.quimicatomix.data.local.entity.EquipmentRarity
import com.educalab.quimicatomix.data.local.entity.LabEquipment

object SeedBadgesAndEquipment {
    val badges: List<Badge> = listOf(
        Badge(id="badge_primeros_pasos", name="Primeros Pasos", description="Completaste tu primer experimento en QuimicAtomix.", category=BadgeCategory.PROGRESO, iconKey="badge_primeros_pasos", criteriaType=CriteriaType.EXPERIMENTOS_COMPLETADOS, criteriaValue=1, orderIndex=1),
        Badge(id="badge_curioso", name="Explorador Curioso", description="Completaste 10 experimentos del laboratorio.", category=BadgeCategory.PROGRESO, iconKey="badge_curioso", criteriaType=CriteriaType.EXPERIMENTOS_COMPLETADOS, criteriaValue=10, orderIndex=2),
        Badge(id="badge_maestro_mezclas", name="Maestro de las Mezclas", description="Dominaste por completo un tema del laboratorio.", category=BadgeCategory.DOMINIO, iconKey="badge_maestro_mezclas", criteriaType=CriteriaType.TEMA_DOMINADO, criteriaValue=1, orderIndex=3),
        Badge(id="badge_racha", name="Racha de Genio", description="Encadenaste 5 aciertos seguidos sin fallar.", category=BadgeCategory.ESPECIAL, iconKey="badge_racha", criteriaType=CriteriaType.RACHA_ACIERTOS, criteriaValue=5, orderIndex=4),
        Badge(id="badge_guardian", name="Guardián de la Seguridad", description="Completaste 10 escenarios de seguridad del laboratorio.", category=BadgeCategory.SEGURIDAD, iconKey="badge_guardian", criteriaType=CriteriaType.ESCENARIOS_SEGURIDAD, criteriaValue=10, orderIndex=5),
        Badge(id="badge_arquitecto", name="Arquitecto Molecular", description="Construiste 5 moléculas distintas correctamente.", category=BadgeCategory.COLECCION, iconKey="badge_arquitecto", criteriaType=CriteriaType.MOLECULAS_CONSTRUIDAS, criteriaValue=5, orderIndex=6),
        Badge(id="badge_nivel5", name="Explorador Nivel 5", description="Alcanzaste el nivel 5 en tu perfil de laboratorio.", category=BadgeCategory.PROGRESO, iconKey="badge_nivel5", criteriaType=CriteriaType.NIVEL_ALCANZADO, criteriaValue=5, orderIndex=7),
        Badge(id="badge_coleccionista", name="Coleccionista de Estrellas", description="Acumulaste 50 estrellas en total.", category=BadgeCategory.COLECCION, iconKey="badge_coleccionista", criteriaType=CriteriaType.ESTRELLAS_TOTALES, criteriaValue=50, orderIndex=8),
        Badge(id="badge_cientifico", name="Científico Completo", description="Completaste 40 experimentos del laboratorio.", category=BadgeCategory.DOMINIO, iconKey="badge_cientifico", criteriaType=CriteriaType.EXPERIMENTOS_COMPLETADOS, criteriaValue=40, orderIndex=9),
        Badge(id="badge_leyenda", name="Leyenda del Laboratorio", description="Dominaste por completo los seis temas de QuimicAtomix.", category=BadgeCategory.ESPECIAL, iconKey="badge_leyenda", criteriaType=CriteriaType.TEMA_DOMINADO, criteriaValue=6, orderIndex=10),
    )

    val equipment: List<LabEquipment> = listOf(
        LabEquipment(id="equip_bata", name="Bata de laboratorio", description="La primera prenda de todo científico responsable.", iconKey="equip_bata", rarity=EquipmentRarity.COMUN, unlockCriteriaType=CriteriaType.EXPERIMENTOS_COMPLETADOS, unlockCriteriaValue=1, orderIndex=1),
        LabEquipment(id="equip_gafas", name="Gafas protectoras", description="Protegen los ojos durante cualquier experimento.", iconKey="equip_gafas", rarity=EquipmentRarity.COMUN, unlockCriteriaType=CriteriaType.EXPERIMENTOS_COMPLETADOS, unlockCriteriaValue=5, orderIndex=2),
        LabEquipment(id="equip_guantes", name="Guantes de laboratorio", description="Ideales para manipular materiales con cuidado.", iconKey="equip_guantes", rarity=EquipmentRarity.COMUN, unlockCriteriaType=CriteriaType.ESCENARIOS_SEGURIDAD, unlockCriteriaValue=5, orderIndex=3),
        LabEquipment(id="equip_vaso", name="Vaso de precipitados", description="Un clásico para medir y observar líquidos.", iconKey="equip_vaso", rarity=EquipmentRarity.POCO_COMUN, unlockCriteriaType=CriteriaType.EXPERIMENTOS_COMPLETADOS, unlockCriteriaValue=10, orderIndex=4),
        LabEquipment(id="equip_matraz", name="Matraz Erlenmeyer", description="Perfecto para mezclas que necesitan agitarse con cuidado.", iconKey="equip_matraz", rarity=EquipmentRarity.POCO_COMUN, unlockCriteriaType=CriteriaType.TEMA_DOMINADO, unlockCriteriaValue=1, orderIndex=5),
        LabEquipment(id="equip_mechero", name="Mechero virtual", description="Una fuente de calor conceptual, siempre segura y simulada.", iconKey="equip_mechero", rarity=EquipmentRarity.POCO_COMUN, unlockCriteriaType=CriteriaType.EXPERIMENTOS_COMPLETADOS, unlockCriteriaValue=20, orderIndex=6),
        LabEquipment(id="equip_microscopio", name="Microscopio", description="Te permite imaginar el mundo diminuto de los átomos.", iconKey="equip_microscopio", rarity=EquipmentRarity.RARO, unlockCriteriaType=CriteriaType.NIVEL_ALCANZADO, unlockCriteriaValue=4, orderIndex=7),
        LabEquipment(id="equip_tabla", name="Tabla periódica de bolsillo", description="Un mapa de todos los átomos que has conocido.", iconKey="equip_tabla", rarity=EquipmentRarity.RARO, unlockCriteriaType=CriteriaType.MOLECULAS_CONSTRUIDAS, unlockCriteriaValue=8, orderIndex=8),
        LabEquipment(id="equip_iman", name="Kit de imanes", description="Ideal para descubrir qué materiales son magnéticos.", iconKey="equip_iman", rarity=EquipmentRarity.RARO, unlockCriteriaType=CriteriaType.RACHA_ACIERTOS, unlockCriteriaValue=8, orderIndex=9),
        LabEquipment(id="equip_medalla", name="Medalla dorada del laboratorio", description="El máximo reconocimiento de QuimicAtomix.", iconKey="equip_medalla", rarity=EquipmentRarity.LEGENDARIO, unlockCriteriaType=CriteriaType.TEMA_DOMINADO, unlockCriteriaValue=6, orderIndex=10),
    )
}
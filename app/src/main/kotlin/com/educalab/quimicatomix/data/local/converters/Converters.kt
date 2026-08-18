package com.educalab.quimicatomix.data.local.converters

import androidx.room.TypeConverter
import com.educalab.quimicatomix.data.local.entity.AtomCategory
import com.educalab.quimicatomix.data.local.entity.BadgeCategory
import com.educalab.quimicatomix.data.local.entity.CriteriaType
import com.educalab.quimicatomix.data.local.entity.EquipmentRarity
import com.educalab.quimicatomix.data.local.entity.ExperimentType
import com.educalab.quimicatomix.data.local.entity.InteractionType
import com.educalab.quimicatomix.data.local.entity.MasteryState
import com.educalab.quimicatomix.data.local.entity.MatterState
import com.educalab.quimicatomix.data.local.entity.OutcomeType
import com.educalab.quimicatomix.data.local.entity.SafetyCategory

/**
 * Convertidores Room: todos los enums se persisten como su [name] textual.
 * Se mantiene explícito (sin reflexión genérica) para que cualquier cambio en un enum
 * obligue a revisar este archivo conscientemente.
 */
class Converters {

    @TypeConverter
    fun fromMatterState(value: MatterState): String = value.name
    @TypeConverter
    fun toMatterState(value: String): MatterState = MatterState.valueOf(value)

    @TypeConverter
    fun fromExperimentType(value: ExperimentType): String = value.name
    @TypeConverter
    fun toExperimentType(value: String): ExperimentType = ExperimentType.valueOf(value)

    @TypeConverter
    fun fromInteractionType(value: InteractionType): String = value.name
    @TypeConverter
    fun toInteractionType(value: String): InteractionType = InteractionType.valueOf(value)

    @TypeConverter
    fun fromOutcomeType(value: OutcomeType): String = value.name
    @TypeConverter
    fun toOutcomeType(value: String): OutcomeType = OutcomeType.valueOf(value)

    @TypeConverter
    fun fromMasteryState(value: MasteryState): String = value.name
    @TypeConverter
    fun toMasteryState(value: String): MasteryState = MasteryState.valueOf(value)

    @TypeConverter
    fun fromAtomCategory(value: AtomCategory): String = value.name
    @TypeConverter
    fun toAtomCategory(value: String): AtomCategory = AtomCategory.valueOf(value)

    @TypeConverter
    fun fromEquipmentRarity(value: EquipmentRarity): String = value.name
    @TypeConverter
    fun toEquipmentRarity(value: String): EquipmentRarity = EquipmentRarity.valueOf(value)

    @TypeConverter
    fun fromCriteriaType(value: CriteriaType): String = value.name
    @TypeConverter
    fun toCriteriaType(value: String): CriteriaType = CriteriaType.valueOf(value)

    @TypeConverter
    fun fromBadgeCategory(value: BadgeCategory): String = value.name
    @TypeConverter
    fun toBadgeCategory(value: String): BadgeCategory = BadgeCategory.valueOf(value)

    @TypeConverter
    fun fromSafetyCategory(value: SafetyCategory): String = value.name
    @TypeConverter
    fun toSafetyCategory(value: String): SafetyCategory = SafetyCategory.valueOf(value)
}

# QuimicAtomix - reglas ProGuard/R8
# La app no usa reflexión dinámica más allá de Room (gestionado por su propio consumer-rules).
-keepattributes *Annotation*
-keep class com.educalab.quimicatomix.data.local.entity.** { *; }
-keep class com.educalab.quimicatomix.domain.model.** { *; }

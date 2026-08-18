# QuimicAtomix 🧪⚛️

Laboratorio de química virtual, seguro y 100% offline para niños de 8 a 12 años. Construido con Kotlin + Jetpack Compose + Room, sin ninguna conexión a internet, cuentas, anuncios ni datos personales.

> **Estado de compilación:** el código fuente está completo, pero **no pudo compilarse ni verificarse dentro del entorno usado para generarlo** (sin Android SDK y con acceso bloqueado a Google Maven / Gradle / Maven Central). Ver [`docs/BUILD_REPORT.md`](docs/BUILD_REPORT.md) para el detalle honesto. El proyecto compila con Android Studio o mediante el workflow de GitHub Actions incluido en `.github/workflows/build-apk.yml`.

## Qué incluye

- 🧫 **6 temas**: Materia y Estados, Mezclas, Separación, Átomos, Constructor Molecular, Reacciones Virtuales.
- 🧪 **55 prácticas interactivas** (predecir, observar, ordenar, clasificar, conectar, configurar, construir...).
- 🛡️ **35 escenarios de seguridad** (casa, escuela, laboratorio, primeros auxilios, etiquetas).
- ⚛️ **16 átomos** y **12 moléculas** construibles en la mesa de construcción molecular.
- 🏅 **10 insignias** y **10 piezas de equipamiento** coleccionable, desbloqueadas por progreso real.
- 🎨 Identidad visual propia ("laboratorio nocturno"), ~30 ilustraciones vectoriales generadas por Compose Canvas, mascota guía animada (Quimi).
- 🔒 100% offline, sin datos personales, sin publicidad, sin analítica.

## Estructura del repositorio

```
QuimicAtomix/
├── app/                    Proyecto Android (Kotlin + Jetpack Compose)
├── database/                schema.sql y sample_data.sql
├── docs/                     Memoria, manuales, base de datos, build report
│   └── pdf/                   Versión PDF de los documentos anteriores
├── tools/                    Generador del contenido semilla (Python) y de PDFs
├── deliverables/              Entregables finales (APK si compiló, ZIP fuente, PDFs)
├── .github/workflows/         CI: compila el APK automáticamente en GitHub Actions
├── gradlew / gradlew.bat      Wrapper de Gradle (gradle-wrapper.jar legítimo incluido)
├── build.gradle.kts / settings.gradle.kts / gradle.properties
└── README.md
```

## Cómo compilar

### Opción A — Android Studio
1. Abre la carpeta raíz del proyecto en Android Studio (Koala o superior recomendado).
2. Deja que Gradle sincronice (descargará AGP/Compose/Room desde Google Maven).
3. `Build > Build APK(s)` o ejecuta desde un emulador/dispositivo con `minSdk 24+`.

### Opción B — Línea de comandos (con Android SDK instalado)
```bash
./gradlew clean
./gradlew testDebugUnitTest
./gradlew lintDebug
./gradlew assembleDebug
```
El APK resultante queda en `app/build/outputs/apk/debug/app-debug.apk`.

### Opción C — GitHub Actions (recomendado si no tienes Android SDK local)
Haz `git push` de este repositorio; el workflow `.github/workflows/build-apk.yml` instala JDK 17 + Android SDK y ejecuta `assembleDebug`, publicando el APK como artefacto descargable de la ejecución.

## Regenerar el contenido educativo

Todo el contenido semilla (experimentos, sustancias, átomos, moléculas, seguridad, insignias, equipamiento) se define en un único lugar:

```bash
python3 tools/generate_seed.py
```

Esto regenera tanto los objetos Kotlin (`app/src/main/kotlin/.../data/seed/Seed*.kt`) como `database/sample_data.sql`, manteniendo app y documentación sincronizadas.

## Documentación

- [`docs/MEMORIA_DESCRIPTIVA.md`](docs/MEMORIA_DESCRIPTIVA.md) — objetivos, alcance, requisitos, arquitectura, UX, privacidad.
- [`docs/MANUAL_USUARIO.md`](docs/MANUAL_USUARIO.md) — cómo usar la app.
- [`docs/MANUAL_TECNICO.md`](docs/MANUAL_TECNICO.md) — arquitectura técnica, stack, mantenimiento.
- [`docs/BASE_DE_DATOS.md`](docs/BASE_DE_DATOS.md) — esquema Room/SQLite, DER, consultas.
- [`docs/BUILD_REPORT.md`](docs/BUILD_REPORT.md) — estado real y honesto de la compilación.

## Privacidad

QuimicAtomix no solicita email, teléfono, dirección, ubicación ni contactos. No requiere el permiso `INTERNET`. El único permiso declarado es `VIBRATE`, opcional y silenciable desde el perfil. Todo el progreso vive únicamente en el dispositivo.

## Licencia y uso

Proyecto educativo generado como entregable de especificación. Ver `docs/MEMORIA_DESCRIPTIVA.md` para el detalle de alcance y limitaciones.

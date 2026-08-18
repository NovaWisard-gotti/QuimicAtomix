-- ============================================================================
-- QuimicAtomix — Esquema de base de datos (Room/SQLite)
-- Generado a partir de las entidades reales en app/src/main/kotlin/.../data/local/entity
-- ============================================================================

PRAGMA foreign_keys = ON;

-- ----------------------------------------------------------------------------
-- Perfil local del jugador (sin datos personales reales)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS user_profile (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    alias           TEXT    NOT NULL,
    avatarId        INTEGER NOT NULL,
    createdAt       INTEGER NOT NULL,
    lastActiveAt    INTEGER NOT NULL,
    soundEnabled    INTEGER NOT NULL DEFAULT 1,
    hapticsEnabled  INTEGER NOT NULL DEFAULT 1,
    totalXp         INTEGER NOT NULL DEFAULT 0,
    level           INTEGER NOT NULL DEFAULT 1
);

-- ----------------------------------------------------------------------------
-- Temas de la Academia
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS chemical_topic (
    id                  TEXT PRIMARY KEY,
    title               TEXT    NOT NULL,
    shortDescription    TEXT    NOT NULL,
    narrativeIntro      TEXT    NOT NULL,
    iconKey             TEXT    NOT NULL,
    colorHex            TEXT    NOT NULL,
    orderIndex          INTEGER NOT NULL,
    minLevelToUnlock    INTEGER NOT NULL
);

-- ----------------------------------------------------------------------------
-- Sustancias virtuales (100% conceptuales, sin procedimientos reales)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS virtual_substance (
    id                      TEXT PRIMARY KEY,
    topicId                 TEXT    NOT NULL,
    name                    TEXT    NOT NULL,
    symbolOrFormula         TEXT    NOT NULL,
    state                   TEXT    NOT NULL,   -- SOLIDO | LIQUIDO | GASEOSO
    colorHex                TEXT    NOT NULL,
    description             TEXT    NOT NULL,
    iconKey                 TEXT    NOT NULL,
    isMiscible              INTEGER NOT NULL,
    isMagneticConceptual    INTEGER NOT NULL,
    densityTier             INTEGER NOT NULL,
    FOREIGN KEY (topicId) REFERENCES chemical_topic(id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_virtual_substance_topic ON virtual_substance(topicId);

-- ----------------------------------------------------------------------------
-- Propiedades observables de una sustancia
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS substance_property (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    substanceId     TEXT    NOT NULL,
    propertyKey     TEXT    NOT NULL,
    propertyValue   TEXT    NOT NULL,
    iconKey         TEXT    NOT NULL,
    FOREIGN KEY (substanceId) REFERENCES virtual_substance(id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_substance_property_substance ON substance_property(substanceId);

-- ----------------------------------------------------------------------------
-- Experimentos (55 practicas semilla)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS experiment (
    id                  TEXT PRIMARY KEY,
    code                TEXT    NOT NULL,
    topicId             TEXT    NOT NULL,
    type                TEXT    NOT NULL,  -- ESTADOS|MEZCLA|SEPARACION|ATOMO|MOLECULA|REACCION
    title               TEXT    NOT NULL,
    narrativeHook       TEXT    NOT NULL,
    description         TEXT    NOT NULL,
    difficulty          INTEGER NOT NULL,
    primaryInteraction  TEXT    NOT NULL,
    xpReward            INTEGER NOT NULL,
    orderIndex          INTEGER NOT NULL,
    requiredLevel       INTEGER NOT NULL,
    iconKey             TEXT    NOT NULL,
    FOREIGN KEY (topicId) REFERENCES chemical_topic(id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_experiment_topic ON experiment(topicId);
CREATE UNIQUE INDEX IF NOT EXISTS idx_experiment_code ON experiment(code);

-- ----------------------------------------------------------------------------
-- Pasos de cada experimento
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS experiment_step (
    id                      INTEGER PRIMARY KEY AUTOINCREMENT,
    experimentId            TEXT    NOT NULL,
    stepIndex               INTEGER NOT NULL,
    instruction             TEXT    NOT NULL,
    interactionType         TEXT    NOT NULL,
    optionsCsv              TEXT    NOT NULL,
    correctAnswerCsv        TEXT    NOT NULL,
    explanationCorrect      TEXT    NOT NULL,
    explanationIncorrect    TEXT    NOT NULL,
    FOREIGN KEY (experimentId) REFERENCES experiment(id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_experiment_step_experiment ON experiment_step(experimentId);

-- ----------------------------------------------------------------------------
-- Combinaciones válidas de sustancias por experimento (mezclas/separación)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS experiment_combination (
    id                              INTEGER PRIMARY KEY AUTOINCREMENT,
    experimentId                    TEXT    NOT NULL,
    substanceAId                    TEXT    NOT NULL,
    substanceBId                    TEXT    NOT NULL,
    isHomogeneous                   INTEGER NOT NULL,
    recommendedSeparationTechnique  TEXT    NOT NULL,
    resultDescription               TEXT    NOT NULL,
    FOREIGN KEY (experimentId) REFERENCES experiment(id) ON DELETE CASCADE,
    FOREIGN KEY (substanceAId) REFERENCES virtual_substance(id) ON DELETE CASCADE,
    FOREIGN KEY (substanceBId) REFERENCES virtual_substance(id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_combo_experiment ON experiment_combination(experimentId);
CREATE INDEX IF NOT EXISTS idx_combo_subA ON experiment_combination(substanceAId);
CREATE INDEX IF NOT EXISTS idx_combo_subB ON experiment_combination(substanceBId);

-- ----------------------------------------------------------------------------
-- Resultado consolidado de un intento sobre un experimento
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS experiment_result (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    attemptId       INTEGER NOT NULL,
    experimentId    TEXT    NOT NULL,
    outcome         TEXT    NOT NULL,  -- EXITO | PARCIAL | REINTENTAR
    starsEarned     INTEGER NOT NULL,
    xpEarned        INTEGER NOT NULL,
    mistakesCount   INTEGER NOT NULL,
    timestamp       INTEGER NOT NULL,
    FOREIGN KEY (attemptId) REFERENCES attempt(id) ON DELETE CASCADE,
    FOREIGN KEY (experimentId) REFERENCES experiment(id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_result_attempt ON experiment_result(attemptId);
CREATE INDEX IF NOT EXISTS idx_result_experiment ON experiment_result(experimentId);

-- ----------------------------------------------------------------------------
-- Átomos (constructor molecular)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS atom (
    id                    TEXT PRIMARY KEY,
    symbol                TEXT    NOT NULL,
    name                  TEXT    NOT NULL,
    protons               INTEGER NOT NULL,
    electronsShellsCsv    TEXT    NOT NULL,
    category              TEXT    NOT NULL,
    colorHex              TEXT    NOT NULL,
    funFact               TEXT    NOT NULL,
    commonValence         INTEGER NOT NULL
);

-- ----------------------------------------------------------------------------
-- Retos de construcción molecular
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS molecule_challenge (
    id              TEXT PRIMARY KEY,
    formula         TEXT    NOT NULL,
    commonName      TEXT    NOT NULL,
    description     TEXT    NOT NULL,
    compositionCsv  TEXT    NOT NULL,
    difficulty      INTEGER NOT NULL,
    xpReward        INTEGER NOT NULL,
    unlockLevel     INTEGER NOT NULL,
    funFact         TEXT    NOT NULL,
    iconKey         TEXT    NOT NULL
);

-- ----------------------------------------------------------------------------
-- Escenarios de seguridad (35 semilla)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS safety_scenario (
    id                      TEXT PRIMARY KEY,
    category                TEXT    NOT NULL,
    title                   TEXT    NOT NULL,
    situationText           TEXT    NOT NULL,
    correctActionText       TEXT    NOT NULL,
    distractorActionCsv     TEXT    NOT NULL,
    explanation             TEXT    NOT NULL,
    iconKey                 TEXT    NOT NULL,
    orderIndex              INTEGER NOT NULL
);

-- ----------------------------------------------------------------------------
-- Intentos del jugador (experimentos, moléculas o escenarios de seguridad)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS attempt (
    id                      INTEGER PRIMARY KEY AUTOINCREMENT,
    userId                  INTEGER NOT NULL,
    experimentId            TEXT,
    moleculeChallengeId     TEXT,
    safetyScenarioId        TEXT,
    startedAt               INTEGER NOT NULL,
    finishedAt              INTEGER NOT NULL,
    success                 INTEGER NOT NULL,
    starsEarned             INTEGER NOT NULL,
    xpEarned                INTEGER NOT NULL,
    mistakesCount           INTEGER NOT NULL,
    FOREIGN KEY (userId) REFERENCES user_profile(id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_attempt_user ON attempt(userId);
CREATE INDEX IF NOT EXISTS idx_attempt_experiment ON attempt(experimentId);
CREATE INDEX IF NOT EXISTS idx_attempt_molecule ON attempt(moleculeChallengeId);
CREATE INDEX IF NOT EXISTS idx_attempt_safety ON attempt(safetyScenarioId);

-- ----------------------------------------------------------------------------
-- Equipamiento de laboratorio coleccionable
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS lab_equipment (
    id                      TEXT PRIMARY KEY,
    name                    TEXT    NOT NULL,
    description             TEXT    NOT NULL,
    iconKey                 TEXT    NOT NULL,
    rarity                  TEXT    NOT NULL,  -- COMUN|POCO_COMUN|RARO|LEGENDARIO
    unlockCriteriaType      TEXT    NOT NULL,
    unlockCriteriaValue     INTEGER NOT NULL,
    orderIndex              INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS unlocked_equipment (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    userId          INTEGER NOT NULL,
    equipmentId     TEXT    NOT NULL,
    unlockedAt      INTEGER NOT NULL,
    FOREIGN KEY (userId) REFERENCES user_profile(id) ON DELETE CASCADE,
    FOREIGN KEY (equipmentId) REFERENCES lab_equipment(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX IF NOT EXISTS idx_unlocked_equipment_unique ON unlocked_equipment(userId, equipmentId);

-- ----------------------------------------------------------------------------
-- Progreso agregado por tema
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS progress (
    id                      INTEGER PRIMARY KEY AUTOINCREMENT,
    userId                  INTEGER NOT NULL,
    topicId                 TEXT    NOT NULL,
    experimentsCompleted    INTEGER NOT NULL DEFAULT 0,
    experimentsTotal        INTEGER NOT NULL,
    starsTotal              INTEGER NOT NULL DEFAULT 0,
    mastery                 TEXT    NOT NULL DEFAULT 'BLOQUEADO',
    lastPlayedAt            INTEGER,
    FOREIGN KEY (userId) REFERENCES user_profile(id) ON DELETE CASCADE,
    FOREIGN KEY (topicId) REFERENCES chemical_topic(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX IF NOT EXISTS idx_progress_unique ON progress(userId, topicId);

-- ----------------------------------------------------------------------------
-- Insignias
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS badge (
    id              TEXT PRIMARY KEY,
    name            TEXT    NOT NULL,
    description     TEXT    NOT NULL,
    category        TEXT    NOT NULL,
    iconKey         TEXT    NOT NULL,
    criteriaType    TEXT    NOT NULL,
    criteriaValue   INTEGER NOT NULL,
    orderIndex      INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS user_badge (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    userId          INTEGER NOT NULL,
    badgeId         TEXT    NOT NULL,
    earnedAt        INTEGER NOT NULL,
    FOREIGN KEY (userId) REFERENCES user_profile(id) ON DELETE CASCADE,
    FOREIGN KEY (badgeId) REFERENCES badge(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX IF NOT EXISTS idx_user_badge_unique ON user_badge(userId, badgeId);

package com.pemmob.mealoo.data.model

fun MealDto.toDomain(): Meal {
    val rawIngredients = listOf(
        strIngredient1 to strMeasure1,
        strIngredient2 to strMeasure2,
        strIngredient3 to strMeasure3,
        strIngredient4 to strMeasure4,
        strIngredient5 to strMeasure5,
        strIngredient6 to strMeasure6,
        strIngredient7 to strMeasure7,
        strIngredient8 to strMeasure8,
        strIngredient9 to strMeasure9,
        strIngredient10 to strMeasure10,
        strIngredient11 to strMeasure11,
        strIngredient12 to strMeasure12,
        strIngredient13 to strMeasure13,
        strIngredient14 to strMeasure14,
        strIngredient15 to strMeasure15,
        strIngredient16 to strMeasure16,
        strIngredient17 to strMeasure17,
        strIngredient18 to strMeasure18,
        strIngredient19 to strMeasure19,
        strIngredient20 to strMeasure20
    )

    val cleanIngredients = rawIngredients.mapNotNull { (ingredient, measure) ->
        val cleanIng = ingredient?.trim().orEmpty()
        val cleanMeasure = measure?.trim().orEmpty()
        if (cleanIng.isNotEmpty() && !cleanIng.equals("null", ignoreCase = true)) {
            cleanIng to cleanMeasure.ifEmpty { "-" }
        } else {
            null
        }
    }

    return Meal(
        id = idMeal.orEmpty(),
        name = strMeal.orEmpty(),
        category = strCategory.orEmpty(),
        area = strArea.orEmpty(),
        instructions = strInstructions.orEmpty(),
        imageUrl = strMealThumb.orEmpty(),
        ingredients = cleanIngredients
    )
}

private val stepOnlyRegex = Regex("^(?i)(step\\s*\\d+[.:)]?|\\d+[.:)]?)$")
private val stepPrefixRegex = Regex("^(?i)(step\\s*\\d+[.:)]?\\s*|\\d+[.:)]?\\s*)")

fun String?.toInstructionSteps(): List<String> {
    if (this.isNullOrBlank()) return emptyList()

    val normalized = this.replace("\r\n", "\n").replace("\r", "\n").trim()
    if (normalized.isEmpty()) return emptyList()

    val hasBlankLines = normalized.contains(Regex("\\n\\s*\\n"))

    val rawCandidates: List<String> = if (hasBlankLines) {
        val candidates = mutableListOf<String>()
        val paragraphs = normalized.split(Regex("\\n\\s*\\n+"))
        for (paragraph in paragraphs) {
            val lines = paragraph.split(Regex("\\n+"))
                .map { it.trim() }
                .filter { it.isNotBlank() && !stepOnlyRegex.matches(it) }
                .map { it.replace(stepPrefixRegex, "").trim() }
                .filter { it.isNotBlank() }

            if (lines.isEmpty()) continue

            // Pisahkan jika ada baris-baris berlabel langkah dalam satu paragraf
            val areDistinctSteps = lines.size > 1 && lines.count { line ->
                line.matches(Regex("^[A-Z][A-Za-z\\s]{2,25}:\\s+.*"))
            } >= 2

            if (areDistinctSteps) {
                candidates.addAll(lines)
            } else {
                candidates.add(lines.joinToString(" ").replace(Regex("\\s+"), " ").trim())
            }
        }
        candidates
    } else {
        normalized.split(Regex("\\n+"))
            .map { it.trim() }
            .filter { it.isNotBlank() && !stepOnlyRegex.matches(it) }
            .map { it.replace(stepPrefixRegex, "").trim() }
            .filter { it.isNotBlank() }
    }

    val expandedCandidates = if (rawCandidates.size <= 1) {
        val single = rawCandidates.firstOrNull() ?: normalized
        val sentences = single.split(Regex("(?<=[.!?])\\s+(?=[A-Z])"))
            .map { it.trim() }
            .filter { it.length > 15 }
        if (sentences.size > 1) sentences else rawCandidates
    } else {
        rawCandidates
    }

    val merged = mutableListOf<String>()
    for (candidate in expandedCandidates) {
        val cleanCandidate = candidate.replace(Regex("\\s+"), " ").trim()
        if (cleanCandidate.isEmpty()) continue

        if (merged.isNotEmpty() && shouldMergeWithPrevious(cleanCandidate, merged.last())) {
            val previous = merged.removeAt(merged.lastIndex)
            val combined = "$previous $cleanCandidate".replace(Regex("\\s+"), " ").trim()
            merged.add(combined)
        } else {
            merged.add(cleanCandidate)
        }
    }

    return merged.map { it.replace(Regex("\\s+"), " ").trim() }
        .filter { step -> step.any { it.isLetterOrDigit() } }
}

private fun shouldMergeWithPrevious(current: String, previous: String): Boolean {
    if (previous.isBlank()) return false
    val firstChar = current.firstOrNull() ?: return false

    if (firstChar.isLowerCase()) return true

    val prevTrimmed = previous.trimEnd()
    if (prevTrimmed.endsWith(":") || prevTrimmed.endsWith(",")) return true

    val endsWithTerminal = prevTrimmed.endsWith(".") || prevTrimmed.endsWith("!") || prevTrimmed.endsWith("?")
    if (!endsWithTerminal && current.length < 25) return true

    return false
}

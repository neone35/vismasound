package com.arturmaslov.vismasound.data.usecase

import com.arturmaslov.vismasound.data.source.MainRepository

class GetRemoteGenreList(
    private val mainRepo: MainRepository
) {
    suspend fun execute(): List<String?>? {
        val trackList = mainRepo.fetchRemoteTrackList(null)
        return trackList
            ?.asSequence()
            ?.distinctBy { it.genre }
            ?.mapNotNull { it.genre }
            ?.filter { genreName ->
                val shorterThan10 = genreName.length <= 10
                val notEmpty = genreName.isNotEmpty()
                val hasLatinLetters = genreName.matches(Regex("^[a-zA-Z]+$"))
                shorterThan10 && notEmpty && hasLatinLetters
            }
            ?.sortedBy { it }
            ?.map {
                val lowerCased = it.lowercase()
                val withFirstUpper =
                    lowerCased.replaceFirstChar { firstChar -> firstChar.uppercase() }
                withFirstUpper
            }
            ?.toList()
    }
}
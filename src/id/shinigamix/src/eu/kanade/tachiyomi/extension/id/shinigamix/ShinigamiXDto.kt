package eu.kanade.tachiyomi.extension.id.shinigamix

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.*

// Data Transfer Object untuk list manga saat browsing
@Serializable
data class ShinigamiXBrowseDto(
    val url: String? = "",
    val title: String? = "",
    @SerialName("cover") val thumbnail: String? = "",
)

// Data Transfer Object untuk detail manga
@Serializable
data class ShinigamiXMangaDetailDto(
    @SerialName("synopsis") val description: String = "",
    val detailList: List<ShinigamiXMangaDetailListDto>? = null,
)

// Data Transfer Object untuk detail list di dalam manga
@Serializable
data class ShinigamiXMangaDetailListDto(
    val name: String = "",
    val value: String = "",
)

// Data Transfer Object untuk daftar chapter
@Serializable
data class ShinigamiXChapterListDto(
    val chapterList: List<ShinigamiXChapterDto>? = null,
)

// Data Transfer Object untuk detail chapter, termasuk parsing `releaseDate`
@Serializable
data class ShinigamiXChapterDto(
    @SerialName("releaseDate") val date: String = "",
    @SerialName("title") val name: String = "",
    val url: String = "",
    @SerialName("imageList") val pages: List<String> = emptyList(),
    val slug: String = "",
) {
    // Fungsi untuk mem-parse `releaseDate` menjadi objek `Date`
    fun getParsedDate(): Date? {
        return try {
            when {
                // Parsing format "hour ago"
                date.contains("hour ago", ignoreCase = true) -> Calendar.getInstance().apply {
                    add(Calendar.HOUR, -1)
                }.time
                
                // Parsing format "yesterday"
                date.contains("yesterday", ignoreCase = true) -> Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, -1)
                }.time
                
                // Parsing tanggal lengkap, seperti "October 26, 2024"
                else -> SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(date)
            }
        } catch (e: Exception) {
            null // Jika parsing gagal, kembalikan null
        }
    }
}

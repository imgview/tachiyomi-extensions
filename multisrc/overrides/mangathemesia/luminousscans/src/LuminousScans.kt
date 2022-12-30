package eu.kanade.tachiyomi.extension.en.luminousscans

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceScreen
import androidx.preference.SwitchPreferenceCompat
import eu.kanade.tachiyomi.multisrc.mangathemesia.MangaThemesia
import eu.kanade.tachiyomi.source.ConfigurableSource
import eu.kanade.tachiyomi.source.model.FilterList
import eu.kanade.tachiyomi.source.model.MangasPage
import eu.kanade.tachiyomi.source.model.SChapter
import eu.kanade.tachiyomi.source.model.SManga
import okhttp3.MediaType.Companion.toMediaType
import rx.Observable
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class LuminousScans :
    MangaThemesia(
        "Luminous Scans",
        "https://luminousscans.com",
        "en",
        "/series"
    ),
    ConfigurableSource {

    private val preferences: SharedPreferences by lazy {
        Injekt.get<Application>().getSharedPreferences("source_$id", 0x0000)
    }

    override val pageSelector = "div#readerarea > :not(.swiper) img"

    // Permanent Url start
    override fun fetchPopularManga(page: Int): Observable<MangasPage> {
        return super.fetchPopularManga(page).tempUrlToPermIfNeeded()
    }

    override fun fetchLatestUpdates(page: Int): Observable<MangasPage> {
        return super.fetchLatestUpdates(page).tempUrlToPermIfNeeded()
    }

    override fun fetchSearchManga(page: Int, query: String, filters: FilterList): Observable<MangasPage> {
        return super.fetchSearchManga(page, query, filters).tempUrlToPermIfNeeded()
    }

    private fun Observable<MangasPage>.tempUrlToPermIfNeeded(): Observable<MangasPage> {
        return this.map { mangasPage ->
            MangasPage(
                mangasPage.mangas.map { it.tempUrlToPermIfNeeded() },
                mangasPage.hasNextPage
            )
        }
    }

    private fun SManga.tempUrlToPermIfNeeded(): SManga {
        val turnTempUrlToPerm = preferences.getBoolean(getPermanentMangaUrlPreferenceKey(), true)
        if (!turnTempUrlToPerm) return this

        val path = this.url.removePrefix("/").removeSuffix("/").split("/")
        path.lastOrNull()?.let { slug -> this.url = "$mangaUrlDirectory/${deobfuscateSlug(slug)}/" }

        return this
    }

    override fun fetchChapterList(manga: SManga) = super.fetchChapterList(manga.tempUrlToPermIfNeeded())
        .map { sChapterList -> sChapterList.map { it.tempUrlToPermIfNeeded() } }

    private fun SChapter.tempUrlToPermIfNeeded(): SChapter {
        val turnTempUrlToPerm = preferences.getBoolean(getPermanentChapterUrlPreferenceKey(), true)
        if (!turnTempUrlToPerm) return this

        val path = this.url.removePrefix("/").removeSuffix("/").split("/")
        path.lastOrNull()?.let { slug -> this.url = "/${deobfuscateSlug(slug)}/" }
        return this
    }

    override fun setupPreferenceScreen(screen: PreferenceScreen) {
        val permanentMangaUrlPref = SwitchPreferenceCompat(screen.context).apply {
            key = getPermanentMangaUrlPreferenceKey()
            title = PREF_PERM_MANGA_URL_TITLE
            summary = PREF_PERM_MANGA_URL_SUMMARY
            setDefaultValue(true)

            setOnPreferenceChangeListener { _, newValue ->
                val checkValue = newValue as Boolean
                preferences.edit()
                    .putBoolean(getPermanentMangaUrlPreferenceKey(), checkValue)
                    .commit()
            }
        }
        val permanentChapterUrlPref = SwitchPreferenceCompat(screen.context).apply {
            key = getPermanentChapterUrlPreferenceKey()
            title = PREF_PERM_CHAPTER_URL_TITLE
            summary = PREF_PERM_CHAPTER_URL_SUMMARY
            setDefaultValue(true)

            setOnPreferenceChangeListener { _, newValue ->
                val checkValue = newValue as Boolean
                preferences.edit()
                    .putBoolean(getPermanentChapterUrlPreferenceKey(), checkValue)
                    .commit()
            }
        }
        screen.addPreference(permanentMangaUrlPref)
        screen.addPreference(permanentChapterUrlPref)
    }

    private fun getPermanentMangaUrlPreferenceKey(): String {
        return PREF_PERM_MANGA_URL_KEY_PREFIX + lang
    }

    private fun getPermanentChapterUrlPreferenceKey(): String {
        return PREF_PERM_CHAPTER_URL_KEY_PREFIX + lang
    }
    // Permanent Url for Manga/Chapter End

    companion object {
        private const val COMPOSED_SUFFIX = "?comp"

        private const val PREF_PERM_MANGA_URL_KEY_PREFIX = "pref_permanent_manga_url_"
        private const val PREF_PERM_MANGA_URL_TITLE = "Permanent Manga URL"
        private const val PREF_PERM_MANGA_URL_SUMMARY = "Turns all manga urls into permanent ones."

        private const val PREF_PERM_CHAPTER_URL_KEY_PREFIX = "pref_permanent_chapter_url"
        private const val PREF_PERM_CHAPTER_URL_TITLE = "Permanent Chapter URL"
        private const val PREF_PERM_CHAPTER_URL_SUMMARY = "Turns all chapter urls into permanent ones."

        /**
         *
         * De-obfuscates the slug of a series or chapter to the permanent slug
         *   * For a series: "12345678-this-is-a-series" -> "this-is-a-series"
         *   * For a chapter: "12345678-this-is-a-series-chapter-1" -> "this-is-a-series-chapter-1"
         *
         * @param obfuscated_slug the obfuscated slug of a series or chapter
         *
         * @return
         */
        private fun deobfuscateSlug(obfuscated_slug: String) = obfuscated_slug
            .replaceFirst(Regex("""^\d+-"""), "")

        private val MEDIA_TYPE = "image/png".toMediaType()
    }
}

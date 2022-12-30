package eu.kanade.tachiyomi.multisrc.mangathemesia

import generator.ThemeSourceData.SingleLang
import generator.ThemeSourceGenerator

// Formerly WPMangaStream & WPMangaReader -> MangaThemesia
class MangaThemesiaGenerator : ThemeSourceGenerator {

    override val themePkg = "mangathemesia"

    override val themeClass = "MangaThemesia"

    override val baseVersionCode: Int = 22

    override val sources = listOf(
        SingleLang("Asura Scans X", "https://www.asurascans.com", "en", overrideVersionCode = 5),
        SingleLang("Flame Scans X", "https://flamescans.org", "en", overrideVersionCode = 2),
        SingleLang("Luminous Scans", "https://luminousscans.com", "en", overrideVersionCode = 2),
    )

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            MangaThemesiaGenerator().createAll()
        }
    }
}

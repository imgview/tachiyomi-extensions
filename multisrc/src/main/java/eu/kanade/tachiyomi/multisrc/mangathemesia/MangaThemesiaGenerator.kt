package eu.kanade.tachiyomi.multisrc.mangathemesia

import generator.ThemeSourceData.SingleLang
import generator.ThemeSourceGenerator

// Formerly WPMangaStream & WPMangaReader -> MangaThemesia
class MangaThemesiaGenerator : ThemeSourceGenerator {

    override val themePkg = "mangathemesia"

    override val themeClass = "MangaThemesia"

    override val baseVersionCode: Int = 26

    override val sources = listOf(
        SingleLang("Asura Scans X", "https://asuracomics.com", "en", overrideVersionCode = 6),
        SingleLang("Luminous Scans", "https://luminousscans.com", "en", overrideVersionCode = 2),
    )

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            MangaThemesiaGenerator().createAll()
        }
    }
}

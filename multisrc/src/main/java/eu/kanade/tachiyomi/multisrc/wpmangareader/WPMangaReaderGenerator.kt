package eu.kanade.tachiyomi.multisrc.wpmangareader

import generator.ThemeSourceData.SingleLang
import generator.ThemeSourceGenerator

class WPMangaReaderGenerator : ThemeSourceGenerator {

    override val themePkg = "wpmangareader"

    override val themeClass = "WPMangaReader"

    override val baseVersionCode: Int = 8

    override val sources = listOf(
        SingleLang("Flame Scans X", "https://flamescans.org", "en", overrideVersionCode = 1),
    )

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            WPMangaReaderGenerator().createAll()
        }
    }
}

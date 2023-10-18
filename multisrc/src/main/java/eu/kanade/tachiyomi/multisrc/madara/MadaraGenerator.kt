package eu.kanade.tachiyomi.multisrc.madara

import generator.ThemeSourceData.SingleLang
import generator.ThemeSourceGenerator

class MadaraGenerator : ThemeSourceGenerator {

    override val themePkg = "madara"

    override val themeClass = "Madara"

    override val baseVersionCode: Int = 31

    override val sources = listOf(
        SingleLang("Reset Scans", "https://reset-scans.com", "en", overrideVersionCode = 1),
        SingleLang("Setsu Scans", "https://setsuscans.com", "en", overrideVersionCode = 1),
    )

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            MadaraGenerator().createAll()
        }
    }
}

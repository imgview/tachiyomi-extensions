package eu.kanade.tachiyomi.multisrc.madara

import generator.ThemeSourceData.SingleLang
import generator.ThemeSourceGenerator

class MadaraGenerator : ThemeSourceGenerator {

    override val themePkg = "madara"

    override val themeClass = "Madara"

    override val baseVersionCode: Int = 4

    override val sources = listOf(
            SingleLang("24hRomance", "https://24hromance.com", "en", className = "Romance24h"),
    )

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            MadaraGenerator().createAll()
        }
    }
}

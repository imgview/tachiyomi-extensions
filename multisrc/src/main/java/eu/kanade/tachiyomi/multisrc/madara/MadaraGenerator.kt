package eu.kanade.tachiyomi.multisrc.madara

import generator.ThemeSourceData.MultiLang
import generator.ThemeSourceData.SingleLang
import generator.ThemeSourceGenerator

class MadaraGenerator : ThemeSourceGenerator {

    override val themePkg = "madara"

    override val themeClass = "Madara"

    override val baseVersionCode: Int = 18

    override val sources = listOf(
        SingleLang("DragonTea", "https://dragontea.ink", "en", overrideVersionCode = 2),
        SingleLang("Setsu Scans", "https://setsuscans.com", "en", overrideVersionCode = 1),
    )

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            MadaraGenerator().createAll()
        }
    }
}

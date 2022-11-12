package eu.kanade.tachiyomi.extension.en.resetscans

import eu.kanade.tachiyomi.multisrc.madara.Madara

class ResetScans : Madara("Reset Scans", "https://reset-scans.com", "en") {

    override val mangaDetailsSelectorDescription = "#tab-summary-content .description-summary"

    override val chapterUrlSelector = ".li__text a"
}

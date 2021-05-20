package eu.kanade.tachiyomi.extension.en.manhuaesmin

import eu.kanade.tachiyomi.multisrc.madara.Madara
import eu.kanade.tachiyomi.source.model.SChapter
import okhttp3.Response

class ManhuaESmin : Madara("Manhua ES -1", "https://manhuaes.com", "en") {
    override val pageListParseSelector = ".reading-content div.text-left :has(>img)"
    override fun chapterListParse(response: Response): List<SChapter> {
        return super.chapterListParse(response).drop(1)
    }
}

package eu.kanade.tachiyomi.extension.en.manga347

import eu.kanade.tachiyomi.multisrc.madara.Madara
import eu.kanade.tachiyomi.network.GET
import eu.kanade.tachiyomi.source.model.FilterList
import eu.kanade.tachiyomi.source.model.SManga
import okhttp3.Request
import org.jsoup.nodes.Element
import java.text.SimpleDateFormat
import java.util.Locale

class Manga347 : Madara("Manga347", "https://manga347.com", "en", SimpleDateFormat("d MMM, yyyy", Locale.US)) {

    // popular
    override fun popularMangaRequest(page: Int): Request = GET("$baseUrl/webtoons/$page/?sort=most-viewd", headers)

    override fun popularMangaSelector() = "div.page-item"

    override val popularMangaUrlSelector = "h3 a"

    // latest
    override fun latestUpdatesRequest(page: Int): Request = GET("$baseUrl/webtoons/$page/?sort=latest-updated", headers)

    // search
    override fun searchMangaRequest(page: Int, query: String, filters: FilterList): Request {
        return GET("$baseUrl/search/$page/?keyword=$query", headers)
    }

    override fun searchMangaSelector() = popularMangaSelector()

    override fun searchMangaFromElement(element: Element): SManga = popularMangaFromElement(element)

    // chapters
    override fun chapterListSelector() = "li.a-h"
}

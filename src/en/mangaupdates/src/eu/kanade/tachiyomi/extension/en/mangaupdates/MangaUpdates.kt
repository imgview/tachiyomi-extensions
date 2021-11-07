package eu.kanade.tachiyomi.extension.en.mangaupdates

import eu.kanade.tachiyomi.network.GET
import eu.kanade.tachiyomi.source.model.FilterList
import eu.kanade.tachiyomi.source.model.SChapter
import eu.kanade.tachiyomi.source.model.SManga
import eu.kanade.tachiyomi.source.online.ParsedHttpSource
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.text.SimpleDateFormat
import java.util.Locale

class MangaUpdates : ParsedHttpSource() {

    override val name = "Manga Updates"

    override val baseUrl = "https://www.mangaupdates.com"

    override val lang = "en"

    override val supportsLatest = false

    override val client: OkHttpClient = network.cloudflareClient

    private val dateFormat: SimpleDateFormat = SimpleDateFormat("MM/dd/yy", Locale.US)

    // Popular

    override fun popularMangaRequest(page: Int): Request {
        return GET("$baseUrl/series.html?page=$page&search", headers)
    }

    override fun popularMangaSelector() = "#main_content .p-2 .no-gutters .col-12.p-3"

    override fun popularMangaFromElement(element: Element): SManga {
        val manga = SManga.create()
        manga.url = element.select("a:has(b)").attr("abs:href").substringAfter(baseUrl)
        manga.title = element.select("u b").text()
        manga.thumbnail_url = element.select(".series_thumb img").attr("abs:src")
        return manga
    }

    override fun popularMangaNextPageSelector() = ".row.no-gutters a:contains(Next Page)"

    // Latest

    override fun latestUpdatesRequest(page: Int) = throw UnsupportedOperationException("Not used")

    override fun latestUpdatesSelector() = throw UnsupportedOperationException("Not used")

    override fun latestUpdatesFromElement(element: Element) = throw UnsupportedOperationException("Not used")

    override fun latestUpdatesNextPageSelector() = throw UnsupportedOperationException("Not used")

    // Search

    override fun searchMangaRequest(page: Int, query: String, filters: FilterList): Request {
        val url = "$baseUrl/series.html?page=$page".toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("search", query)

        return GET(url.toString(), headers)
    }

    override fun searchMangaSelector() = popularMangaSelector()

    override fun searchMangaFromElement(element: Element): SManga = popularMangaFromElement(element)

    override fun searchMangaNextPageSelector() = popularMangaNextPageSelector()

    // Details

    override fun mangaDetailsParse(document: Document): SManga {
        val infoElement = document.select("#main_content .p-2 .row").first()
        val manga = SManga.create()
        manga.title = document.select(".releasestitle").text()
        manga.genre = infoElement.select(".col-6 .sContent:nth-child(5):has(a) a:has(>u)").joinToString { it.text() }
//        manga.status = parseStatus(infoElement.select(".post-info > span:eq(7)").text())
        var descSelector = "#div_desc_more"
        if (document.select("#div_desc_more").text().isNullOrBlank()) descSelector = ".sCat:contains(Description) + .sContent"
        manga.description = infoElement.select(descSelector).firstOrNull()?.ownText()
        if (manga.description.toString() == "N/A") manga.description = ""

        manga.thumbnail_url = infoElement.select(".sContent img.img-fluid").attr("abs:src")

        // add alternative name to manga description
        val altName = "Alternative Name" + ": "
        document.select(".sCat:contains(Associated Names) + .sContent").html()
            ?.replace("""<\s*br\s*/?>""".toRegex(), "\n")
            ?.replace("""\n$""".toRegex(), "")
            ?.let {
                if (it.isNullOrBlank().not()) {
                    manga.description = when {
                        manga.description.isNullOrBlank() -> altName + "\n" + it
                        else -> manga.description + "\n\n$altName\n" + it
                    }
                }
            }

        return manga
    }

    private fun String.toStatus() = when {
        this.contains("Ongoing", true) -> SManga.ONGOING
        this.contains("Completed", true) -> SManga.COMPLETED
        else -> SManga.UNKNOWN
    }

    // Chapters
    override fun chapterListRequest(manga: SManga) = chapterListRequest(
        "$baseUrl/releases.html?stype=series&search=" +
            manga.url.substringAfter("id=")
    )

    private fun chapterListRequest(mangaUrl: String): Request {
        return GET(mangaUrl, headers)
    }

    override fun chapterListSelector() = "#main_content .p-2 .row.no-gutters> div:has(span):not(:has(a))"

    override fun chapterFromElement(element: Element) = SChapter.create().apply {
        val scanlators = element.nextElementSibling().text()
        name = "Ch " + element.select("span").text() + " - " + scanlators
        url = name
        date_upload = parseChapterDate(element.previousElementSibling().previousElementSibling().previousElementSibling().text())
    }

    private fun parseChapterDate(date: String): Long {
        return dateFormat.parse(date)?.time ?: 0L
    }

    // Pages

    override fun pageListParse(document: Document) = throw UnsupportedOperationException("Not used")

    override fun imageUrlParse(document: Document): String = throw UnsupportedOperationException("Not used")

    // Filters
}

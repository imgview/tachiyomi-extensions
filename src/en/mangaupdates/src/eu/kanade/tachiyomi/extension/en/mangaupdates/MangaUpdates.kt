package eu.kanade.tachiyomi.extension.en.mangaupdates

import eu.kanade.tachiyomi.network.GET
import eu.kanade.tachiyomi.source.model.FilterList
import eu.kanade.tachiyomi.source.model.SChapter
import eu.kanade.tachiyomi.source.model.SManga
import eu.kanade.tachiyomi.source.online.ParsedHttpSource
import eu.kanade.tachiyomi.util.asJsoup
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.text.SimpleDateFormat
import java.util.Locale

class MangaUpdates : ParsedHttpSource() {

    override val name = "Manga Updates"

    override val versionId = 1

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
//        manga.title = document.select(".releasestitle").text()
        manga.thumbnail_url = infoElement.select(".sContent img.img-fluid").first().attr("abs:src")
        manga.genre = infoElement.select(".col-6 .sContent:nth-child(5):has(a) a:has(>u)").joinToString { it.text() }
//        manga.status = parseStatus(infoElement.select(".post-info > span:eq(7)").text())
        manga.description = infoElement.select(".sCat:contains(Description) + .sContent").text()
        if (manga.description!!.contains("More...")) manga.description = infoElement.select("#div_desc_more").text()
//        manga.description = infoElement.select("#div_desc_more").text()
//        if (manga.description.isNullOrEmpty()) manga.description = infoElement.select(".sCat:contains(Description) + .sContent")
//            .toString().substringAfter("\">").substringBefore("<br><").replace("<br>", "\n")
//        else manga.description = infoElement.select("#div_desc_more").toString().substringAfter("\">")
//            .substringBefore("<br><").replace("<br>", "\n")
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
            manga.url.substringAfter("id="),
        1
    )

    private fun chapterListRequest(mangaUrl: String, page: Int): Request {
        return GET("$mangaUrl&page=$page", headers)
    }

    override fun chapterListParse(response: Response): List<SChapter> {
        var document = response.asJsoup()
        val chapters = mutableListOf<SChapter>()
        var nextPage = 2
        document.select(chapterListSelector()).map { chapters.add(chapterFromElement(it)) }
        while (document.select(paginationNextPageSelector).isNotEmpty()) {
            val currentPage = document.select(".p-1.col-sm-6 form").attr("action")
            document = client.newCall(chapterListRequest(currentPage, nextPage)).execute().asJsoup()
            document.select(chapterListSelector()).map { chapters.add(chapterFromElement(it)) }
            nextPage++
        }

        return chapters
    }

    private val paginationNextPageSelector = popularMangaNextPageSelector()

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

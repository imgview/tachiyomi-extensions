package eu.kanade.tachiyomi.extension.id.shinigamix

import android.util.Base64
import eu.kanade.tachiyomi.network.GET
import eu.kanade.tachiyomi.network.interceptor.rateLimit
import eu.kanade.tachiyomi.source.model.FilterList
import eu.kanade.tachiyomi.source.model.MangasPage
import eu.kanade.tachiyomi.source.model.Page
import eu.kanade.tachiyomi.source.model.SChapter
import eu.kanade.tachiyomi.source.model.SManga
import eu.kanade.tachiyomi.source.online.HttpSource
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Headers
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import rx.Observable
import uy.kohesive.injekt.injectLazy
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class ShinigamiX : HttpSource() {

    // aplikasi premium shinigami ID APK free gratis

    override val name = "Shinigami X"

    override val baseUrl = "https://shinigami03.com"

    private val apiUrl = "https://api.shinigami.ae"

    override val lang = "id"

    override val supportsLatest = true

    private val json: Json by injectLazy()

    private val apiHeaders: Headers by lazy { apiHeadersBuilder().build() }

    override val client: OkHttpClient = network.cloudflareClient.newBuilder()
        .addInterceptor { chain ->
            val request = chain.request()
            val headers = request.headers.newBuilder().apply {
                if (request.header("X-Requested-With")!!.isNotBlank()) {
                    removeAll("X-Requested-With")
                }
            }.build()

            chain.proceed(request.newBuilder().headers(headers).build())
        }
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .rateLimit(24, 1, TimeUnit.SECONDS)
        .build()

    override fun headersBuilder(): Headers.Builder = Headers.Builder()
        .add("X-Requested-With", randomValue[Random.nextInt(randomValue.size)])

    private val randomValue = listOf("com.opera.gx", "com.mi.globalbrowser.mini", "com.opera.browser", "com.duckduckgo.mobile.android", "com.brave.browser", "com.vivaldi.browser", "com.android.chrome")

    private val encodedString2 = "AAAApA" + "AAAHU" + "AAABisA" + "AAAVAA" + "AAGgAAAB" + "pAAAAbgA" + "AAGcAA" + "ABzAAAAdwAAA" + "HcAAAB3"

    private val decodedString2 = Base64.decode(
        encodedString2
            .replace("ApA", "AbA")
            .replace("BisA", "BsA", true),
        Base64.DEFAULT,
    )
        .toString(Charsets.UTF_32).replace("www", "")

    private val encodedString3 = "AAAAaQAAAH" + "kAAABhAAA" + "AaQAAAG4AAA" + "BapAkAAAeQ" + "AAAGEAAABpA" + "AAAbgAAAGkA" + "AAB5AAAAYQAAA" + "GkAAABuAAAAZ" + "AAAAGGGUAA" + "AAxAAAAMgA" + "AADMAAAA0"

    private val decodedString3 = Base64.decode(
        encodedString3
            .replace("BaPAk", "BpA", true)
            .replace("GGGUA", "GUA", true),
        Base64.DEFAULT,
    )
        .toString(Charsets.UTF_32).substringBefore("4")

    private fun apiHeadersBuilder(): Headers.Builder = headersBuilder()
        .add(decodedString2, decodedString3)
        .add("Accept", "application/json")
        .add("User-Agent", "okhttp/3.14.9")

    override fun popularMangaRequest(page: Int): Request {
        // Adjust page number based on the pattern: 1, 3, 5, 7, ...
        val adjustedPage = (page - 1) * 2 + 1

        val url = "$apiUrl/$API_BASE_PATH/filter/views".toHttpUrl().newBuilder()
            .addQueryParameter("page", adjustedPage.toString())
            .addQueryParameter("multiple", "true")
            .toString()

        return GET(url, apiHeaders)
    }

    override fun popularMangaParse(response: Response): MangasPage {
        val result = response.parseAs<List<ShinigamiXBrowseDto>>()

        val projectList = result.map(::popularMangaFromObject)

        val hasNextPage = true

        return MangasPage(projectList, hasNextPage)
    }

    private fun popularMangaFromObject(obj: ShinigamiXBrowseDto): SManga = SManga.create().apply {
        title = obj.title.toString()
        thumbnail_url = obj.thumbnail
        url = obj.url.toString()
    }

    override fun latestUpdatesRequest(page: Int): Request {
        // Adjust page number based on the pattern: 1, 3, 5, 7, ...
        val adjustedPage = (page - 1) * 2 + 1

        val url = "$apiUrl/$API_BASE_PATH/filter/latest".toHttpUrl().newBuilder()
            .addQueryParameter("page", adjustedPage.toString())
            .addQueryParameter("multiple", "true")
            .toString()

        return GET(url, apiHeaders)
    }

    override fun latestUpdatesParse(response: Response): MangasPage = popularMangaParse(response)

    override fun searchMangaRequest(page: Int, query: String, filters: FilterList): Request {
        val url = "$apiUrl/$API_BASE_PATH".toHttpUrl().newBuilder()
            .addQueryParameter("page", page.toString())

        if (query.isNotEmpty()) {
            url.addPathSegment("search")
            url.addQueryParameter("keyword", query)
        }

        return GET(url.toString(), apiHeaders)
    }

    override fun searchMangaParse(response: Response): MangasPage = popularMangaParse(response)

    override fun getMangaUrl(manga: SManga): String {
        return manga.url.substringAfter("?url=")
    }

    override fun mangaDetailsRequest(manga: SManga): Request {
        return GET(
            "$apiUrl/$API_BASE_PATH/comic?url=${"$baseUrl/series/" +
                manga.url.substringAfter("?url=").substringAfter("/series/")}",
            apiHeaders,
        )
    }

    override fun mangaDetailsParse(response: Response): SManga {
        val mangaDetails = response.parseAs<ShinigamiXMangaDetailDto>()

        return SManga.create().apply {
            author = getValue(mangaDetails.detailList, "Author(s)").replace("Updating", "")
            artist = getValue(mangaDetails.detailList, "Artist(s)").replace("Updating", "")
            status = getValue(mangaDetails.detailList, "Tag(s)").toStatus()
            description = mangaDetails.description

            val type = getValue(mangaDetails.detailList, "Type")
            genre = getValue(mangaDetails.detailList, "Genre(s)") +
                if (type.isNullOrBlank().not()) ", $type" else ""
        }
    }

    private fun getValue(detailList: List<ShinigamiXMangaDetailListDto>?, name: String): String {
        val value = detailList!!.firstOrNull { it.name == name }?.value

        return value.orEmpty()
    }

    override fun getChapterUrl(chapter: SChapter): String {
        return chapter.url.substringAfter("?url=")
    }

    override fun chapterListRequest(manga: SManga): Request = mangaDetailsRequest(manga)

    override fun chapterListParse(response: Response): List<SChapter> {
        val result = response.parseAs<ShinigamiXChapterListDto>()

        return result.chapterList!!.map(::chapterFromObject)
    }

    private fun chapterFromObject(obj: ShinigamiXChapterDto): SChapter = SChapter.create().apply {
        name = obj.name
        date_upload = obj.date.toDate()
        url = "$apiUrl/$API_BASE_PATH_2/chapter?url=" + obj.url
    }

    override fun pageListRequest(chapter: SChapter): Request {
        return GET(chapter.url, apiHeaders)
    }

    override fun pageListParse(response: Response): List<Page> {
        val result = response.parseAs<ShinigamiXChapterDto>()
        return result.pages.mapIndexedNotNull { index, data ->
            // filtering image
            if (data == null || data.contains("_desktop")) null else Page(index = index, imageUrl = data)
        }
    }

    override fun fetchImageUrl(page: Page): Observable<String> = Observable.just(page.imageUrl!!)

    override fun imageUrlParse(response: Response): String = ""

    override fun imageRequest(page: Page): Request {
        val newHeaders = headersBuilder()
            .add("Accept", "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8")
            .set("Referer", page.url)
            .build()

        return GET(page.imageUrl!!, newHeaders)
    }

    private inline fun <reified T> Response.parseAs(): T = use {
        json.decodeFromString(it.body.string())
    }

    private fun String.toDate(): Long {
        return runCatching { DATE_FORMATTER.parse(this)?.time }
            .getOrNull() ?: 0L
    }

    private fun String.toStatus() = when (this) {
        "Updating", "OnGoing" -> SManga.ONGOING
        "Completed", "Finished" -> SManga.COMPLETED
        "Canceled" -> SManga.CANCELLED
        else -> SManga.UNKNOWN
    }

    companion object {
        private const val API_BASE_PATH = "api/v1"
        private const val API_BASE_PATH_2 = "api/v2"

        private val DATE_FORMATTER by lazy {
            SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)
        }
    }
}

package eu.kanade.tachiyomi.extension.en.asurascansx

import eu.kanade.tachiyomi.multisrc.wpmangastream.WPMangaStream
import okhttp3.Headers

class AsuraScansX : WPMangaStream("Asura Scans X", "https://asurascans.com", "en") {

    override fun headersBuilder(): Headers.Builder = Headers.Builder()
        .add(
            "Accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9," +
                "image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9"
        )
        .add("Accept-Language", "en-US, en;q=0.5")
        .add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36 Edg/90.0.818.62")
        .add("Referer", "https://www.google.com")

    override val pageSelector = "div.rdminimal img[loading*=lazy]"
}

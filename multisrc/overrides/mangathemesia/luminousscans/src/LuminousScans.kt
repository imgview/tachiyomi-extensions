package eu.kanade.tachiyomi.extension.en.luminousscans

import eu.kanade.tachiyomi.multisrc.mangathemesia.MangaThemesia

class LuminousScans : MangaThemesia("Luminous Scans", "https://luminousscans.com", "en", mangaUrlDirectory = "/series") {
    override val pageSelector = "div#readerarea > :not(.swiper) img"
}

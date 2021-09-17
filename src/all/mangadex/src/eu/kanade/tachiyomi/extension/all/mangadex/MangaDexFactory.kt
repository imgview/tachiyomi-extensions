package eu.kanade.tachiyomi.extension.all.mangadexplusen

import eu.kanade.tachiyomi.annotations.Nsfw
import eu.kanade.tachiyomi.source.Source
import eu.kanade.tachiyomi.source.SourceFactory

@Nsfw
class MangaDexFactory : SourceFactory {
    override fun createSources(): List<Source> = listOf(
//        MangaDexEnglish(),
        MangaDexJapanese(),
        MangaDexPolish(),
        MangaDexSerboCroatian(),
        MangaDexDutch(),
        MangaDexItalian(),
        MangaDexRussian(),
        MangaDexGerman(),
        MangaDexHungarian(),
        MangaDexFrench(),
        MangaDexFinnish(),
        MangaDexVietnamese(),
        MangaDexGreek(),
        MangaDexBulgarian(),
        MangaDexSpanishSpain(),
        MangaDexPortugueseBrazil(),
        MangaDexPortuguesePortugal(),
        MangaDexSwedish(),
        MangaDexArabic(),
        MangaDexDanish(),
        MangaDexChineseSimp(),
        MangaDexBengali(),
        MangaDexRomanian(),
        MangaDexCzech(),
        MangaDexMongolian(),
        MangaDexTurkish(),
        MangaDexIndonesian(),
        MangaDexKorean(),
        MangaDexSpanishLTAM(),
        MangaDexPersian(),
        MangaDexMalay(),
        MangaDexThai(),
        MangaDexCatalan(),
        MangaDexFilipino(),
        MangaDexChineseTrad(),
        MangaDexUkrainian(),
        MangaDexBurmese(),
        MangaDexLithuanian(),
        MangaDexHebrew(),
        MangaDexHindi(),
        MangaDexNorwegian(),
        Other(),
    )
}

// class MangaDexEnglish : MangaDex("en", "en&translatedLanguage[]=en")
class MangaDexJapanese : MangaDex("ja", "ja&translatedLanguage[]=en")
class MangaDexPolish : MangaDex("pl", "pl&translatedLanguage[]=en")
class MangaDexSerboCroatian : MangaDex("sh", "sh&translatedLanguage[]=en")
class MangaDexDutch : MangaDex("nl", "nl&translatedLanguage[]=en")
class MangaDexItalian : MangaDex("it", "it&translatedLanguage[]=en")
class MangaDexRussian : MangaDex("ru", "ru&translatedLanguage[]=en")
class MangaDexGerman : MangaDex("de", "de&translatedLanguage[]=en")
class MangaDexHungarian : MangaDex("hu", "hu&translatedLanguage[]=en")
class MangaDexFrench : MangaDex("fr", "fr&translatedLanguage[]=en")
class MangaDexFinnish : MangaDex("fi", "fi&translatedLanguage[]=en")
class MangaDexVietnamese : MangaDex("vi", "vi&translatedLanguage[]=en")
class MangaDexGreek : MangaDex("el", "el&translatedLanguage[]=en")
class MangaDexBulgarian : MangaDex("bg", "bg&translatedLanguage[]=en")
class MangaDexSpanishSpain : MangaDex("es", "es&translatedLanguage[]=en")
class MangaDexPortugueseBrazil : MangaDex("pt-BR", "pt-br&translatedLanguage[]=en")
class MangaDexPortuguesePortugal : MangaDex("pt", "pt&translatedLanguage[]=en")
class MangaDexSwedish : MangaDex("sv", "sv&translatedLanguage[]=en")
class MangaDexArabic : MangaDex("ar", "ar&translatedLanguage[]=en")
class MangaDexDanish : MangaDex("da", "da&translatedLanguage[]=en")
class MangaDexChineseSimp : MangaDex("zh-Hans", "zh&translatedLanguage[]=en")
class MangaDexBengali : MangaDex("bn", "bn&translatedLanguage[]=en")
class MangaDexRomanian : MangaDex("ro", "ro&translatedLanguage[]=en")
class MangaDexCzech : MangaDex("cs", "cs&translatedLanguage[]=en")
class MangaDexMongolian : MangaDex("mn", "mn&translatedLanguage[]=en")
class MangaDexTurkish : MangaDex("tr", "tr&translatedLanguage[]=en")
class MangaDexIndonesian : MangaDex("id", "id&translatedLanguage[]=en")
class MangaDexKorean : MangaDex("ko", "ko&translatedLanguage[]=en")
class MangaDexSpanishLTAM : MangaDex("es-419", "es-la&translatedLanguage[]=en")
class MangaDexPersian : MangaDex("fa", "fa&translatedLanguage[]=en")
class MangaDexMalay : MangaDex("ms", "ms&translatedLanguage[]=en")
class MangaDexThai : MangaDex("th", "th&translatedLanguage[]=en")
class MangaDexCatalan : MangaDex("ca", "ca&translatedLanguage[]=en")
class MangaDexFilipino : MangaDex("fil", "tl&translatedLanguage[]=en")
class MangaDexChineseTrad : MangaDex("zh-Hant", "zh-hk&translatedLanguage[]=en")
class MangaDexUkrainian : MangaDex("uk", "uk&translatedLanguage[]=en")
class MangaDexBurmese : MangaDex("my", "my&translatedLanguage[]=en")
class MangaDexLithuanian : MangaDex("lt", "lt&translatedLanguage[]=en")
class MangaDexHebrew : MangaDex("he", "he&translatedLanguage[]=en")
class MangaDexHindi : MangaDex("hi", "hi&translatedLanguage[]=en")
class MangaDexNorwegian : MangaDex("no", "no&translatedLanguage[]=en")
class Other : MangaDex("other", "NULL&translatedLanguage[]=en")

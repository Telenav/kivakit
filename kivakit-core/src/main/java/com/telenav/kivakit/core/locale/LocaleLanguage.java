////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.locale;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.internal.lexakai.DiagramLocale;
import com.telenav.kivakit.core.value.name.Name;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.collections.list.ObjectList.objectList;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.locale.LocaleRegion.WORLD;

/**
 * ISO-2 and ISO-3 codes for the most common languages in the world. There are, of course, vastly more than just these.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "unused", "SpellCheckingInspection" })
@UmlClassDiagram(diagram = DiagramLocale.class)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class LocaleLanguage extends Name implements Comparable<LocaleLanguage>
{
    private static final Map<String, LocaleLanguage> forIso2Code = new HashMap<>();

    private static final Map<String, LocaleLanguage> forIso3Code = new HashMap<>();

    public static final LocaleLanguage ALBANIAN = new LocaleLanguage("Albanian", "sq", "sqi");

    public static final LocaleLanguage AMHARIC = new LocaleLanguage("Amharic", "am", "amh");

    public static final LocaleLanguage ARABIC = new LocaleLanguage("Arabic", "ar", "ara");

    public static final LocaleLanguage AZERI = new LocaleLanguage("Azeri", "az", "aze");

    public static final LocaleLanguage BAHASA = new LocaleLanguage("Bahasa", "id", "ind");

    public static final LocaleLanguage BASQUE = new LocaleLanguage("Basque", "eu", "baq");

    public static final LocaleLanguage BELARUSIAN = new LocaleLanguage("Belarusian", "be", "bel");

    public static final LocaleLanguage BENGALI = new LocaleLanguage("Bengali", "bn", "ben");

    public static final LocaleLanguage BOSNIAN = new LocaleLanguage("Bosnian", "bs", "bos");

    public static final LocaleLanguage BULGARIAN = new LocaleLanguage("Bosnian", "bg", "bul");

    public static final LocaleLanguage BURMESE = new LocaleLanguage("Bosnian", "bg", "bul");

    public static final LocaleLanguage CATALAN = new LocaleLanguage("Catalan", "ca", "cat");

    public static final LocaleLanguage CHINESE = new LocaleLanguage("Chinese", "zh", "zho");

    public static final LocaleLanguage CHINESE_GAN = new LocaleLanguage("Gan Chinese", "zh", "gan");

    public static final LocaleLanguage CHINESE_HAKKA = new LocaleLanguage("Hakka Chinese", "zh", "hak");

    public static final LocaleLanguage CHINESE_MANDARIN = new LocaleLanguage("Mandarin Chinese", "zh", "chi");

    public static final LocaleLanguage CHINESE_MIN = new LocaleLanguage("Min Chinese", "zh", "cdo");

    public static final LocaleLanguage CHINESE_WU = new LocaleLanguage("Wu Chinese", "zh", "wuu");

    public static final LocaleLanguage CHINESE_XIANG = new LocaleLanguage("Xiang Chinese", "zh", "hsn");

    public static final LocaleLanguage CHINESE_YUE = new LocaleLanguage("Yue Chinese", "zh", "yue");

    public static final LocaleLanguage CROATIAN = new LocaleLanguage("Croatian", "hr", "hrv");

    public static final LocaleLanguage CZECH = new LocaleLanguage("Czech", "cs", "ces");

    public static final LocaleLanguage DANISH = new LocaleLanguage("Danish", "da", "dan");

    public static final LocaleLanguage DUTCH = new LocaleLanguage("Dutch", "nl", "nld");

    public static final LocaleLanguage ENGLISH = new LocaleLanguage("English", "en", "eng");

    public static final LocaleLanguage ESTONIAN = new LocaleLanguage("Estonian", "et", "est");

    public static final LocaleLanguage FILIPINO = new LocaleLanguage("Filipino", "??", "fil");

    public static final LocaleLanguage FINNISH = new LocaleLanguage("Finnish", "fi", "fin");

    public static final LocaleLanguage FRENCH = new LocaleLanguage("French", "fr", "fra");

    public static final LocaleLanguage GALICIAN = new LocaleLanguage("Galician", "gl", "glg");

    public static final LocaleLanguage GEORGIAN = new LocaleLanguage("Georgian", "ka", "kat");

    public static final LocaleLanguage GERMAN = new LocaleLanguage("German", "de", "deu");

    public static final LocaleLanguage GREEK = new LocaleLanguage("Greek", "el", "ell");

    public static final LocaleLanguage GUARANI = new LocaleLanguage("Guarani", "gn", "grn");

    public static final LocaleLanguage HAUSA = new LocaleLanguage("Hausa", "ha", "hau");

    public static final LocaleLanguage HEBREW = new LocaleLanguage("Hebrew", "he", "heb");

    public static final LocaleLanguage HINDI = new LocaleLanguage("Hindi", "hi", "hin");

    public static final LocaleLanguage HUNGARIAN = new LocaleLanguage("Hungarian", "hu", "hun");

    public static final LocaleLanguage ICELANDIC = new LocaleLanguage("Icelandic", "is", "isl");

    public static final LocaleLanguage IGBO = new LocaleLanguage("Igbo", "ig", "ibo");

    public static final LocaleLanguage INDONESIAN = new LocaleLanguage("Indonesian", "id", "ind");

    public static final LocaleLanguage IRISH = new LocaleLanguage("Irish", "ga", "gle");

    public static final LocaleLanguage ITALIAN = new LocaleLanguage("Italian", "it", "ita");

    public static final LocaleLanguage JAPANESE = new LocaleLanguage("Japanese", "ja", "jpn");

    public static final LocaleLanguage KAZAKH = new LocaleLanguage("Kazakh", "kk", "kaz");

    public static final LocaleLanguage KOREAN = new LocaleLanguage("Korean", "ko", "kor");

    public static final LocaleLanguage KURDISH = new LocaleLanguage("Kurdish", "ku", "kur");

    public static final LocaleLanguage LATVIAN = new LocaleLanguage("Latvian", "lv", "lav");

    public static final LocaleLanguage LITHUANIAN = new LocaleLanguage("Lithuanian", "lt", "lit");

    public static final LocaleLanguage MACEDONIAN = new LocaleLanguage("Macedonian", "mk", "mkd");

    public static final LocaleLanguage MALAY = new LocaleLanguage("Malay", "ms", "msa");

    public static final LocaleLanguage MALTESE = new LocaleLanguage("Maltese", "mt", "mlt");

    public static final LocaleLanguage MARATHI = new LocaleLanguage("Marathi", "mr", "mar");

    public static final LocaleLanguage MOLDAVIAN = new LocaleLanguage("Moldavian", "ro", "rum");

    public static final LocaleLanguage MONTENEGRIN = new LocaleLanguage("Montenegrin", "mn", "mne");

    public static final LocaleLanguage NORWEGIAN = new LocaleLanguage("Norwegian", "no", "nor");

    public static final LocaleLanguage OROMO = new LocaleLanguage("Oromo", "om", "orm");

    public static final LocaleLanguage PERSIAN = new LocaleLanguage("Persian", "fa", "fas");

    public static final LocaleLanguage POLISH = new LocaleLanguage("Polish", "pl", "pol");

    public static final LocaleLanguage PORTUGUESE = new LocaleLanguage("Portuguese", "pt", "por");

    public static final LocaleLanguage PUNJABI = new LocaleLanguage("Punjabi", "pa", "pan");

    public static final LocaleLanguage QUECHUA = new LocaleLanguage("Quechua", "qu", "que");

    public static final LocaleLanguage ROMANIAN = new LocaleLanguage("Russian", "ro", "rum");

    public static final LocaleLanguage RUSSIAN = new LocaleLanguage("Russian", "ru", "rus");

    public static final LocaleLanguage SERBIAN = new LocaleLanguage("Serbian", "sr", "srp");

    public static final LocaleLanguage SHONA = new LocaleLanguage("Shona", "sn", "sna");

    public static final LocaleLanguage SLOVAK = new LocaleLanguage("Slovak", "sk", "slo");

    public static final LocaleLanguage SLOVENIAN = new LocaleLanguage("Serbian", "sl", "slv");

    public static final LocaleLanguage SPANISH = new LocaleLanguage("Spanish", "es", "spa");

    public static final LocaleLanguage SWAHILI = new LocaleLanguage("Swahili", "sw", "swa");

    public static final LocaleLanguage SWEDISH = new LocaleLanguage("Swedish", "sv", "swe");

    public static final LocaleLanguage SYRIAN = new LocaleLanguage("Syrian", "??", "aii");

    public static final LocaleLanguage TAMIL = new LocaleLanguage("Tamil", "ta", "tam");

    public static final LocaleLanguage TELUGU = new LocaleLanguage("Telugu", "te", "tel");

    public static final LocaleLanguage THAI = new LocaleLanguage("Thai", "th", "tha");

    public static final LocaleLanguage TURKISH = new LocaleLanguage("Turkish", "tr", "tur");

    public static final LocaleLanguage UKRAINIAN = new LocaleLanguage("Ukrainian", "uk", "ukr");

    public static final LocaleLanguage URDU = new LocaleLanguage("Urdu", "ur", "urd");

    public static final LocaleLanguage VIETNAMESE = new LocaleLanguage("Vietnamese", "vi", "vie");

    public static final LocaleLanguage WELSH = new LocaleLanguage("Welsh", "cy", "wel");

    public static final LocaleLanguage YORUBA = new LocaleLanguage("Yoruba", "yo", "yor");

    public static final LocaleLanguage ZULU = new LocaleLanguage("Zulu", "zu", "zul");

    static
    {
        for (var locale : java.util.Locale.getAvailableLocales())
        {
            var iso2Code = locale.getLanguage();
            var iso3Code = locale.getISO3Language();
            if (iso2Code.length() == 2 && iso3Code.length() == 3)
            {
                new LocaleLanguage(locale.getDisplayLanguage(), iso2Code, iso3Code);
            }
        }
    }

    /**
     * Returns a list of all languages
     */
    public static ObjectList<LocaleLanguage> allLanguages()
    {
        return objectList(forIso2Code.values()).sorted();
    }

    /**
     * @return The language for a two character ISO-639-1 code
     */
    public static LocaleLanguage languageForIso2Code(String code)
    {
        return forIso2Code.get(code.toLowerCase());
    }

    /**
     * @return The language for a three character ISO-639-2/T language code
     */
    public static LocaleLanguage languageForIso3Code(String code)
    {
        return forIso3Code.get(code.toLowerCase());
    }

    /** The ISO 2-letter code */
    private final String iso2Code;

    /** The ISO 3-letter code */
    private final String iso3Code;

    private LocaleLanguage(String languageName, String iso2Code, String iso3Code)
    {
        super(languageName);

        ensure(iso2Code.length() == 2, "ISO 2 code for $ must be two characters", languageName);
        ensure(iso3Code.length() == 3, "ISO 3 code for $ must be three characters", languageName);

        this.iso2Code = iso2Code.toLowerCase();
        this.iso3Code = iso3Code.toLowerCase();

        forIso2Code.put(this.iso2Code, this);
        forIso3Code.put(this.iso3Code, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(LocaleLanguage that)
    {
        return name().compareTo(that.name());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof LocaleLanguage)
        {
            var that = (LocaleLanguage) object;
            return iso2Code.equals(that.iso2Code);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return iso2Code.hashCode();
    }

    /**
     * Returns the ISO2 code for this language
     */
    public String iso2Code()
    {
        return iso2Code;
    }

    /**
     * Returns the ISO3 code for this language
     */
    public String iso3Code()
    {
        return iso3Code;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return name();
    }

    /**
     * Returns the world locale, with all languages spoken there
     */
    public Locale world()
    {
        return new Locale(WORLD, LocaleLanguage.allLanguages());
    }
}

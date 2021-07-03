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

package com.telenav.kivakit.kernel.language.locales;

import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.language.values.name.Name;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageLocale;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ISO-2 and ISO-3 codes for the most common languages in the world. There are, of course, vastly more than just these.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageLocale.class)
@LexakaiJavadoc(complete = true)
public class LanguageIsoCode extends Name implements Comparable<LanguageIsoCode>
{
    private static final Map<String, LanguageIsoCode> forIso2Code = new HashMap<>();

    private static final Map<String, LanguageIsoCode> forIso3Code = new HashMap<>();

    public static final LanguageIsoCode ALBANIAN = new LanguageIsoCode("Albanian", "sq", "sqi");

    public static final LanguageIsoCode AMHARIC = new LanguageIsoCode("Amharic", "am", "amh");

    public static final LanguageIsoCode ARABIC = new LanguageIsoCode("Arabic", "ar", "ara");

    public static final LanguageIsoCode AZERI = new LanguageIsoCode("Azeri", "az", "aze");

    public static final LanguageIsoCode BAHASA = new LanguageIsoCode("Bahasa", "id", "ind");

    public static final LanguageIsoCode BASQUE = new LanguageIsoCode("Basque", "eu", "baq");

    public static final LanguageIsoCode BELARUSIAN = new LanguageIsoCode("Belarusian", "be", "bel");

    public static final LanguageIsoCode BENGALI = new LanguageIsoCode("Bengali", "bn", "ben");

    public static final LanguageIsoCode BOSNIAN = new LanguageIsoCode("Bosnian", "bs", "bos");

    public static final LanguageIsoCode BULGARIAN = new LanguageIsoCode("Bosnian", "bg", "bul");

    public static final LanguageIsoCode BURMESE = new LanguageIsoCode("Bosnian", "bg", "bul");

    public static final LanguageIsoCode CATALAN = new LanguageIsoCode("Catalan", "ca", "cat");

    public static final LanguageIsoCode CHINESE = new LanguageIsoCode("Chinese", "zh", "zho");

    public static final LanguageIsoCode CHINESE_GAN = new LanguageIsoCode("Gan Chinese", "zh", "gan");

    public static final LanguageIsoCode CHINESE_HAKKA = new LanguageIsoCode("Hakka Chinese", "zh", "hak");

    public static final LanguageIsoCode CHINESE_MANDARIN = new LanguageIsoCode("Mandarin Chinese", "zh", "chi");

    public static final LanguageIsoCode CHINESE_MIN = new LanguageIsoCode("Min Chinese", "zh", "cdo");

    public static final LanguageIsoCode CHINESE_WU = new LanguageIsoCode("Wu Chinese", "zh", "wuu");

    public static final LanguageIsoCode CHINESE_XIANG = new LanguageIsoCode("Xiang Chinese", "zh", "hsn");

    public static final LanguageIsoCode CHINESE_YUE = new LanguageIsoCode("Yue Chinese", "zh", "yue");

    public static final LanguageIsoCode CROATIAN = new LanguageIsoCode("Croatian", "hr", "hrv");

    public static final LanguageIsoCode CZECH = new LanguageIsoCode("Czech", "cs", "ces");

    public static final LanguageIsoCode DANISH = new LanguageIsoCode("Danish", "da", "dan");

    public static final LanguageIsoCode DUTCH = new LanguageIsoCode("Dutch", "nl", "nld");

    public static final LanguageIsoCode ENGLISH = new LanguageIsoCode("English", "en", "eng");

    public static final LanguageIsoCode ESTONIAN = new LanguageIsoCode("Estonian", "et", "est");

    public static final LanguageIsoCode FILIPINO = new LanguageIsoCode("Filipino", "??", "fil");

    public static final LanguageIsoCode FINNISH = new LanguageIsoCode("Finnish", "fi", "fin");

    public static final LanguageIsoCode FRENCH = new LanguageIsoCode("French", "fr", "fra");

    public static final LanguageIsoCode GALICIAN = new LanguageIsoCode("Galician", "gl", "glg");

    public static final LanguageIsoCode GEORGIAN = new LanguageIsoCode("Georgian", "ka", "kat");

    public static final LanguageIsoCode GERMAN = new LanguageIsoCode("German", "de", "deu");

    public static final LanguageIsoCode GREEK = new LanguageIsoCode("Greek", "el", "ell");

    public static final LanguageIsoCode GUARANI = new LanguageIsoCode("Guarani", "gn", "grn");

    public static final LanguageIsoCode HAUSA = new LanguageIsoCode("Hausa", "ha", "hau");

    public static final LanguageIsoCode HEBREW = new LanguageIsoCode("Hebrew", "he", "heb");

    public static final LanguageIsoCode HINDI = new LanguageIsoCode("Hindi", "hi", "hin");

    public static final LanguageIsoCode HUNGARIAN = new LanguageIsoCode("Hungarian", "hu", "hun");

    public static final LanguageIsoCode ICELANDIC = new LanguageIsoCode("Icelandic", "is", "isl");

    public static final LanguageIsoCode IGBO = new LanguageIsoCode("Igbo", "ig", "ibo");

    public static final LanguageIsoCode INDONESIAN = new LanguageIsoCode("Indonesian", "id", "ind");

    public static final LanguageIsoCode IRISH = new LanguageIsoCode("Irish", "ga", "gle");

    public static final LanguageIsoCode ITALIAN = new LanguageIsoCode("Italian", "it", "ita");

    public static final LanguageIsoCode JAPANESE = new LanguageIsoCode("Japanese", "ja", "jpn");

    public static final LanguageIsoCode KAZAKH = new LanguageIsoCode("Kazakh", "kk", "kaz");

    public static final LanguageIsoCode KOREAN = new LanguageIsoCode("Korean", "ko", "kor");

    public static final LanguageIsoCode KURDISH = new LanguageIsoCode("Kurdish", "ku", "kur");

    public static final LanguageIsoCode LATVIAN = new LanguageIsoCode("Latvian", "lv", "lav");

    public static final LanguageIsoCode LITHUANIAN = new LanguageIsoCode("Lithuanian", "lt", "lit");

    public static final LanguageIsoCode MACEDONIAN = new LanguageIsoCode("Macedonian", "mk", "mkd");

    public static final LanguageIsoCode MALAY = new LanguageIsoCode("Malay", "ms", "msa");

    public static final LanguageIsoCode MALTESE = new LanguageIsoCode("Maltese", "mt", "mlt");

    public static final LanguageIsoCode MARATHI = new LanguageIsoCode("Marathi", "mr", "mar");

    public static final LanguageIsoCode MOLDAVIAN = new LanguageIsoCode("Moldavian", "ro", "rum");

    public static final LanguageIsoCode MONTENEGRIN = new LanguageIsoCode("Montenegrin", "mn", "mne");

    public static final LanguageIsoCode NORWEGIAN = new LanguageIsoCode("Norwegian", "no", "nor");

    public static final LanguageIsoCode OROMO = new LanguageIsoCode("Oromo", "om", "orm");

    public static final LanguageIsoCode PERSIAN = new LanguageIsoCode("Persian", "fa", "fas");

    public static final LanguageIsoCode POLISH = new LanguageIsoCode("Polish", "pl", "pol");

    public static final LanguageIsoCode PORTUGUESE = new LanguageIsoCode("Portuguese", "pt", "por");

    public static final LanguageIsoCode PUNJABI = new LanguageIsoCode("Punjabi", "pa", "pan");

    public static final LanguageIsoCode QUECHUA = new LanguageIsoCode("Quechua", "qu", "que");

    public static final LanguageIsoCode ROMANIAN = new LanguageIsoCode("Russian", "ro", "rum");

    public static final LanguageIsoCode RUSSIAN = new LanguageIsoCode("Russian", "ru", "rus");

    public static final LanguageIsoCode SERBIAN = new LanguageIsoCode("Serbian", "sr", "srp");

    public static final LanguageIsoCode SHONA = new LanguageIsoCode("Shona", "sn", "sna");

    public static final LanguageIsoCode SLOVAK = new LanguageIsoCode("Slovak", "sk", "slo");

    public static final LanguageIsoCode SLOVENIAN = new LanguageIsoCode("Serbian", "sl", "slv");

    public static final LanguageIsoCode SPANISH = new LanguageIsoCode("Spanish", "es", "spa");

    public static final LanguageIsoCode SWAHILI = new LanguageIsoCode("Swahili", "sw", "swa");

    public static final LanguageIsoCode SWEDISH = new LanguageIsoCode("Swedish", "sv", "swe");

    public static final LanguageIsoCode SYRIAN = new LanguageIsoCode("Syrian", "??", "aii");

    public static final LanguageIsoCode TAMIL = new LanguageIsoCode("Tamil", "ta", "tam");

    public static final LanguageIsoCode TELUGU = new LanguageIsoCode("Telugu", "te", "tel");

    public static final LanguageIsoCode THAI = new LanguageIsoCode("Thai", "th", "tha");

    public static final LanguageIsoCode TURKISH = new LanguageIsoCode("Turkish", "tr", "tur");

    public static final LanguageIsoCode UKRAINIAN = new LanguageIsoCode("Ukrainian", "uk", "ukr");

    public static final LanguageIsoCode URDU = new LanguageIsoCode("Urdu", "ur", "urd");

    public static final LanguageIsoCode VIETNAMESE = new LanguageIsoCode("Vietnamese", "vi", "vie");

    public static final LanguageIsoCode WELSH = new LanguageIsoCode("Welsh", "cy", "wel");

    public static final LanguageIsoCode YORUBA = new LanguageIsoCode("Yoruba", "yo", "yor");

    public static final LanguageIsoCode ZULU = new LanguageIsoCode("Zulu", "zu", "zul");

    static
    {
        for (final var locale : java.util.Locale.getAvailableLocales())
        {
            final var iso2Code = locale.getLanguage();
            final var iso3Code = locale.getISO3Language();
            if (iso2Code.length() == 2 && iso3Code.length() == 3)
            {
                new LanguageIsoCode(locale.getDisplayLanguage(), iso2Code, iso3Code);
            }
        }
    }

    public static List<LanguageIsoCode> all()
    {
        final List<LanguageIsoCode> all = new ArrayList<>(forIso2Code.values());
        Collections.sort(all);
        return all;
    }

    /**
     * @return The language for a two character ISO-639-1 code
     */
    public static LanguageIsoCode forIso2Code(final String code)
    {
        return forIso2Code.get(code.toLowerCase());
    }

    /**
     * @return The language for a three character ISO-639-2/T language code
     */
    public static LanguageIsoCode forIso3Code(final String code)
    {
        return forIso3Code.get(code.toLowerCase());
    }

    /**
     * Converts to and from a {@link LanguageIsoCode}. Both ISO2 and ISO3 values are supported.
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<LanguageIsoCode>
    {
        public Converter(final Listener listener)
        {
            super(listener);
        }

        @Override
        protected LanguageIsoCode onToValue(final String value)
        {
            return value.length() == 2 ? forIso2Code(value) : forIso3Code(value);
        }

        @Override
        protected String onToString(final LanguageIsoCode value)
        {
            return value.iso2Code();
        }
    }

    private final String iso2Code;

    private final String iso3Code;

    private LanguageIsoCode(final String name, final String iso2Code, final String iso3Code)
    {
        super(name);

        Ensure.ensure(iso2Code.length() == 2, "ISO 2 code for $ must be two characters", name);
        Ensure.ensure(iso3Code.length() == 3, "ISO 3 code for $ must be three characters", name);

        this.iso2Code = iso2Code.toLowerCase();
        this.iso3Code = iso3Code.toLowerCase();

        forIso2Code.put(this.iso2Code, this);
        forIso3Code.put(this.iso3Code, this);
    }

    @Override
    public int compareTo(final LanguageIsoCode that)
    {
        return name().compareTo(that.name());
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof LanguageIsoCode)
        {
            final var that = (LanguageIsoCode) object;
            return iso2Code.equals(that.iso2Code);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return iso2Code.hashCode();
    }

    public String iso2Code()
    {
        return iso2Code;
    }

    public String iso3Code()
    {
        return iso3Code;
    }

    @Override
    public String toString()
    {
        return name();
    }

    public Locale world()
    {
        return new Locale(this, null);
    }
}

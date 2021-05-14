////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.kernel.language.locales;

import com.telenav.kivakit.kernel.language.objects.Objects;
import com.telenav.kivakit.kernel.language.paths.StringPath;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageLocale;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

/**
 * A locale constructed from a {@link LanguageIsoCode} and an optional {@link CountryIsoCode}. Provides a unique path to
 * localized resources for this locale with {@link #path()}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageLocale.class)
public class Locale
{
    /** The language spoken in this locale */
    @UmlAggregation
    private final LanguageIsoCode language;

    /** The country for the locale, which might increase specificity, for example, US and Australian English. */
    @UmlAggregation
    private CountryIsoCode country;

    /**
     * @param language The language spoken in this locale
     * @param country The country
     */
    public Locale(final LanguageIsoCode language, final CountryIsoCode country)
    {
        this.language = language;
        this.country = country;
    }

    /**
     * @param language The language for this locale throughout the world, for example, World English
     */
    public Locale(final LanguageIsoCode language)
    {
        this.language = language;
    }

    /**
     * @return A Java {@link java.util.Locale} for this KivaKit {@link Locale}
     */
    public java.util.Locale asJavaLocale()
    {
        return country == null
                ? new java.util.Locale(language.iso3Code())
                : new java.util.Locale(language.iso3Code(), country.alpha2Code());
    }

    /**
     * @return Any country for this locale, or null if the locale is relevant to the whole world
     */
    public CountryIsoCode country()
    {
        return country;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Locale)
        {
            final var that = (Locale) object;
            return Objects.equal(language, that.language);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return language().hashCode();
    }

    /**
     * @return True if this is a world locale
     */
    public boolean isWorld()
    {
        return country == null;
    }

    /**
     * @return The language for this locale
     */
    public LanguageIsoCode language()
    {
        return language;
    }

    /**
     * Produces a path of the form "locales/[language-name](/[country-name])?. This path can be used when loading
     * localizable resources.
     *
     * @return A relative path of the given type for this locale
     */
    public StringPath path()
    {
        return country == null
                ? StringPath.stringPath("locales", language.name())
                : StringPath.stringPath("locales", language.name(), country.name());
    }

    @Override
    public String toString()
    {
        return language().name() + (country == null ? "" : "." + country.name());
    }
}

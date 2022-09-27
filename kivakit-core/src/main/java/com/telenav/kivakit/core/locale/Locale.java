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

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.internal.lexakai.DiagramLocale;
import com.telenav.kivakit.core.language.Objects;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.string.StringTo;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.util.Collection;

import static com.telenav.kivakit.core.collections.list.ObjectList.objectList;

/**
 * A locale constructed from a {@link LocaleLanguage} and an optional {@link LocaleCountry}. Provides a unique path to
 * localized resources for this locale with {@link #path(String)}.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramLocale.class)
public class Locale
{
    /** The country for the locale, which might increase specificity, for example, US and Australian English. */
    @UmlAggregation
    private final LocaleCountry country;

    /** ISO codes for the languages spoken in this locale */
    @UmlAggregation
    private final ObjectList<LocaleLanguage> languages;

    /**
     * @param country The country
     * @param languages The languages spoken in this locale
     */
    public Locale(LocaleCountry country, Collection<LocaleLanguage> languages)
    {
        this.country = country;
        this.languages = objectList(languages);
    }

    /**
     * @param languages The language for this locale throughout the world, for example, World English
     */
    public Locale(Collection<LocaleLanguage> languages)
    {
        this.country = null;
        this.languages = objectList(languages);
    }

    /**
     * @param name The name of the given language within this locale
     * @return A Java {@link java.util.Locale} for this KivaKit {@link Locale}
     */
    public java.util.Locale asJavaLocale(String name)
    {
        return country == null
                ? new java.util.Locale(language(name).iso3Code())
                : new java.util.Locale(language(name).iso3Code(), country.alpha2Code());
    }

    /**
     * @return Any country for this locale, or null if the locale is relevant to the whole world
     */
    public LocaleCountry country()
    {
        return country;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Locale)
        {
            var that = (Locale) object;
            return Objects.isEqual(languages, that.languages);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return languages().hashCode();
    }

    /**
     * @return True if this is the world locale
     */
    public boolean isWorld()
    {
        return country == null;
    }

    /**
     * The language ISO codes for the given language
     *
     * @param name The name of the language
     * @return The ISO codes
     */
    public LocaleLanguage language(String name)
    {
        for (var language : languages())
        {
            if (language.name().equalsIgnoreCase(name))
            {
                return language;
            }
        }
        return null;
    }

    /**
     * @return The languages spoken in this locale
     */
    public ObjectList<LocaleLanguage> languages()
    {
        return languages;
    }

    /**
     * Produces a path of the form "locales/[language-name](/[country-name])?. This path can be used when loading
     * localized resources.
     *
     * @param name The name of the language
     * @return A relative path of the given type for this locale
     */
    public StringPath path(String name)
    {
        return country == null
                ? StringPath.stringPath("locales", name)
                : StringPath.stringPath("locales", name, country.name());
    }

    @Override
    public String toString()
    {
        return "[country = " + StringTo.nonNullString(country.name()) + ": " + languages().join(", ") + "]";
    }
}

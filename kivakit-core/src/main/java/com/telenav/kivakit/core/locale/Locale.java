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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.internal.lexakai.DiagramLocale;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.language.Objects.isEqual;
import static com.telenav.kivakit.core.path.StringPath.stringPath;
import static com.telenav.kivakit.core.string.StringConversions.toNonNullString;

/**
 * A locale constructed from a {@link LocaleLanguage} and an optional {@link LocaleRegion}. Provides a unique path to
 * localized resources for this locale with {@link #path(LocaleLanguage)}.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramLocale.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class Locale
{
    /** The region for the locale, which might increase specificity, for example, US and Australian English. */
    @UmlAggregation
    private final LocaleRegion region;

    /** ISO codes for the languages spoken in this locale */
    @UmlAggregation
    private final ObjectList<LocaleLanguage> languages;

    /**
     * Constructs a locale for a given region with a collection of languages. The first language in the collection is
     * considered the "primary" language for the locale
     *
     * @param region The region
     * @param languages The languages spoken in this locale
     */
    public Locale(LocaleRegion region, @NotNull Collection<LocaleLanguage> languages)
    {
        this.region = region;
        this.languages = list(languages);
    }

    /**
     * @param name The name of the given language within this locale
     * @return A Java {@link java.util.Locale} for this KivaKit {@link Locale}
     */
    public java.util.Locale asJavaLocale(String name)
    {
        return region == null
                ? new java.util.Locale(language(name).iso3Code())
                : new java.util.Locale(language(name).iso3Code(), region.alpha2Code());
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
            return isEqual(languages, that.languages);
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
     * Returns true if this is the world locale
     */
    public boolean isWorld()
    {
        return region == LocaleRegion.WORLD;
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
     * Returns the languages spoken in this locale
     */
    public ObjectList<LocaleLanguage> languages()
    {
        return languages;
    }

    /**
     * Produces a path of the form "locales/[language-name](/[region-name])?. This path can be used when loading
     * localized resources.
     *
     * @param language The name of the language
     * @return A relative path of the given type for this locale
     */
    public StringPath path(LocaleLanguage language)
    {
        return region == null
                ? stringPath("locales", language.name())
                : stringPath("locales", language.name(), region.name());
    }

    /**
     * Returns the primary language for this locale
     */
    public LocaleLanguage primaryLanguage()
    {
        return languages.get(0);
    }

    /**
     * Returns any region for this locale, or null if the locale is relevant to the whole world
     */
    public LocaleRegion region()
    {
        return region;
    }

    @Override
    public String toString()
    {
        return "[region = " + toNonNullString(region.name()) + ": " + languages().join(", ") + "]";
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.reflection.property.filters;

import com.telenav.kivakit.core.kernel.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Set;

/**
 * A filter using {@link BasePropertyFilter.NamingConvention#KivaKit}
 */
@UmlClassDiagram(diagram = DiagramLanguageReflection.class)
public class KivaKitProperties extends BasePropertyFilter
{
    public static PropertyFilter ALL_PROPERTIES_AND_FIELDS = new KivaKitProperties(Set.of(
            Include.PUBLIC_PROPERTIES,
            Include.NON_PUBLIC_PROPERTIES,
            Include.TRANSIENT_FIELDS,
            Include.NON_TRANSIENT_FIELDS));

    public static PropertyFilter PUBLIC_PROPERTIES = new KivaKitProperties(Set.of(Include.PUBLIC_PROPERTIES));

    public static PropertyFilter ALL_PROPERTIES = new KivaKitProperties(Set.of(Include.PUBLIC_PROPERTIES, Include.NON_PUBLIC_PROPERTIES));

    public static PropertyFilter ALL_PROPERTIES_AND_NON_TRANSIENT_FIELDS = new KivaKitProperties(Set.of(Include.PUBLIC_PROPERTIES, Include.NON_PUBLIC_PROPERTIES, Include.NON_TRANSIENT_FIELDS));

    public static PropertyFilter INCLUDED_PROPERTIES = new KivaKitProperties(Set.of(Include.KIVAKIT_INCLUDED_PROPERTIES));

    public static final PropertyFilter INCLUDED_PROPERTIES_AND_FIELDS = new KivaKitProperties(Set.of(Include.KIVAKIT_INCLUDED_PROPERTIES, Include.KIVAKIT_INCLUDED_FIELDS));

    /**
     * @param included Set of fields and properties to include
     */
    public KivaKitProperties(final Set<Include> included)
    {
        super(NamingConvention.KivaKit, included);
    }
}

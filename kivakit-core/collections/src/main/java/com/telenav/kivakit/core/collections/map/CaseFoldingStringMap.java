////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.map;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramMap;
import com.telenav.kivakit.core.kernel.language.collections.map.string.StringMap;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

/**
 * A {@link StringMap} where values that are added with {@link #put(Object, Object)} are automatically converted to
 * lowercase and can be retrieved with {@link #get(Object)} in a case-independent manner.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMap.class)
public class CaseFoldingStringMap<Element> extends StringMap<Element>
{
    public CaseFoldingStringMap(final Maximum maximumSize)
    {
        super(maximumSize);
    }

    @Override
    public Element get(final Object key)
    {
        if (key == null)
        {
            return null;
        }
        if (key instanceof String)
        {
            return super.get(key.toString().toLowerCase());
        }
        return fail("Key must be a String");
    }

    @Override
    public Element put(final String key, final Element value)
    {
        return super.put(key.toLowerCase(), value);
    }
}

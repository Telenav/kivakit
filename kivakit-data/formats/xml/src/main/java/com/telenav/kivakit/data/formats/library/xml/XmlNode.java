////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.formats.library.xml;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.core.kernel.language.collections.map.string.StringToStringMap;
import com.telenav.kivakit.core.kernel.language.reflection.Type;
import com.telenav.kivakit.core.kernel.language.reflection.populator.ObjectPopulator;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitProperties;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.data.formats.library.xml.project.lexakai.diagrams.DiagramXml;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.List;

@UmlClassDiagram(diagram = DiagramXml.class)
public class XmlNode
{
    private final StringToStringMap attributes = new StringToStringMap(Maximum._1_000);

    private final List<XmlNode> children = new ArrayList<>();

    private final String name;

    private final XmlReader stream;

    private String text;

    private boolean initialized;

    private boolean openTag;

    private final int position;

    XmlNode(final XmlReader stream, final String name)
    {
        this.stream = stream;
        this.name = name;
        position = stream.position();
    }

    public <T> T asObject(final Class<T> type, final XmlPath... paths)
    {
        try
        {
            return new ObjectPopulator(stream, KivaKitProperties.INCLUDED_PROPERTIES_AND_FIELDS, search(paths)).populate(Type.forClass(type).newInstance());
        }
        catch (final Exception e)
        {
            stream.problem(e, "Unable to populate ${debug}", type);
            e.printStackTrace();
            return null;
        }
    }

    public String attribute(final String name)
    {
        return attributes().get(name);
    }

    @KivaKitIncludeProperty
    public StringToStringMap attributes()
    {
        return attributes;
    }

    public XmlNode child(final String name)
    {
        for (final var child : children())
        {
            if (child.name().equals(name))
            {
                return child;
            }
        }
        return null;
    }

    public List<XmlNode> children(final Matcher<XmlNode> matcher)
    {
        final List<XmlNode> children = new ArrayList<>();
        for (final var child : children())
        {
            if (matcher.matches(child))
            {
                children.add(child);
            }
        }
        return children;
    }

    @KivaKitIncludeProperty
    public List<XmlNode> children()
    {
        initialize();
        return children;
    }

    public List<XmlNode> childrenNamed(final String name)
    {
        return children(value -> value.name().equals(name));
    }

    @KivaKitIncludeProperty
    public boolean isOpenTag()
    {
        return openTag;
    }

    @KivaKitIncludeProperty
    public String name()
    {
        return name;
    }

    public String text()
    {
        initialize();
        return text;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    void addChild(final XmlNode node)
    {
        node.initialized = true;
        children.add(node);
    }

    void addText(final String text)
    {
        if (this.text == null)
        {
            this.text = text;
        }
        else
        {
            this.text += text;
        }
    }

    void setOpenTag(final boolean openTag)
    {
        this.openTag = openTag;
    }

    private void initialize()
    {
        if (!initialized)
        {
            initialized = true;

            // We can only read in the children of this XmlNode if the stream has not been advanced
            if (position == stream.position())
            {
                stream.readChildren(this);
            }
            else
            {
                throw new IllegalStateException(
                        "Cannot lazy-read children of an XmlNode once the stream has been advanced");
            }
        }
    }

    /**
     * Searches for a set of {@link XmlPath}s in this XmlNode.
     *
     * @param paths A list of paths to extract
     * @return A map from {@link XmlPath#name()} to the value for the given path
     */
    private StringToStringMap search(final XmlPath[] paths)
    {
        final var map = new StringToStringMap(Maximum.maximum(paths));
        for (final var path : paths)
        {
            try
            {
                map.put(path.name(), path.extract(this));
            }
            catch (final Exception ignored)
            {
            }
        }
        return map;
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.formats.library.xml;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.core.kernel.language.io.IO;
import com.telenav.kivakit.core.kernel.language.iteration.BaseIterable;
import com.telenav.kivakit.core.kernel.language.iteration.Iterables;
import com.telenav.kivakit.core.kernel.language.iteration.Next;
import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.reading.BaseObjectReader;
import com.telenav.kivakit.data.formats.library.xml.project.lexakai.diagrams.DiagramXml;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.Reader;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

/**
 * Acts as an iterator of {@link XmlNode} objects as an XML input stream is read. The stream can only be read one time
 * (it cannot be rewound), however there is convenient functionality for randomly accessing the children of an {@link
 * XmlNode} as an in-memory mini-DOM. The way this works is both lazy and fairly transparent:
 * <ol>
 *     <li>All {@link XmlNode}s are initially read as shallow objects (one node object only)</li>
 *     <li>If a given node's text or children are accessed and the stream has not yet been advanced, the node
 *       will be populated with (possibly nested levels of) child nodes.</li>
 *     <li>If a nodes children have not been accessed and the stream moves forward, those children can no longer
 *     be accessed (because there is no way to rewind and re-read the stream). Any attempt to access children of an
 *     un-populated node once the stream has moved forward will result in a run-time exception.</li>
 * </ol>
 * <p>
 * The {@link #advanceTo(String)} and {@link #advanceTo(Matcher)} methods can be used to advance the stream to a
 * matching node. The node can then be read with {@link #current()}. The current node can be conveniently matched using
 * {@link #lookingAt(String)} or {@link #lookingAt(Matcher)}. The {@link #childrenMatching(Matcher)}, {@link
 * #nodesMatching(Matcher)} and {@link #siblingsMatching(Matcher)} methods can be used to iterate through several XML
 * nodes in the input stream.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramXml.class)
@UmlRelation(label = "reads", referent = XmlNode.class)
public class XmlReader extends BaseObjectReader<XmlNode>
{
    /** The current node */
    private XmlNode current;

    /** The underlying XML stream reader */
    private XMLStreamReader xmlStreamReader;

    /** The underlying input reader, which must be closed separately from the XML stream reader */
    private final Reader reader;

    /** The position in the input stream during the last call onFindNext() */
    private int lastFindNextPosition = -1;

    /**
     * Construct from a resource
     */
    public XmlReader(final Resource resource)
    {
        // Save reader to close later
        reader = resource.reader().textReader();

        // Create XML reader
        final var factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.TRUE);
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.TRUE);
        factory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        try
        {
            xmlStreamReader = factory.createXMLStreamReader(reader);
        }
        catch (final XMLStreamException e)
        {
            fail(e, "Cannot create XML stream reader");
        }

        // Load up the first XML node
        current();
    }

    public void advance()
    {
        try
        {
            xmlStreamReader.next();
            current = null;
        }
        catch (final XMLStreamException e)
        {
            fail(e, "Unable to advance stream");
        }
    }

    /**
     * Advances to the next node matching the given matcher. If the current node matches the matcher, the input is not
     * advanced at all.
     *
     * @param matcher The matcher
     * @return The matching current node or null if out of input
     */
    public XmlNode advanceTo(final Matcher<XmlNode> matcher)
    {
        return advanceToButNotBeyond(matcher, null);
    }

    /**
     * Advances to the next open tag with the given name. If the current node matches the named open tag, the input is
     * not advanced at all.
     *
     * @param name The open tag name
     * @return The matching current node or null if out of input
     */
    @SuppressWarnings("UnusedReturnValue")
    public XmlNode advanceTo(final String name)
    {
        return advanceToButNotBeyond(new OpenTag(name), null);
    }

    /**
     * Advances to the next open tage with the given name, but not beyond the specified name. If the current node
     * matches the named open tage the input is not advanced at all.
     *
     * @param searchName The matcher to search for
     * @param stopMatcher The matcher to end the search. Null can be passed in to allow advancing to the end of the
     * stream.
     * @return The matching current node or null if out of input or the stop tag has been encountered
     */
    public XmlNode advanceToButNotBeyond(final Matcher<XmlNode> searchName, final Matcher<XmlNode> stopMatcher)
    {
        while (advanceToOpenTag())
        {
            final var current = current();
            if (stopMatcher != null && stopMatcher.matches(current))
            {
                return null;
            }
            if (searchName.matches(current))
            {
                return current;
            }
            advance();
        }
        return null;
    }

    /**
     * Advances to the next open tag with the given name. If the current node matches the named open tag, the input is
     * not advanced at all.
     *
     * @param searchName The open tag name to search
     * @param stopName The name to trigger a stop search
     * @return The matching current node or null if out of input
     */
    public XmlNode advanceToButNotBeyond(final String searchName, final String stopName)
    {
        return advanceToButNotBeyond(new OpenTag(searchName), stopName == null ? null : new OpenTag(stopName));
    }

    /**
     * Returns a one-shot {@link Iterable} of child nodes matching the given matcher.
     *
     * @param matcher The matcher
     * @return The child nodes, which may be iterated only once.
     */
    public Iterable<XmlNode> childrenMatching(final Matcher<XmlNode> matcher)
    {
        final var current = current();
        return new BaseIterable<>()
        {
            @Override
            protected Next<XmlNode> newNext()
            {
                return () ->
                {
                    // If no children due to open/close node
                    if (atCloseElementFor(current))
                    {
                        return null;
                    }

                    // Skip current (either parent or last child)
                    advance();

                    // If no children
                    if (atCloseElementFor(current))
                    {
                        return null;
                    }

                    // Move to next matching node
                    return advanceToButNotBeyond(matcher, new OpenTag(current.name()));
                };
            }
        };
    }

    public Iterable<XmlNode> childrenMatching(final String name)
    {
        return childrenMatching(new OpenTag(name));
    }

    /**
     * Closes the XML stream reader as well as the underlying input stream
     */
    @Override
    public void close()
    {
        try
        {
            xmlStreamReader.close();
        }
        catch (final XMLStreamException ignored)
        {
        }
        IO.close(reader);
    }

    /**
     * @return The current {@link XmlNode} in the xml input stream
     */
    public XmlNode current()
    {
        if (current == null)
        {
            // If we're at the start of input
            if (atStartOfInput())
            {
                // force the first node to load
                advance();
            }

            if (xmlStreamReader.getEventType() == XMLStreamConstants.END_DOCUMENT)
            {
                return fail("No XML node found");
            }

            // Ensure that we are on an open tag
            if (!atOpenTag())
            {
                advanceToOpenTag();
                if (!atOpenTag())
                {
                    return null;
                }
            }

            final var qualifiedName = getQualifiedName();
            current = new XmlNode(this, qualifiedName);
            current.setOpenTag(xmlStreamReader.isStartElement());

            // Read any attributes of the node
            for (var i = 0; i < xmlStreamReader.getAttributeCount(); i++)
            {
                final var attributeName = xmlStreamReader.getAttributeLocalName(i);
                final var attributePrefix = xmlStreamReader.getAttributePrefix(i);
                final var qualifiedAttributeName = attributePrefix == null || attributePrefix.isEmpty() ? attributeName
                        : attributePrefix + ":" + attributeName;
                current.attributes().put(qualifiedAttributeName, xmlStreamReader.getAttributeValue(i));
            }
        }
        return current;
    }

    public XmlNode fastAdvanceTo(final String search)
    {
        return fastAdvanceToButNotBeyond(search, null);
    }

    public XmlNode fastAdvanceToButNotBeyond(final String regularExpression, final String stop)
    {
        while (advanceToOpenTag())
        {
            final var qualifiedName = getQualifiedName();
            if (qualifiedName.matches(regularExpression))
            {
                return current();
            }
            if (qualifiedName.equals(stop))
            {
                break;
            }
            advance();
        }
        return null;
    }

    public String fastGetCurrentText()
    {
        return xmlStreamReader.getText();
    }

    /**
     * Returns true if the current node matches the given matcher
     *
     * @param matcher The matcher
     * @return True if the current node matches
     */
    public boolean lookingAt(final Matcher<XmlNode> matcher)
    {
        return matcher.matches(current());
    }

    /**
     * Returns true if the current node has the given name
     *
     * @param name The expected node name
     * @return True if we are looking at the expected node
     */
    public boolean lookingAt(final String name)
    {
        return current().name().equals(name);
    }

    /**
     * Returns a one-shot {@link Iterable} of nodes matching the given matcher
     *
     * @param matcher The matcher
     * @return The matching nodes, which may only be iterated once.
     */
    public Iterable<XmlNode> nodesMatching(final Matcher<XmlNode> matcher)
    {
        return Iterables.iterable(() -> new Next<>()
        {
            int lastPosition;

            @Override
            public XmlNode onNext()
            {
                try
                {
                    if (lastPosition == position())
                    {
                        advance();
                    }
                    return advanceTo(matcher);
                }
                finally
                {
                    lastPosition = position();
                }
            }
        });
    }

    public Iterable<XmlNode> nodesMatching(final String name)
    {
        return nodesMatching(new OpenTag(name));
    }

    public int position()
    {
        return xmlStreamReader.getLocation().getCharacterOffset();
    }

    /**
     * Returns a one-shot {@link Iterable} of sibling nodes matching the given matcher
     *
     * @param matcher The matcher
     * @return The matching nodes, which may only be iterated once.
     */
    public Iterable<XmlNode> siblingsMatching(final Matcher<XmlNode> matcher)
    {
        return siblingsMatching(matcher, value -> false);
    }

    /**
     * Returns a one-shot {@link Iterable} of sibling nodes matching the given matcher until the stopAt matcher is
     * matched
     *
     * @param matcher The matcher
     * @param stopAt The stop matcher
     * @return The matching nodes, which may only be iterated once.
     */
    public Iterable<XmlNode> siblingsMatching(final Matcher<XmlNode> matcher, final Matcher<XmlNode> stopAt)
    {
        return new BaseIterable<>()
        {

            @Override
            protected Next<XmlNode> newNext()
            {
                return () ->
                {
                    if (current() == null || stopAt.matches(current()))
                    {
                        return null;
                    }
                    return advanceTo(matcher);
                };
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final XmlNode onNext()
    {
        try
        {
            advanceToOpenTag();
            if (atOpenTag())
            {
                if (lastFindNextPosition == position())
                {
                    advance();
                }
                return current();
            }
            return null;
        }
        finally
        {
            lastFindNextPosition = position();
        }
    }

    boolean atCloseElementFor(final XmlNode node)
    {
        return atCloseTag() && xmlStreamReader.getName().getLocalPart().equals(node.name());
    }

    /**
     * Reads children of the current node
     */
    void readChildren(final XmlNode parent)
    {
        while (canAdvance() && !atCloseElementFor(parent))
        {
            advance();

            // Add any free text to the node
            if (xmlStreamReader.isCharacters() && xmlStreamReader.hasText())
            {
                parent.addText(xmlStreamReader.getText());
            }

            if (xmlStreamReader.getEventType() == XMLStreamConstants.CDATA)
            {
                parent.addText(xmlStreamReader.getText());
            }

            if (atOpenTag())
            {
                final var current = current();
                parent.addChild(current);
                readChildren(current);
            }
        }
    }

    /**
     * If the current tag is not already an open tag, moves to the next open tag.
     *
     * @return True if an open tag was found. False if no open tag was found in the remaining input.
     */
    private boolean advanceToOpenTag()
    {
        while (canAdvance())
        {
            if (atOpenTag())
            {
                return true;
            }
            advance();
        }
        return false;
    }

    private boolean atCloseTag()
    {
        return xmlStreamReader.isEndElement();
    }

    private boolean atOpenTag()
    {
        return xmlStreamReader.isStartElement();
    }

    private boolean atStartOfInput()
    {
        return position() <= 0;
    }

    private boolean canAdvance()
    {
        try
        {
            return xmlStreamReader.hasNext();
        }
        catch (final XMLStreamException e)
        {
            fail(e, "Cannot advance");
            return false;
        }
    }

    private String getQualifiedName()
    {
        final var name = xmlStreamReader.getName().getLocalPart();
        final var prefix = xmlStreamReader.getPrefix();
        return prefix == null || prefix.isEmpty() ? name : prefix + ":" + name;
    }
}

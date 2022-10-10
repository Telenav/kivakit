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

package com.telenav.kivakit.core.path;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.internal.lexakai.DiagramPath;
import com.telenav.kivakit.core.language.Hash;
import com.telenav.kivakit.core.language.Objects;
import com.telenav.kivakit.core.language.Streams;
import com.telenav.kivakit.interfaces.collection.Sized;
import com.telenav.kivakit.interfaces.string.StringFormattable;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

/**
 * Abstraction of an immutable path of elements of a given type with an optional root element. Functional methods in
 * Path work on subtypes of Path because subtypes implement {@link #copy()} to return the correct subtype.
 * <p>
 * <b>Kinds of Paths</b>
 * <ul>
 *     <li>"" - The empty path (with no elements and no root)</li>
 *     <li>"/" - The root path (UNIX)</li>
 *     <li>"C:\" - A root path (Windows)</li>
 *     <li>"D:\" - A root path (Windows)</li>
 *     <li>"/a/b/c" - Absolute path</li>
 *     <li>"a/b/c" - Relative path</li>
 * </ul>
 *
 * <p><b>Relative and Absolute Paths</b></p>
 *
 * <p>Paths that have a {@link #root()} element are absolute and {@link #isAbsolute()} will return true.
 * Any path without a root is a relative path and {@link #isRelative()} will return true. A root path
 * has a root element but no path elements and {@link #isRoot()} will return true. The empty path has
 * neither a root nor any elements and {@link #isEmpty()} will return true.</p>
 *
 * <p><b>Adding Elements</b></p>
 *
 * <ul>
 *     <li>{@link #push(Comparable)}</li>
 * </ul>
 *
 * <p><b>Element Retrieval</b></p>
 *
 * <ul>
 *     <li>{@link #elements()} - The elements in this path</li>
 *     <li>{@link #first()} - The first element in the path or null if there is none</li>
 *     <li>{@link #first(int)} - The first n elements in the path or null if there are fewer elements</li>
 *     <li>{@link #get(int)} - The nth element in the path or null if there is no nth element</li>
 *     <li>{@link #iterator()} - The elements in this path</li>
 *     <li>{@link #last()} - The last element in the path or null if there is none</li>
 *     <li>{@link #last(int)} - The last n elements in the path or null if there are fewer elements</li>
 *     <li>{@link #pop()}</li>
 *     <li>{@link #rootElement()} - The root element, if any</li>
 *     <li>{@link #stream()} - A stream of the elements in this path (not including the root)</li>
 * </ul>
 *
 * <p><b>Parenting</b></p>
 *
 * <ul>
 *     <li>{@link #parent()}</li>
 *     <li>{@link #root()}</li>
 *     <li>{@link #rootElement()}</li>
 * </ul>
 *
 *
 * <p><b>Conversion</b></p>
 *
 * <ul>
 *     <li>{@link #asString(Format)}</li>
 * </ul>
 *
 * <p><b>Comparison</b></p>
 *
 * <ul>
 *     <li>{@link #compareTo(Path)}</li>
 *     <li>{@link #endsWith(Path)} - True if this path ends with the given path</li>
 *     <li>{@link #startsWith(Path)} - True if this path starts with the given path</li>
 * </ul>
 *
 * <p><b>Checks</b></p>
 *
 * <ul>
 *     <li>{@link #isAbsolute()} - True if this path has a root element</li>
 *     <li>{@link #isEmpty()} - True if this path has no root and no elements</li>
 *     <li>{@link #isRelative()} - True if this path does not have a root element</li>
 *     <li>{@link #isRoot()} - True if this path has a root element but no path elements</li>
 * </ul>
 *
 * <p><b>Functional Methods</b></p>
 *
 * <p>
 * The following methods are functional, meaning that they return a copy of the path that is changed in some
 * way while this path itself is not altered. The copy is made with the method {@link #copy()}, which calls the
 * subclass to create the copy through {@link #onCopy(Comparable, List)}. This allows subclasses to reuse
 * the logic in this class while still dealing in instances of the subclass type.
 * </p>
 *
 * <ul>
 *     <li>{@link #copy()} - A copy of this path</li>
 *     <li>{@link #emptyPath()} - The empty path</li>
 *     <li>{@link #parent()} - The parent of this path</li>
 *     <li>{@link #root()} - The root path of this path</li>
 *     <li>{@link #subpath(int start, int end)} - The elements from start to end (exclusive)</li>
 *     <li>{@link #transformed(Function)} - This path with each element transformed by the given function</li>
 *     <li>{@link #withChild(Element)} - This path with the given element appended</li>
 *     <li>{@link #withChild(Path)} - This path with the given path appended</li>
 *     <li>{@link #withParent(Element)} - This path with the given element prepended</li>
 *     <li>{@link #withParent(Path)} - This path with the given element prepended</li>
 *     <li>{@link #withRoot(Element)} - This path with the given root element</li>
 *     <li>{@link #withoutFirst()} - This path without any first element or null if there is no first element</li>
 *     <li>{@link #withoutLast()} - This path without any last element or null if there is no last element</li>
 *     <li>{@link #withoutOptionalPrefix(Path)} - This path without the prefix, if it exists</li>
 *     <li>{@link #withoutOptionalSuffix(Path)} - This path without the suffix, if it exists</li>
 *     <li>{@link #withoutPrefix(Path)} - This path without the given prefix path or null if it is not found</li>
 *     <li>{@link #withoutRoot()} - This path without the root element</li>
 *     <li>{@link #withoutSuffix(Path)} - This path without the given suffix path or null if it is not found</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "UnusedReturnValue", "SpellCheckingInspection", "SwitchStatementWithTooFewBranches" })
@UmlClassDiagram(diagram = DiagramPath.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public abstract class Path<Element extends Comparable<Element>> implements
        Iterable<Element>,
        Comparable<Path<Element>>,
        Sized,
        StringFormattable
{
    /** The list of elements */
    private ObjectList<Element> elements = new ObjectList<>();

    /** The path root, if any */
    private Element root;

    /**
     * Constructs a rooted path with the given list of elements
     */
    protected Path(Element root, List<Element> elements)
    {
        this.root = root;
        this.elements.addAll(ensureNotNull(elements));
    }

    /**
     * Copy constructor
     */
    protected Path(Path<Element> that)
    {
        this(that.root, that.elements);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asString(@NotNull Format format)
    {
        switch (format)
        {
            case FILESYSTEM:
                return elements.join(File.separator);

            default:
                return elements.join("/");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Path<Element> that)
    {
        var a = elements();
        var b = that.elements();
        for (int i = 0; i < Math.min(a.size(), b.size()); i++)
        {
            var result = a.get(i).compareTo(b.get(i));
            if (result != 0)
            {
                return result;
            }
        }
        return a.size() < b.size() ? -1 : 1;
    }

    /**
     * Returns a copy of this path
     */
    public Path<Element> copy()
    {
        return onCopy(root, elements);
    }

    /**
     * Returns the elements in this path as a list
     */
    public List<Element> elements()
    {
        return elements;
    }

    /**
     * Returns a relative path with no elements
     */
    public Path<Element> emptyPath()
    {
        var copy = copy();
        copy.root = null;
        copy.elements.clear();
        return copy;
    }

    /**
     * Returns true if this path ends with the given suffix
     */
    public boolean endsWith(Path<Element> suffix)
    {
        return elements.endsWith(suffix.elements);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object object)
    {
        if (object instanceof Path)
        {
            var that = (Path<Element>) object;
            return Objects.areEqualPairs(root, that.root, elements, that.elements);
        }
        return false;
    }

    /**
     * Returns the first element in this path, or null if there is none.
     */
    public Element first()
    {
        return get(0);
    }

    /**
     * Returns a path consisting of the first n elements of this path, or null if there are not that many elements. If
     * the path is absolute, the returned path will also be absolute, with the same root element.
     */
    public Path<Element> first(int n)
    {
        if (size() >= n)
        {
            return subpath(0, n);
        }
        return null;
    }

    /**
     * Returns the element at the given index or null if the index is out of range
     */
    public Element get(int index)
    {
        if (index >= 0 && index < size())
        {
            return elements.get(index);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Hash.hashMany(root, elements);
    }

    /**
     * Returns true if this path is an absolute path with a root element
     */
    public boolean isAbsolute()
    {
        return root != null;
    }

    /**
     * Returns true if this path has no elements
     */
    @Override
    public boolean isEmpty()
    {
        return elements.isEmpty();
    }

    /**
     * Returns true if this is a relative path, having no root element
     */
    public boolean isRelative()
    {
        return root == null;
    }

    /**
     * Returns true if this path is a root path (there is more than one on Windows)
     */
    public boolean isRoot()
    {
        return root != null && isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Iterator<Element> iterator()
    {
        return elements.iterator();
    }

    /**
     * Returns a path consisting of the last n elements of this path, or null if there are not that many elements. The
     * returned path will be relative in all cases.
     */
    public Path<Element> last(int n)
    {
        if (size() >= n)
        {
            return subpath(size() - n, size()).withoutRoot();
        }
        return null;
    }

    /**
     * Returns the last element of this path, or null if this is an empty path
     */
    public Element last()
    {
        return get(size() - 1);
    }

    /**
     * Returns the parent of this path, or null if there is no parent
     */
    public Path<Element> parent()
    {
        return withoutLast();
    }

    /**
     * Pops the last element off the path
     * <p>
     * Returns the last element
     */
    public Element pop()
    {
        return elements.pop();
    }

    /**
     * Pushes the given element onto the end of the path
     *
     * @param element The element
     */
    public void push(Element element)
    {
        elements.push(element);
    }

    /**
     * Returns the root path if this path is absolute or null if it is relative
     */
    public Path<Element> root()
    {
        if (isAbsolute())
        {
            var copy = copy();
            copy.elements.clear();
            return copy;
        }
        return null;
    }

    /**
     * Returns the root element for this path if it is absolute, or null if the path is relative
     */
    public Element rootElement()
    {
        return root;
    }

    /**
     * Returns the number of elements in this path, or zero if this is an empty path
     */
    @Override
    public int size()
    {
        return elements.size();
    }

    /**
     * Returns true if this path starts with the given path
     */
    public boolean startsWith(Path<Element> prefix)
    {
        return elements.startsWith(prefix.elements);
    }

    /**
     * Returns the elements in this path as a {@link Stream}
     */
    public Stream<Element> stream()
    {
        return Streams.stream(this);
    }

    /**
     * Returns the sub-path of this path starting at given start index (inclusive) and extending to the end index
     * (exclusive). If start and end are equal, the empty path is returned. If start and end are not equal and the path
     * is absolute, the sub-path will still be absolute with the same root. If the start and end indexes are invalid,
     * null is returned.
     */
    public Path<Element> subpath(int start, int end)
    {
        if (start <= end && start >= 0 && end <= size())
        {
            if (start == end)
            {
                return emptyPath();
            }
            var copy = copy();
            copy.elements = ObjectList.objectList(elements.subList(start, end));
            return copy;
        }
        return null;
    }

    /**
     * Returns a copy of this path with each element transformed by the given function
     */
    public Path<Element> transformed(Function<Element, Element> function)
    {
        var elements = new ArrayList<Element>();
        for (var element : this)
        {
            var transformed = function.apply(element);
            Ensure.ensure(transformed != null);
            elements.add(transformed);
        }
        return onCopy(root, elements);
    }

    /**
     * Returns this path with the given path appended
     */
    public Path<Element> withChild(Path<Element> that)
    {
        var copy = copy();
        copy.elements.appendAll(that.elements);
        return copy;
    }

    /**
     * Returns this path with the given element appended
     */
    public Path<Element> withChild(Element element)
    {
        var copy = copy();
        copy.elements.append(element);
        return copy;
    }

    /**
     * Returns this path with the given element prepended
     */
    public Path<Element> withParent(Element element)
    {
        var copy = copy();
        copy.elements.prepend(element);
        return copy;
    }

    /**
     * Returns this path with the given path prepended
     */
    public Path<Element> withParent(Path<Element> that)
    {
        return that.withChild(this);
    }

    /**
     * Returns this path with the given root element (whether the path is absolute or relative)
     */
    public Path<Element> withRoot(Element root)
    {
        var copy = copy();
        copy.root = root;
        return copy;
    }

    /**
     * Returns this path without the first element. If this is an empty path, null is returned. If there is only one
     * element in this path, the root path or empty path is returned, for absolute and relative paths, respectively.
     */
    public Path<Element> withoutFirst()
    {
        switch (size())
        {
            case 0:
                return null;

            case 1:
                return isAbsolute() ? root() : emptyPath();

            default:
                return subpath(1, size());
        }
    }

    /**
     * Returns this path without the last element. If this is an empty path, null is returned. If there is only one
     * element in this path, the root path is returned. If there is only one element in this path, the root path or
     * empty path is returned, for absolute and relative paths, respectively.
     */
    public Path<Element> withoutLast()
    {
        switch (size())
        {
            case 0:
                return null;

            case 1:
                return isAbsolute() ? root() : emptyPath();

            default:
                if ("".equals(last()))
                {
                    return subpath(0, size() - 2);
                }
                return subpath(0, size() - 1);
        }
    }

    /**
     * Returns this path without the given prefix. If this path doesn't start with the prefix, the path is returned
     * unchanged.
     */
    public Path<Element> withoutOptionalPrefix(Path<Element> prefix)
    {
        var without = withoutPrefix(prefix);
        return without == null ? this : without;
    }

    /**
     * Returns this path without the given suffix. If this path doesn't end with the suffix, the path is returned
     * unchanged.
     */
    public Path<Element> withoutOptionalSuffix(Path<Element> suffix)
    {
        var without = withoutSuffix(suffix);
        return without == null ? this : without;
    }

    /**
     * Returns this path after the given prefix. If the path doesn't start with the prefix null is returned. If the path
     * is equal to the prefix, a root or empty path is returned for absolute and relative paths respectively.
     */
    public Path<Element> withoutPrefix(Path<Element> prefix)
    {
        if (equals(prefix))
        {
            return isAbsolute() ? root() : emptyPath();
        }
        if (startsWith(prefix))
        {
            return subpath(prefix.size(), size());
        }
        return null;
    }

    /**
     * Returns this path without the root element or null if this path is a root element
     */
    public Path<Element> withoutRoot()
    {
        if (!isRoot())
        {
            var copy = copy();
            copy.root = null;
            return copy;
        }
        return null;
    }

    /**
     * Returns this path before the given suffix. If the path doesn't end with the suffix null is returned.  If the path
     * doesn't start with the prefix null is returned. If the path * is equal to the prefix, a root or empty path is
     * returned for absolute and relative paths respectively.
     */
    public Path<Element> withoutSuffix(Path<Element> suffix)
    {
        if (equals(suffix))
        {
            return isAbsolute() ? root() : emptyPath();
        }
        if (endsWith(suffix))
        {
            return subpath(0, size() - suffix.size());
        }
        return null;
    }

    /**
     * Returns a copy of this path
     */
    protected abstract Path<Element> onCopy(Element root, List<Element> elements);
}

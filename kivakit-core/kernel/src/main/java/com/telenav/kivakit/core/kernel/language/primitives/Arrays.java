////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.primitives;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageCollectionsList;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Utility methods for working with arrays
 */
@UmlClassDiagram(diagram = DiagramLanguageCollectionsList.class)
public class Arrays
{
    /**
     * Reverses the elements in the given array
     */
    public static long[] reverse(final long[] elements)
    {
        for (var i = 0; i < elements.length / 2; i++)
        {
            final var opposite = elements.length - 1 - i;
            final var temp = elements[i];
            elements[i] = elements[opposite];
            elements[opposite] = temp;
        }
        return elements;
    }

    /**
     * Reverses the elements in the given array
     */
    public static int[] reverse(final int[] elements)
    {
        for (var i = 0; i < elements.length / 2; i++)
        {
            final var temp = elements[i];
            elements[i] = elements[elements.length - i - 1];
            elements[elements.length - i - 1] = temp;
        }
        return elements;
    }

    /**
     * Reverses the values in the given range of indexes
     */
    public static void reverseRange(final long[] array, final int fromIndex, final int toIndex)
    {
        for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--)
        {
            final long temporary = array[i];
            array[i] = array[j];
            array[j] = temporary;
        }
    }
}

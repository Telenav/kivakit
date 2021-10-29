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

package com.telenav.kivakit.kernel.language.primitives;

import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageCollectionsList;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Utility methods for working with arrays
 */
@UmlClassDiagram(diagram = DiagramLanguageCollectionsList.class)
@LexakaiJavadoc(complete = true)
public class Arrays
{
    /**
     * Reverses the elements in the given array
     */
    public static long[] reverse(long[] elements)
    {
        for (var i = 0; i < elements.length / 2; i++)
        {
            var opposite = elements.length - 1 - i;
            var temp = elements[i];
            elements[i] = elements[opposite];
            elements[opposite] = temp;
        }
        return elements;
    }

    /**
     * Reverses the elements in the given array
     */
    public static int[] reverse(int[] elements)
    {
        for (var i = 0; i < elements.length / 2; i++)
        {
            var temp = elements[i];
            elements[i] = elements[elements.length - i - 1];
            elements[elements.length - i - 1] = temp;
        }
        return elements;
    }

    /**
     * Reverses the values in the given range of indexes
     */
    public static void reverseRange(long[] array, int fromIndex, int toIndex)
    {
        for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--)
        {
            long temporary = array[i];
            array[i] = array[j];
            array[j] = temporary;
        }
    }
}

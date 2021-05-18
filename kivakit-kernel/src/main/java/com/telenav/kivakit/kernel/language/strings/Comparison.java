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

package com.telenav.kivakit.kernel.language.strings;

import com.telenav.kivakit.kernel.language.primitives.Doubles;
import com.telenav.kivakit.kernel.language.values.level.Percent;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Methods for comparing strings, including by <a href="https://en.wikipedia.org/wiki/Levenshtein_distance">Levenshtein
 * distance.</a>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageString.class)
@LexakaiJavadoc(complete = true)
public class Comparison
{
    /**
     * Compares two strings avoiding problems with null values
     *
     * @return Comparison value for the two strings
     */
    public static int compare(final String a, final String b)
    {
        if (a == null && b == null)
        {
            return 0;
        }
        if (a == null)
        {
            return 1;
        }
        if (b == null)
        {
            return -1;
        }
        return a.compareTo(b);
    }

    /**
     * @return The percentage difference between the two strings using the Levenshtein distance algorithm
     */
    public static Percent levenshteinDifference(final String a, final String b)
    {
        final var lexicalDistance = levenshteinDistance(a, b);
        if (lexicalDistance == 0)
        {
            return Percent._0;
        }
        return Percent.of(Doubles.inRange(100.0 * lexicalDistance / a.length(), 0.0, 100.0));
    }

    /**
     * Compute the Levenshtein Distance between a source String and a target String. From Wikipedia, "the Levenshtein
     * distance between two words is the minimum number of single-character edits (insertion, deletion, substitution)
     * required to change one word into the other." (https://en.wikipedia.org/wiki/Levenshtein_distance)
     *
     * @param source The source String
     * @param target The target String
     * @return The Levenshtein Distance between source and target
     */
    public static int levenshteinDistance(final String source, final String target)
    {
        final var sourceLength = source.length();
        final var targetLength = target.length();

        if (sourceLength == 0)
        {
            return targetLength;
        }

        if (targetLength == 0)
        {
            return sourceLength;
        }

        final var distanceMatrix = new int[sourceLength + 1][targetLength + 1];

        for (var i = 0; i <= sourceLength; i++)
        {
            distanceMatrix[i][0] = i;
        }

        for (var j = 0; j <= targetLength; j++)
        {
            distanceMatrix[0][j] = j;
        }

        for (var i = 1; i <= sourceLength; i++)
        {
            final int s_i = source.charAt(i - 1);

            for (var j = 1; j <= targetLength; j++)
            {
                final int t_j = target.charAt(j - 1);

                final int cost;
                if (s_i == t_j)
                {
                    cost = 0;
                }
                else
                {
                    cost = 1;
                }

                distanceMatrix[i][j] = minimum(distanceMatrix[i - 1][j] + 1, distanceMatrix[i][j - 1] + 1,
                        distanceMatrix[i - 1][j - 1] + cost);
            }
        }

        return distanceMatrix[sourceLength][targetLength];
    }

    /**
     * @return The similarity of the two strings
     * @see Comparison#levenshteinDifference(String, String)
     */
    public static Percent levenshteinSimilarity(final String a, final String b)
    {
        return levenshteinDifference(a, b).inverse();
    }

    /**
     * @return The minimum of the three values
     */
    private static int minimum(final int a, final int b, final int c)
    {
        return Math.min(a, Math.min(b, c));
    }
}

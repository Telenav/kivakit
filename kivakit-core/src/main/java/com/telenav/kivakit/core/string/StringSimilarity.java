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

package com.telenav.kivakit.core.string;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramString;
import com.telenav.kivakit.core.language.primitive.Doubles;
import com.telenav.kivakit.core.value.level.Percent;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_STATIC_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Methods for comparing strings, including by <a href="https://en.wikipedia.org/wiki/Levenshtein_distance">Levenshtein
 * distance.</a>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramString.class)
@ApiQuality(stability = STABLE_STATIC_EXPANDABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class StringSimilarity
{
    /**
     * Compares two strings avoiding problems with null values
     *
     * @return Comparison value for the two strings
     */
    public static int compare(String a, String b)
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
    public static Percent levenshteinDifference(String a, String b)
    {
        var lexicalDistance = levenshteinDistance(a, b);
        if (lexicalDistance == 0)
        {
            return Percent._0;
        }
        return Percent.percent(Doubles.inRange(100.0 * lexicalDistance / a.length(), 0.0, 100.0));
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
    @SuppressWarnings("JavadocLinkAsPlainText")
    public static int levenshteinDistance(String source, String target)
    {
        var sourceLength = source.length();
        var targetLength = target.length();

        if (sourceLength == 0)
        {
            return targetLength;
        }

        if (targetLength == 0)
        {
            return sourceLength;
        }

        var distanceMatrix = new int[sourceLength + 1][targetLength + 1];

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
            int s_i = source.charAt(i - 1);

            for (var j = 1; j <= targetLength; j++)
            {
                int t_j = target.charAt(j - 1);

                int cost;
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
     * @see StringSimilarity#levenshteinDifference(String, String)
     */
    public static Percent levenshteinSimilarity(String a, String b)
    {
        return levenshteinDifference(a, b).inverse();
    }

    /**
     * @return The minimum of the three values
     */
    private static int minimum(int a, int b, int c)
    {
        return Math.min(a, Math.min(b, c));
    }
}

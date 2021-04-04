package com.telenav.kivakit.core.kernel.language.strings;

import com.telenav.kivakit.core.kernel.language.values.level.Percent;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.kivakit.core.kernel.language.primitives.Doubles;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageString.class)
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
    public static Percent difference(final String a, final String b)
    {
        final var lexicalDistance = levenshteinDistance(a, b);
        if (lexicalDistance == 0)
        {
            return Percent._0;
        }
        return Percent.percent(Doubles.inRange(100.0 * lexicalDistance / a.length(), 0.0, 100.0));
    }

    /**
     * Compute the Levenshtein Distance between a source String and a target String. From Wikipedia, "the Levenshtein
     * distance between two words is the minimum number of single-character edits (insertion, deletion, substitution)
     * required to change one word into the other." (http://en.wikipedia.org/wiki/Levenshtein_distance)
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
     * @see Comparison#difference(String, String)
     */
    public static Percent similarity(final String a, final String b)
    {
        return difference(a, b).inverse();
    }

    /**
     * @return The minimum of the three values
     */
    private static int minimum(final int a, final int b, final int c)
    {
        return Math.min(a, Math.min(b, c));
    }
}

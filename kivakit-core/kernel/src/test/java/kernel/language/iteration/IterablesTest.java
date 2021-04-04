////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.language.iteration;

import com.telenav.kivakit.core.kernel.language.iteration.BaseIterator;
import com.telenav.kivakit.core.kernel.language.iteration.Iterables;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;

/**
 * Test {@link Iterables}
 *
 * @author matthieun
 */
public class IterablesTest
{
    @Test
    public void testSize()
    {
        final List<String> listString = new ArrayList<>();

        for (var i = 0; i < 3; i++)
        {
            listString.add("a");
        }

        final Iterable<String> stringList = new Iterable<>()
        {
            private static final int MAX = 5;

            @Override
            public Iterator<String> iterator()
            {
                return new BaseIterator<>()
                {
                    private int counter;

                    @Override
                    protected String onNext()
                    {
                        if (counter < MAX)
                        {
                            counter++;
                            return "b";
                        }
                        return null;
                    }
                };
            }
        };

        ensureEqual(3, Iterables.size(listString));
        ensureEqual(5, Iterables.size(stringList));
    }
}

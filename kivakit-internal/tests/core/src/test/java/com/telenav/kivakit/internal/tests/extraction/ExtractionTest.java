package com.telenav.kivakit.internal.tests.extraction;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.extraction.BaseExtractor;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.messaging.Listener.consoleListener;
import static com.telenav.kivakit.core.messaging.Listener.nullListener;
import static java.lang.Integer.parseInt;

public class ExtractionTest extends CoreUnitTest
{
    static class TestExtractor extends BaseExtractor<Integer, String>
    {
        protected TestExtractor(Listener listener)
        {
            super(listener);
        }

        @Override
        public Integer onExtract(String text)
        {
            return parseInt(text);
        }
    }

    @Test
    public void testArrayExtraction()
    {
        var extractor = new TestExtractor(consoleListener());
        ensureEqual(extractor.extractAll(new String[] { "12", "13", "14" }), list(12, 13, 14));
    }

    @Test
    public void testExtraction()
    {
        var extractor = new TestExtractor(nullListener());
        ensureEqual(extractor.extract("12"), 12);
        ensureEqual(extractor.extract("12o"), null);
        ensureEqual(extractor.extract(null), null);
    }

    @Test
    public void testListExtraction()
    {
        var extractor = new TestExtractor(consoleListener());
        ensureEqual(extractor.extractAll(stringList("12", "13", "14")), list(12, 13, 14));
    }
}

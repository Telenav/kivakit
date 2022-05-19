package com.telenav.kivakit.internal.tests.extraction;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.extraction.BaseExtractor;
import com.telenav.kivakit.internal.test.support.CoreUnitTest;
import org.junit.Test;

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
            return Integer.parseInt(text);
        }
    }

    @Test
    public void testArrayExtraction()
    {
        var extractor = new TestExtractor(Listener.consoleListener());
        ensureEqual(extractor.extract(new String[] { "12", "13", "14" }), ObjectList.objectList(12, 13, 14));
    }

    @Test
    public void testExtraction()
    {
        var extractor = new TestExtractor(Listener.emptyListener());
        ensureEqual(extractor.extract("12"), 12);
        ensureEqual(extractor.extract("12o"), null);
        ensureEqual(extractor.extract((String) null), null);
    }

    @Test
    public void testListExtraction()
    {
        var extractor = new TestExtractor(Listener.consoleListener());
        ensureEqual(extractor.extract(StringList.stringList("12", "13", "14")), ObjectList.objectList(12, 13, 14));
    }
}

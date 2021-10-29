package kernel.data.extraction;

import com.telenav.kivakit.kernel.data.extraction.BaseExtractor;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.messaging.Listener;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;

public class ExtractionTest
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
        var extractor = new TestExtractor(Listener.console());
        ensureEqual(extractor.extract(new String[] { "12", "13", "14" }), ObjectList.objectList(12, 13, 14));
    }

    @Test
    public void testExtraction()
    {
        var extractor = new TestExtractor(Listener.none());
        ensureEqual(extractor.extract("12"), 12);
        ensureEqual(extractor.extract("12o"), null);
        ensureEqual(extractor.extract((String) null), null);
    }

    @Test
    public void testListExtraction()
    {
        var extractor = new TestExtractor(Listener.console());
        ensureEqual(extractor.extract(StringList.stringList("12", "13", "14")), ObjectList.objectList(12, 13, 14));
    }
}

package kernel.language.primitive;

import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.language.primitives.Doubles;
import org.junit.Test;

/**
 * @author jonathanl (shibo)
 */
public class DoublesTest
{
    @Test
    public void testParse()
    {
        Ensure.ensureEqual(3.1415, Doubles.fastParse("3.1415", 1_0000));
        Ensure.ensureEqual(31.415, Doubles.fastParse("31.415", 1_000));
    }
}

package kernel.messaging;

import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.monads.ResultTrait;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;

public class ResultTest extends BaseRepeater implements ResultTrait
{
    @Test
    public void test()
    {
        try
        {
            var result = result(this::a) // get result of calling a(),
                    .or(this::b) // or the result of b(), if a() failed,
                    .orThrow(); // and if both failed, throw an exception, otherwise return the result.

            // The result should be 7,
            ensureEqual(result, 7);
        }
        catch (Exception ignored)
        {
            // or an exception, which we ignore.
        }
    }

    private Integer a()
    {
        if (Time.now().asSeconds() % 2 == 0)
        {
            problem("a() failed");
            return null;
        }

        return 7;
    }

    private Integer b()
    {
        problem("b() failed");
        return null;
    }
}

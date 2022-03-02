package com.telenav.kivakit.core.function;

import com.telenav.kivakit.core.test.UnitTest;
import com.telenav.kivakit.core.time.Time;
import org.junit.Test;

public class ResultTest extends UnitTest implements ResultTrait
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

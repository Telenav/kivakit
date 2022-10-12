package com.telenav.kivakit.internal.tests.core.function;

import com.telenav.kivakit.core.function.Result;
import com.telenav.kivakit.core.function.ResultTrait;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;import com.telenav.kivakit.internal.testing.CoreUnitTest;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.interfaces.code.Code;
import org.junit.Test;

public class ResultTest extends CoreUnitTest implements ResultTrait
{
    static class Operation extends BaseRepeater implements Code<String>
    {
        private final boolean succeed;

        public Operation(boolean succeed)
        {
            this.succeed = succeed;
        }

        public String run()
        {
            if (succeed)
            {
                return "Hello, World!";
            }
            problem("Failed!");
            return null;
        }
    }

    @Test
    public void testCapture()
    {
        var operation = new Operation(true);
        var result = Result.run(operation, operation);
        ensure(result.succeeded());
        ensureEqual(result.get(), "Hello, World!");

        operation = new Operation(false);
        result = Result.run(operation, operation);
        ensureEqual(result.get(), null);
        ensure(result.failed());
        ensure(result.messages().isNonEmpty());
        ensure("Failed!".equals(result.messages().get(0).formatted()));
    }

    @Test
    public void testOr()
    {
        try
        {
            var result = run(this::a) // get result of calling a(),
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

    @Test
    public void testOrProblem()
    {
        ensureBroadcastsProblem(Result.absent(), result -> result.orProblem("missing"));
        ensureBroadcastsNoProblem(Result.success(3), result -> result.orProblem("missing"));
    }

    private Integer a()
    {
        if (Time.now().asSeconds() % 2 == 0)
        {
            return null;
        }

        return 7;
    }

    private Integer b()
    {
        return null;
    }
}

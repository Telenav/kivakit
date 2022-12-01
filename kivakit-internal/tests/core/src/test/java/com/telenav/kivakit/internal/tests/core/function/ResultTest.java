package com.telenav.kivakit.internal.tests.core.function;

import com.telenav.kivakit.core.ensure.EnsureTrait;
import com.telenav.kivakit.core.function.Result;
import com.telenav.kivakit.core.messaging.CheckTrait;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.interfaces.code.Code;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.messaging.Listener.nullListener;
import static com.telenav.kivakit.internal.tests.core.function.ResultTest.OperationResult.FAIL;
import static com.telenav.kivakit.internal.tests.core.function.ResultTest.OperationResult.SUCCEED;
import static com.telenav.kivakit.internal.tests.core.function.ResultTest.OperationResult.THROW;

@SuppressWarnings("CodeBlock2Expr")
public class ResultTest extends CoreUnitTest implements
        CheckTrait,
        EnsureTrait
{
    enum OperationResult
    {
        SUCCEED,
        FAIL,
        THROW
    }

    static class Operation extends BaseRepeater implements Code<String>, EnsureTrait
    {
        private final OperationResult result;

        public Operation(OperationResult result)
        {
            this.result = result;
        }

        @Override
        public String run()
        {
            if (result == SUCCEED)
            {
                return "Hello, World!";
            }
            if (result == THROW)
            {
                illegalState("Throwing!");
            }
            problem("Failed!");
            return null;
        }
    }

    @Test
    public void testCapture()
    {
        var operation = new Operation(SUCCEED);
        var result = Result.run(operation, operation);
        ensure(result.succeeded());
        ensureEqual(result.get(), "Hello, World!");

        operation = new Operation(FAIL);
        result = Result.run(operation, operation);
        ensureEqual(result.get(), null);
        ensure(result.failed());
        ensure(result.messages().isNonEmpty());
        ensure("Failed!".equals(result.messages().get(0).formatted()));
    }

    @Test
    public void testCheckedCodeFailure()
    {
        var operation = new Operation(FAIL);
        var result = read(operation);
        ensure(result.failed());
        ensure(result.get() == null);
        ensureEqual(result.messages().get(0).formatted(), "Code broadcast failure: Could not read string");
    }

    @Test
    public void testCheckedCodeSuccess()
    {
        var operation = new Operation(SUCCEED);
        var result = read(operation);
        ensure(result.succeeded());
        ensure(result.get().equals("Hello, World!"));
    }

    @Test
    public void testCheckedCodeThrow()
    {
        var operation = new Operation(THROW);
        var result = read(operation);
        ensure(result.failed());
        ensure(result.get() == null);
        ensure(result.messages().get(0).formatted().startsWith("Code threw exception: Could not read string: Throwing!"));
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

    /**
     * Tests the check method by invoking the given operation
     */
    Result<String> read(Operation operation)
    {
        clearListeners();
        nullListener().listenTo(this);
        return check("Could not read string", s -> s != null && !s.isBlank(), () ->
        {
            return success(listenTo(operation).run());
        });
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

package com.telenav.kivakit.core.function;

import com.telenav.kivakit.core.test.UnitTest;
import com.telenav.kivakit.core.value.mutable.MutableValue;
import org.junit.Test;

import java.util.stream.Collectors;

public class MaybeTest extends UnitTest
{
    @Test
    public void testAbsentIf()
    {
        ensureEqual(null, Maybe.present(3).absentIf(x -> true).get());
        ensureEqual(3, Maybe.present(3).absentIf(x -> false).get());
    }

    @Test
    public void testApply()
    {
        var seven = Maybe.present(7);
        var three = Maybe.present(3);
        ensureEqual(10, seven.apply(a -> three.apply(b -> Maybe.present(a + b))).get());
        seven = Maybe.absent();
        ensureEqual(null, seven.apply(a -> three.apply(b -> Maybe.present(a + b))).get());
    }

    @Test
    public void testAsStream()
    {
        ensureEqual(3, Maybe.present(3).asStream().collect(Collectors.toList()).get(0));
        ensure(Maybe.absent().asStream().findAny().isEmpty());
    }

    @Test
    public void testDefaultTo()
    {
        ensureEqual(5, Maybe.present(5).orDefaultTo(9));
        ensureEqual(9, Maybe.absent().orDefaultTo(9));
        ensureEqual(9, Maybe.maybe(null).orDefaultTo(9));
        ensureEqual(5, Maybe.maybe(5).orDefaultTo(9));

        ensureEqual(5, Maybe.present(5).orDefaultTo(() -> 9));
        ensureEqual(9, Maybe.absent().orDefaultTo(() -> 9));
        ensureEqual(9, Maybe.maybe(null).orDefaultTo(() -> 9));
        ensureEqual(5, Maybe.maybe(5).orDefaultTo(() -> 9));
    }

    @Test
    public void testHas()
    {
        ensure(Maybe.maybe(9).has());
        ensureFalse(Maybe.maybe(null).has());
    }

    @Test
    public void testIfAbsent()
    {
        ensure(Maybe.maybe(null).isAbsent());
        ensure(Maybe.absent().isAbsent());
        ensureFalse(Maybe.maybe(9).isAbsent());
    }

    @Test
    public void testIfPresent()
    {
        ensure(Maybe.maybe(9).isPresent());
        ensureFalse(Maybe.maybe(null).isPresent());
        ensureFalse(Maybe.absent().isPresent());
    }

    @Test
    public void testIfPresentConsume()
    {
        var x = new MutableValue<Integer>();
        Maybe.present(3).ifPresent(x::set);
        ensureEqual(3, x.get());

        Integer maybe = 5;
        x.set(0);
        Maybe.maybe(maybe).ifPresent(x::set);
        ensureEqual(5, x.get());

        maybe = null;
        x.set(1);
        Maybe.maybe(maybe).ifPresent(x::set);
        ensureEqual(1, x.get());
    }

    @Test
    public void testIfPresentOr()
    {
        var x = new MutableValue<Integer>();
        Maybe.present(3).ifPresentOr(x::set, () -> x.set(5));
        ensureEqual(3, x.get());

        Integer maybe = 5;
        x.set(0);
        Maybe.maybe(maybe).ifPresentOr(x::set, () -> x.set(7));
        ensureEqual(5, x.get());

        maybe = null;
        x.set(0);
        Maybe.maybe(maybe).ifPresentOr(x::set, () -> x.set(7));
        ensureEqual(7, x.get());
    }

    @Test
    public void testMap()
    {
        ensureEqual(9, Maybe.present(8).map(x -> x + 1).get());
    }

    @Test
    public void testOrMaybe()
    {
        ensureEqual(8, Maybe.present(8).orMaybe(5).get());
        ensureEqual(5, Maybe.absent().orMaybe(5).get());
        ensureEqual(8, Maybe.present(8).orMaybe(5).get());
        ensureEqual(5, Maybe.maybe(null).orMaybe(5).get());
        ensureEqual(3, Maybe.maybe(3).orMaybe(5).get());

        ensureEqual(8, Maybe.present(8).orMaybe(() -> 5).get());
        ensureEqual(5, Maybe.absent().orMaybe(() -> 5).get());
        ensureEqual(8, Maybe.present(8).orMaybe(() -> 5).get());
        ensureEqual(5, Maybe.maybe(null).orMaybe(() -> 5).get());
        ensureEqual(3, Maybe.maybe(3).orMaybe(() -> 5).get());
    }

    @Test
    public void testOrProblem()
    {
        ensureBroadcastsProblem(() -> Maybe.absent().orProblem("missing"));
        ensureBroadcastsNoProblem(() -> Maybe.present(3).orProblem("missing"));
    }

    @Test
    public void testOrThrow()
    {
        ensureThrows(() -> Maybe.absent().orThrow());
        ensureThrows(() -> Maybe.maybe(null).orThrow());
        ensureEqual(9, Maybe.maybe(9).orThrow());
        ensureEqual(9, Maybe.present(9).orThrow());

        ensureThrows(() -> Maybe.absent().orThrow("missing"));
        ensureThrows(() -> Maybe.maybe(null).orThrow("missing"));
        ensureEqual(9, Maybe.maybe(9).orThrow("missing"));
        ensureEqual(9, Maybe.present(9).orThrow("missing"));
    }

    @Test
    public void testPresentIf()
    {
        ensureEqual(3, Maybe.present(3).presentIf(x -> true).get());
        ensureEqual(null, Maybe.present(3).presentIf(x -> false).get());
    }

    @Test
    public void testThen()
    {
        ensureEqual(32, Maybe.present(3)
                .then(x -> x + 1)
                .then(x -> x * 8)
                .get());

        ensureEqual(null, Maybe.maybe((Integer) null)
                .then(x -> x + 1)
                .then(x -> x * 8)
                .get());

        ensureEqual(12, Maybe.present(3)
                .then(Integer::sum, 9).get());

        ensureEqual(null, Maybe.maybe((Integer) null)
                .then(Integer::sum, 9).get());

        ensureEqual(12, Maybe.present(3)
                .then(Integer::sum, Maybe.maybe(9)).get());

        ensureEqual(null, Maybe.maybe((Integer) null)
                .then(Integer::sum, Maybe.maybe(9)).get());
    }
}

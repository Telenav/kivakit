package com.telenav.kivakit.internal.tests.core.function;

import com.telenav.kivakit.core.function.Maybe;
import com.telenav.kivakit.core.language.primitive.Ints;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import com.telenav.kivakit.core.value.mutable.MutableValue;
import org.junit.Test;

public class MaybeTest extends CoreUnitTest
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
        ensureEqual(3, Maybe.present(3).asStream().toList().get(0));
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
    public void testMediumRocketSurgeryArticle()
    {
        ensureEqual(Maybe.present("123") // "123"
                .map(Integer::parseInt) // 123
                .map(Integer::sum, 123) // 246
                .map(String::valueOf) // "246"
                .map(String::concat, "xyz") // "246xyz"
                .map(String::substring, 2, 4) // "6x"
                .get(), "6x");

        ensureEqual(Maybe.present("abc") // "123"
                .map(Integer::parseInt) // null
                .map(Integer::sum, 123) // null
                .map(String::valueOf) // null
                .map(String::concat, "xyz") // null
                .map(String::substring, 2, 4) // null
                .get(), null);
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

    @SuppressWarnings("SpellCheckingInspection")
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
                .map(Integer::sum, 9).get());

        ensureEqual(null, Maybe.maybe((Integer) null)
                .map(Integer::sum, 9).get());

        ensureEqual(Maybe.present("abc")
                .then(String::toUpperCase)
                .map(String::concat, "def")
                .then(it -> it + "!")
                .presentIf(it -> it.contains("A"))
                .get(), "ABCdef!");
    }

    @Test
    public void testThenBiFunction()
    {
        ensureEqual(Maybe.present("9")
                .map(Ints::parseFastInt)
                .map(Integer::sum, 3)
                .get(), 12);

        ensureEqual(Maybe.present("9")
                .map(String::repeat, 5)
                .get(), "99999");

        ensureEqual(Maybe.maybe((String) null)
                .map(String::repeat, 5)
                .get(), null);
    }

    @Test
    @SuppressWarnings("SpellCheckingInspection")
    public void testThenPentaFunction()
    {
        ensureEqual(Maybe.present(123456789)
                .map(String::valueOf)
                .map(String::regionMatches, 1, "234", 0, 3)
                .get(), true);

        ensureEqual(Maybe.maybe((Integer) null)
                .map(String::valueOf)
                .map(String::regionMatches, 1, "234", 0, 3)
                .get(), null);
    }

    @Test
    public void testThenQuadFunction()
    {
        ensureEqual(Maybe.present(":")
                .map(String::join, "a", "b", "c")
                .get(), "a:b:c");

        ensureEqual(Maybe.maybe((String) null)
                .map(String::join, "a", "b", "c")
                .get(), null);
    }

    @Test
    public void testThenTriFunction()
    {
        ensureEqual(Maybe.present(123456789)
                .map(String::valueOf)
                .map(String::substring, 3, 5)
                .get(), "45");

        ensureEqual(Maybe.maybe((Integer) null)
                .map(String::valueOf)
                .map(String::substring, 3, 5)
                .get(), null);
    }
}

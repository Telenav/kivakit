package com.telenav.kivakit.internal.tests.core.function;

import org.junit.Test;

import static com.telenav.kivakit.core.ensure.Ensure.ensureEqual;
import static com.telenav.kivakit.core.function.Functions.applyTo;

public class FunctionsTest
{
    private record Street(String name) {}
    private record Address(Street street) {}
    private record User(Address address) {}

    private User user()
    {
        return new User(new Address(new Street("Shibo Street")));
    }

    @Test
    public void test()
    {
        var name = applyTo(user(), user ->
            applyTo(user.address(), address ->
                applyTo(address.street(), Street::name)));
        ensureEqual(name, "Shibo Street");
    }
}

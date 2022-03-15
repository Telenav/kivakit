package com.telenav.kivakit.interfaces.comparison.matchers;

import com.telenav.kivakit.interfaces.comparison.Matcher;

public class Anything<T> implements Matcher<T>
{
    @Override
    public boolean matches(final T t)
    {
        return true;
    }
}

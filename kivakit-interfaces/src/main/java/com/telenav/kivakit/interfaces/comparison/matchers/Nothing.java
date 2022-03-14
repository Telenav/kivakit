package com.telenav.kivakit.interfaces.comparison.matchers;

import com.telenav.kivakit.interfaces.comparison.Matcher;

public class Nothing<T> implements Matcher<T>
{
    @Override
    public boolean matches(final T t)
    {
        return false;
    }
}

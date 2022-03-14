package com.telenav.kivakit.interfaces.comparison.matchers;

import com.telenav.kivakit.interfaces.comparison.Matcher;

import java.util.regex.Pattern;

public class PatternMatcher implements Matcher<String>
{
    private final Pattern pattern;

    public PatternMatcher(Pattern pattern)
    {
        this.pattern = pattern;
    }

    @Override
    public boolean matches(String value)
    {
        return pattern.matcher(value).matches();
    }
}

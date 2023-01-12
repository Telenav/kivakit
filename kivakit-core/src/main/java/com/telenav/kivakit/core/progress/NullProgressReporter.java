package com.telenav.kivakit.core.progress;

import com.telenav.kivakit.core.value.count.Count;

import static com.telenav.kivakit.core.value.count.Count._0;

public class NullProgressReporter implements ProgressReporter
{
    @Override
    public Count at()
    {
        return _0;
    }

    @Override
    public void next()
    {
    }

    @Override
    public void problems(long problems)
    {
    }

    @Override
    public Count problems()
    {
        return _0;
    }
}

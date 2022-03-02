package com.telenav.kivakit.core.string;

import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

public class DifferencesTest extends UnitTest
{
    @Test
    public void test()
    {
        var differences = new Differences();
        differences.compare("First", "a", "a");
        ensure(differences.isIdentical());
        ensureEqual(differences.size(), 0);
        differences.compare("Second", "a", "b");
        ensure(differences.isDifferent());
        ensureEqual(differences.size(), 1);
    }
}

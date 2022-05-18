package com.telenav.kivakit.internal.tests.core.string;
import com.telenav.kivakit.core.string.Differences;
import com.telenav.kivakit.internal.test.support.CoreUnitTest;
import org.junit.Test;

public class DifferencesTest extends CoreUnitTest
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

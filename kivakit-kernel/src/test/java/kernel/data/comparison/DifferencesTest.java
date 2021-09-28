package kernel.data.comparison;

import com.telenav.kivakit.kernel.data.comparison.Differences;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;

public class DifferencesTest
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

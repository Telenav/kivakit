package kernel.language.mixin.mixins;

import com.telenav.kivakit.kernel.language.mixin.mixins.Attributed;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;

public class AttributedTest
{
    static class A implements Attributed<String, String>
    {
    }

    static class B implements Attributed<String, String>
    {
    }

    @Test
    public void test()
    {
        final var a = new A();
        final var b = new B();

        a.attribute("name", "This is object A");
        b.attribute("name", "This is object B");

        ensureEqual("This is object A", a.attribute("name"));
        ensureEqual("This is object B", b.attribute("name"));
    }
}

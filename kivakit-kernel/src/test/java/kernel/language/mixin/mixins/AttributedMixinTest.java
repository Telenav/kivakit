package kernel.language.mixin.mixins;

import com.telenav.kivakit.kernel.language.mixin.mixins.AttributedMixin;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;

public class AttributedMixinTest
{
    static class A implements AttributedMixin<String, String>
    {
    }

    static class B implements AttributedMixin<String, String>
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

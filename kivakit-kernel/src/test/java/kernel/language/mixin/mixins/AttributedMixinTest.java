package kernel.language.mixin.mixins;

import com.telenav.kivakit.mixins.AttributesMixin;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;

public class AttributedMixinTest
{
    static class A implements AttributesMixin<String, String>
    {
    }

    static class B implements AttributesMixin<String, String>
    {
    }

    @Test
    public void test()
    {
        var a = new A();
        var b = new B();

        a.attribute("name", "This is object A");
        b.attribute("name", "This is object B");

        ensureEqual("This is object A", a.attribute("name"));
        ensureEqual("This is object B", b.attribute("name"));
    }
}

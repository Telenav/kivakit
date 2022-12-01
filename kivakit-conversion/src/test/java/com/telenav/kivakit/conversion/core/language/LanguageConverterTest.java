package com.telenav.kivakit.conversion.core.language;

import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import java.util.List;
import java.util.regex.Pattern;

import static com.telenav.kivakit.conversion.core.language.LanguageConverterTest.Type.A;
import static com.telenav.kivakit.conversion.core.language.LanguageConverterTest.Type.B;
import static java.util.Objects.requireNonNull;

public class LanguageConverterTest extends CoreUnitTest
{
    enum Type
    {
        A,
        B
    }

    @Test
    public void testClassConverter()
    {
        ensureEqual(new ClassConverter(this).convert("java.util.List"), List.class);
    }

    @Test
    public void testEnumConverter()
    {
        EnumConverter<Type> converter = new EnumConverter<>(this, Type.class);

        ensureEqual(A, converter.convert("A"));
        ensureEqual("A", converter.unconvert(A));

        ensureEqual(B, converter.convert("B"));
        ensureEqual("B", converter.unconvert(B));
    }

    @Test
    public void testIdentityConverter()
    {
        ensureEqual(new IdentityConverter(this).convert("x"), "x");
    }

    @Test
    public void testPatternConverter()
    {
        ensureEqual(requireNonNull(new PatternConverter(this).convert("a*b")).pattern(), Pattern.compile("a*b").pattern());
    }
}

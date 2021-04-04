////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.language.string;

import com.telenav.kivakit.core.kernel.language.strings.formatting.IndentingStringBuilder;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;
import static com.telenav.kivakit.core.kernel.language.strings.formatting.IndentingStringBuilder.Indentation;
import static com.telenav.kivakit.core.kernel.language.strings.formatting.IndentingStringBuilder.Style.TEXT;

public class IndentingStringBuilderTest
{
    @Test
    public void test()
    {
        final var builder = new IndentingStringBuilder(TEXT, Indentation.of(2));
        builder.appendLine("<a>");
        builder.indent();
        builder.appendLine("<b>");
        builder.indent();
        builder.appendLine("<c/>");
        builder.unindent();
        builder.appendLine("</b>");
        builder.unindent();
        builder.appendLine("</a>");
        ensureEqual("<a>\n  <b>\n    <c/>\n  </b>\n</a>", builder.toString());
    }
}

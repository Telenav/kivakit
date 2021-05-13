////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.language.string;

import com.telenav.kivakit.kernel.language.strings.formatting.IndentingStringBuilder;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;
import static com.telenav.kivakit.kernel.language.strings.formatting.IndentingStringBuilder.Indentation;
import static com.telenav.kivakit.kernel.language.strings.formatting.IndentingStringBuilder.Style.TEXT;

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

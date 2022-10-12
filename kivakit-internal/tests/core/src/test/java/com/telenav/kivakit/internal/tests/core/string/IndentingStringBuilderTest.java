////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.internal.tests.core.string;

import com.telenav.kivakit.core.string.IndentingStringBuilder;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.string.IndentingStringBuilder.Indentation.indentation;
import static com.telenav.kivakit.core.string.IndentingStringBuilder.Style.TEXT;

public class IndentingStringBuilderTest extends CoreUnitTest
{
    @Test
    public void test()
    {
        var builder = new IndentingStringBuilder(TEXT, indentation(2));
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

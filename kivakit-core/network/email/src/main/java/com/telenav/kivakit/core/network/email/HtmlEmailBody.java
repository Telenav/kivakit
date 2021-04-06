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

package com.telenav.kivakit.core.network.email;

import com.telenav.kivakit.core.network.email.project.lexakai.diagrams.DiagramEmail;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramEmail.class)
public class HtmlEmailBody extends EmailBody
{
    public static final String MIME_TYPE = "text/html";

    public HtmlEmailBody(final String text)
    {
        super(text);
    }

    @Override
    public String mimeType()
    {
        return MIME_TYPE;
    }
}

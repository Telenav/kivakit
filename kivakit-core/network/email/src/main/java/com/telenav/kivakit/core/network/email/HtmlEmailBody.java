////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
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

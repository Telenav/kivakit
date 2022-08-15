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

package com.telenav.kivakit.network.email;

import com.telenav.kivakit.core.language.reflection.property.KivaKitExcludeProperty;
import com.telenav.kivakit.network.email.internal.lexakai.DiagramEmail;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.util.ByteArrayDataSource;

/**
 * An email attachment specified by either a byte array or a filename.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramEmail.class)
@LexakaiJavadoc(complete = true)
public class EmailAttachment
{
    private byte[] data;

    private String mimeType;

    private String filename;

    @KivaKitExcludeProperty
    public byte[] data()
    {
        return data;
    }

    public EmailAttachment data(byte[] data)
    {
        this.data = data;
        return this;
    }

    public String filename()
    {
        return filename;
    }

    public EmailAttachment filename(String filename)
    {
        this.filename = filename;
        return this;
    }

    public String mimeType()
    {
        return mimeType;
    }

    public EmailAttachment mimeType(String mimeType)
    {
        this.mimeType = mimeType;
        return this;
    }

    MimeBodyPart bodyPart()
    {
        try
        {
            var part = new MimeBodyPart();
            part.setDataHandler(new DataHandler(new ByteArrayDataSource(data, mimeType)));
            part.setFileName(filename);
            return part;
        }
        catch (MessagingException e)
        {
            throw new IllegalStateException("Cannot create body part for attachment " + this, e);
        }
    }
}

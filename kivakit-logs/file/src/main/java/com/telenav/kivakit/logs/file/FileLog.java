////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.logs.file;

import com.telenav.kivakit.core.filesystem.File;
import com.telenav.kivakit.core.kernel.language.strings.StringTo;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.kernel.logging.Log;
import com.telenav.kivakit.core.kernel.logging.loggers.LogServiceLogger;
import com.telenav.kivakit.core.resource.path.FileName;
import com.telenav.kivakit.logs.file.project.lexakai.diagrams.DiagramLogsFile;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.OutputStream;
import java.util.Map;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

/**
 * A {@link Log} service provider that logs messages to text file(s). Configuration occurs via the command line. See
 * {@link LogServiceLogger} for details. Further details are available in the markdown help. The options available for
 * configuration with this logger are:
 *
 * <ul>
 *     <li><i>file</i> - The output file</li>
 *     <li><i>maximum-size</i> - The maximum size in {@link Bytes}</li>
 *     <li><i>rollover</i> -Rollover period (none, daily or hourly)</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLogsFile.class)
@LexakaiJavadoc(complete = true)
public class FileLog extends BaseRolloverTextLog
{
    private File file;

    @Override
    public void configure(final Map<String, String> properties)
    {
        final var path = properties.get("file");
        if (path != null)
        {
            try
            {
                file = File.parse(path);
                final var rollover = properties.get("rollover");
                if (rollover != null)
                {
                    rollover(Rollover.valueOf(rollover.toUpperCase()));
                }
                final var maximumSize = properties.get("maximum-size");
                if (maximumSize != null)
                {
                    maximumLogSize(Bytes.parse(maximumSize));
                }
            }
            catch (final Exception e)
            {
                fail(e, "FileLog file parameter '" + path + "' is not valid");
            }
        }
        else
        {
            fail("FileLog missing 'file' parameter");
        }
    }

    @Override
    public String name()
    {
        return "File";
    }

    @Override
    protected OutputStream newOutputStream()
    {
        return newFile().openForWriting();
    }

    private File newFile()
    {
        return File.parse(file.withoutExtension() + "-" + FileName.dateTime(started().localTime())
                + StringTo.nonNullString(file.extension())).withoutOverwriting();
    }
}

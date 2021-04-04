////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.io;

import com.telenav.kivakit.core.kernel.language.io.LookAheadReader;
import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

import java.io.*;

public class LookAheadReaderTest extends UnitTest
{
    @Test
    public void test() throws IOException
    {
        final var reader = new LookAheadReader(new StringReader("xyz<XML"));
        while (reader.current() != '<')
        {
            reader.next();
        }
        ensureEqual('<', (char) reader.current());
        ensureEqual('X', (char) reader.lookAhead());
        ensureEqual('<', (char) reader.read());
        ensureEqual('X', (char) reader.read());
        ensureEqual('M', (char) reader.read());
        ensureEqual('L', (char) reader.read());
        ensureFalse(reader.hasNext());
        reader.close();
    }
}

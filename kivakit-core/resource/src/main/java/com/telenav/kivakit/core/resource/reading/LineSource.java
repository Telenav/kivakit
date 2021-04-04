////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.reading;

/**
 * Source of text lines
 *
 * @author jonathanl (shibo)
 */
public interface LineSource
{
    /**
     * @return The line reader
     */
    LineReader lineReader();

    /**
     * @return Lines in this line source
     */
    Iterable<String> lines();
}

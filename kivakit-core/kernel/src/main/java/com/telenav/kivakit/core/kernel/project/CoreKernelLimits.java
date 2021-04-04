////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.project;

import com.telenav.kivakit.core.kernel.language.values.count.Maximum;

public class CoreKernelLimits
{
    // Command Line
    public static final Maximum COMMAND_LINE_ARGUMENTS = Maximum.parse("1024");

    public static final Maximum COMMAND_LINE_SWITCHES = Maximum.parse("1024");

    // Debug
    public static final Maximum STACK_FRAMES = Maximum.parse("2,000");

    public static final Maximum NESTED_EXCEPTIONS = Maximum.parse("50");

    // Messaging
    public static final Maximum MESSAGE_CLASSES = Maximum.parse("100");

    // Maps
    public static final Maximum VARIABLES_PER_VARIABLE_MAP = Maximum.parse("5,000");

    // NioPaths
    public static final Maximum PATH_COMPONENTS = Maximum.parse("100");

    // Reflection
    public static final Maximum PROPERTIES_PER_OBJECT = Maximum.parse("1,000");

    public static final Maximum REFLECTED_CLASSES = Maximum.parse("5,000");

    // Logging
    public static final Maximum UNIQUE_LOG_ENTRIES = Maximum.parse("5,000");
}

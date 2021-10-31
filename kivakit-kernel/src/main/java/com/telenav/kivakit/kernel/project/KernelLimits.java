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

package com.telenav.kivakit.kernel.project;

import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;

public class KernelLimits
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    // Command Line
    public static final Maximum COMMAND_LINE_ARGUMENTS = Maximum.parseMaximum(LOGGER, "1024");

    public static final Maximum COMMAND_LINE_SWITCHES = Maximum.parseMaximum(LOGGER, "1024");

    // Debug
    public static final Maximum STACK_FRAMES = Maximum.parseMaximum(LOGGER, "2,000");

    public static final Maximum NESTED_EXCEPTIONS = Maximum.parseMaximum(LOGGER, "50");

    // Messaging
    public static final Maximum MESSAGE_CLASSES = Maximum.parseMaximum(LOGGER, "100");

    // Maps
    public static final Maximum VARIABLES_PER_VARIABLE_MAP = Maximum.parseMaximum(LOGGER, "5,000");

    // NioPaths
    public static final Maximum PATH_COMPONENTS = Maximum.parseMaximum(LOGGER, "100");

    // Reflection
    public static final Maximum PROPERTIES_PER_OBJECT = Maximum.parseMaximum(LOGGER, "1,000");

    public static final Maximum REFLECTED_CLASSES = Maximum.parseMaximum(LOGGER, "5,000");

    // Logging
    public static final Maximum UNIQUE_LOG_ENTRIES = Maximum.parseMaximum(LOGGER, "5,000");
}

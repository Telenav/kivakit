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

package com.telenav.kivakit.kernel.project;

import com.telenav.kivakit.kernel.language.values.count.Maximum;

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

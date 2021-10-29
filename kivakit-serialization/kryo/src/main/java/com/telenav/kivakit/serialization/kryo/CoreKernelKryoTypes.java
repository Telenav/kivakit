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

package com.telenav.kivakit.serialization.kryo;

import com.telenav.kivakit.kernel.language.collections.CompressibleCollection;
import com.telenav.kivakit.kernel.language.matchers.AnythingMatcher;
import com.telenav.kivakit.kernel.language.threading.context.CodeContext;
import com.telenav.kivakit.kernel.language.threading.context.StackTrace;
import com.telenav.kivakit.kernel.language.threading.status.ThreadStatus;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.time.Frequency;
import com.telenav.kivakit.kernel.language.time.LocalTime;
import com.telenav.kivakit.kernel.language.time.Meridiem;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.values.count.BitCount;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.language.values.count.Estimate;
import com.telenav.kivakit.kernel.language.values.count.LongRange;
import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.kivakit.kernel.language.values.count.MutableCount;
import com.telenav.kivakit.kernel.language.values.count.Range;
import com.telenav.kivakit.kernel.language.values.level.Level;
import com.telenav.kivakit.kernel.language.values.level.Percent;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.language.vm.JavaVirtualMachineHealth;
import com.telenav.kivakit.kernel.logging.LogEntry;
import com.telenav.kivakit.kernel.logging.LoggerCodeContext;
import com.telenav.kivakit.kernel.messaging.messages.Severity;
import com.telenav.kivakit.kernel.messaging.messages.lifecycle.OperationFailed;
import com.telenav.kivakit.kernel.messaging.messages.lifecycle.OperationHalted;
import com.telenav.kivakit.kernel.messaging.messages.lifecycle.OperationStarted;
import com.telenav.kivakit.kernel.messaging.messages.lifecycle.OperationSucceeded;
import com.telenav.kivakit.kernel.messaging.messages.status.Alert;
import com.telenav.kivakit.kernel.messaging.messages.status.CriticalAlert;
import com.telenav.kivakit.kernel.messaging.messages.status.Glitch;
import com.telenav.kivakit.kernel.messaging.messages.status.Information;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.kernel.messaging.messages.status.Trace;
import com.telenav.kivakit.kernel.messaging.messages.status.Warning;
import com.telenav.kivakit.kernel.messaging.messages.status.activity.Activity;
import com.telenav.kivakit.kernel.project.Release;
import com.telenav.kivakit.serialization.kryo.project.lexakai.diagrams.DiagramSerializationKryo;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Kryo types to register for kivakit-core-kernel
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
@UmlClassDiagram(diagram = DiagramSerializationKryo.class)
public class CoreKernelKryoTypes extends KryoTypes
{
    public CoreKernelKryoTypes()
    {
        //----------------------------------------------------------------------------------------------
        // NOTE: To maintain backward compatibility, classes are assigned identifiers by KryoSerialization.
        // If classes are appended to groups and no classes are removed, older data can always be read.
        //----------------------------------------------------------------------------------------------

        group("primitives", () ->
        {
            register(byte[].class);
            register(short[].class);
            register(int[].class);
            register(long[].class);
            register(char[].class);
            register(Object[].class);
            register(Class.class);
        });

        group("scalars", () ->
        {
            register(Version.class);
            register(Release.class);
            register(Count.class);
            register(MutableCount.class);
            register(Level.class);
            register(Percent.class);
            register(Bytes.class);
            register(Maximum.class);
            register(Estimate.class);
            register(BitCount.class);
            register(Range.class);
            register(LongRange.class);
        });

        group("collections", () ->
        {
            register(HashMap.class);
            register(HashSet.class);
            register(ArrayList.class);
            register(LinkedList.class);
            register(CompressibleCollection.Method.class);
        });

        group("language", () ->
        {
            register(StackTrace.class);
            register(StackTrace.Frame.class);
            register(StackTrace.Frame[].class);
            register(JavaVirtualMachineHealth.class);
            register(ThreadStatus.class);
            register(Thread.State.class);
        });

        group("matching", () -> register(AnythingMatcher.class));

        group("time", () ->
        {
            register(Time.class);
            register(Duration.class);
            register(LocalTime.class);
            register(Meridiem.class);
            register(Frequency.class);
            register(ZoneId.class);
        });

        group("messages", () ->
        {
            register(Severity.class);
            register(OperationStarted.class);
            register(OperationSucceeded.class);
            register(OperationFailed.class);
            register(OperationHalted.class);
            register(Activity.class);
            register(Alert.class);
            register(CriticalAlert.class);
            register(Information.class);
            register(Problem.class);
            register(Glitch.class);
            register(Trace.class);
            register(Warning.class);
        });

        group("logging", () ->
        {
            register(LogEntry.class);
            register(LoggerCodeContext.class);
            register(CodeContext.class);
        });
    }
}

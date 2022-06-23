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

package com.telenav.kivakit.serialization.kryo.types;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.logging.LoggerCodeContext;
import com.telenav.kivakit.core.messaging.context.CodeContext;
import com.telenav.kivakit.core.messaging.context.StackTrace;
import com.telenav.kivakit.core.messaging.messages.Severity;
import com.telenav.kivakit.core.messaging.messages.lifecycle.OperationFailed;
import com.telenav.kivakit.core.messaging.messages.lifecycle.OperationHalted;
import com.telenav.kivakit.core.messaging.messages.lifecycle.OperationStarted;
import com.telenav.kivakit.core.messaging.messages.lifecycle.OperationSucceeded;
import com.telenav.kivakit.core.messaging.messages.status.Alert;
import com.telenav.kivakit.core.messaging.messages.status.CriticalAlert;
import com.telenav.kivakit.core.messaging.messages.status.Glitch;
import com.telenav.kivakit.core.messaging.messages.status.Information;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Trace;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.core.messaging.messages.status.activity.Activity;
import com.telenav.kivakit.core.time.Day;
import com.telenav.kivakit.core.time.DayOfWeek;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Frequency;
import com.telenav.kivakit.core.time.Hour;
import com.telenav.kivakit.core.time.HourOfWeek;
import com.telenav.kivakit.core.time.HourOfWeekSpan;
import com.telenav.kivakit.core.time.LocalTime;
import com.telenav.kivakit.core.time.Meridiem;
import com.telenav.kivakit.core.time.Minute;
import com.telenav.kivakit.core.time.Month;
import com.telenav.kivakit.core.time.Quarter;
import com.telenav.kivakit.core.time.Rate;
import com.telenav.kivakit.core.time.Second;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.time.TimeSpan;
import com.telenav.kivakit.core.time.Week;
import com.telenav.kivakit.core.time.Year;
import com.telenav.kivakit.core.value.count.BitCount;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Estimate;
import com.telenav.kivakit.core.value.count.LongRange;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.core.value.count.MutableCount;
import com.telenav.kivakit.core.value.count.Range;
import com.telenav.kivakit.core.value.level.Level;
import com.telenav.kivakit.core.value.level.Percent;
import com.telenav.kivakit.core.version.Release;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.core.vm.JavaVirtualMachineHealth;
import com.telenav.kivakit.core.vm.ThreadStatus;
import com.telenav.kivakit.interfaces.time.Nanoseconds;
import com.telenav.kivakit.serialization.kryo.lexakai.DiagramKryo;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Kryo types to register for kivakit-core
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
@UmlClassDiagram(diagram = DiagramKryo.class)
public class CoreKryoTypes extends KryoTypes
{
    public CoreKryoTypes()
    {
        //----------------------------------------------------------------------------------------------
        // NOTE: To maintain backward compatibility, classes are assigned identifiers by KryoTypes.
        // If classes are appended to a group, and no classes are removed, older data can always be read.
        //----------------------------------------------------------------------------------------------

        group("primitives", () ->
        {
            register(byte[].class);
            register(short[].class);
            register(int[].class);
            register(long[].class);
            register(char[].class);
            register(Object[].class);
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
            register(ObjectList.class);
            register(ObjectSet.class);
        });

        group("language", () ->
        {
            register(StackTrace.class);
            register(StackTrace.Frame.class);
            register(StackTrace.Frame[].class);
            register(JavaVirtualMachineHealth.class);
            register(ThreadStatus.class);
            register(Thread.State.class);
            register(Class.class);
        });

        group("time", () ->
        {
            register(Time.class);
            register(Duration.class);
            register(LocalTime.class);
            register(Meridiem.class);
            register(Frequency.class);
            register(ZoneId.class);
            register(Day.class);
            register(Day.Type.class);
            register(DayOfWeek.class);
            register(Hour.class);
            register(HourOfWeek.class);
            register(HourOfWeekSpan.class);
            register(Minute.class);
            register(Month.class);
            register(Quarter.class);
            register(Rate.class);
            register(Second.class);
            register(TimeSpan.class);
            register(Week.class);
            register(Year.class);
            register(Nanoseconds.class);
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

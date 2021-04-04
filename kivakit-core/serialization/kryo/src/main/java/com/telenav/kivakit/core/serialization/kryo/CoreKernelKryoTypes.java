////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package

        com.telenav.kivakit.core.serialization.kryo;

import com.telenav.kivakit.core.kernel.language.collections.CompressibleCollection;
import com.telenav.kivakit.core.kernel.language.matching.matchers.All;
import com.telenav.kivakit.core.kernel.language.threading.context.CodeContext;
import com.telenav.kivakit.core.kernel.language.threading.context.StackTrace;
import com.telenav.kivakit.core.kernel.language.threading.status.ThreadStatus;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.time.Frequency;
import com.telenav.kivakit.core.kernel.language.time.LocalTime;
import com.telenav.kivakit.core.kernel.language.time.Meridiem;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.language.values.count.*;
import com.telenav.kivakit.core.kernel.language.values.level.Level;
import com.telenav.kivakit.core.kernel.language.values.level.Percent;
import com.telenav.kivakit.core.kernel.language.values.version.Version;
import com.telenav.kivakit.core.kernel.language.vm.JavaVirtualMachineHealth;
import com.telenav.kivakit.core.kernel.logging.LogEntry;
import com.telenav.kivakit.core.kernel.logging.LoggerCodeContext;
import com.telenav.kivakit.core.kernel.messaging.messages.Severity;
import com.telenav.kivakit.core.kernel.messaging.messages.lifecycle.OperationFailed;
import com.telenav.kivakit.core.kernel.messaging.messages.lifecycle.OperationHalted;
import com.telenav.kivakit.core.kernel.messaging.messages.lifecycle.OperationStarted;
import com.telenav.kivakit.core.kernel.messaging.messages.lifecycle.OperationSucceeded;
import com.telenav.kivakit.core.kernel.messaging.messages.status.*;
import com.telenav.kivakit.core.kernel.project.Release;
import com.telenav.kivakit.core.serialization.kryo.project.lexakai.diagrams.DiagramSerializationKryo;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * @author jonathanl (shibo)
 */
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

        group("matching", () -> register(All.class));

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
            register(Quibble.class);
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

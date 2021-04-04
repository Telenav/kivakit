////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.language.vm;

import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.kernel.language.vm.JavaVirtualMachine;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;

public class JavaVirtualMachineTest
{
    @Test
    public void test()
    {
        final var vm = JavaVirtualMachine.local();
        ensure(vm.freeMemory().isGreaterThan(Bytes._0));
        ensure(vm.freeMemory().isLessThan(vm.maximumMemory()));
        ensure(vm.usedMemory().isGreaterThan(Bytes._0));
        ensure(vm.usedMemory().isLessThan(vm.maximumMemory()));
    }

    @Ignore
    public void testSizeOf()
    {
        final var vm = JavaVirtualMachine.local();
        ensureEqual(Bytes.bytes(4), vm.sizeOfPrimitive(4));
        ensureEqual(Bytes.bytes(8), vm.sizeOfPrimitive(4L));
        ensureEqual(Bytes.bytes(8), vm.sizeOfPrimitive(4.0));
        final var sizeOfTenThousandIntegers = vm.sizeOf(new int[10_000]);
        if (sizeOfTenThousandIntegers != null)
        {
            ensure(sizeOfTenThousandIntegers.isGreaterThanOrEqualTo(Bytes.bytes(4 * 10_000)));
            ensure(sizeOfTenThousandIntegers.isLessThanOrEqualTo(Bytes.bytes(4 * 10_000 + 32)));
        }
    }

    @Ignore
    public void testSizeOfGraph()
    {
        final var vm = JavaVirtualMachine.local();
        final Set<Integer> set = new HashSet<>();
        for (var i = 0; i < 100; i++)
        {
            set.add(i);
        }
        final var size = vm.sizeOfObjectGraph(set, "test", Bytes._0);
        if (size != null)
        {
            ensure(size.isGreaterThan(Bytes.bytes(100 * 50)));
        }
    }
}

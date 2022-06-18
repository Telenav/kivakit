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

package com.telenav.kivakit.internal.tests.core.vm;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.vm.JavaVirtualMachine;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class JavaVirtualMachineTest extends CoreUnitTest
{
    @Test
    public void test()
    {
        var vm = JavaVirtualMachine.local();
        ensure(vm.freeMemory().isGreaterThan(Bytes._0));
        ensure(vm.freeMemory().isLessThan(vm.maximumMemory()));
        ensure(vm.usedMemory().isGreaterThan(Bytes._0));
        ensure(vm.usedMemory().isLessThan(vm.maximumMemory()));
    }

    @Ignore
    public void testSizeOf()
    {
        var vm = JavaVirtualMachine.local();
        ensureEqual(Bytes.bytes(4), vm.sizeOfPrimitive(4));
        ensureEqual(Bytes.bytes(8), vm.sizeOfPrimitive(4L));
        ensureEqual(Bytes.bytes(8), vm.sizeOfPrimitive(4.0));
        var sizeOfTenThousandIntegers = vm.sizeOf(new int[10_000]);
        if (sizeOfTenThousandIntegers != null)
        {
            ensure(sizeOfTenThousandIntegers.isGreaterThanOrEqualTo(Bytes.bytes(4 * 10_000)));
            ensure(sizeOfTenThousandIntegers.isLessThanOrEqualTo(Bytes.bytes(4 * 10_000 + 32)));
        }
    }

    @Ignore
    public void testSizeOfGraph()
    {
        var vm = JavaVirtualMachine.local();
        final Set<Integer> set = new HashSet<>();
        for (var i = 0; i < 100; i++)
        {
            set.add(i);
        }
        var size = vm.sizeOfObjectGraph(set, "test", Bytes._0);
        if (size != null)
        {
            ensure(size.isGreaterThan(Bytes.bytes(100 * 50)));
        }
    }
}

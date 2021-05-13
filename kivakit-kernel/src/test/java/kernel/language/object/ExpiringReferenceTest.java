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

package kernel.language.object;

import com.telenav.kivakit.kernel.language.objects.reference.ExpiringReference;
import com.telenav.kivakit.kernel.language.time.Duration;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;

public class ExpiringReferenceTest
{
    @Test
    public void test()
    {
        final ExpiringReference<Integer> reference = new ExpiringReference<>(Duration.milliseconds(10))
        {
            int i;

            @Override
            protected Integer onNewObject()
            {
                return i++;
            }
        };

        // Initially the reference will be to the integer 0.
        ensureEqual(0, reference.get());

        // We wait for it to expire,
        Duration.milliseconds(150).sleep();

        // then retrieving the reference will cause it to become 1
        ensureEqual(1, reference.get());

        // and it should stay 1 until
        ensureEqual(1, reference.get());

        // it expires again,
        Duration.milliseconds(150).sleep();

        // at which point it will become 2
        ensureEqual(2, reference.get());

        // and stay 2
        ensureEqual(2, reference.get());
    }
}

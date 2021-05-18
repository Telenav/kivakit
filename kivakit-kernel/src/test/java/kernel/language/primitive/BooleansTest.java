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

package kernel.language.primitive;

import com.telenav.kivakit.kernel.language.primitives.Booleans;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureFalse;

public class BooleansTest
{
    @Test
    public void test()
    {
        ensure(Booleans.isTrue("true"));
        ensure(Booleans.isTrue("yes"));
        ensure(Booleans.isTrue("enabled"));
        ensure(Booleans.isTrue("True"));
        ensure(Booleans.isTrue("Yes"));
        ensure(Booleans.isTrue("Enabled"));
        ensureFalse(Booleans.isTrue("1"));
        ensureFalse(Booleans.isTrue(""));
        ensureFalse(Booleans.isTrue(null));
    }
}

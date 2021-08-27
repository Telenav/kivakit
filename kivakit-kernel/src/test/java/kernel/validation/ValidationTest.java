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

package kernel.validation;

import com.telenav.kivakit.kernel.data.validation.BaseValidator;
import com.telenav.kivakit.kernel.data.validation.Validatable;
import com.telenav.kivakit.kernel.data.validation.ValidationType;
import com.telenav.kivakit.kernel.data.validation.Validator;
import org.junit.Assert;

public class ValidationTest
{
    public static class A implements Validatable
    {
        final int x;

        public A(final int x)
        {
            this.x = x;
        }

        @Override
        public Validator validator(final ValidationType type)
        {
            return new BaseValidator()
            {
                @Override
                protected void onValidate()
                {
                    warningIf(x > 7, "x cannot be > 7");
                    try
                    {
                        problemIf(x == 0, "x cannot be zero");
                        Assert.fail("Should have thrown an exception");
                    }
                    catch (final AssertionError ignored)
                    {
                    }
                }
            };
        }
    }
}

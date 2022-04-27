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

package com.telenav.kivakit.validation;

import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.test.support.CoreUnitTest;

public class ValidationTest extends CoreUnitTest
{
    public static class A implements Validatable
    {
        final int x;

        public A(int x)
        {
            this.x = x;
        }

        @Override
        public Validator validator(ValidationType type)
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
                        //noinspection MaskedAssertion
                        Ensure.fail("Should have thrown an exception");
                    }
                    catch (AssertionError ignored)
                    {
                    }
                }
            };
        }
    }
}

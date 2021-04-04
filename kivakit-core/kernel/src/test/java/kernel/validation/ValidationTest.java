////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.validation;

import com.telenav.kivakit.core.kernel.data.validation.Validatable;
import com.telenav.kivakit.core.kernel.data.validation.Validation;
import com.telenav.kivakit.core.kernel.data.validation.Validator;
import com.telenav.kivakit.core.kernel.data.validation.validators.BaseValidator;
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
        public Validator validator(final Validation type)
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

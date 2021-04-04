////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.language.object;

import com.telenav.kivakit.core.kernel.language.objects.reference.ExpiringReference;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;

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

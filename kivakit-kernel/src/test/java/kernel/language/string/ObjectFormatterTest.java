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

package kernel.language.string;

import com.telenav.kivakit.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import org.junit.Assert;
import org.junit.Test;

public class ObjectFormatterTest
{
    public static class Bean
    {
        @KivaKitIncludeProperty
        final int y = 7;

        public int getY()
        {
            return y;
        }

        @Override
        public String toString()
        {
            return new ObjectFormatter(this).toString();
        }
    }

    final Bean bean = new Bean();

    @KivaKitIncludeProperty
    public Bean bean()
    {
        return bean;
    }

    @Test
    public void test()
    {
        final var beanString = "[ObjectFormatterTest.Bean y = 7]";
        Assert.assertEquals(beanString, new ObjectFormatter(bean).toString());
        Assert.assertEquals("[ObjectFormatterTest bean = " + beanString + "]", new ObjectFormatter(this).toString());
    }
}

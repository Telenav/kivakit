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

package com.telenav.kivakit.kernel.interfaces.code;

/**
 * Code that can be executed, returning a value, as opposed to {@link Runnable} which does not return a value.
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
public interface Code<Value>
{
    static <T> Code<T> of(final Code<T> code)
    {
        return code;
    }

    static <T> Code<T> of(final Runnable code)
    {
        return of(() ->
        {
            code.run();
            return null;
        });
    }

    /**
     * @return Executes the code
     */
    Value run();
}

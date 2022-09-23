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

package com.telenav.kivakit.interfaces.value;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.interfaces.factory.Factory;

import java.util.function.Supplier;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNNECESSARY;

/**
 * A source of values.
 *
 * @param <Value> The object type
 * @author jonathanl (shibo)
 * @see Factory
 */
@FunctionalInterface
@CodeQuality(stability = STABLE,
             testing = UNNECESSARY,
             documentation = COMPLETE)
public interface Source<Value> extends Supplier<Value>
{
}

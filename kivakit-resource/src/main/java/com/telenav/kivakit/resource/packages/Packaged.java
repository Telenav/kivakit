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

package com.telenav.kivakit.resource.packages;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import static com.telenav.kivakit.resource.packages.PackagePath.packagePath;

/**
 * An object having a (KivaKit) {@link Package}.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public interface Packaged
{
    /**
     * @return The {@link Package} for this object
     */
    default Package _package()
    {
        return new Package(Listener.throwingListener(), packagePath(getClass()));
    }
}

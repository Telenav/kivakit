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

package com.telenav.kivakit.resource;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * The mode for copying resources into a folder. Nested resources can be copied preserving the hierarchy, or by
 * flattening the hierarchy
 *
 * <p><b>Values</b></p>
 *
 * <ul>
 *     <li>{@link #FLATTEN}</li>
 *     <li>{@link #PRESERVE_HIERARCHY}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public enum FolderCopyMode
{
    /** Copy resources by filename from the source, without folder hierarchy */
    FLATTEN,

    /** Copy resources by path from the source, preserving the folder hierarchy */
    PRESERVE_HIERARCHY
}

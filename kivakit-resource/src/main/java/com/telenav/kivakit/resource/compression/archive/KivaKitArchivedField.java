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

package com.telenav.kivakit.resource.compression.archive;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceArchive;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Annotation for labeling fields that should be archived.
 *
 * @author jonathanl (shibo)
 * @see FieldArchive
 * @see ArchivedFields
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@UmlClassDiagram(diagram = DiagramResourceArchive.class)
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public @interface KivaKitArchivedField
{
    /**
     * @return True if this field should be lazy-loaded
     */
    boolean lazy() default false;
}

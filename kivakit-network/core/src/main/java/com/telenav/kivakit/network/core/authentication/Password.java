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

package com.telenav.kivakit.network.core.authentication;

import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.network.core.internal.lexakai.DiagramAuthentication;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A password can be used to match against another password to perform authentication.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramAuthentication.class)
@LexakaiJavadoc(complete = true)
public interface Password extends Matcher<Password>
{
}

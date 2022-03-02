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

package com.telenav.kivakit.network.http;

import com.telenav.kivakit.network.http.project.lexakai.DiagramHttp;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

/**
 * The status of an HTTP request.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramHttp.class)
@LexakaiJavadoc(complete = true)
public class HttpStatus
{
    private final int code;

    public HttpStatus(int code)
    {
        this.code = code;
    }

    /**
     * @return True if this status code is accompanied by an error message
     */
    public boolean hasAssociatedErrorMessages()
    {
        switch (code)
        {
            case SC_INTERNAL_SERVER_ERROR:
            case SC_BAD_REQUEST:
            case SC_UNAUTHORIZED:
                return true;

            default:
                return false;
        }
    }

    /**
     * @return True if this status code represents request failure
     */
    public boolean isFailure()
    {
        return !isOkay();
    }

    /**
     * @return True if this status code represents request success
     */
    public boolean isOkay()
    {
        return code >= 200 && code <= 300;
    }

    @Override
    public String toString()
    {
        return Integer.toString(code);
    }
}

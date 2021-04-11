////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.web.swagger;

import com.telenav.kivakit.core.resource.resources.packaged.PackageResource;
import com.telenav.kivakit.web.jetty.resources.BaseJettyResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Provides the Swagger JavaScript resources required to show Swagger documentation.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class JettySwaggerIndex extends BaseJettyResource
{
    /**
     * Servlet that produces the Swagger index
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    class IndexServlet extends HttpServlet
    {
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException
        {
            response.setContentType("text/html");
            try (final var out = response.getWriter())
            {
                out.println(index());
            }
        }
    }

    private final int port;

    /**
     * @param port The port where Swagger is running
     */
    public JettySwaggerIndex(final int port)
    {
        super("[SwaggerIndex]");
        this.port = port;
    }

    @Override
    public ServletHolder holder()
    {
        return new ServletHolder(name(), new IndexServlet());
    }

    String index()
    {
        return PackageResource.of(JettySwaggerIndex.class, "webapp/index.html")
                .reader()
                .string()
                .replaceAll("PORT", Integer.toString(port));
    }
}

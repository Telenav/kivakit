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

package

        com.telenav.kivakit.core.serialization.jersey.json;

import com.google.gson.Gson;
import com.telenav.kivakit.core.serialization.json.GsonFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @author jonathanl (shibo)
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class JerseyGsonSerializer<T> implements MessageBodyReader<T>, MessageBodyWriter<T>
{
    private final Gson gson;

    public JerseyGsonSerializer(final GsonFactory factory)
    {
        gson = factory.newInstance();
    }

    @Override
    public long getSize(final T object, final Class<?> type, final Type genericType,
                        final Annotation[] annotations, final MediaType mediaType)
    {
        return -1;
    }

    @Override
    public boolean isReadable(final Class<?> type, final Type genericType,
                              final Annotation[] annotations, final MediaType mediaType)
    {
        return true;
    }

    @Override
    public boolean isWriteable(final Class<?> type, final Type genericType,
                               final Annotation[] annotations, final MediaType mediaType)
    {
        return true;
    }

    @Override
    public T readFrom(final Class<T> type, final Type genericType,
                      final Annotation[] annotations, final MediaType mediaType,
                      final MultivaluedMap<String, String> map, final InputStream in)
            throws IOException, WebApplicationException
    {
        try (final InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8))
        {
            return gson.fromJson(reader, type);
        }
    }

    @Override
    public void writeTo(final T object, final Class<?> type, final Type genericType,
                        final Annotation[] annotations, final MediaType mediaType,
                        final MultivaluedMap<String, Object> httpHeaders, final OutputStream out)
            throws WebApplicationException
    {
        try (final var writer = new PrintWriter(out))
        {
            final var json = gson.toJson(object);
            writer.write(json);
            writer.flush();
        }
    }
}

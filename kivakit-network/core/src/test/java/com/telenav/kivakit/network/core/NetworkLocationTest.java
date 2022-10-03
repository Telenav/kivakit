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

package com.telenav.kivakit.network.core;

import com.telenav.kivakit.testing.UnitTest;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static com.telenav.kivakit.network.core.Loopback.loopback;

public class NetworkLocationTest extends UnitTest
{
    @Test
    public void test() throws MalformedURLException
    {
        var location = new NetworkLocation(loopback().http().path(this, "/foo"));
        ensureEqual(loopback(), location.host());
        ensureEqual(80, location.port().portNumber());
        ensureEqual(location.toString(), "http://localhost/foo");
        ensureEqual(new URL("http://127.0.0.1/foo"), location.asUrl());
        ensureEqual(Protocol.HTTP, location.protocol());
        var parameters = new QueryParameters("x=9");
        location.queryParameters(parameters);
        ensureEqual("9", location.queryParameters().asVariableMap().get("x"));
    }
}

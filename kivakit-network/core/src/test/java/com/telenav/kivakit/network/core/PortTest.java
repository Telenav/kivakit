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

import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

public class PortTest extends UnitTest
{
    @Test
    public void testPort()
    {
        var host = new Host("192.168.0.4");
        var port = host.http(8081);
        ensureEqual("192.168.0.4:8081", port.toString());
    }

    @Test
    public void testProtocol()
    {
        var host = new Host("192.168.0.4");
        var port = host.http(8081);
        port.protocol(Protocol.HTTP);
        ensure(port.isHttp());
    }
}

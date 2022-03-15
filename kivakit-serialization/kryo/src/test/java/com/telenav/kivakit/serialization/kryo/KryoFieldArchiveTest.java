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

package com.telenav.kivakit.serialization.kryo;

import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.naming.NamedObject;
import com.telenav.kivakit.resource.compression.archive.FieldArchive;
import com.telenav.kivakit.resource.compression.archive.KivaKitArchivedField;
import org.junit.Test;

import java.io.Serializable;

import static com.telenav.kivakit.resource.compression.archive.ZipArchive.Mode.READ;
import static com.telenav.kivakit.resource.compression.archive.ZipArchive.Mode.WRITE;

public class KryoFieldArchiveTest extends KryoUnitTest
{
    public static class TestClass implements NamedObject, Serializable
    {
        private static final long serialVersionUID = 6038256860472601994L;

        @KivaKitArchivedField
        private final String x = "this is a test of the emergency broadcasting system";

        @KivaKitArchivedField
        private final int y = 5;

        @Override
        public String objectName()
        {
            return "test";
        }

        public String x()
        {
            return x;
        }

        public int y()
        {
            return y;
        }
    }

    @Test
    public void test()
    {
        var file = Folder.kivakitTest(getClass()).file("field-archive-test.zip");

        var sessionFactory = sessionFactory();

        var serializer = new KryoObjectSerializer(kryoTypes());

        try (var archive = listenTo(new FieldArchive(file, WRITE)))
        {
            var test = new TestClass();
            archive.saveFieldsOf(serializer, test, Version.parseVersion(this, "1.0"));
        }

        try (var archive = listenTo(new FieldArchive(file, READ)))
        {
            var test = new TestClass();
            archive.loadFieldOf(serializer, test, "x");
            ensureEqual(test.x, "this is a test of the emergency broadcasting system");
            archive.loadFieldOf(serializer, test, "y");
            ensureEqual(test.y, 5);
        }

        try (var archive = listenTo(new FieldArchive(file, READ)))
        {
            var test = new TestClass();
            archive.loadFieldsOf(serializer, test);
            ensureEqual(test.x, "this is a test of the emergency broadcasting system");
            ensureEqual(test.y, 5);
        }
    }
}

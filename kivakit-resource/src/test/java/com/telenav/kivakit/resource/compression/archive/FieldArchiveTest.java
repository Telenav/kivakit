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

import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.interfaces.naming.NamedObject;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.serialization.kryo.CoreKernelKryoTypes;
import com.telenav.kivakit.serialization.kryo.KryoTypes;
import com.telenav.kivakit.serialization.kryo.KryoUnitTest;
import org.junit.Test;

import java.io.Serializable;

public class FieldArchiveTest extends KryoUnitTest
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
    public void testSaveAndLoad()
    {
        var file = Folder.kivakitTest(getClass()).file("field-archive-test.zip");

        var sessionFactory = sessionFactory();

        try (var archive = listenTo(new FieldArchive(file, sessionFactory, ProgressReporter.NULL, ZipArchive.Mode.WRITE)))
        {
            final var test = new TestClass();
            archive.saveFieldsOf(test, Version.parse(this, "1.0"));
        }

        try (var archive = listenTo(new FieldArchive(file, sessionFactory, ProgressReporter.NULL, ZipArchive.Mode.READ)))
        {
            var test = new TestClass();
            archive.loadFieldOf(test, "x");
            ensureEqual(test.x, "this is a test of the emergency broadcasting system");
            archive.loadFieldOf(test, "y");
            ensureEqual(test.y, 5);
        }

        try (var archive = listenTo(new FieldArchive(file, sessionFactory, ProgressReporter.NULL, ZipArchive.Mode.READ)))
        {
            var test = new TestClass();
            archive.loadFieldsOf(test);
            ensureEqual(test.x, "this is a test of the emergency broadcasting system");
            ensureEqual(test.y, 5);
        }
    }

    @Override
    protected KryoTypes kryoTypes()
    {
        return new CoreKernelKryoTypes();
    }
}

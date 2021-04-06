////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.filesystems.hdfs;

import com.telenav.kivakit.core.filesystem.Folder;
import com.telenav.kivakit.core.network.core.EmailAddress;
import com.telenav.kivakit.core.test.UnitTest;
import com.telenav.kivakit.core.test.annotations.SlowTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category({ SlowTests.class })
public class HdfsFileSystemTest extends UnitTest
{
    /**
     * The navhacluster doesn't seem to work from the EU, so we can turn off the relevant tests here
     */
    private static final boolean TEST_NAVTEAM_CLUSTER = false;

    /** Only run the tests about one in every ten times to speed up the build */
    private static final boolean TEST_OSMTEAM_CLUSTER = true;

    @Override
    public void testBeforeUnitTest()
    {
        final var settings = new HdfsSettings()
                .configurationFolder(com.telenav.kivakit.core.resource.resources.packaged.Package.of(getClass(), "configurations/test"))
                .contactEmail(EmailAddress.parse("jonathanl@telenav.com"))
                .username("automation")
                .proxyJar();
    }

    @Test
    public void testFolder()
    {
        if (TEST_OSMTEAM_CLUSTER)
        {
            final var folder = Folder.parse(osmteamZambia().toString());
            assert folder != null;
            ensure(folder.exists());
            ensure(!folder.isEmpty());
            ensure(folder.nestedFiles().size() > 1);
        }
    }

    @Test
    public void testFoldersAndFiles()
    {
        if (TEST_OSMTEAM_CLUSTER)
        {
            final var hdfs = osmteamHdfs().toString();
            ensureEqual(hdfs + "foo/bar", osmteamHdfs().folder("foo/bar").toString());
            ensureEqual(hdfs + "foo/bar/baz.txt", osmteamHdfs().file("foo/bar/baz.txt").toString());
            ensureEqual(hdfs + "foo/bar/baz.txt", osmteamHdfs().folder("foo/bar").file("baz.txt").toString());
            ensureEqual(hdfs + "foo/bar/baz.txt", osmteamHdfs().folder("foo").file("bar/baz.txt").toString());
        }
    }

    @Test
    public void testListFiles()
    {
        if (TEST_OSMTEAM_CLUSTER)
        {
            ensure(osmteamZambia().files().size() > 1);
        }
    }

    @Test
    public void testListFolders()
    {
        if (TEST_OSMTEAM_CLUSTER)
        {
            final var folders = osmteamZambia().folders();
            ensure(folders.size() > 1);
        }
    }

    @Test
    public void testReadFile()
    {
        if (TEST_OSMTEAM_CLUSTER)
        {
            // Read a file from the osmteam HDFS
            final var file = osmteamHdfs().file("/graph-api/graph-server/repository/Zambia/test.txt");
            var i = 1;
            for (final String line : file.reader().lines())
            {
                ensureEqual(i++, Integer.parseInt(line));
            }
        }

        if (TEST_NAVTEAM_CLUSTER)
        {
            // Read a file from the navteam HDFS to test using multiple HDFS filesystems
            final var file = navteamHdfs().file("graph-api/kivakit-hdfs-unit-test-do-not-remove.txt");
            ensure(file.exists());
            var i = 1;
            for (final String line : file.reader().lines())
            {
                ensureEqual(i++, Integer.parseInt(line));
            }
            ensureEqual(6, i);
        }
    }

    private Folder navteamHdfs()
    {
        return Folder.parse("hdfs://navhacluster");
    }

    private Folder osmteamHdfs()
    {
        return Folder.parse("hdfs://cluster1ns");
    }

    private Folder osmteamZambia()
    {
        return osmteamHdfs().folder("graph-api/graph-server/repository/Zambia");
    }
}

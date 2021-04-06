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

package com.telenav.kivakit.filesystems.hdfs.proxy.spi;

import com.telenav.kivakit.core.kernel.language.values.version.Version;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.filesystems.hdfs.proxy.spi.project.lexakai.diagrams.DiagramHdfsSpi;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

@UmlClassDiagram(diagram = DiagramHdfsSpi.class)
public interface HdfsProxy extends Remote
{
    /** The name of the proxy client's entry in the RMI registry */
    String RMI_REGISTRY_NAME = "kivakit-hdfs-proxy";

    /** The RMI port being used */
    int RMI_REGISTRY_PORT = 1099;

    Version VERSION = Version.parse("8.1");

    Logger LOGGER = LoggerFactory.newLogger();

    boolean deleteFile(final String path) throws RemoteException;

    boolean deleteFolder(final String path) throws RemoteException;

    boolean exists(final String path) throws RemoteException;

    List<String> files(final String path) throws RemoteException;

    List<String> folders(final String path) throws RemoteException;

    boolean isFile(final String path) throws RemoteException;

    boolean isFolder(final String path) throws RemoteException;

    boolean isWritable(final String path) throws RemoteException;

    long lastModified(final String path) throws RemoteException;

    boolean lastModified(final String pathAsString, final long time) throws RemoteException;

    long length(final String path) throws RemoteException;

    boolean mkdirs(final String path) throws RemoteException;

    List<String> nestedFiles(final String path) throws RemoteException;

    long openForReading(final String path) throws RemoteException;

    long openForWriting(final String path) throws RemoteException;

    boolean rename(final String from, final String to) throws RemoteException;

    String root(final String path) throws RemoteException;

    String temporaryFile(final String path, final String baseName) throws RemoteException;

    String temporaryFolder(final String path, final String baseName) throws RemoteException;
}

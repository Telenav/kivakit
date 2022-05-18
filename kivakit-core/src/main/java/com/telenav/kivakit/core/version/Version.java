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

package com.telenav.kivakit.core.version;

import com.telenav.kivakit.core.language.primitive.Ints;
import com.telenav.kivakit.core.messaging.Listener;

import java.util.Objects;
import java.util.regex.Pattern;

import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * Represents a <a href=https://semver.org><i>semantic version</i></a>, such as "6.3" or "1.2.1" or "6.3-rc". Supports
 * {@link #major()}, {@link #minor()}, {@link #patch()}, {@link #release()} and snapshot values.
 *
 * <p><b>Parsing</b></p>
 *
 * <p>
 * Versions can be created by parsing a {@link String} with {@link #parseVersion(Listener listener, String)} as well as
 * by using the of() factory methods, passing in major, minor, patch and release values.
 * </p>
 *
 * <p><b>Information</b></p>
 *
 * <ul>
 *     <li>{@link #major()} - The major version</li>
 *     <li>{@link #minor()} - The minor version</li>
 *     <li>{@link #patch()} - The optional 'dot' revision, or NO_REVISION if there is none</li>
 *     <li>{@link #release()} - The release name, or null if there is none</li>
 *     <li>{@link #hasPatch()} - True if this version has a revision value</li>
 *     <li>{@link #hasRelease()} - True if this version has a release name</li>
 *     <li>{@link #isSnapshot()} - True if this version is a snapshot release</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #withoutPatch()} - This version without the revision value</li>
 *     <li>{@link #withoutRelease()} - This version without the release name</li>
 * </ul>
 *
 * <p><b>Comparison</b></p>
 *
 * <ul>
 *     <li>#equals(Object) - True if the versions are equal</li>
 *     <li>{@link #isNewerThan(Version)} - True if this version is newer than the given version</li>
 *     <li>{@link #isNewerThanOrEqualTo(Version)} - True if this version is newer than or the same as the given version</li>
 *     <li>{@link #isOlderThan(Version)} - True if this version is older than the given version</li>
 *     <li>{@link #isOlderThanOrEqualTo(Version)} - True if this version is older than or the same as the given version</li>
 *     <li>{@link #newer(Version)} - The newer of this version and the given version</li>
 *     <li>{@link #older(Version)} - The older of this version and the given version</li>
 * </ul>
 *
 * <p>
 * {@link Version} objects implement the {@link #hashCode()} / {@link #equals(Object)} contract.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see <a href=https://semver.org>*Semantic Versioning*</a>
 */
public class Version
{
    /** Value for no patch revision */
    public static final int NO_PATCH = -1;

    /** Value for no minor revision */
    public static final int NO_MINOR = -1;

    /** Pattern to match versions of the form [major](.[minor])?(.[revision])?(-release)?(-SNAPSHOT)? */
    private static final Pattern PATTERN;

    static
    {
        PATTERN = Pattern.compile("(?x) "
                + "(?<major> \\d+)"
                + "(\\. (?<minor> \\d+))?"
                + "(\\. (?<patch> \\d+))?"
                + "(- (?<release> \\w+))??"
                + "(- (?<snapshot> SNAPSHOT))?", CASE_INSENSITIVE);
    }

    /**
     * @return A version for the given major and minor values, as in 8.0
     */
    public static Version of(int major, int minor)
    {
        return of(major, minor, NO_PATCH);
    }

    /**
     * @return A version for the given major and minor values, as in 8.0
     */
    public static Version of(int major)
    {
        return of(major, NO_MINOR);
    }

    /**
     * @return A version for the given major, minor and patch values, as in 8.0.1
     */
    public static Version of(int major, int minor, int patch)
    {
        return of(major, minor, patch, null, false);
    }

    /**
     * @return A version for the given major, minor, patch and release values, as in 8.0.1-Beta
     */
    public static Version of(int major,
                             int minor,
                             int patch,
                             Release release,
                             boolean snapshot)
    {
        return new Version(major, minor, patch, release, snapshot);
    }

    public static Version parseVersion(String text)
    {
        return parseVersion(Listener.throwingListener(), text);
    }

    /**
     * @return The given text, of the form [major].[minor](.[revision])?(-release)?, parsed as a {@link Version} object,
     * or null if the text is not of that form.
     */
    public static Version parseVersion(Listener listener, String text)
    {
        // If the text matches the version pattern,
        var matcher = PATTERN.matcher(text);
        if (matcher.matches())
        {
            // Extract the required major and minor versions
            var major = Ints.parseInt(listener, matcher.group("major"));
            var minor = matcher.group("minor");
            var minorVersion = minor == null ? NO_MINOR : Ints.parseInt(listener, minor);

            // then get the patch group and convert it to a number or NO_PATCH if there is none
            var patch = matcher.group("patch");
            var patchNumber = patch == null ? NO_PATCH : Ints.parseInt(listener, patch);

            // and the release name or null if there is none
            var releaseName = matcher.group("release");
            var release = releaseName == null ? null : Release.parse(listener, releaseName);
            var snapshot = "SNAPSHOT".equalsIgnoreCase(matcher.group("snapshot"));

            // and finally, construct the version object
            return of(major, minorVersion, patchNumber, release, snapshot);
        }

        listener.problem("Could not parse version: $", text);
        return null;
    }

    /**
     * Parses the given text for a version, throwing an exception if any errors occur.
     *
     * @return The given text, of the form [major].[minor](.[revision])?(-release)?, parsed as a {@link Version} object,
     * or null if the text is not of that form.
     */
    public static Version version(String text)
    {
        return parseVersion(Listener.throwingListener(), text);
    }

    private int major;

    private int minor;

    private int patch;

    private Release release;

    private boolean snapshot;

    protected Version(int major, int minor, int patch, Release release, boolean snapshot)
    {
        this.minor = (byte) minor;
        this.major = (byte) major;
        this.patch = (byte) patch;
        this.release = release;
        this.snapshot = snapshot;
    }

    protected Version()
    {
    }

    /**
     * @return This version as a double. For example, if major is 1 and minor is 9, the return value will be 1.9.
     */
    public double asDouble()
    {
        if (minor < 10)
        {
            return major + (minor * .1);
        }
        else if (minor < 100)
        {
            return major + (minor * .01);
        }
        else
        {
            fail("Cannot convert to double: $", this);
            return -1;
        }
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Version)
        {
            var that = (Version) object;
            return major == that.major
                    && minor == that.minor
                    && patch == that.patch
                    && release == that.release;
        }
        return false;
    }

    public boolean hasMinorVersion()
    {
        return patch != NO_MINOR;
    }

    public boolean hasPatch()
    {
        return patch != NO_PATCH;
    }

    public boolean hasRelease()
    {
        return release != null;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(major, minor, patch, release);
    }

    public boolean isNewerThan(Version that)
    {
        if (major == that.major)
        {
            if (minor == that.minor)
            {
                if (patch == that.patch)
                {
                    return false;
                }
                else
                {
                    return patch > that.patch;
                }
            }
            else
            {
                return minor > that.minor;
            }
        }
        else
        {
            return major > that.major;
        }
    }

    public boolean isNewerThanOrEqualTo(Version that)
    {
        return equals(that) || isNewerThan(that);
    }

    public boolean isOlderThan(Version that)
    {
        return !equals(that) && !isNewerThan(that);
    }

    public boolean isOlderThanOrEqualTo(Version that)
    {
        return equals(that) || isOlderThan(that);
    }

    public boolean isSnapshot()
    {
        return snapshot;
    }

    public int major()
    {
        return major;
    }

    public int minor()
    {
        return minor;
    }

    public Version newer(Version that)
    {
        return isNewerThan(that) ? this : that;
    }

    public Version older(Version that)
    {
        return isOlderThan(that) ? this : that;
    }

    /**
     * @return The patch number, as in [major].[minor].[patch], or NO_PATCH if there is no patch number
     */
    public int patch()
    {
        return patch;
    }

    public Release release()
    {
        return release;
    }

    @Override
    public String toString()
    {
        return major
                + (minor == NO_MINOR ? "" : "." + minor)
                + (patch == NO_PATCH ? "" : "." + patch)
                + (release == null ? "" : "-" + release.name().toLowerCase())
                + (snapshot ? "-SNAPSHOT" : "");
    }

    /**
     * @return This version without the patch number
     */
    public Version withoutPatch()
    {
        return of(major, minor, -1, release, snapshot);
    }

    /**
     * @return This version without the {@link Release}
     */
    public Version withoutRelease()
    {
        return of(major, minor, patch, null, snapshot);
    }

    /**
     * @return This version as a non-snapshot
     */
    public Version withoutSnapshot()
    {
        return of(major, minor, patch, release, false);
    }
}

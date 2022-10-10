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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.language.primitive.Ints;
import com.telenav.kivakit.core.messaging.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Pattern;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * Represents a <a href=https://semver.org><i>semantic version</i></a>, such as "6.3" or "1.2.1" or "6.3-rc". Supports
 * {@link #major()}, {@link #minor()}, {@link #patch()}, {@link #releaseType()} and snapshot values.
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
 *     <li>{@link #releaseType()} - The release name, or null if there is none</li>
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
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_INSUFFICIENT,
             documentation = DOCUMENTATION_COMPLETE)
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
     * Returns a version for the given text, throwing an exception if it is invalid
     */
    public static Version parseVersion(String text)
    {
        return parseVersion(Listener.throwingListener(), text);
    }

    /**
     * Parses the given text into a version, reporting any problems to the given listener
     *
     * @return The given text, of the form [major].[minor](.[revision])?(-release)?, parsed as a {@link Version} object,
     * or null if the text is not of that form.
     */
    public static Version parseVersion(Listener listener, @NotNull String text)
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
            var release = releaseName == null ? null : ReleaseType.parseRelease(listener, releaseName);
            var snapshot = "SNAPSHOT".equalsIgnoreCase(matcher.group("snapshot"));

            // and finally, construct the version object
            return version(major, minorVersion, patchNumber, release, snapshot);
        }

        listener.problem("Could not parse version: $", text);
        return null;
    }

    /**
     * Returns a version for the given major, minor and patch values, as in 8.0.1
     */
    public static Version version(int major, int minor, int patch)
    {
        return version(major, minor, patch, null, false);
    }

    /**
     * Returns a version for the given major, minor, patch and release values, as in 8.0.1-Beta
     */
    public static Version version(int major,
                                  int minor,
                                  int patch,
                                  ReleaseType release,
                                  boolean snapshot)
    {
        return new Version(major, minor, patch, release, snapshot);
    }

    /**
     * Returns a version for the given major and minor values, as in 8.0
     */
    public static Version version(int major, int minor)
    {
        return version(major, minor, NO_PATCH);
    }

    /**
     * Returns a version for the given major and minor values, as in 8.0
     */
    public static Version version(int major)
    {
        return version(major, NO_MINOR);
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

    /** The major version */
    private int major;

    /** The minor version */
    private int minor;

    /** The patch version, also known as the "dot" version */
    private int patch;

    /** The release type */
    private ReleaseType releaseType;

    /** True if this is a development snapshot */
    private boolean snapshot;

    protected Version(int major, int minor, int patch, ReleaseType release, boolean snapshot)
    {
        this.minor = (byte) minor;
        this.major = (byte) major;
        this.patch = (byte) patch;
        this.releaseType = release;
        this.snapshot = snapshot;
    }

    protected Version()
    {
    }

    /**
     * Returns this version as a double. For example, if major is 1 and minor is 9, the return value will be 1.9.
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
                    && releaseType == that.releaseType;
        }
        return false;
    }

    /**
     * Returns true if this has a minor version
     */
    public boolean hasMinorVersion()
    {
        return patch != NO_MINOR;
    }

    /**
     * Returns true if this has a patch version
     */
    public boolean hasPatch()
    {
        return patch != NO_PATCH;
    }

    /**
     * Returns true if this has a release
     */
    public boolean hasRelease()
    {
        return releaseType != null;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(major, minor, patch, releaseType);
    }

    /**
     * Returns true if this is newer than the given version
     */
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

    /**
     * Returns true if this is newer than or equal to the given version
     */
    public boolean isNewerThanOrEqualTo(Version that)
    {
        return equals(that) || isNewerThan(that);
    }

    /**
     * Returns true if this is older than the given version
     */
    public boolean isOlderThan(Version that)
    {
        return !equals(that) && !isNewerThan(that);
    }

    /**
     * Returns true if this is older than or equal to the given version
     */
    public boolean isOlderThanOrEqualTo(Version that)
    {
        return equals(that) || isOlderThan(that);
    }

    /**
     * Returns true if this is a development snapshot
     */
    public boolean isSnapshot()
    {
        return snapshot;
    }

    /**
     * Returns the major version
     */
    public int major()
    {
        return major;
    }

    /**
     * Returns the minor version
     */
    public int minor()
    {
        return minor;
    }

    /**
     * Returns the newer of this version or the given version
     */
    public Version newer(Version that)
    {
        return isNewerThan(that) ? this : that;
    }

    /**
     * Returns the older of this version or the given version
     */
    public Version older(Version that)
    {
        return isOlderThan(that) ? this : that;
    }

    /**
     * Returns the patch number, as in [major].[minor].[patch], or NO_PATCH if there is no patch number
     */
    public int patch()
    {
        return patch;
    }

    /**
     * Returns the release type
     */
    public ReleaseType releaseType()
    {
        return releaseType;
    }

    @Override
    public String toString()
    {
        return major
                + (minor == NO_MINOR ? "" : "." + minor)
                + (patch == NO_PATCH ? "" : "." + patch)
                + (releaseType == null ? "" : "-" + releaseType.name().toLowerCase())
                + (snapshot ? "-SNAPSHOT" : "");
    }

    /**
     * Returns this version without the patch number
     */
    public Version withoutPatch()
    {
        return version(major, minor, -1, releaseType, snapshot);
    }

    /**
     * Returns this version without the {@link ReleaseType}
     */
    public Version withoutRelease()
    {
        return version(major, minor, patch, null, snapshot);
    }

    /**
     * Returns this version as a non-snapshot
     */
    public Version withoutSnapshot()
    {
        return version(major, minor, patch, releaseType, false);
    }
}

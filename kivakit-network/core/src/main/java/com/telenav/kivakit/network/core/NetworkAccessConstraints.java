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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.network.core.authentication.Password;
import com.telenav.kivakit.network.core.authentication.UserName;
import com.telenav.kivakit.network.core.internal.lexakai.DiagramNetworkLocation;
import com.telenav.kivakit.validation.BaseValidator;
import com.telenav.kivakit.validation.Validatable;
import com.telenav.kivakit.validation.ValidationType;
import com.telenav.kivakit.validation.Validator;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.time.Duration.seconds;
import static com.telenav.kivakit.core.value.count.Count._8;

/**
 * Access constraints for network requests, including {@link #timeout()} and basic authentication requirements.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramNetworkLocation.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class NetworkAccessConstraints implements Validatable
{
    /**
     * Returns the default constraints to use for network access
     */
    public static NetworkAccessConstraints defaultNetworkAccessConstraints()
    {
        return new NetworkAccessConstraints().timeout(seconds(60));
    }

    @UmlAggregation
    private Duration timeout;

    @UmlAggregation
    private UserName userName;

    @UmlAggregation
    private Password password;

    @UmlAggregation
    private Count retries = _8;

    @UmlAggregation
    private Duration pauseBetweenRetries = seconds(3);

    @FormatProperty
    public Password password()
    {
        return password;
    }

    public void password(Password password)
    {
        this.password = password;
    }

    public Duration pauseBetweenRetries()
    {
        return pauseBetweenRetries;
    }

    public NetworkAccessConstraints pauseBetweenRetries(Duration pauseBetweenRetries)
    {
        this.pauseBetweenRetries = pauseBetweenRetries;
        return this;
    }

    public Count retries()
    {
        return retries;
    }

    public NetworkAccessConstraints retries(Count retries)
    {
        this.retries = retries;
        return this;
    }

    @FormatProperty
    public Duration timeout()
    {
        return timeout;
    }

    public NetworkAccessConstraints timeout(Duration timeout)
    {
        this.timeout = timeout;
        return this;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    public void userName(UserName userName)
    {
        this.userName = userName;
    }

    @FormatProperty
    public UserName userName()
    {
        return userName;
    }

    @Override
    public Validator validator(ValidationType type)
    {
        return new BaseValidator()
        {

            @Override
            protected void onValidate()
            {
                problemIf(timeout() == null, "timeout is missing");
            }
        };
    }
}

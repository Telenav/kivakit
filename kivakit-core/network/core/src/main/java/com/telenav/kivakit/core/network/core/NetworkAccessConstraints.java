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

package com.telenav.kivakit.core.network.core;

import com.telenav.kivakit.core.kernel.data.validation.Validatable;
import com.telenav.kivakit.core.kernel.data.validation.Validation;
import com.telenav.kivakit.core.kernel.data.validation.Validator;
import com.telenav.kivakit.core.kernel.data.validation.validators.BaseValidator;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.network.core.project.lexakai.diagrams.DiagramNetworkLocation;
import com.telenav.kivakit.core.security.authentication.Password;
import com.telenav.kivakit.core.security.authentication.UserName;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

@UmlClassDiagram(diagram = DiagramNetworkLocation.class)
public class NetworkAccessConstraints implements Validatable
{
    public static final NetworkAccessConstraints DEFAULT = new NetworkAccessConstraints().timeout(Duration.seconds(60));

    @UmlAggregation
    private Duration timeout;

    @UmlAggregation
    private UserName userName;

    @UmlAggregation
    private Password password;

    @KivaKitIncludeProperty
    public Password password()
    {
        return password;
    }

    public void password(final Password password)
    {
        this.password = password;
    }

    @KivaKitIncludeProperty
    public Duration timeout()
    {
        return timeout;
    }

    public NetworkAccessConstraints timeout(final Duration timeout)
    {
        this.timeout = timeout;
        return this;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    public void userName(final UserName userName)
    {
        this.userName = userName;
    }

    @KivaKitIncludeProperty
    public UserName userName()
    {
        return userName;
    }

    @Override
    public Validator validator(final Validation type)
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

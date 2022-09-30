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

package com.telenav.kivakit.network.email;

import com.telenav.kivakit.testing.UnitTest;
import com.telenav.kivakit.network.core.EmailAddress;
import com.telenav.kivakit.network.core.Host;
import com.telenav.kivakit.network.core.authentication.UserName;
import com.telenav.kivakit.network.core.authentication.passwords.PlainTextPassword;
import com.telenav.kivakit.network.email.senders.SmtpEmailSender;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class EmailTest extends UnitTest
{
    public EmailTest()
    {
    }

    @Test
    @SuppressWarnings("resource")
    public void test()
    {
        var shibo = EmailAddress.parseEmailAddress(this, "jonathanl@telenav.com");

        var email = new Email()
                .from(shibo)
                .subject("testing")
                .addTo(shibo)
                .body(new EmailBody("this is a test"));

        var configuration = new SmtpEmailSender.Configuration()
                .host(Host.localhost())
                .username(UserName.parseUserName(this, ""))
                .password(PlainTextPassword.parsePlainTextPassword(this, ""));

        var sender = new SmtpEmailSender(configuration).sendingOn(false);
        sender.start();
        sender.enqueue(email);
        sender.stop();
    }
}

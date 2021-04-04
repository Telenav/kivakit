////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.email;

import com.telenav.kivakit.core.network.email.senders.SmtpEmailSender;
import com.telenav.kivakit.core.network.core.EmailAddress;
import com.telenav.kivakit.core.network.core.Host;
import com.telenav.kivakit.core.security.authentication.UserName;
import com.telenav.kivakit.core.security.authentication.passwords.PlainTextPassword;
import com.telenav.kivakit.core.test.UnitTest;
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
        final var shibo = EmailAddress.parse("jonathanl@telenav.com");

        final var email = new Email()
                .from(shibo)
                .subject("testing")
                .addTo(shibo)
                .body(new EmailBody("this is a test"));

        final var configuration = new SmtpEmailSender.Configuration()
                .host(Host.local())
                .username(new UserName(""))
                .password(new PlainTextPassword(""));

        final var sender = new SmtpEmailSender(configuration).sendingOn(false);
        sender.start();
        sender.enqueue(email);
        sender.stop();
    }
}

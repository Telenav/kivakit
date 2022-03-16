[//]: # (start-user-text)

<a href="https://www.kivakit.org">
<img src="https://www.kivakit.org/images/web-32.png" srcset="https://www.kivakit.org/images/web-32-2x.png 2x"/>
</a>
&nbsp;
<a href="https://twitter.com/openkivakit">
<img src="https://www.kivakit.org/images/twitter-32.png" srcset="https://www.kivakit.org/images/twitter-32-2x.png 2x"/>
</a>
&nbsp;
<a href="https://kivakit.zulipchat.com">
<img src="https://www.kivakit.org/images/zulip-32.png" srcset="https://www.kivakit.org/images/zulip-32-2x.png 2x"/>
</a>

[//]: # (end-user-text)

# kivakit-network email &nbsp;&nbsp; <img src="https://www.kivakit.org/images/envelope-32.png" srcset="https://www.kivakit.org/images/envelope-32-2x.png 2x"/>

This module enables easy composition and sending of emails.

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  
[**Emails**](#emails)  
[**Sending Emails**](#sending-emails)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/dependencies-32.png" srcset="https://www.kivakit.org/images/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/1.4.0/lexakai/kivakit/kivakit-network/email/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-network-email</artifactId>
        <version>1.4.1-SNAPSHOT</version>
    </dependency>

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

This module allows the composition of an email message model, *Email*, which can be sent to
recipients using the *EmailSender* class. Attachments and HTML emails are supported.

### Emails <a name = "emails"></a>

The *Email* class allows fluent construction of emails, like this:

    var shibo = EmailAddress.parse("jonathanl@telenav.com");
    var bernie = EmailAddress.parse("bernies@whitehouse.gov");

    var email = new Email()
            .from(shibo)
            .subject("testing")
            .addTo(bernie)
            .body(new EmailBody("this is a test"));

Attachments can be added with the *EmailAttachment* class, similarly.

### Sending Emails <a name = "sending-emails"></a>

Emails can be sent with *SmtpEmailSender* by configuring an instance of this class, starting
it and enqueueing *Email* objects. No attempt is made to persist the queue of unsent emails.

    var configuration = new SmtpEmailSender.Configuration()
            .host(Host.local())
            .username(new UserName("shibo"))
            .password(new PlainTextPassword("xyzzy"));

    var sender = new SmtpEmailSender(configuration).sendingOn(true);
    sender.start();

       [...]

    sender.enqueue(email);

       [...]

    sender.stop();

[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://www.kivakit.org/images/diagram-40.png" srcset="https://www.kivakit.org/images/diagram-40-2x.png 2x"/>

[*E-Mail Composition and Sending*](https://www.kivakit.org/1.4.0/lexakai/kivakit/kivakit-network/email/documentation/diagrams/diagram-email.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/box-32.png" srcset="https://www.kivakit.org/images/box-32-2x.png 2x"/>

[*com.telenav.kivakit.network.email*](https://www.kivakit.org/1.4.0/lexakai/kivakit/kivakit-network/email/documentation/diagrams/com.telenav.kivakit.network.email.svg)  
[*com.telenav.kivakit.network.email.converters*](https://www.kivakit.org/1.4.0/lexakai/kivakit/kivakit-network/email/documentation/diagrams/com.telenav.kivakit.network.email.converters.svg)  
[*com.telenav.kivakit.network.email.lexakai*](https://www.kivakit.org/1.4.0/lexakai/kivakit/kivakit-network/email/documentation/diagrams/com.telenav.kivakit.network.email.lexakai.svg)  
[*com.telenav.kivakit.network.email.senders*](https://www.kivakit.org/1.4.0/lexakai/kivakit/kivakit-network/email/documentation/diagrams/com.telenav.kivakit.network.email.senders.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/books-32.png" srcset="https://www.kivakit.org/images/books-32-2x.png 2x"/>

Javadoc coverage for this project is 84.5%.  
  
&nbsp; &nbsp; <img src="https://www.kivakit.org/images/meter-80-96.png" srcset="https://www.kivakit.org/images/meter-80-96-2x.png 2x"/>




| Class | Documentation Sections |
|---|---|
| [*AttachmentConverter*](https://www.kivakit.org/1.4.0/javadoc/kivakit/kivakit.network.email/com/telenav/kivakit/network/email/converters/AttachmentConverter.html) |  |  
| [*BodyConverter*](https://www.kivakit.org/1.4.0/javadoc/kivakit/kivakit.network.email/com/telenav/kivakit/network/email/converters/BodyConverter.html) |  |  
| [*DiagramEmail*](https://www.kivakit.org/1.4.0/javadoc/kivakit/kivakit.network.email/com/telenav/kivakit/network/email/lexakai/DiagramEmail.html) |  |  
| [*Email*](https://www.kivakit.org/1.4.0/javadoc/kivakit/kivakit.network.email/com/telenav/kivakit/network/email/Email.html) |  |  
| [*EmailAddressConverter*](https://www.kivakit.org/1.4.0/javadoc/kivakit/kivakit.network.email/com/telenav/kivakit/network/email/converters/EmailAddressConverter.html) |  |  
| [*EmailAttachment*](https://www.kivakit.org/1.4.0/javadoc/kivakit/kivakit.network.email/com/telenav/kivakit/network/email/EmailAttachment.html) |  |  
| [*EmailBody*](https://www.kivakit.org/1.4.0/javadoc/kivakit/kivakit.network.email/com/telenav/kivakit/network/email/EmailBody.html) |  |  
| [*EmailMessageAlarm*](https://www.kivakit.org/1.4.0/javadoc/kivakit/kivakit.network.email/com/telenav/kivakit/network/email/EmailMessageAlarm.html) |  |  
| [*EmailQueue*](https://www.kivakit.org/1.4.0/javadoc/kivakit/kivakit.network.email/com/telenav/kivakit/network/email/EmailQueue.html) |  |  
| [*EmailSender*](https://www.kivakit.org/1.4.0/javadoc/kivakit/kivakit.network.email/com/telenav/kivakit/network/email/EmailSender.html) |  |  
| [*EmailSender.Configuration*](https://www.kivakit.org/1.4.0/javadoc/kivakit/kivakit.network.email/com/telenav/kivakit/network/email/EmailSender.Configuration.html) |  |  
| [*HtmlEmailBody*](https://www.kivakit.org/1.4.0/javadoc/kivakit/kivakit.network.email/com/telenav/kivakit/network/email/HtmlEmailBody.html) |  |  
| [*SmtpEmailSender*](https://www.kivakit.org/1.4.0/javadoc/kivakit/kivakit.network.email/com/telenav/kivakit/network/email/senders/SmtpEmailSender.html) |  |  
| [*SmtpEmailSender.Configuration*](https://www.kivakit.org/1.4.0/javadoc/kivakit/kivakit.network.email/com/telenav/kivakit/network/email/senders/SmtpEmailSender.Configuration.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](https://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://lexakai.org). UML diagrams courtesy of [PlantUML](https://plantuml.com).</sub>


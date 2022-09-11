[//]: # (start-user-text)

<a href="https://www.kivakit.org">
<img src="https://telenav.github.io/telenav-assets/images/icons/web-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/web-32-2x.png 2x"/>
</a>
&nbsp;
<a href="https://twitter.com/openkivakit">
<img src="https://telenav.github.io/telenav-assets/images/logos/twitter/twitter-32.png" srcset="https://telenav.github.io/telenav-assets/images/logos/twitter/twitter-32-2x.png 2x"/>
</a>
&nbsp;
<a href="https://kivakit.zulipchat.com">
<img src="https://telenav.github.io/telenav-assets/images/logos/zulip/zulip-32.png" srcset="https://telenav.github.io/telenav-assets/images/logos/zulip/zulip-32-2x.png 2x"/>
</a>

[//]: # (end-user-text)

# kivakit-network email &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/envelope-64.png" srcset="https://telenav.github.io/telenav-assets/images/icons/envelope-64-2x.png 2x"/>

This module enables easy composition and sending of emails.

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  
[**Emails**](#emails)  
[**Sending Emails**](#sending-emails)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/dependencies-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/1.7.0/lexakai/kivakit/kivakit-network/email/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-network-email</artifactId>
        <version>1.7.0</version>
    </dependency>

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

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

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/diagram-40.png" srcset="https://telenav.github.io/telenav-assets/images/icons/diagram-40-2x.png 2x"/>

[*E-Mail Composition and Sending*](https://www.kivakit.org/1.7.0/lexakai/kivakit/kivakit-network/email/documentation/diagrams/diagram-email.svg)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/box-24.png" srcset="https://telenav.github.io/telenav-assets/images/icons/box-24-2x.png 2x"/>

[*com.telenav.kivakit.network.email*](https://www.kivakit.org/1.7.0/lexakai/kivakit/kivakit-network/email/documentation/diagrams/com.telenav.kivakit.network.email.svg)  
[*com.telenav.kivakit.network.email.converters*](https://www.kivakit.org/1.7.0/lexakai/kivakit/kivakit-network/email/documentation/diagrams/com.telenav.kivakit.network.email.converters.svg)  
[*com.telenav.kivakit.network.email.internal.lexakai*](https://www.kivakit.org/1.7.0/lexakai/kivakit/kivakit-network/email/documentation/diagrams/com.telenav.kivakit.network.email.internal.lexakai.svg)  
[*com.telenav.kivakit.network.email.senders*](https://www.kivakit.org/1.7.0/lexakai/kivakit/kivakit-network/email/documentation/diagrams/com.telenav.kivakit.network.email.senders.svg)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/books-24.png" srcset="https://telenav.github.io/telenav-assets/images/icons/books-24-2x.png 2x"/>

Javadoc coverage for this project is 84.7%.  
  
&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-80-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-80-96-2x.png 2x"/>




| Class | Documentation Sections |
|---|---|
| [*AttachmentConverter*](https://www.kivakit.org/1.7.0/javadoc/kivakit/kivakit.network.email/////////////////////////////////////////////////////////////////.html) |  |  
| [*BodyConverter*](https://www.kivakit.org/1.7.0/javadoc/kivakit/kivakit.network.email///////////////////////////////////////////////////////////.html) |  |  
| [*DiagramEmail*](https://www.kivakit.org/1.7.0/javadoc/kivakit/kivakit.network.email////////////////////////////////////////////////////////////////.html) |  |  
| [*Email*](https://www.kivakit.org/1.7.0/javadoc/kivakit/kivakit.network.email////////////////////////////////////////.html) |  |  
| [*EmailAddressConverter*](https://www.kivakit.org/1.7.0/javadoc/kivakit/kivakit.network.email///////////////////////////////////////////////////////////////////.html) |  |  
| [*EmailAttachment*](https://www.kivakit.org/1.7.0/javadoc/kivakit/kivakit.network.email//////////////////////////////////////////////////.html) |  |  
| [*EmailBody*](https://www.kivakit.org/1.7.0/javadoc/kivakit/kivakit.network.email////////////////////////////////////////////.html) |  |  
| [*EmailMessageAlarm*](https://www.kivakit.org/1.7.0/javadoc/kivakit/kivakit.network.email////////////////////////////////////////////////////.html) |  |  
| [*EmailQueue*](https://www.kivakit.org/1.7.0/javadoc/kivakit/kivakit.network.email/////////////////////////////////////////////.html) |  |  
| [*EmailSender*](https://www.kivakit.org/1.7.0/javadoc/kivakit/kivakit.network.email//////////////////////////////////////////////.html) |  |  
| [*EmailSender.Configuration*](https://www.kivakit.org/1.7.0/javadoc/kivakit/kivakit.network.email////////////////////////////////////////////////////////////.html) |  |  
| [*HtmlEmailBody*](https://www.kivakit.org/1.7.0/javadoc/kivakit/kivakit.network.email////////////////////////////////////////////////.html) |  |  
| [*SmtpEmailSender*](https://www.kivakit.org/1.7.0/javadoc/kivakit/kivakit.network.email//////////////////////////////////////////////////////////.html) |  |  
| [*SmtpEmailSender.Configuration*](https://www.kivakit.org/1.7.0/javadoc/kivakit/kivakit.network.email////////////////////////////////////////////////////////////////////////.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](https://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://lexakai.org). UML diagrams courtesy of [PlantUML](https://plantuml.com).</sub>

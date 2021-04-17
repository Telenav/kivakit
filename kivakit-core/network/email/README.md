# KivaKit Core Network - Email &nbsp;&nbsp;![](https://www.kivakit.org/images/envelope-40.png)

This module enables easy composition and sending of emails.

![](https://www.kivakit.org/images/horizontal-line.png)

### Index

[**Summary**](#summary)  
[**Emails**](#emails)  
[**Sending Emails**](#sending-emails)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

![](https://www.kivakit.org/images/horizontal-line.png)

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; ![](https://www.kivakit.org/images/dependencies-40.png)

[*Dependency Diagram*](https://www.kivakit.org/lexakai/kivakit-core/network/email/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId></artifactId>
        <version>0.9.0-SNAPSHOT</version>
    </dependency>

![](https://www.kivakit.org/images/short-horizontal-line.png)

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

![](https://www.kivakit.org/images/short-horizontal-line.png)

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; ![](https://www.kivakit.org/images/diagram-48.png)

[*E-Mail Composition and Sending*](https://www.kivakit.org/lexakai/diagrams/diagram-email.svg)

![](https://www.kivakit.org/images/short-horizontal-line.png)

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; ![](https://www.kivakit.org/images/box-40.png)

[*com.telenav.kivakit.core.network.email*](https://www.kivakit.org/lexakai/diagrams/com.telenav.kivakit.core.network.email.svg)
  [*com.telenav.kivakit.core.network.email.converters*](https://www.kivakit.org/lexakai/diagrams/com.telenav.kivakit.core.network.email.converters.svg)
  [*com.telenav.kivakit.core.network.email.project*](https://www.kivakit.org/lexakai/diagrams/com.telenav.kivakit.core.network.email.project.svg)
  [*com.telenav.kivakit.core.network.email.senders*](https://www.kivakit.org/lexakai/diagrams/com.telenav.kivakit.core.network.email.senders.svg)

![](https://www.kivakit.org/images/short-horizontal-line.png)

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; ![](https://www.kivakit.org/images/books-40.png)

Javadoc coverage for this project is 88.3%.  
  
&nbsp; &nbsp;  ![](https://www.kivakit.org/images/meter-90-12.png)



| Class | Documentation Sections |
|---|---|
| [*AttachmentConverter*](null/com/telenav/kivakit/core/network/email/converters/AttachmentConverter.html) |  |  
| [*AttachmentListConverter*](null/com/telenav/kivakit/core/network/email/converters/AttachmentListConverter.html) |  |  
| [*BodyConverter*](null/com/telenav/kivakit/core/network/email/converters/BodyConverter.html) |  |  
| [*CoreNetworkEmailProject*](null/com/telenav/kivakit/core/network/email/project/CoreNetworkEmailProject.html) |  |  
| [*Email*](null/com/telenav/kivakit/core/network/email/Email.html) |  |  
| [*EmailAddressConverter*](null/com/telenav/kivakit/core/network/email/converters/EmailAddressConverter.html) |  |  
| [*EmailAddressSetConverter*](null/com/telenav/kivakit/core/network/email/converters/EmailAddressSetConverter.html) |  |  
| [*EmailAttachment*](null/com/telenav/kivakit/core/network/email/EmailAttachment.html) |  |  
| [*EmailBody*](null/com/telenav/kivakit/core/network/email/EmailBody.html) |  |  
| [*EmailQueue*](null/com/telenav/kivakit/core/network/email/EmailQueue.html) |  |  
| [*EmailSender*](null/com/telenav/kivakit/core/network/email/EmailSender.html) |  |  
| [*EmailSender.Configuration*](null/com/telenav/kivakit/core/network/email/EmailSender.Configuration.html) |  |  
| [*HtmlEmailBody*](null/com/telenav/kivakit/core/network/email/HtmlEmailBody.html) |  |  
| [*SmtpEmailSender*](null/com/telenav/kivakit/core/network/email/senders/SmtpEmailSender.html) |  |  
| [*SmtpEmailSender.Configuration*](null/com/telenav/kivakit/core/network/email/senders/SmtpEmailSender.Configuration.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<br/>

![](https://www.kivakit.org/images/horizontal-line.png)

<sub>Copyright &#169; 2011-2021 [Telenav](http://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://github.com/Telenav/lexakai) on 2021.04.16. UML diagrams courtesy
of [PlantUML](http://plantuml.com).</sub>


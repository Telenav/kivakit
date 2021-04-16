# kivakit-core-network email &nbsp;&nbsp;![](https://kivakit.org/images/envelope-40.png)

This module enables easy composition and sending of emails.

![](https://kivakit.org/images/horizontal-line.png)

### Index

[**Summary**](#summary)  
[**Emails**](#emails)  
[**Sending Emails**](#sending-emails)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

![](https://kivakit.org/images/horizontal-line.png)

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; ![](https://kivakit.org/images/dependencies-40.png)

[*Dependency Diagram*](documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId></artifactId>
        <version>0.9.0-SNAPSHOT</version>
    </dependency>

![](https://kivakit.org/images/short-horizontal-line.png)

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

![](https://kivakit.org/images/short-horizontal-line.png)

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; ![](https://kivakit.org/images/diagram-48.png)

[*E-Mail Composition and Sending*](documentation/diagrams/diagram-email.svg)  

![](https://kivakit.org/images/short-horizontal-line.png)

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; ![](https://kivakit.org/images/box-40.png)

[*com.telenav.kivakit.core.network.email*](documentation/diagrams/com.telenav.kivakit.core.network.email.svg)  
[*com.telenav.kivakit.core.network.email.converters*](documentation/diagrams/com.telenav.kivakit.core.network.email.converters.svg)  
[*com.telenav.kivakit.core.network.email.project*](documentation/diagrams/com.telenav.kivakit.core.network.email.project.svg)  
[*com.telenav.kivakit.core.network.email.senders*](documentation/diagrams/com.telenav.kivakit.core.network.email.senders.svg)  

![](https://kivakit.org/images/short-horizontal-line.png)

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; ![](https://kivakit.org/images/books-40.png)

Javadoc coverage for this project is 88.3%.  
  
&nbsp; &nbsp;  ![](https://kivakit.org/images/meter-90-12.png)



| Class | Documentation Sections |
|---|---|
| [*AttachmentConverter*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.email/com/telenav/kivakit/core/network/email/converters/AttachmentConverter.html) |  |  
| [*AttachmentListConverter*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.email/com/telenav/kivakit/core/network/email/converters/AttachmentListConverter.html) |  |  
| [*BodyConverter*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.email/com/telenav/kivakit/core/network/email/converters/BodyConverter.html) |  |  
| [*CoreNetworkEmailProject*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.email/com/telenav/kivakit/core/network/email/project/CoreNetworkEmailProject.html) |  |  
| [*Email*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.email/com/telenav/kivakit/core/network/email/Email.html) |  |  
| [*EmailAddressConverter*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.email/com/telenav/kivakit/core/network/email/converters/EmailAddressConverter.html) |  |  
| [*EmailAddressSetConverter*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.email/com/telenav/kivakit/core/network/email/converters/EmailAddressSetConverter.html) |  |  
| [*EmailAttachment*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.email/com/telenav/kivakit/core/network/email/EmailAttachment.html) |  |  
| [*EmailBody*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.email/com/telenav/kivakit/core/network/email/EmailBody.html) |  |  
| [*EmailQueue*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.email/com/telenav/kivakit/core/network/email/EmailQueue.html) |  |  
| [*EmailSender*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.email/com/telenav/kivakit/core/network/email/EmailSender.html) |  |  
| [*EmailSender.Configuration*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.email/com/telenav/kivakit/core/network/email/EmailSender.Configuration.html) |  |  
| [*HtmlEmailBody*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.email/com/telenav/kivakit/core/network/email/HtmlEmailBody.html) |  |  
| [*SmtpEmailSender*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.email/com/telenav/kivakit/core/network/email/senders/SmtpEmailSender.html) |  |  
| [*SmtpEmailSender.Configuration*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.network.email/com/telenav/kivakit/core/network/email/senders/SmtpEmailSender.Configuration.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<br/>

![](https://kivakit.org/images/horizontal-line.png)

<sub>Copyright &#169; 2011-2021 [Telenav](http://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://github.com/Telenav/lexakai) on 2021.04.15. UML diagrams courtesy
of [PlantUML](http://plantuml.com).</sub>


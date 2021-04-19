# kivakit-web-wicket &nbsp;&nbsp;![](https://www.kivakit.org/images/wicket-48.png)

This project contains support for using Apache Wicket on Jetty.

![](https://www.kivakit.org/images/horizontal-line.png)

### Index

[**Summary**](#summary)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

![](https://www.kivakit.org/images/horizontal-line.png)

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; ![](https://www.kivakit.org/images/dependencies-40.png)

[*Dependency Diagram*](https://www.kivakit.org/lexakai/kivakit/kivakit-web/wicket/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-web-wicket</artifactId>
        <version>0.9.0-SNAPSHOT</version>
    </dependency>

![](https://www.kivakit.org/images/short-horizontal-line.png)

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

This module makes it easy to configure and start a simple [Apache Wicket](https://wicket.apache.org) application:

    listenTo(new JettyServer())
            .port(8080)
            .add("/*", new JettyWicket(MyWebApplication.class))
            .start();

Other servlets and filters, for example [Swagger](../swagger/README.md) and [Jersey](../jersey/README.md), can be added in a similar way.

[//]: # (end-user-text)

![](https://www.kivakit.org/images/short-horizontal-line.png)

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; ![](https://www.kivakit.org/images/diagram-48.png)

None

![](https://www.kivakit.org/images/short-horizontal-line.png)

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; ![](https://www.kivakit.org/images/box-40.png)

[*com.telenav.kivakit.web.wicket*](https://www.kivakit.org/lexakai/kivakit/kivakit-web/wicket/documentation/diagrams/com.telenav.kivakit.web.wicket.svg)  
[*com.telenav.kivakit.web.wicket.behaviors.status*](https://www.kivakit.org/lexakai/kivakit/kivakit-web/wicket/documentation/diagrams/com.telenav.kivakit.web.wicket.behaviors.status.svg)  
[*com.telenav.kivakit.web.wicket.components.feedback*](https://www.kivakit.org/lexakai/kivakit/kivakit-web/wicket/documentation/diagrams/com.telenav.kivakit.web.wicket.components.feedback.svg)  
[*com.telenav.kivakit.web.wicket.components.header*](https://www.kivakit.org/lexakai/kivakit/kivakit-web/wicket/documentation/diagrams/com.telenav.kivakit.web.wicket.components.header.svg)  
[*com.telenav.kivakit.web.wicket.components.refresh*](https://www.kivakit.org/lexakai/kivakit/kivakit-web/wicket/documentation/diagrams/com.telenav.kivakit.web.wicket.components.refresh.svg)  
[*com.telenav.kivakit.web.wicket.library*](https://www.kivakit.org/lexakai/kivakit/kivakit-web/wicket/documentation/diagrams/com.telenav.kivakit.web.wicket.library.svg)  
[*com.telenav.kivakit.web.wicket.project*](https://www.kivakit.org/lexakai/kivakit/kivakit-web/wicket/documentation/diagrams/com.telenav.kivakit.web.wicket.project.svg)  
[*com.telenav.kivakit.web.wicket.theme*](https://www.kivakit.org/lexakai/kivakit/kivakit-web/wicket/documentation/diagrams/com.telenav.kivakit.web.wicket.theme.svg)

![](https://www.kivakit.org/images/short-horizontal-line.png)

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; ![](https://www.kivakit.org/images/books-40.png)

Javadoc coverage for this project is 92.9%.  
  
&nbsp; &nbsp;  ![](https://www.kivakit.org/images/meter-90-12.png)



| Class | Documentation Sections |
|---|---|
| [*Components*](https://www.kivakit.org/javadoc/kivakit/kivakit.web.wicket/com/telenav/kivakit/web/wicket/library/Components.html) |  |  
| [*FeedbackPanel*](https://www.kivakit.org/javadoc/kivakit/kivakit.web.wicket/com/telenav/kivakit/web/wicket/components/feedback/FeedbackPanel.html) |  |  
| [*HeaderPanel*](https://www.kivakit.org/javadoc/kivakit/kivakit.web.wicket/com/telenav/kivakit/web/wicket/components/header/HeaderPanel.html) |  |  
| [*JettyWicket*](https://www.kivakit.org/javadoc/kivakit/kivakit.web.wicket/com/telenav/kivakit/web/wicket/JettyWicket.html) |  |  
| [*JettyWicketFilterHolder*](https://www.kivakit.org/javadoc/kivakit/kivakit.web.wicket/com/telenav/kivakit/web/wicket/JettyWicketFilterHolder.html) |  |  
| [*KivaKitTheme*](https://www.kivakit.org/javadoc/kivakit/kivakit.web.wicket/com/telenav/kivakit/web/wicket/theme/KivaKitTheme.html) |  |  
| [*MessageColor*](https://www.kivakit.org/javadoc/kivakit/kivakit.web.wicket/com/telenav/kivakit/web/wicket/behaviors/status/MessageColor.html) |  |  
| [*UpdatingContainer*](https://www.kivakit.org/javadoc/kivakit/kivakit.web.wicket/com/telenav/kivakit/web/wicket/components/refresh/UpdatingContainer.html) |  |  
| [*WebWicketProject*](https://www.kivakit.org/javadoc/kivakit/kivakit.web.wicket/com/telenav/kivakit/web/wicket/project/WebWicketProject.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<br/>

![](https://www.kivakit.org/images/horizontal-line.png)

<sub>Copyright &#169; 2011-2021 [Telenav](http://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://github.com/Telenav/lexakai). UML diagrams courtesy
of [PlantUML](http://plantuml.com).</sub>


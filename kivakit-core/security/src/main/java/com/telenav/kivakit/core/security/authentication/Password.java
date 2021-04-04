////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.security.authentication;

import com.telenav.kivakit.core.security.project.lexakai.diagrams.DiagramSecurity;
import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramSecurity.class)
public interface Password extends Matcher<Password>
{}

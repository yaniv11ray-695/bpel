/*******************************************************************************
 * Copyright (c) 2005, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Oracle Corporation
 *******************************************************************************/
package org.eclipse.bpel.ui.details.providers;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.wst.wsdl.Definition;

/**
 * Content provider for XSDSchema. 
 * 
 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
 */

public class WSDLDefinitionFromResourceContentProvider extends AbstractResourceContentProvider  {

	static private final String KIND [] = { "wsdl" }; //$NON-NLS-1$
	
	static private final Class KIND_CLASS [] = {Definition.class} ;
	
	
	 
	public WSDLDefinitionFromResourceContentProvider ( ResourceSet set ) {
		super(set);
	}

	@Override
	public String[] getKind () {
		return KIND;
	}
	
	
	@Override
	public Class [] getKindClass () {
		return KIND_CLASS;
	}	
	
}

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
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.bpel.ui.adapters;

import java.text.MessageFormat;

import org.eclipse.bpel.ui.Messages;
import org.eclipse.xsd.XSDSchema;


public class XSDSchemaAdapter extends XSDAbstractAdapter  {

		
	@Override
	public String getLabel(Object obj) {
		XSDSchema schema = (XSDSchema) obj;
		String tns = schema.getTargetNamespace();
		
		if (tns == null) {
			return Messages.XSDSchemaAdapter_0;
		}		
		return MessageFormat.format(Messages.XSDSchemaAdapter_1,
										new Object[] { tns } );
	}

	@Override
	public String getTypeLabel(Object object) {
		return Messages.XSDTypeDefinitionAdapter_XSD_Type_1; 
	}
	
}

/*******************************************************************************
 * Copyright (c) 2008, 2012 IBM Corporation and others.
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
/**
 * 
 */
package org.eclipse.bpel.ui.adapters;

import org.eclipse.bpel.ui.BPELUIPlugin;
import org.eclipse.bpel.ui.IBPELUIConstants;
import org.eclipse.bpel.ui.Messages;
import org.eclipse.swt.graphics.Image;

/**
 * @author mchmiele
 *
 */
public class XSDComplexTypeDefinitionAdapter extends XSDAbstractAdapter implements
		ILabeledElement {
	
	@Override
	public Image getSmallImage(Object object) {		
		return BPELUIPlugin.INSTANCE.getImage(IBPELUIConstants.ICON_XSD_COMPLEX_TYPE_DEFINITION_16);
	}
		
	@Override
	public String getTypeLabel(Object object) {
		return Messages.XSDComplexTypeDefinitionAdapter_0; 
	}
	
}

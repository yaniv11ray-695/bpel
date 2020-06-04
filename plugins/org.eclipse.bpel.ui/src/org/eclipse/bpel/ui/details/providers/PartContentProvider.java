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
package org.eclipse.bpel.ui.details.providers;

import java.util.List;

import org.eclipse.wst.wsdl.Message;

/**
 * Content provider for Parts.
 * 
 * Expects a Message type as input.
 */
public class PartContentProvider extends AbstractContentProvider {

	@Override
	public void collectElements (Object input, List list) {
		if (input instanceof Message) {
			Message msg = (Message) input;
			list.addAll( msg.getEParts() );			
		}		
	}
}

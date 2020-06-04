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

import org.eclipse.wst.wsdl.Definition;

/**
 * Content provider for Message types.
 * 
 * Expects a Definition as input.
 */
public class MessageTypeContentProvider extends AbstractContentProvider  {

	// https://issues.jboss.org/browse/JBIDE-8075
	// provide a filter for enabling/disabling selection of Messages
	private boolean showMessages = true;
	
	@Override
	public void collectElements ( Object input, List list) {
		
		if (showMessages) {
			if (input instanceof Definition) {
				list.addAll( ((Definition)input).getEMessages() );
				return;
			}
			
			collectComplex( input, list);
		}
	}

	public void setFilter(int filter) {
		showMessages = (filter!=0);
	}
}

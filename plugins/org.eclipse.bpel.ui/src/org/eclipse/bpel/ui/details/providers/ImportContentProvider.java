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

import org.eclipse.bpel.model.Process;

/**
 * Content provider for impots.
 * 
 * Expects a Process as input.
 */
public class ImportContentProvider extends AbstractContentProvider  {

	
	/**
	 * @see org.eclipse.bpel.ui.details.providers.AbstractContentProvider#collectElements(java.lang.Object, java.util.List)
	 */
	@Override
	public void collectElements(Object input, List<Object> list)  {
		if (input instanceof Process) {
			Process process = (Process) input;
			list.addAll ( process.getImports() );			
		}		
	}
	
}

/*******************************************************************************
 * Copyright (c) 2006 Oracle Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Oracle Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.bpel.common.ui.details;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;

/**
 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
 * @date Jul 27, 2007
 *
 */
public class ViewerIValue implements IValue {

	StructuredViewer fViewer;
	
	/**
	 * @param viewer
	 */
	public ViewerIValue ( StructuredViewer viewer ) {
		fViewer = viewer;
	}
	
	/**
	 * @see org.eclipse.bpel.common.ui.details.IValue#get()
	 */
	public Object get() {
		StructuredSelection ss = (StructuredSelection) fViewer.getSelection();
		return ss.getFirstElement();
	}

	/**
	 * @see org.eclipse.bpel.common.ui.details.IValue#set(java.lang.Object)
	 */
	public void set (Object object) {
		fViewer.setSelection( object == null ? StructuredSelection.EMPTY : new StructuredSelection(object), true);
	}

}

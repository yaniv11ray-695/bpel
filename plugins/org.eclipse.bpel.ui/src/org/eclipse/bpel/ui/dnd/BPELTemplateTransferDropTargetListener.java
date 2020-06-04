/*******************************************************************************
 * Copyright (c) 2006, 2012 Oracle Corporation and others.
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
package org.eclipse.bpel.ui.dnd;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;

/**
 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
 * @date May 18, 2007
 *
 */
public class BPELTemplateTransferDropTargetListener extends TemplateTransferDropTargetListener  {

	/**
	 * @param arg0
	 */
	public BPELTemplateTransferDropTargetListener(EditPartViewer arg0) {
		super(arg0);
	}		
	
}

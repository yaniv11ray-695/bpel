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
package org.eclipse.bpel.ui;

import org.eclipse.gef.editparts.ScalableRootEditPart;

public class GraphicalBPELRootEditPart extends ScalableRootEditPart {

	public GraphicalBPELRootEditPart() { 
		super();
		double zoom[] = {.1, .25, .5, .75, 1.0, 1.5, 2.0, 2.5, 3, 4, 6, 9};
		this.getZoomManager().setZoomLevels(zoom);
	}

}

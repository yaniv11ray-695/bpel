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
package org.eclipse.bpel.ui.editparts.figures;

import org.eclipse.bpel.ui.editparts.borders.ScopeBorder;
import org.eclipse.swt.graphics.Image;


public class CollapsableScopeContainerFigure extends CollapsableContainerFigure {
	
	public CollapsableScopeContainerFigure(Object modelObject, Image image, String text) {
		super(modelObject, image, text);
	}

	/**
	 * Override initializeBorder
	 */
	@Override
	protected void initializeBorder() {
		this.border = new ScopeBorder(this, borderText, borderImage);
	}
}

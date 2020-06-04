/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
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
package org.eclipse.bpel.common.ui.details.widgets;

import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.Graphics;
import org.eclipse.swt.graphics.Image;

public class NoBorderButton extends Button {

	public NoBorderButton(Image image) {
		super(image);
	}
	@Override
	protected void paintBorder(Graphics graphics) {
		// Do nothing
	}
}

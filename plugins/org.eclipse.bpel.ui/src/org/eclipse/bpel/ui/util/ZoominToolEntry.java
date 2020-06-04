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
package org.eclipse.bpel.ui.util;

import org.eclipse.bpel.ui.Messages;
import org.eclipse.gef.SharedImages;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.ToolEntry;


public class ZoominToolEntry extends ToolEntry {
	public ZoominToolEntry() {
		this(Messages.ZoominToolEntry_Zoom_in_1); 
	}

	public ZoominToolEntry(String label) {
		this(label, null);
	}

	public ZoominToolEntry(String label, String shortDesc) {
		super(
			label,
			shortDesc,
			SharedImages.DESC_SELECTION_TOOL_16, // these aren't the ones we want but leave them as placeholders
			SharedImages.DESC_SELECTION_TOOL_24);
		setUserModificationPermission(PERMISSION_NO_MODIFICATION);
	}

	@Override
	public Tool createTool() {
		return new ZoominTool();
	}
}

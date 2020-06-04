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
package org.eclipse.bpel.ui.commands;

import org.eclipse.bpel.ui.IBPELUIConstants;
import org.eclipse.bpel.ui.uiextensionmodel.ProcessExtension;


/** 
 * Sets the the process spec-compliant flag.
 */
public class SetSpecCompliantProcessCommand extends SetCommand {

	public String getDefaultLabel() { return IBPELUIConstants.CMD_SELECT_SPECCOMPLIANT; }

	public SetSpecCompliantProcessCommand(ProcessExtension target, boolean specCompliant)  {
		super(target, Boolean.valueOf( specCompliant ));
	}

	@Override
	public Object get() {
		return Boolean.valueOf(((ProcessExtension)fTarget).isSpecCompliant());
	}
	@Override
	public void set(Object o) {
		((ProcessExtension)fTarget).setSpecCompliant(((Boolean)o).booleanValue());
	}
}

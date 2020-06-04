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
package org.eclipse.bpel.ui.actions;

import java.text.MessageFormat;

import org.eclipse.bpel.ui.Messages;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.LabelRetargetAction;


public class RevertRetargetAction extends LabelRetargetAction {
	/**
	 * Constructs a new RedoRetargetAction with the default ID, label and image.
	 */
	public RevertRetargetAction() {
		super(ActionFactory.REVERT.getId(),
			MessageFormat.format(Messages.RevertRetargetAction_Rever_t_1,  
			new Object[] {""}).trim()); //$NON-NLS-1$
	}
}

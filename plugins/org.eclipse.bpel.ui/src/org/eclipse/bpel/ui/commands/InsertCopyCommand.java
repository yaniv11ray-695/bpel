/*******************************************************************************
 * Copyright (c) 2008, 2012 Intel Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Oleg Danilov, Intel - Initial API and Implementation
 *
 *******************************************************************************/

package org.eclipse.bpel.ui.commands;

import org.eclipse.bpel.model.Assign;
import org.eclipse.bpel.model.Copy;
import org.eclipse.bpel.ui.IBPELUIConstants;
import org.eclipse.emf.common.util.EList;


/**
 * Adds a new Copy to an Assign activity.
 */
public class InsertCopyCommand extends InsertToListCommand {
	
	public InsertCopyCommand(Assign target, Copy newCopy, int pos) {
		super(target, newCopy, pos, IBPELUIConstants.CMD_INSERT_COPY);
	}

	@Override
	protected EList<Copy> getList() {
		return ((Assign)target).getCopy();
	}
}

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

public class SwapCopyCommand extends SwapInListCommand {

	public SwapCopyCommand(Assign target, int firstElement, int secondElement) {
		super(target, firstElement, secondElement,
				IBPELUIConstants.CMD_SWAP_COPY);
	}

	@Override
	protected EList<Copy> getList() {
		return ((Assign) target).getCopy();
	}

}

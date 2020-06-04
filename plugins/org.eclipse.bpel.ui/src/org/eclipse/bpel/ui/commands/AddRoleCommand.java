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

import org.eclipse.bpel.model.partnerlinktype.PartnerLinkType;
import org.eclipse.bpel.model.partnerlinktype.Role;
import org.eclipse.bpel.ui.IBPELUIConstants;
import org.eclipse.emf.common.util.EList;


/**
 * Adds a new Role to a PartnerLinkType.
 */
public class AddRoleCommand extends AddToListCommand {

	public AddRoleCommand(PartnerLinkType target, Role newRole) {
		super(target, newRole, IBPELUIConstants.CMD_ADD_ROLE);
	}

	@Override
	protected EList<Role> getList() {
		return ((PartnerLinkType)target).getRole();
	}
}

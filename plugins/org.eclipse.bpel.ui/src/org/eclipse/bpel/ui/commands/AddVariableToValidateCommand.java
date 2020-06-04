/*******************************************************************************
 * Copyright (c) 2006, 2012 IBM Corporation and others.
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

import org.eclipse.bpel.model.Variable;
import org.eclipse.bpel.ui.IBPELUIConstants;
import org.eclipse.bpel.ui.util.ModelHelper;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;


/**
 * Adds a Variable to an Validate.
 */
public class AddVariableToValidateCommand extends AddToListCommand {

	public AddVariableToValidateCommand(EObject target, Variable newVariable) {
		super(target, newVariable, IBPELUIConstants.CMD_ADD_VALIDATE_VARIABLE);
	}

	@Override
	protected EList<Variable> getList() {
		EList<Variable> l = ModelHelper.getValidateVariables(target);
		return (l == null)? null : l;
	}

	@Override
	protected void deleteList() {
		EList<Variable> l = ModelHelper.getValidateVariables(target);
		if (l != null)
			l.clear();
	}
}

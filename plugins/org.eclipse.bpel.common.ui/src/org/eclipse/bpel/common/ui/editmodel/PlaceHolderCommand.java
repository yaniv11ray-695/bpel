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
package org.eclipse.bpel.common.ui.editmodel;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gef.commands.Command;

public final class PlaceHolderCommand extends Command implements IEditModelCommand {

	private static Resource[] EMTPY_RESOURCE_ARRAY = new Resource[0];
/**
 * @param label
 * @todo Generated comment
 */
public PlaceHolderCommand(String label) {
	super(label);
}

@Override
public final void execute() {}
@Override
public final void undo() {}
@Override
public final void redo() {}

public Resource[] getResources() {
	return EMTPY_RESOURCE_ARRAY;
}

public Resource[] getModifiedResources() {
	return EMTPY_RESOURCE_ARRAY;
}
}

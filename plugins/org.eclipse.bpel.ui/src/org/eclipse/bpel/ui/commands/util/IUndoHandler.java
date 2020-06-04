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
package org.eclipse.bpel.ui.commands.util;

/**
 * Clients can implement this interface and register their IUndoHandler to perform
 * arbitrary actions at a given point during automatic undo/redo of an AutoUndoCommand. 
 */
public interface IUndoHandler {
	public void undo();
	public void redo();
}

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

import java.io.IOException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

/**
 * Used by the shared resource framework. Should not be implemented
 * by clients.
 */
interface SynchronizationHandler {
	/**
	 * The file was changed from another editor. 
	 * The user wants to close the editor.
	 */
	public void closeEditor();
	/**
	 * The file was changed from another editor.
	 * The user wants to save this file with another name.
	 */
	public boolean saveFileAs(ResourceInfo resource,IFile file) throws CoreException, IOException;
	/**
	 * The file was changed from another editor.
	 * The user wants to reload its content.
	 */
	public void refresh(ResourceInfo resource);
}
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
package org.eclipse.bpel.ui.extensions;

import org.eclipse.bpel.ui.IHoverHelper;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;


public class HoverHelperDescriptor {

	protected IConfigurationElement element;

	public IHoverHelper createHoverHelper() throws CoreException {
		return (IHoverHelper)element.createExecutableExtension(BPELUIRegistry.ATT_CLASS);
	}

	IConfigurationElement getElement() {
		return element;
	}

	void setElement(IConfigurationElement element) {
		this.element = element;
	}
}

/*******************************************************************************
 * Copyright (c) 2008, 2012 IBM Corporation and others.
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
package org.eclipse.bpel.examples.extensionpoints.palette;

import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.ui.factories.UIObjectFactoryProvider;
import org.eclipse.bpel.ui.util.BPELCreationToolEntry;

/**
 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
 * @date May 21, 2007
 *
 */

public class ExitToolEntry extends BPELCreationToolEntry {

	/**
	 * Use the default constructor.
	 */
	static UIObjectFactoryProvider fProvider = UIObjectFactoryProvider.getInstance();
	
	/**
	 * 
	 */
	public ExitToolEntry () {
		super("Exit", "Exit Activity", fProvider.getFactoryFor(BPELPackage.eINSTANCE.getExit()) );
	}

}

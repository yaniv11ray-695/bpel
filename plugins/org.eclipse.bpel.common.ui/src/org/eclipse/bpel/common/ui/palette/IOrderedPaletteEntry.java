/*******************************************************************************
 * Copyright (c) 2006 Oracle Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Oracle Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.bpel.common.ui.palette;

/**
 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
 * @date May 29, 2007
 *
 */
public interface IOrderedPaletteEntry {

	/**
	 * Return the order of this palette entry within the palette.
	 * 
	 * @return the order (lower means first).
	 */
	
	int getOrder ();
	
	
	
	/**
	 * Return the category id.
	 * 
	 * @return the category id.
	 */
	
	String getCategoryId ();
}

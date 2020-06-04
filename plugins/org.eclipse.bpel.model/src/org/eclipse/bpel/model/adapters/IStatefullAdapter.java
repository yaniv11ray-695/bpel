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
package org.eclipse.bpel.model.adapters;

/**
 * If an adapter implements this interface, it is assumed to be statefull.
 * The AdapterProvider helper class will always return new instances of
 * such adapter classes.
 * 
 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
 * @date Jul 28, 2006
 *
 */

public interface IStatefullAdapter {
	
	/**
	 * Set the target object.
	 * 
	 * @param target the target object
	 */
	public void setTarget (Object target);

}

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
 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
 * @date Sep 18, 2006
 *
 */
public class AbstractStatefulAdapter extends AbstractAdapter implements
		IStatefullAdapter {

	/**
	 * @see org.eclipse.bpel.model.adapters.IStatefullAdapter#setTarget(java.lang.Object)
	 */
	
	@Override
	public void setTarget (Object newTarget) {		
		super.setTarget(newTarget);
	}

}

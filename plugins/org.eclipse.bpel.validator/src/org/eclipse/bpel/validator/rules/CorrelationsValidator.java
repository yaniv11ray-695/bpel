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
package org.eclipse.bpel.validator.rules;

import org.eclipse.bpel.validator.model.IFilter;
import org.eclipse.bpel.validator.model.INode;
import org.eclipse.bpel.validator.model.NodeNameFilter;


/**
 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
 * @date Dec 7, 2006
 *
 */
public class CorrelationsValidator extends CContainerValidator {

	/** Nodes which can be our parents */
	static public IFilter<INode> PARENTS = new NodeNameFilter( 
		ND_RECEIVE, 
		ND_INVOKE, 
		ND_REPLY,
		ND_ON_EVENT, 
		ND_ON_MESSAGE 
	);
	
	
	/** (non-Javadoc)
	 * @see org.eclipse.bpel.validator.rules.CContainerValidator#checkChildren()
	 */
	
	@Override
	public void checkChildren () {		
		super.checkChildren();		
		checkChild(ND_CORRELATION, 1, Integer.MAX_VALUE);
	}
}

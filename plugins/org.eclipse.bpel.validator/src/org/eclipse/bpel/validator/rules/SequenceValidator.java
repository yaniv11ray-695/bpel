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


import org.eclipse.bpel.validator.model.Filters;


/**
 * Validates sequences.
 * 
 *  
 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
 * @date Sep 14, 2006
 *
 */

public class SequenceValidator extends CActivityValidator {
	
	/**
	 * @see org.eclipse.bpel.validator.rules.CActivityValidator#checkChildren()
	 */
	
	@Override
	public void checkChildren () {
		super.checkChildren();
		checkChild(Filters.ACTIVITIES,1,Integer.MAX_VALUE);
	}
	
	/**
	 * Start the validation process
	 */
	
	@Override
	protected void start () {		
		super.start();
							
	}
	
			
	/** End of public rule methods.
	 * 
	 * Other methods are support methods for this class to perform its
	 * validation function.
	 * 
	 */

}

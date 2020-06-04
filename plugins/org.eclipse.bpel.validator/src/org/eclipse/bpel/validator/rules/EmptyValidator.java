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

import org.eclipse.bpel.validator.model.ARule;




/**
 * Validates Empty nodes.
 * 
 *  
 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
 * @date Sep 14, 2006
 *
 */


public class EmptyValidator extends CActivityValidator {
			
		
	
	/**
	 * This rule is empty by design, because the spec has an empty SA rule.
	 * It's rule 49, and it's missing :-)
	 * 
     * Clearly, for completeness, no better place to put it then here.
	 */
	
	@ARule(
		sa = 49,
		desc = "The static analysis check number 49. Refer to spec for description",
		author = "michal.chmielewski@oracle.com",
		date = "02/28/2007",
		order = 0
	)
	public void rule_SA49_0 () {
		
	}
	
	/** End of public rule methods.
	 * 
	 * Other methods are support methods for this class to perform its
	 * validation function.
	 * 
	 */

}

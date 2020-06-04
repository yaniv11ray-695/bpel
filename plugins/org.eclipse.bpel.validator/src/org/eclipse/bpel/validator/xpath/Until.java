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
package org.eclipse.bpel.validator.xpath;

import org.eclipse.bpel.validator.model.ARule;

/**
 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
 * @date Mar 23, 2007
 *
 */
public class Until extends XPathValidator {

	
	/**
	 * Check the deadline expression.
	 */	
	
	@Override
	@ARule(
		sa = 1024,
		desc = "Check deadline expression on the Until node",
		author = "michal.chmielewski@oracle.com",
		date = "01/20/2007",
		order = 15
	)
			
	public void checkDeadlineExpression () {
		super.checkDeadlineExpression();
	}	
	
}

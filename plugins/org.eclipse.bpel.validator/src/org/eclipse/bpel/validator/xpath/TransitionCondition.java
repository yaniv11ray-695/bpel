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


public class TransitionCondition extends XPathValidator {
	
	
	/**
	 * Boolean expressions on transitionCondition
	 */
	// fix: Bug 323939
	@ARule(
		sa = 1022,
		desc = "Checks for boolean expression on transitionCondition",
		author = "michal.chmielewski@oracle.com",
		date = "01/30/2007",
		order = 999
	)
	@Override
	public void checkBooleanExpression () {
		super.checkBooleanExpression();
	}
	
	
	
	
	
}

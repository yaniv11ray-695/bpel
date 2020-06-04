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
package org.eclipse.bpel.validator.helpers;

/**
 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
 * @date Aug 6, 2007
 *
 */
public class QNameWordDetector extends XMLNameWordDetector {
	
	int colCount = 0;
	
	/**
	 * These always start with a $
	 */
	@Override
	public boolean isWordStart (char c) {
		colCount = 0;
		return super.isWordStart(c);
	}
	
	/** 
	 *
	 */
	@Override
	public boolean isWordPart (char c) {
		if (c == ':') {
			if (colCount == 0) {
				colCount += 1;
				return true;
			}				
			return false;
		}
		return super.isWordPart(c); 
	}
}

/*******************************************************************************
 * Copyright (c) 2005, 2012 IBM Corporation and others.
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
package org.eclipse.bpel.ui.commands;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.CompensateScope;


/** 
 * Sets a compensate activity link property of a model object.
 * Supported for compensate activity
 */
public class SetCompensateCommand extends SetCommand {
	
	/**
	 * @param activity
	 * @param targetActivity
	 */
	public SetCompensateCommand (CompensateScope activity, Activity targetActivity)  {
		super(activity, targetActivity, BPELPackage.eINSTANCE.getCompensateScope_Target() );				
	}
	
	/**
	 * @return the target of this set command.
	 */
	public Object getTarget(){
		return fTarget;
	}
}

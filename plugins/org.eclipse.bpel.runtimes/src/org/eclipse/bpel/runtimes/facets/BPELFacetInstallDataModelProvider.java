/*******************************************************************************
 * Copyright (c) 2008, 2012 IBM Corporation and others.
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
package org.eclipse.bpel.runtimes.facets;

import java.util.Set;

import org.eclipse.bpel.runtimes.IBPELModuleFacetConstants;
import org.eclipse.wst.common.componentcore.datamodel.FacetInstallDataModelProvider;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetDataModelProperties;
import org.eclipse.wst.common.project.facet.core.IActionConfigFactory;

public class BPELFacetInstallDataModelProvider extends
		FacetInstallDataModelProvider implements IActionConfigFactory, IFacetDataModelProperties, IBPELModuleFacetConstants {
	
	@Override
	public Set getPropertyNames() {
		Set names = super.getPropertyNames();
		names.add(FACET_PROJECT_NAME);
		names.add(FACET_ID);
		names.add(BPEL_CONTENT_FOLDER);
		return names;
	}

	@Override
	public Object getDefaultProperty(String propertyName) {
		if (propertyName.equals(FACET_ID)) {
			return IBPELModuleFacetConstants.BPEL20_PROJECT_FACET;
		}
		return super.getDefaultProperty(propertyName);
	}
}

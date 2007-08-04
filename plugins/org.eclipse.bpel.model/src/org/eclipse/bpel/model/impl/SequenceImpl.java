/**
 * <copyright>
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 * </copyright>
 *
 * $Id: SequenceImpl.java,v 1.2.2.1 2007/08/04 09:28:40 smoser Exp $
 */
package org.eclipse.bpel.model.impl;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.Assign;
import org.eclipse.bpel.model.BPELFactory;
import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.Compensate;
import org.eclipse.bpel.model.CompensateScope;
import org.eclipse.bpel.model.Documentation;
import org.eclipse.bpel.model.Empty;
import org.eclipse.bpel.model.Exit;
import org.eclipse.bpel.model.Flow;
import org.eclipse.bpel.model.ForEach;
import org.eclipse.bpel.model.If;
import org.eclipse.bpel.model.Invoke;
import org.eclipse.bpel.model.Pick;
import org.eclipse.bpel.model.Receive;
import org.eclipse.bpel.model.Reply;
import org.eclipse.bpel.model.RepeatUntil;
import org.eclipse.bpel.model.Rethrow;
import org.eclipse.bpel.model.Scope;
import org.eclipse.bpel.model.Sequence;
import org.eclipse.bpel.model.Sources;
import org.eclipse.bpel.model.Targets;
import org.eclipse.bpel.model.Throw;
import org.eclipse.bpel.model.Validate;
import org.eclipse.bpel.model.Wait;
import org.eclipse.bpel.model.While;
import org.eclipse.bpel.model.util.BPELConstants;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.w3c.dom.Element;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Sequence</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.bpel.model.impl.SequenceImpl#getActivities <em>Activities</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SequenceImpl extends ActivityImpl implements Sequence {
	/**
	 * The cached value of the '{@link #getActivities() <em>Activities</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActivities()
	 * @generated
	 * @ordered
	 */
	protected EList activities = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SequenceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return BPELPackage.eINSTANCE.getSequence();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getActivities() {
		if (activities == null) {
			activities = new EObjectContainmentEList(Activity.class, this, BPELPackage.SEQUENCE__ACTIVITIES);
		}
		return activities;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case BPELPackage.SEQUENCE__EEXTENSIBILITY_ELEMENTS:
					return ((InternalEList)getEExtensibilityElements()).basicRemove(otherEnd, msgs);
				case BPELPackage.SEQUENCE__DOCUMENTATION:
					return basicUnsetDocumentation(msgs);
				case BPELPackage.SEQUENCE__TARGETS:
					return basicSetTargets(null, msgs);
				case BPELPackage.SEQUENCE__SOURCES:
					return basicSetSources(null, msgs);
				case BPELPackage.SEQUENCE__ACTIVITIES:
					return ((InternalEList)getActivities()).basicRemove(otherEnd, msgs);
				default:
					return eDynamicInverseRemove(otherEnd, featureID, baseClass, msgs);
			}
		}
		return eBasicSetContainer(null, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case BPELPackage.SEQUENCE__DOCUMENTATION_ELEMENT:
				return getDocumentationElement();
			case BPELPackage.SEQUENCE__ELEMENT:
				return getElement();
			case BPELPackage.SEQUENCE__EEXTENSIBILITY_ELEMENTS:
				return getEExtensibilityElements();
			case BPELPackage.SEQUENCE__DOCUMENTATION:
				return getDocumentation();
			case BPELPackage.SEQUENCE__NAME:
				return getName();
			case BPELPackage.SEQUENCE__SUPPRESS_JOIN_FAILURE:
				return getSuppressJoinFailure();
			case BPELPackage.SEQUENCE__TARGETS:
				return getTargets();
			case BPELPackage.SEQUENCE__SOURCES:
				return getSources();
			case BPELPackage.SEQUENCE__ACTIVITIES:
				return getActivities();
		}
		return eDynamicGet(eFeature, resolve);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(EStructuralFeature eFeature, Object newValue) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case BPELPackage.SEQUENCE__DOCUMENTATION_ELEMENT:
				setDocumentationElement((Element)newValue);
				return;
			case BPELPackage.SEQUENCE__ELEMENT:
				setElement((Element)newValue);
				return;
			case BPELPackage.SEQUENCE__EEXTENSIBILITY_ELEMENTS:
				getEExtensibilityElements().clear();
				getEExtensibilityElements().addAll((Collection)newValue);
				return;
			case BPELPackage.SEQUENCE__DOCUMENTATION:
				setDocumentation((Documentation)newValue);
				return;
			case BPELPackage.SEQUENCE__NAME:
				setName((String)newValue);
				return;
			case BPELPackage.SEQUENCE__SUPPRESS_JOIN_FAILURE:
				setSuppressJoinFailure((Boolean)newValue);
				return;
			case BPELPackage.SEQUENCE__TARGETS:
				setTargets((Targets)newValue);
				return;
			case BPELPackage.SEQUENCE__SOURCES:
				setSources((Sources)newValue);
				return;
			case BPELPackage.SEQUENCE__ACTIVITIES:
				getActivities().clear();
				getActivities().addAll((Collection)newValue);
				return;
		}
		eDynamicSet(eFeature, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case BPELPackage.SEQUENCE__DOCUMENTATION_ELEMENT:
				setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
				return;
			case BPELPackage.SEQUENCE__ELEMENT:
				setElement(ELEMENT_EDEFAULT);
				return;
			case BPELPackage.SEQUENCE__EEXTENSIBILITY_ELEMENTS:
				getEExtensibilityElements().clear();
				return;
			case BPELPackage.SEQUENCE__DOCUMENTATION:
				unsetDocumentation();
				return;
			case BPELPackage.SEQUENCE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case BPELPackage.SEQUENCE__SUPPRESS_JOIN_FAILURE:
				unsetSuppressJoinFailure();
				return;
			case BPELPackage.SEQUENCE__TARGETS:
				setTargets((Targets)null);
				return;
			case BPELPackage.SEQUENCE__SOURCES:
				setSources((Sources)null);
				return;
			case BPELPackage.SEQUENCE__ACTIVITIES:
				getActivities().clear();
				return;
		}
		eDynamicUnset(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case BPELPackage.SEQUENCE__DOCUMENTATION_ELEMENT:
				return DOCUMENTATION_ELEMENT_EDEFAULT == null ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
			case BPELPackage.SEQUENCE__ELEMENT:
				return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
			case BPELPackage.SEQUENCE__EEXTENSIBILITY_ELEMENTS:
				return eExtensibilityElements != null && !eExtensibilityElements.isEmpty();
			case BPELPackage.SEQUENCE__DOCUMENTATION:
				return isSetDocumentation();
			case BPELPackage.SEQUENCE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case BPELPackage.SEQUENCE__SUPPRESS_JOIN_FAILURE:
				return isSetSuppressJoinFailure();
			case BPELPackage.SEQUENCE__TARGETS:
				return targets != null;
			case BPELPackage.SEQUENCE__SOURCES:
				return sources != null;
			case BPELPackage.SEQUENCE__ACTIVITIES:
				return activities != null && !activities.isEmpty();
		}
		return eDynamicIsSet(eFeature);
	}
	
	protected void handleReconciliation(Collection remainingModelObjects) {
		for (Iterator i = remainingModelObjects.iterator(); i.hasNext();){
			remove(this, i.next());
		}
	}

	public void handleUnreconciledElement(Element child, Collection remainingModelObjects) {
		String bpelType = child.getLocalName();

		int childIndex = getActivityNodeIndex(child);

		if (bpelType.equals(BPELConstants.ND_EMPTY)) {
			Empty empty = BPELFactory.eINSTANCE.createEmpty();
			empty.setElement(child);
			getActivities().add(childIndex, empty);
		} else if (bpelType.equals(BPELConstants.ND_INVOKE)) {
			Invoke invoke = BPELFactory.eINSTANCE.createInvoke();
			invoke.setElement(child);
			getActivities().add(childIndex, invoke);
		} else if (bpelType.equals(BPELConstants.ND_RECEIVE)) {
			Receive receive = BPELFactory.eINSTANCE.createReceive();
			receive.setElement(child);
			getActivities().add(childIndex, receive);
		} else if (bpelType.equals(BPELConstants.ND_REPLY)) {
			Reply reply = BPELFactory.eINSTANCE.createReply();
			reply.setElement(child);
			getActivities().add(childIndex, reply);
		} else if (bpelType.equals(BPELConstants.ND_VALIDATE)) {
			Validate validate = BPELFactory.eINSTANCE.createValidate();
			validate.setElement(child);
			getActivities().add(childIndex, validate);
		} else if (bpelType.equals(BPELConstants.ND_IF)) {
			If _if = BPELFactory.eINSTANCE.createIf();
			_if.setElement(child);
			getActivities().add(childIndex, _if);
		} else if (bpelType.equals(BPELConstants.ND_PICK)) {
			Pick pick = BPELFactory.eINSTANCE.createPick();
			pick.setElement(child);
			getActivities().add(childIndex, pick);
		} else if (bpelType.equals(BPELConstants.ND_WHILE)) {
			While _while = BPELFactory.eINSTANCE.createWhile();
			_while.setElement(child);
			getActivities().add(childIndex, _while);
		} else if (bpelType.equals(BPELConstants.ND_FOR_EACH)) {
			ForEach foreach = BPELFactory.eINSTANCE.createForEach();
			foreach.setElement(child);
			getActivities().add(childIndex, foreach);
		} else if (bpelType.equals(BPELConstants.ND_REPEAT_UNTIL)) {
			RepeatUntil repeatUntil = BPELFactory.eINSTANCE.createRepeatUntil();
			repeatUntil.setElement(child);
			getActivities().add(childIndex, repeatUntil);
		} else if (bpelType.equals(BPELConstants.ND_WAIT)) {
			Wait wait = BPELFactory.eINSTANCE.createWait();
			wait.setElement(child);
			getActivities().add(childIndex, wait);
		} else if (bpelType.equals(BPELConstants.ND_SEQUENCE)) {
			Sequence sequence = BPELFactory.eINSTANCE.createSequence();
			sequence.setElement(child);
			getActivities().add(childIndex, sequence);
		} else if (bpelType.equals(BPELConstants.ND_SCOPE)) {
			Scope scope = BPELFactory.eINSTANCE.createScope();
			scope.setElement(child);
			getActivities().add(childIndex, scope);
		} else if (bpelType.equals(BPELConstants.ND_FLOW)) {
			Flow flow = BPELFactory.eINSTANCE.createFlow();
			flow.setElement(child);
			getActivities().add(childIndex, flow);
		} else if (bpelType.equals(BPELConstants.ND_EXIT)) {
			Exit exit = BPELFactory.eINSTANCE.createExit();
			exit.setElement(child);
			getActivities().add(childIndex, exit);
		} else if (bpelType.equals(BPELConstants.ND_THROW)) {
			Throw _throw = BPELFactory.eINSTANCE.createThrow();
			_throw.setElement(child);
			getActivities().add(childIndex, _throw);
		} else if (bpelType.equals(BPELConstants.ND_RETHROW)) {
			Rethrow rethrow = BPELFactory.eINSTANCE.createRethrow();
			rethrow.setElement(child);
			getActivities().add(childIndex, rethrow);
		} else if (bpelType.equals(BPELConstants.ND_COMPENSATE)) {
			Compensate compensate = BPELFactory.eINSTANCE.createCompensate();
			compensate.setElement(child);
			getActivities().add(childIndex, compensate);
		} else if (bpelType.equals(BPELConstants.ND_COMPENSATE_SCOPE)) {
			CompensateScope compensateScope = BPELFactory.eINSTANCE.createCompensateScope();
			compensateScope.setElement(child);
			getActivities().add(childIndex, compensateScope);
		} else if (bpelType.equals(BPELConstants.ND_ASSIGN)) {
			Assign assign = BPELFactory.eINSTANCE.createAssign();
			assign.setElement(child);
			getActivities().add(childIndex, assign);
		} else {
    	  	super.handleUnreconciledElement(child, remainingModelObjects);
		}
	}
	
	protected void remove(Object component, Object modelObject) {
	    ((Sequence)component).getActivities().remove(modelObject);
	}


} //SequenceImpl

/*******************************************************************************
 * Copyright (c) 2005, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.bpel.ui.properties;

import org.eclipse.bpel.common.ui.details.IOngoingChange;
import org.eclipse.bpel.common.ui.editmodel.AbstractEditModelCommand;
import org.eclipse.bpel.common.ui.flatui.FlatFormAttachment;
import org.eclipse.bpel.common.ui.flatui.FlatFormData;
import org.eclipse.bpel.model.BPELFactory;
import org.eclipse.bpel.model.Expression;
import org.eclipse.bpel.ui.IBPELUIConstants;
import org.eclipse.bpel.ui.commands.CompoundCommand;
import org.eclipse.bpel.ui.commands.SetCommand;
import org.eclipse.bpel.ui.editors.xpath.XPathTextEditor;
import org.eclipse.bpel.ui.expressions.IEditorConstants;
import org.eclipse.bpel.ui.util.BatchedMultiObjectAdapter;
import org.eclipse.bpel.ui.util.MultiObjectAdapter;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ide.IGotoMarker;

/**
 * Base class with some shared behavior for details panes that edit a XPath expression.
 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
 * @author Vincent Zurczak - EBM WebSourcing (Merge this class with TextSection and simplify it by only using the XPath editor)
 */
public abstract class ExpressionSection extends BPELPropertySection {

	protected Composite fEditorArea ;
	protected XPathTextEditor fEditor;

	protected Font boldFont;
	protected String title;
	protected Label titleLabel;

	protected EStructuralFeature fStructuralFeature;
	protected IOngoingChange change;


	public ExpressionSection() {

		this.change = new IOngoingChange() {
			public String getLabel() {
				return getCommandLabel();
			}
			public Command createApplyCommand() {
				if (ExpressionSection.this.fEditor == null) {
					return null;
				}

				CompoundCommand result = new CompoundCommand();
				Expression exp = BPELFactory.eINSTANCE.createCondition();
				exp.setBody( ExpressionSection.this.fEditor.getContents());
				result.add( new SetCommand( getExpressionTarget(), getExpression4Target( exp ) , getStructuralFeature()));

				// refresh the editor
				result.add( new AbstractEditModelCommand() {
					@Override
					public void execute() {
						if (ExpressionSection.this.fEditor != null) {
							ExpressionSection.this.fEditor.markAsClean();
						}
					}
					// TODO: is this correct?
					@Override
					public Resource[] getResources() {
						return new Resource[ 0 ];
					}
					@Override
					public Resource[] getModifiedResources() {
						return new Resource[ 0 ];
					}
				});

				return wrapInShowContextCommand(result);
			}

			public void restoreOldState() {
				// updateWidgets();
			}
		};
	}


	protected void notifyEditorChanged() {
		// TODO: why is this method being called before createClient() has finished
		// when a BuiltInExpressionEditor was selected in the model?
		if (this.change != null) {
			getCommandFramework().notifyChangeInProgress(this.change);
		}
	}


	/**
	 * @see org.eclipse.bpel.ui.properties.BPELPropertySection#aboutToBeHidden()
	 */
	@Override
	public void aboutToBeHidden() {
		super.aboutToBeHidden();
		if (this.change != null)
			getCommandFramework().notifyChangeDone(this.change);

		// if (this.fEditor != null) this.fEditor.aboutToBeHidden();
	}


	@Override
	protected void addAllAdapters() {
		super.addAllAdapters();
		Expression e = getExprFromModel();
		if (e != null)
			this.fAdapters[0].addToObject(e);
	}


	@Override
	protected MultiObjectAdapter[] createAdapters() {

		MultiObjectAdapter adapter = new BatchedMultiObjectAdapter() {

			boolean needRefresh = false;

			@Override
			public void notify (Notification n) {

				if (markersHaveChanged(n)) {
					updateMarkers();
					return ;
				}
				this.needRefresh = this.needRefresh || isBodyAffected(n);
				refreshAdapters();
			}

			@Override
			public void finish() {
				if (this.needRefresh) {
					// updateWidgets();
				}

				this.needRefresh = false;
			}
		};

		return new MultiObjectAdapter[] { adapter };
	}


	/**
	 *  When this method is called, the section has already been created.
	 *
	 *  The widgets are available to be configured, however, the section may not be shown yet.
	 *
	 *  The concept here is that we are reflecting the input selected in the UI. So the path that is taken
	 *  by this code must not create any re-doable commands, just set the widgets to what they are supposed to
	 *  be based on the model.
	 */
	@Override
	protected void basicSetInput (EObject newInput) {

		super.basicSetInput(newInput);

		/** Figure out based in the input, what EMF structural feature we are setting */
		this.fStructuralFeature = getStructuralFeature( newInput );
	}


	/**
	 * The expression target is the target object on which we can execute
	 * the SetCommand(target,object,structural-feature).
	 *
	 * In most cases, it is just the input of the section. But in some cases
	 * the input if the section does not match the target, so sub-classes
	 * may override this method.
	 *
	 * @return
	 */
	protected EObject getExpressionTarget () {
		return getInput();
	}

	protected String getExpressionType() {
		return IEditorConstants.ET_ANY ;
	}


	/**
	 * Return the previously computed structural feature of the input object.
	 * @return
	 */
	protected final EStructuralFeature getStructuralFeature() {
		return this.fStructuralFeature;
	}


	/**
	 * @param object
	 * @return the structural feature to update on the model object
	 */
	protected abstract EStructuralFeature getStructuralFeature( EObject object );


	/**
	 * Return the actual namespace from the expression, or null if not set.
	 */
	protected String getExpressionLanguageFromModel() {
		Expression expression = getExprFromModel();
		return expression != null ? expression.getExpressionLanguage() : null;
	}



	protected Expression getExprFromModel() {

		EObject target = getExpressionTarget();
		if (target != null) {
			Object result = target.eGet( getStructuralFeature());
			if (result instanceof Expression) {
				return (Expression) result;
			}
		}
		return null;
	}


	protected Expression getExpression4Target ( Expression expression ) {
		return expression;
	}


	/**
	 * Determines whether a notification affects this section.
	 * @param n a notification
	 * @return true if it affects this section, false otherwise
	 */
	protected boolean isBodyAffected( Notification n ) {

		return n.getOldValue() instanceof Expression
				|| n.getNewValue() instanceof Expression
				|| n.getNotifier() instanceof Expression
				|| n.getFeature() == getStructuralFeature();
	}


	/*
	 * (non-Javadoc)
	 * @see org.eclipse.bpel.ui.properties.BPELPropertySection
	 * #isValidMarker(org.eclipse.core.resources.IMarker)
	 */
	@Override
	public final boolean isValidMarker( IMarker marker ) {

		// FIXME: the implementation may not be valid for all the sub-classes
		boolean result = false;
		try {
			String context = (String) marker.getAttribute( "href.context" );
			result = "name".equals ( context );

		} catch( Exception ex ) {
			// nothing
		}

		return result;
	}


	protected String getCommandLabel() {
		return IBPELUIConstants.CMD_EDIT_EXPRESSION;
	}


	/**
	 * Create the client area. This is just done once.
	 */

	@Override
	protected void createClient( Composite parent ) {

		// The top
		this.fEditorArea =  createFlatFormComposite( parent );
		if( this.title != null ) {

			// The font
			FontData[] fontData = parent.getDisplay().getSystemFont().getFontData();
			fontData[ 0 ].setStyle( SWT.BOLD );
			this.boldFont = new Font( parent.getDisplay(), fontData[ 0 ]);

			// The title
			FlatFormData data;
			this.titleLabel = this.fWidgetFactory.createLabel( this.fEditorArea, this.title);
			this.titleLabel.setFont(this.boldFont);
			data = new FlatFormData();
			data.left = new FlatFormAttachment(0, 0);
			data.top = new FlatFormAttachment(0, 0);
			data.right = new FlatFormAttachment(100, 0);
			this.titleLabel.setLayoutData(data);
		}

		// The expression editor
		this.fEditor.createPartControl( this.fEditorArea );
	}


	/*
	 * (non-Javadoc)
	 * @see org.eclipse.bpel.ui.properties.TextSection
	 * #dispose()
	 */
	@Override
	public void dispose() {

		this.fEditor.dispose();
		if( this.boldFont != null && ! this.boldFont.isDisposed())
			this.boldFont.dispose();

		super.dispose();
	}


	/*
	 * (non-Javadoc)
	 * @see org.eclipse.bpel.ui.properties.BPELPropertySection
	 * #updateMarkers()
	 */
	@Override
	protected void updateMarkers () {
		// TODO: implement it
	}


	/*
	 * (non-Javadoc)
	 * @see org.eclipse.bpel.ui.properties.BPELPropertySection
	 * #gotoMarker(org.eclipse.core.resources.IMarker)
	 */
	@Override
	public void gotoMarker( IMarker marker ) {
		IGotoMarker gotoMarker = (IGotoMarker) ((IAdaptable) this.fEditor).getAdapter( IGotoMarker.class );
		if( gotoMarker != null )
			gotoMarker.gotoMarker( marker );
	}
}
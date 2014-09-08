/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3IntLink;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3Title;

public class IntLinkImpl
		extends
			BackboneContainer
		implements
			Wom3IntLink
{
	private static final long serialVersionUID = 1L;
	
	private static final ChildDescriptor[] BODY_DESCRIPTOR = {
			childDesc("title") };
	
	private TitleImpl title;
	
	// =========================================================================
	
	public IntLinkImpl(DocumentImpl owner)
	{
		super(owner);
		setTarget("unknown");
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "intlink";
	}
	
	// =========================================================================
	
	@Override
	protected void allowsInsertion(Backbone prev, Backbone child)
	{
		checkInsertion(prev, child, BODY_DESCRIPTOR);
	}
	
	@Override
	protected void allowsRemoval(Backbone child)
	{
		checkRemoval(child, BODY_DESCRIPTOR);
	}
	
	@Override
	protected void allowsReplacement(Backbone oldChild, Backbone newChild)
	{
		checkReplacement(oldChild, newChild, BODY_DESCRIPTOR);
	}
	
	@Override
	public void childInserted(Backbone prev, Backbone added)
	{
		if (added instanceof Wom3Title)
			this.title = (TitleImpl) added;
	}
	
	@Override
	public void childRemoved(Backbone prev, Backbone removed)
	{
		if (removed == this.title)
			this.title = null;
	}
	
	// =========================================================================
	
	@Override
	public String getTarget()
	{
		return getAttribute("target");
	}
	
	@Override
	public String getLinkTarget()
	{
		return getTarget();
	}
	
	@Override
	public String setTarget(String target)
	{
		return setAttributeDirect(Attributes.TARGET, "target", target);
	}
	
	// =========================================================================
	
	@Override
	public Wom3Title setLinkTitle(Wom3Title title) throws NullPointerException
	{
		return (Wom3Title) replaceOrAppend(this.title, title, false);
	}
	
	@Override
	public Wom3Title getLinkTitle()
	{
		return title;
	}
	
	// =========================================================================
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDescStrict(namespaceUri, localName, qualifiedName,
				"target", Attributes.TARGET);
	}
	
	private static enum Attributes implements AttributeDescriptor
	{
		TARGET
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				if (verified.strValue == null)
					verified.strValue = (String) verified.value;
				Toolbox.checkValidTarget(verified.strValue);
				return true;
			}
		};
		
		// =====================================================================
		
		@Override
		public boolean isRemovable()
		{
			return false;
		}
		
		@Override
		public Normalization getNormalizationMode()
		{
			return Normalization.CDATA;
		}
		
		@Override
		public void customAction(
				Wom3Node parent,
				AttributeBase oldAttr,
				AttributeBase newAttr)
		{
		}
	}
}

/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import java.util.HashMap;
import java.util.Map;

import org.sweble.wom3.Wom3HorizAlign;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3Paragraph;

public class ParagraphImpl
		extends
			BackboneWomElemWithCoreAttrs
		implements
			Wom3Paragraph
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public ParagraphImpl(DocumentImpl owner)
	{
		super(owner);
		setTopGapAttr(0);
		setBottomGapAttr(0);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "p";
	}
	
	// =========================================================================
	
	@Override
	public Wom3HorizAlign getAlign()
	{
		return getAlignAttr("align");
	}
	
	@Override
	public Wom3HorizAlign setAlign(Wom3HorizAlign align)
	{
		return setAlignAttr(Attributes.ALIGN, "align", align);
	}
	
	@Override
	public int getTopGap()
	{
		return getIntAttr("topgap");
	}
	
	@Override
	public int setTopGap(int lines)
	{
		return setTopGapAttr(lines);
	}
	
	private Integer setTopGapAttr(int lines)
	{
		return setIntAttr(Attributes.TOPGAP, "topgap", lines);
	}
	
	@Override
	public int getBottomGap()
	{
		return getIntAttr("bottomgap");
	}
	
	@Override
	public int setBottomGap(int lines)
	{
		return setBottomGapAttr(lines);
	}
	
	private Integer setBottomGapAttr(int lines)
	{
		return setIntAttr(Attributes.BOTTOMGAP, "bottomgap", lines);
	}
	
	// =========================================================================
	
	private static final Map<String, AttributeDescriptor> nameMap = getNameMap();
	
	private static Map<String, AttributeDescriptor> getNameMap()
	{
		Map<String, AttributeDescriptor> nameMap =
				new HashMap<String, AttributeDescriptor>();
		
		nameMap.putAll(UniversalAttributes.getNameMap());
		nameMap.put("align", Attributes.ALIGN);
		nameMap.put("topgap", Attributes.TOPGAP);
		nameMap.put("bottomgap", Attributes.BOTTOMGAP);
		
		return nameMap;
	}
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDesc(namespaceUri, localName, qualifiedName, nameMap);
	}
	
	private static enum Attributes implements AttributeDescriptor
	{
		ALIGN
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.LCRJ_ALIGN.verifyAndConvert(parent, verified);
			}
			
			@Override
			public boolean isRemovable()
			{
				return true;
			}
		},
		
		TOPGAP
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.NUMBER.verifyAndConvert(parent, verified);
			}
		},
		
		BOTTOMGAP
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.NUMBER.verifyAndConvert(parent, verified);
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
			return Normalization.NON_CDATA;
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

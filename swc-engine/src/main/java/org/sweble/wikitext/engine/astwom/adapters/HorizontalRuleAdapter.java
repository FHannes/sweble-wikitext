/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sweble.wikitext.engine.astwom.adapters;

import lombok.AccessLevel;
import lombok.Delegate;
import lombok.Getter;
import lombok.Setter;

import org.sweble.wikitext.engine.astwom.AstToWomNodeFactory;
import org.sweble.wikitext.engine.astwom.AttributeDescriptor;
import org.sweble.wikitext.engine.astwom.AttributeManager;
import org.sweble.wikitext.engine.astwom.ChildManager;
import org.sweble.wikitext.engine.astwom.GenericAttributeDescriptor;
import org.sweble.wikitext.engine.astwom.MustBeOneOfException;
import org.sweble.wikitext.engine.astwom.NativeOrXmlElement;
import org.sweble.wikitext.engine.astwom.OtherAttributes;
import org.sweble.wikitext.engine.astwom.Toolbox;
import org.sweble.wikitext.engine.astwom.UniversalAttributes;
import org.sweble.wikitext.engine.wom.WomHorizAlign;
import org.sweble.wikitext.engine.wom.WomHorizontalRule;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.engine.wom.WomUniversalAttributes;
import org.sweble.wikitext.engine.wom.WomValueWithUnit;
import org.sweble.wikitext.lazy.parser.HorizontalRule;
import org.sweble.wikitext.lazy.parser.XmlElement;

import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.utils.Utils;

public class HorizontalRuleAdapter
		extends
			NativeOrXmlElement
		implements
			WomHorizontalRule
{
	private static final long serialVersionUID = 1L;
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Delegate(types = { WomUniversalAttributes.class, AttribAccessors.class })
	private AttributeManager attribManager = AttributeManager.emptyManager();
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	private ChildManager childManager = ChildManager.emptyManager();
	
	// =========================================================================
	
	public HorizontalRuleAdapter()
	{
		super(Toolbox.addRtData(new HorizontalRule()));
	}
	
	public HorizontalRuleAdapter(HorizontalRule astNode)
	{
		super(astNode);
	}
	
	public HorizontalRuleAdapter(
			AstToWomNodeFactory womNodeFactory,
			XmlElement astNode)
	{
		super("hr", astNode);
		addAttributes(womNodeFactory, astNode.getXmlAttributes());
		addContent(womNodeFactory, astNode.getBody());
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "hr";
	}
	
	// =========================================================================
	
	protected XmlElement convertToXmlElement()
	{
		return Toolbox.addRtData(new XmlElement(
				"hr",
				true,
				new NodeList(),
				new NodeList()));
	}
	
	// =========================================================================
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(String name)
	{
		AttributeDescriptor d = Utils.fromString(Attributes.class, name);
		if (d != null)
			return d;
		d = Utils.fromString(UniversalAttributes.class, name);
		if (d != null)
			return d;
		return GenericAttributeDescriptor.get();
	}
	
	private static enum Attributes implements AttributeDescriptor
	{
		align
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				return OtherAttributes.LCR_ALIGN.verify(parent, value);
			}
		},
		noshade
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				if (!Utils.isOneOf(NOSHADE_EXPECTED, value))
					throw new MustBeOneOfException(NOSHADE_EXPECTED, value);
				
				return value;
			}
		},
		size
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				return OtherAttributes.PIXELS.verify(parent, value);
			}
		},
		width
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				return OtherAttributes.LENGTH.verify(parent, value);
			}
		};
		
		// =====================================================================
		
		static final String[] NOSHADE_EXPECTED =
				new String[] { "noshade" };
		
		// =====================================================================
		
		@Override
		public boolean isRemovable()
		{
			return true;
		}
		
		@Override
		public boolean syncToAst()
		{
			return true;
		}
		
		@Override
		public Normalization getNormalizationMode()
		{
			return Normalization.NON_CDATA;
		}
		
		@Override
		public void customAction(WomNode parent, String value)
		{
		}
	}
	
	private interface AttribAccessors
	{
		public WomHorizAlign getAlign();
		
		public WomHorizAlign setAlign(WomHorizAlign align);
		
		public boolean isNoshade();
		
		public boolean setNoshade(boolean noshade);
		
		public Integer getSize();
		
		public Integer setSize(Integer size);
		
		public WomValueWithUnit getWidth();
		
		public WomValueWithUnit setWidth(WomValueWithUnit width);
	}
}

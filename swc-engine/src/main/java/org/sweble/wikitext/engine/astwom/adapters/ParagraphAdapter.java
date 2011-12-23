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

import java.util.ListIterator;

import lombok.AccessLevel;
import lombok.Delegate;
import lombok.Getter;
import lombok.Setter;

import org.sweble.wikitext.engine.astwom.AstToWomNodeFactory;
import org.sweble.wikitext.engine.astwom.AttributeDescriptor;
import org.sweble.wikitext.engine.astwom.AttributeManager;
import org.sweble.wikitext.engine.astwom.ChildManager;
import org.sweble.wikitext.engine.astwom.GenericAttributeDescriptor;
import org.sweble.wikitext.engine.astwom.NativeOrXmlElement;
import org.sweble.wikitext.engine.astwom.OtherAttributes;
import org.sweble.wikitext.engine.astwom.Toolbox;
import org.sweble.wikitext.engine.astwom.UniversalAttributes;
import org.sweble.wikitext.engine.wom.WomHorizAlign;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.engine.wom.WomParagraph;
import org.sweble.wikitext.engine.wom.WomUniversalAttributes;
import org.sweble.wikitext.lazy.AstNodeTypes;
import org.sweble.wikitext.lazy.parser.Newline;
import org.sweble.wikitext.lazy.parser.Paragraph;
import org.sweble.wikitext.lazy.parser.XmlElement;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.Text;
import de.fau.cs.osr.utils.StringUtils;
import de.fau.cs.osr.utils.Utils;

public class ParagraphAdapter
		extends
			NativeOrXmlElement
		implements
			WomParagraph
{
	private static final int MAX_GAP = 65535;
	
	private static final long serialVersionUID = 1L;
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Delegate(types = { WomUniversalAttributes.class, AttribAccessors.class })
	private AttributeManager attribManager = AttributeManager.emptyManager();
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	private ChildManager childManager = ChildManager.emptyManager();
	
	// =========================================================================
	
	public ParagraphAdapter()
	{
		super(new Paragraph());
	}
	
	public ParagraphAdapter(
			AstToWomNodeFactory womNodeFactory,
			Paragraph astNode)
	{
		super(astNode);
		addContent(womNodeFactory, astNode.getContent());
		gapsFromAst();
	}
	
	public ParagraphAdapter(
			AstToWomNodeFactory womNodeFactory,
			XmlElement astNode)
	{
		super("p", astNode);
		addContent(womNodeFactory, astNode.getBody());
		addAttributes(womNodeFactory, astNode.getXmlAttributes());
		
		// FIXME: How about gaps and <p> elements?
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "p";
	}
	
	// =========================================================================
	
	@Override
	public int getTopGap()
	{
		return getIntAttribute("topgap");
	}
	
	@Override
	public int setTopGap(int lines)
	{
		NativeOrXmlAttributeAdapter old = setAttribute(
				Attributes.topgap,
				"topgap",
				lines);
		
		return (old == null) ? null : old.getIntValue(0);
	}
	
	@Override
	public int getBottomGap()
	{
		return getIntAttribute("bottomgap");
	}
	
	@Override
	public int setBottomGap(int lines)
	{
		NativeOrXmlAttributeAdapter old = setAttribute(
				Attributes.bottomgap,
				"bottomgap",
				lines);
		
		return (old == null) ? null : old.getIntValue(0);
	}
	
	// =========================================================================
	
	protected XmlElement convertToXmlElement()
	{
		return Toolbox.addRtData(new XmlElement(
				"p",
				false,
				new NodeList(),
				getAstChildContainer()));
	}
	
	@Override
	public NodeList getAstChildContainer()
	{
		return isXml() ? xml().getBody() : ((Paragraph) getAstNode()).getContent();
	}
	
	// =========================================================================
	
	private void gapsFromAst()
	{
		NodeList container = getAstChildContainer();
		ListIterator<AstNode> i;
		
		int topGap = 0;
		i = container.listIterator();
		outer: while (i.hasNext())
		{
			AstNode n = i.next();
			switch (n.getNodeType())
			{
				case AstNode.NT_TEXT:
					if (StringUtils.isWhitespace(((Text) n).getContent()))
						break;
					break outer;
				case AstNodeTypes.NT_NEWLINE:
					++topGap;
					break;
				default:
					break outer;
			}
		}
		
		setAttribute(Attributes.TOPGAP_FROM_AST, "topgap", topGap);
		
		int bottomGap = 0;
		i = container.listIterator(container.size());
		outer: while (i.hasPrevious())
		{
			AstNode n = i.previous();
			switch (n.getNodeType())
			{
				case AstNode.NT_TEXT:
					if (StringUtils.isWhitespace(((Text) n).getContent()))
						break;
					break outer;
				case AstNodeTypes.NT_NEWLINE:
					++bottomGap;
					break;
				default:
					break outer;
			}
		}
		
		setAttribute(Attributes.BOTTOMGAP_FROM_AST, "bottomgap", bottomGap);
	}
	
	private void setTopGapInAst(int lines)
	{
		NodeList container = getAstChildContainer();
		ListIterator<AstNode> i = container.listIterator();
		
		int j = 0;
		outer: while (i.hasNext())
		{
			AstNode n = i.next();
			switch (n.getNodeType())
			{
				case AstNode.NT_TEXT:
					if (StringUtils.isWhitespace(((Text) n).getContent()))
						break;
					break outer;
				case AstNodeTypes.NT_NEWLINE:
					++j;
					if (j >= lines)
						break outer;
					break;
				default:
					break outer;
			}
		}
		
		if (j < lines)
		{
			// add more newlines
			// i points to the first non-newline (or to end-of-list)
			while (j < lines)
			{
				i.add(new Newline("\n"));
				++j;
			}
		}
		else
		{
			// remove all remaining newlines (if any)
			// i points to last newline
			outer: while (i.hasNext())
			{
				AstNode n = i.next();
				switch (n.getNodeType())
				{
					case AstNode.NT_TEXT:
						if (StringUtils.isWhitespace(((Text) n).getContent()))
							i.remove();
						break outer;
					case AstNodeTypes.NT_NEWLINE:
						i.remove();
						break;
					default:
						break outer;
				}
			}
		}
	}
	
	private void setBottomGapInAst(int lines)
	{
		NodeList container = getAstChildContainer();
		ListIterator<AstNode> i = container.listIterator(container.size());
		
		// We need one more newline at the end if we want "lines"
		// empty lines in the document.
		++lines;
		
		int j = 0;
		outer: while (i.hasPrevious())
		{
			AstNode n = i.previous();
			switch (n.getNodeType())
			{
				case AstNode.NT_TEXT:
					if (StringUtils.isWhitespace(((Text) n).getContent()))
						break;
					break outer;
				case AstNodeTypes.NT_NEWLINE:
					++j;
					if (j >= lines)
						break outer;
					break;
				default:
					break outer;
			}
		}
		
		if (j < lines)
		{
			// add more newlines
			// i points to the last non-newline (or to end-of-list)
			while (j < lines)
			{
				i.add(new Newline("\n"));
				++j;
			}
		}
		else
		{
			// remove all remaining newlines (if any)
			// i points to last newline
			outer: while (i.hasPrevious())
			{
				AstNode n = i.previous();
				switch (n.getNodeType())
				{
					case AstNode.NT_TEXT:
						if (StringUtils.isWhitespace(((Text) n).getContent()))
							i.remove();
						break outer;
					case AstNodeTypes.NT_NEWLINE:
						i.remove();
						break;
					default:
						break outer;
				}
			}
		}
	}
	
	// =========================================================================
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(String name)
	{
		AttributeDescriptor d = Utils.fromString(Attributes.class, name);
		if (d != null
				&& d != Attributes.TOPGAP_FROM_AST
				&& d != Attributes.BOTTOMGAP_FROM_AST)
			return d;
		d = Utils.fromString(UniversalAttributes.class, name);
		if (d != null)
			return d;
		return GenericAttributeDescriptor.get();
	}
	
	private enum Attributes implements AttributeDescriptor
	{
		topgap
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				int x = AttributeManager.verifyRange(value, 0, MAX_GAP);
				return (x == 0) ? null : value;
			}
			
			@Override
			public void customAction(WomNode parent, String value)
			{
				ParagraphAdapter p = (ParagraphAdapter) parent;
				int lines = (value != null) ? Integer.parseInt(value) : 0;
				p.setTopGapInAst(lines);
			}
		},
		
		bottomgap
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				int x = AttributeManager.verifyRange(value, 0, MAX_GAP);
				return (x == 0) ? null : value;
			}
			
			@Override
			public void customAction(WomNode parent, String value)
			{
				ParagraphAdapter p = (ParagraphAdapter) parent;
				int lines = (value != null) ? Integer.parseInt(value) : 0;
				p.setBottomGapInAst(lines);
			}
		},
		
		TOPGAP_FROM_AST
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				return value;
			}
			
			@Override
			public void customAction(WomNode parent, String value)
			{
			}
		},
		
		BOTTOMGAP_FROM_AST
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				return value;
			}
			
			@Override
			public void customAction(WomNode parent, String value)
			{
			}
		},
		
		align
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				return OtherAttributes.TEXT_ALIGN.verify(parent, value);
			}
			
			@Override
			public boolean syncToAst()
			{
				return true;
			}
			
			@Override
			public void customAction(WomNode parent, String value)
			{
			}
		};
		
		@Override
		public boolean syncToAst()
		{
			return false;
		}
		
		@Override
		public Normalization getNormalizationMode()
		{
			return Normalization.NON_CDATA;
		}
		
		@Override
		public boolean isRemovable()
		{
			return true;
		}
	}
	
	private interface AttribAccessors
	{
		public WomHorizAlign getAlign();
		
		public WomHorizAlign setAlign(WomHorizAlign align);
	}
}

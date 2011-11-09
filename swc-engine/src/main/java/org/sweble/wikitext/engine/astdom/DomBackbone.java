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
package org.sweble.wikitext.engine.astdom;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.sweble.wikitext.engine.astdom.adapters.NativeOrXmlAttributeAdapter;
import org.sweble.wikitext.engine.wom.WomAttribute;
import org.sweble.wikitext.engine.wom.WomNode;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.utils.StringUtils;

public abstract class DomBackbone
        implements
            WomNode
{
	private static final long serialVersionUID = 1L;
	
	private AstNode astNode;
	
	private DomBackbone parent;
	
	private DomBackbone prevSibling;
	
	private DomBackbone nextSibling;
	
	// =========================================================================
	
	public DomBackbone(AstNode astNode)
	{
		this.astNode = astNode;
	}
	
	// =========================================================================
	
	public AstNode getAstNode()
	{
		return astNode;
	}
	
	protected AstNode setAstNode(AstNode astNode)
	{
		AstNode old = this.astNode;
		this.astNode = astNode;
		return old;
	}
	
	public void replaceAstChild(AstNode oldNode, AstNode newNode)
	{
		throw new InternalError();
	}
	
	// =========================================================================
	
	protected void setParent(DomBackbone parent)
	{
		this.parent = parent;
	}
	
	protected void setPrevSibling(DomBackbone prevSibling)
	{
		this.prevSibling = prevSibling;
	}
	
	protected void setNextSibling(DomBackbone nextSibling)
	{
		this.nextSibling = nextSibling;
	}
	
	public final boolean isLinked()
	{
		return parent != null;
	}
	
	public void unlink()
	{
		parent = null;
		
		if (prevSibling != null)
			prevSibling.nextSibling = nextSibling;
		if (nextSibling != null)
			nextSibling.prevSibling = prevSibling;
		
		prevSibling = null;
		nextSibling = null;
	}
	
	public void link(DomBackbone parent, DomBackbone prevSibling, DomBackbone nextSibling)
	{
		if (isLinked())
			throw new IllegalStateException(
			        "Node is still child of another DOM node.");
		
		this.parent = parent;
		
		this.prevSibling = prevSibling;
		if (prevSibling != null)
		{
			if (prevSibling.nextSibling != nextSibling)
				throw new IllegalStateException(
				        "DOM sibling chain inconsistent.");
			
			prevSibling.nextSibling = this;
		}
		
		this.nextSibling = nextSibling;
		if (nextSibling != null)
		{
			if (nextSibling.prevSibling != prevSibling)
				throw new IllegalStateException(
				        "DOM sibling chain inconsistent.");
			
			nextSibling.prevSibling = this;
		}
	}
	
	// =========================================================================
	
	@Override
	public final DomBackbone getParent()
	{
		return parent;
	}
	
	@Override
	public final DomBackbone getPrevSibling()
	{
		return prevSibling;
	}
	
	@Override
	public final DomBackbone getNextSibling()
	{
		return nextSibling;
	}
	
	// =========================================================================
	
	@Override
	public final WomNode cloneNode()
	{
		try
		{
			return (WomNode) this.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new AssertionError();
		}
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		// FIXME: Implement automatic cloning in sub classes.
		DomBackbone node = (DomBackbone) super.clone();
		node.parent = null;
		node.nextSibling = null;
		node.prevSibling = null;
		return node;
	}
	
	// =========================================================================
	
	@Override
	public String getText()
	{
		return null;
	}
	
	@Override
	public String getValue()
	{
		return null;
	}
	
	@Override
	public void appendText(String text) throws UnsupportedOperationException
	{
		doesNotSupportTextOperations();
	}
	
	@Override
	public void deleteText(int from, int length) throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		doesNotSupportTextOperations();
	}
	
	@Override
	public void insertText(int at, String text) throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		doesNotSupportTextOperations();
	}
	
	@Override
	public String replaceText(String text) throws UnsupportedOperationException
	{
		doesNotSupportTextOperations();
		return null;
	}
	
	@Override
	public String replaceText(int from, int length, String text) throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		doesNotSupportTextOperations();
		return null;
	}
	
	protected void doesNotSupportTextOperations() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("This node does not support text operations!");
	}
	
	// =========================================================================
	
	@Override
	public boolean supportsAttributes()
	{
		return false;
	}
	
	@Override
	public Collection<WomAttribute> getAttributes()
	{
		return Collections.emptyList();
	}
	
	@Override
	public String getAttribute(String name)
	{
		return null;
	}
	
	@Override
	public NativeOrXmlAttributeAdapter getAttributeNode(String name)
	{
		return null;
	}
	
	@Override
	public NativeOrXmlAttributeAdapter removeAttribute(String name) throws UnsupportedOperationException
	{
		doesNotSupportAttributes();
		return null;
	}
	
	@Override
	public void removeAttributeNode(WomAttribute attr) throws UnsupportedOperationException, IllegalArgumentException
	{
		doesNotSupportAttributes();
	}
	
	@Override
	public NativeOrXmlAttributeAdapter setAttribute(String name, String value) throws UnsupportedOperationException
	{
		doesNotSupportAttributes();
		return null;
	}
	
	@Override
	public NativeOrXmlAttributeAdapter setAttributeNode(WomAttribute attr) throws UnsupportedOperationException, IllegalArgumentException
	{
		doesNotSupportAttributes();
		return null;
	}
	
	protected void doesNotSupportAttributes() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("This node does not support attributes!");
	}
	
	// =========================================================================
	
	@Override
	public boolean supportsChildren()
	{
		return false;
	}
	
	@Override
	public boolean hasChildNodes()
	{
		return false;
	}
	
	@Override
	public Collection<WomNode> childNodes()
	{
		return Collections.emptyList();
	}
	
	@Override
	public WomNode getFirstChild()
	{
		return null;
	}
	
	@Override
	public WomNode getLastChild()
	{
		return null;
	}
	
	@Override
	public void appendChild(WomNode child) throws UnsupportedOperationException
	{
		doesNotSupportChildNodes();
	}
	
	@Override
	public void insertBefore(WomNode before, WomNode child) throws UnsupportedOperationException, IllegalArgumentException
	{
		doesNotSupportChildNodes();
	}
	
	@Override
	public void removeChild(WomNode child) throws UnsupportedOperationException
	{
		doesNotSupportChildNodes();
	}
	
	@Override
	public void replaceChild(WomNode search, WomNode replace) throws UnsupportedOperationException
	{
		doesNotSupportChildNodes();
	}
	
	protected void doesNotSupportChildNodes() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("This node does not support child nodes!");
	}
	
	// =========================================================================
	
	@Override
	public Iterator<WomNode> iterator()
	{
		return new SiblingCollection<WomNode>(getFirstChild()).iterator();
	}
	
	// =========================================================================
	
	@Override
	public String toString()
	{
		return toString(new StringBuilder()).toString();
	}
	
	private StringBuilder toString(StringBuilder b)
	{
		switch (getNodeType())
		{
			case ATTRIBUTE:
				b.append(((WomAttribute) this).getName());
				b.append("=\"");
				b.append(StringUtils.escHtml(getValue()));
				b.append('"');
				break;
			
			case COMMENT:
				b.append("<!--");
				b.append(getValue());
				b.append("-->");
				break;
			
			case DOCUMENT:
			case ELEMENT:
				b.append('<');
				b.append(getNodeName());
				for (WomAttribute attr : getAttributes())
				{
					b.append(' ');
					if (attr instanceof DomBackbone)
						((DomBackbone) attr).toString(b);
					else
						b.append(attr.toString());
				}
				if (hasChildNodes())
				{
					b.append('>');
					for (WomNode child : childNodes())
					{
						if (child instanceof DomBackbone)
							((DomBackbone) child).toString(b);
						else
							b.append(child.toString());
					}
					b.append("</");
					b.append(getNodeName());
					b.append('>');
				}
				else
				{
					b.append(" />");
				}
				break;
			
			case TEXT:
				b.append(StringUtils.escHtml(getText()));
				break;
		}
		return b;
	}
}

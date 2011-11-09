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
import java.util.ListIterator;

import org.sweble.wikitext.engine.astdom.adapters.NativeOrXmlAttributeAdapter;
import org.sweble.wikitext.engine.wom.WomAttribute;
import org.sweble.wikitext.engine.wom.WomHorizAlign;
import org.sweble.wikitext.engine.wom.WomI18nDir;
import org.sweble.wikitext.engine.wom.WomValueWithUnit;
import org.sweble.wikitext.lazy.AstNodeTypes;
import org.sweble.wikitext.lazy.utils.XmlAttribute;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

public abstract class AttributeManager
{
	private static final AttributeManager emptyManager = new EmptyAttributeManager();
	
	public static AttributeManager emptyManager()
	{
		return emptyManager;
	}
	
	public static AttributeManager createManager()
	{
		return new AttributeManagerThingy();
	}
	
	// =========================================================================
	
	public abstract Collection<WomAttribute> getAttributes();
	
	public abstract String getAttribute(String name);
	
	public abstract NativeOrXmlAttributeAdapter getAttributeNode(String name);
	
	public abstract NativeOrXmlAttributeAdapter removeAttribute(String name, NodeList attrContainer);
	
	public abstract void removeAttributeNode(WomAttribute attr, DomBackbone parent, NodeList attrContainer) throws IllegalArgumentException;
	
	public abstract NativeOrXmlAttributeAdapter setAttribute(String name, String value, DomBackbone parent, NodeList attrContainer);
	
	public abstract NativeOrXmlAttributeAdapter setAttributeNode(WomAttribute attr, DomBackbone parent, NodeList attrContainer) throws IllegalArgumentException;
	
	// =========================================================================
	
	public abstract boolean isEmptyManager();
	
	// =========================================================================
	
	public String getId()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String setId(String id) throws IllegalArgumentException
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getClasses()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String setClasses(String classes)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getStyle()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String setStyle(String style)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getTitlte()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String setTitlte(String title)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// =========================================================================
	
	public WomI18nDir getDir()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public WomI18nDir setDir(WomI18nDir dir)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getLang()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String setLang(String lang)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getXmlLang()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String setXmlLang(String lang)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// =========================================================================
	
	public String getOnclick()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String setOnclick(String handler)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getOndblclick()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String setOndblclick(String handler)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getOnmousedown()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String setOnmousedown(String handler)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getOnmouseup()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String setOnmouseup(String handler)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getOnmouseover()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String setOnmouseover(String handler)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getOnmousemove()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String setOnmousemove(String handler)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getOnmouseout()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String setOnmouseout(String handler)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getOnkeypress()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String setOnkeypress(String handler)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getOnkeydown()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String setOnkeydown(String handler)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getOnkeyup()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String setOnkeyup(String handler)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// =========================================================================
	
	// Used by HorizontalRuleAdapter, ParagraphAdapter
	public WomHorizAlign getAlign()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// Used by HorizontalRuleAdapter, ParagraphAdapter
	public WomHorizAlign setAlign(WomHorizAlign align)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// Used by HorizontalRuleAdapter
	public boolean isNoshade()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	// Used by HorizontalRuleAdapter
	public boolean setNoshade(boolean noshade)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	// Used by HorizontalRuleAdapter
	public Integer getSize()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// Used by HorizontalRuleAdapter
	public Integer setSize(Integer size)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// Used by HorizontalRuleAdapter
	public WomValueWithUnit getWidth()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// Used by HorizontalRuleAdapter
	public WomValueWithUnit setWidth(WomValueWithUnit width)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// =========================================================================
	
	public static final class EmptyAttributeManager
	        extends
	            AttributeManager
	{
		public Collection<WomAttribute> getAttributes()
		{
			return Collections.emptyList();
		}
		
		public String getAttribute(String name)
		{
			return null;
		}
		
		public NativeOrXmlAttributeAdapter getAttributeNode(String name)
		{
			return null;
		}
		
		public NativeOrXmlAttributeAdapter removeAttribute(String name, NodeList attrContainer)
		{
			// Doesn't fail if an attribute with the specified name doesn't
			// exist and therefore should also not fail if the node has no
			// attributes.
			return null;
		}
		
		public void removeAttributeNode(WomAttribute attr, DomBackbone parent, NodeList attrContainer) throws IllegalArgumentException
		{
			// The node has no attributes yet someone passes an attribute he
			// claims to be of this node ...
			unsupported();
		}
		
		public NativeOrXmlAttributeAdapter setAttribute(String name, String value, DomBackbone parent, NodeList attrContainer)
		{
			unsupported();
			return null;
		}
		
		public NativeOrXmlAttributeAdapter setAttributeNode(WomAttribute attr, DomBackbone parent, NodeList attrContainer) throws IllegalArgumentException
		{
			unsupported();
			return null;
		}
		
		public boolean isEmptyManager()
		{
			return true;
		}
		
		private static void unsupported()
		{
			throw new UnsupportedOperationException("Cannot modify an instance of EmptyAttributeManager!");
		}
	}
	
	// =========================================================================
	
	public static final class AttributeManagerThingy
	        extends
	            AttributeManager
	{
		private NativeOrXmlAttributeAdapter firstAttr;
		
		// =====================================================================
		
		public boolean isEmptyManager()
		{
			return false;
		}
		
		// =====================================================================
		
		public Collection<WomAttribute> getAttributes()
		{
			return new SiblingCollection<WomAttribute>(firstAttr);
		}
		
		public String getAttribute(String name)
		{
			final WomAttribute attributeNode = getAttributeNode(name);
			if (attributeNode == null)
				return null;
			return attributeNode.getValue();
		}
		
		public NativeOrXmlAttributeAdapter getAttributeNode(String name)
		{
			if (name != null)
			{
				WomAttribute i = firstAttr;
				while (i != null)
				{
					if (i.getName().equalsIgnoreCase(name))
						return (NativeOrXmlAttributeAdapter) i;
					
					i = (WomAttribute) i.getNextSibling();
				}
			}
			return null;
		}
		
		public NativeOrXmlAttributeAdapter removeAttribute(String name, NodeList attrContainer)
		{
			if (name == null)
				throw new IllegalArgumentException("Argument `name' is null.");
			
			NativeOrXmlAttributeAdapter remove = getAttributeNode(name);
			if (remove == null)
				return null;
			
			removeAttribute(remove, attrContainer);
			
			return remove;
		}
		
		public void removeAttributeNode(WomAttribute attr, DomBackbone parent, NodeList attrContainer) throws IllegalArgumentException
		{
			if (attr == null)
				throw new IllegalArgumentException("Argument `attr' is null.");
			
			if (attr.getParent() != parent)
				throw new IllegalArgumentException("Given attribute `attr' is not an attribute of this XML element.");
			
			removeAttribute((NativeOrXmlAttributeAdapter) attr, attrContainer);
		}
		
		public NativeOrXmlAttributeAdapter setAttribute(String name, String value, DomBackbone parent, NodeList attrContainer)
		{
			if (name == null)
				throw new IllegalArgumentException("Argument `name' and/or `value' is null.");
			
			if (value == null)
			{
				return removeAttribute(name, attrContainer);
			}
			else
			{
				NativeOrXmlAttributeAdapter attr = new NativeOrXmlAttributeAdapter(name, value);
				
				return setAttributeNode(attr, parent, attrContainer);
			}
		}
		
		public NativeOrXmlAttributeAdapter setAttributeNode(WomAttribute attr, DomBackbone parent, NodeList attrContainer) throws IllegalArgumentException
		{
			if (attr == null)
				throw new IllegalArgumentException("Argument `attr' is null.");
			
			final NativeOrXmlAttributeAdapter newAttr =
			        Toolbox.expectType(NativeOrXmlAttributeAdapter.class, attr, "attr");
			
			if (newAttr.isLinked())
				throw new IllegalStateException(
				        "Given attribute `attr' is still attribute of another DOM node.");
			
			NativeOrXmlAttributeAdapter oldAttr = getAttributeNode(attr.getName());
			
			replaceAttribute(oldAttr, newAttr, parent, attrContainer);
			
			return oldAttr;
		}
		
		// =====================================================================
		
		private void removeAttribute(NativeOrXmlAttributeAdapter remove, NodeList attrContainer)
		{
			// remove from DOM
			if (remove == firstAttr)
				firstAttr = (NativeOrXmlAttributeAdapter) remove.getNextSibling();
			remove.unlink();
			
			if (attrContainer != null)
			{
				// remove from AST
				// the ast can contain an attribute with the same name multiple times!
				Iterator<AstNode> i = attrContainer.iterator();
				while (i.hasNext())
				{
					AstNode node = i.next();
					// there could also be garbage nodes ...
					if (!node.isNodeType(AstNodeTypes.NT_XML_ATTRIBUTE))
						continue;
					
					XmlAttribute attr = (XmlAttribute) node;
					if (attr.getName().equalsIgnoreCase(remove.getName()))
					{
						i.remove();
						// the dom will always refer to the last attribute
						if (attr == remove.getAstNode())
						{
							i = null;
							break;
						}
					}
				}
				if (i != null)
					throw new AssertionError("WOM and AST out of sync!");
			}
		}
		
		private void replaceAttribute(final NativeOrXmlAttributeAdapter oldAttr, final NativeOrXmlAttributeAdapter newAttr, DomBackbone parent, NodeList attrContainer)
		{
			// replace in DOM
			DomBackbone prev = null;
			DomBackbone next = null;
			if (oldAttr != null)
			{
				prev = oldAttr.getPrevSibling();
				next = oldAttr.getNextSibling();
				oldAttr.unlink();
			}
			else if (firstAttr != null)
			{
				prev = firstAttr;
				while (prev.getNextSibling() != null)
					prev = (DomBackbone) prev.getNextSibling();
			}
			
			newAttr.link(parent, prev, next);
			if (firstAttr == null || oldAttr == firstAttr)
				firstAttr = newAttr;
			
			if (attrContainer != null)
			{
				if (newAttr.getAstNode() == null)
					newAttr.attachAstNode();
				
				// replace in AST
				// the ast can contain an attribute with the same name multiple times!
				
				XmlAttribute oldAstNode = null;
				if (oldAttr != null)
					oldAstNode = oldAttr.getAstNode();
				
				String name = newAttr.getName();
				XmlAttribute newAstNode = newAttr.getAstNode();
				
				boolean replaced = false;
				
				ListIterator<AstNode> i = attrContainer.listIterator();
				while (i.hasNext())
				{
					AstNode node = i.next();
					if (!node.isNodeType(AstNodeTypes.NT_XML_ATTRIBUTE))
						continue;
					
					XmlAttribute attr = (XmlAttribute) node;
					if (attr.getName().equalsIgnoreCase(name))
					{
						if (attr == oldAstNode)
						{
							i.set(newAstNode);
							replaced = true;
							
							Object rtd = oldAstNode.getAttribute("RTD");
							if (rtd != null)
								newAstNode.setAttribute("RTD", rtd);
							
							// The node the DOM refers to comes always last
							break;
						}
						else
						{
							// Remove all nodes with the same name and replace only the last
							i.remove();
						}
					}
				}
				
				if (!replaced)
				{
					if (oldAttr != null)
						throw new AssertionError();
					
					attrContainer.add(newAstNode);
				}
			}
			else
			{
				if (newAttr.getAstNode() == null)
					newAttr.detachAstNode();
			}
		}
	}
}

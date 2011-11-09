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
package org.sweble.wikitext.engine.astdom.adapters;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.sweble.wikitext.engine.astdom.ChildManager;
import org.sweble.wikitext.engine.astdom.FullElement;
import org.sweble.wikitext.engine.astdom.WomNodeFactory;
import org.sweble.wikitext.engine.wom.WomNodeType;
import org.sweble.wikitext.engine.wom.WomValue;

import de.fau.cs.osr.ptk.common.ast.NodeList;

public class ValueAdapter
        extends
            FullElement
        implements
            WomValue
{
	private static final long serialVersionUID = 1L;
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	private ChildManager childManager = ChildManager.emptyManager();
	
	// =========================================================================
	
	public ValueAdapter()
	{
		super(new NodeList());
	}
	
	public ValueAdapter(WomNodeFactory womNodeFactory, NodeList value)
	{
		super(value);
		
		if (value == null)
			throw new NullPointerException();
		
		if (!value.isEmpty())
			super.addContent(womNodeFactory, value, getChildManagerForModificationOrFail());
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "value";
	}
	
	@Override
	public NodeList getAstNode()
	{
		return (NodeList) super.getAstNode();
	}
	
	@Override
	public WomNodeType getNodeType()
	{
		return WomNodeType.ELEMENT;
	}
	
	// =========================================================================
	
	@Override
	public NodeList getAstChildContainer()
	{
		return getAstNode();
	}
}

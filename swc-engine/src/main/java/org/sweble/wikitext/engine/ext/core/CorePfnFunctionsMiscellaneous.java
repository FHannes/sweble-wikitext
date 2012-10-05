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

package org.sweble.wikitext.engine.ext.core;

import static org.sweble.wikitext.parser.utils.AstBuilder.astList;
import static org.sweble.wikitext.parser.utils.AstBuilder.astTagExtension;
import static org.sweble.wikitext.parser.utils.AstBuilder.astXmlAttrib;

import java.util.List;
import java.util.ListIterator;

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.PfnArgumentMode;
import org.sweble.wikitext.engine.astwom.Toolbox;
import org.sweble.wikitext.engine.config.ParserFunctionGroup;
import org.sweble.wikitext.parser.AstNodeTypes;
import org.sweble.wikitext.parser.nodes.WtTagExtension;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.nodes.WtTemplateArgument;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.utils.RtWikitextPrinter;
import org.sweble.wikitext.parser.utils.StringConversionException;
import org.sweble.wikitext.parser.utils.StringConverter;

import de.fau.cs.osr.utils.XmlGrammar;

public class CorePfnFunctionsMiscellaneous
		extends
			ParserFunctionGroup
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	protected CorePfnFunctionsMiscellaneous()
	{
		super("Core - Parser Functions - Miscellaneous");
		addParserFunction(new TagPfn());
	}
	
	public static CorePfnFunctionsMiscellaneous group()
	{
		return new CorePfnFunctionsMiscellaneous();
	}
	
	// =========================================================================
	// ==
	// == TODO: {{#language:language code}}
	// ==       {{#language:ar}}
	// ==       {{#language:language code|target language code}}
	// ==       {{#language:ar|en}}
	// == TODO: {{#special:special page name}}
	// ==       {{#special:userlogin}}
	// == TODO: {{#speciale:special page name}}
	// ==       {{#speciale:userlogin}}
	// ==
	// =========================================================================
	
	// =========================================================================
	// ==
	// == TODO: {{#tag:tagname
	// ==           |content
	// ==           |attribute1=value1
	// ==           |attribute2=value2
	// ==       }}
	// ==
	// =========================================================================
	
	public static final class TagPfn
			extends
				CorePfnFunction
	{
		private static final long serialVersionUID = 1L;
		
		public TagPfn()
		{
			super(PfnArgumentMode.TEMPLATE_ARGUMENTS, "tag");
		}
		
		@Override
		public WtNode invoke(
				WtTemplate pfn,
				ExpansionFrame frame,
				List<? extends WtNode> argsValues)
		{
			if (argsValues.size() < 2)
				return pfn;
			
			WtTemplateArgument nameNode = (WtTemplateArgument) argsValues.get(0);
			
			String nameStr;
			try
			{
				WtNode expNameNode = frame.expand(nameNode.getValue());
				nameStr = StringConverter.convert(expNameNode).trim();
			}
			catch (StringConversionException e)
			{
				return pfn;
			}
			
			// FIXME: Meld 'name=' part into value
			// FIXME: Do something about the "remove comments" hack
			WtTemplateArgument bodyNode = (WtTemplateArgument) argsValues.get(1);
			WtNode expValueNode = frame.expand(bodyNode.getValue());
			expValueNode = stripComments(expValueNode);
			String bodyStr = RtWikitextPrinter.print(expValueNode);
			
			WtTagExtension tagExt = astTagExtension()
					.withName(nameStr)
					.withBody(bodyStr)
					.build();
			
			WtNodeList attribs = astList();
			for (int i = 2; i < argsValues.size(); ++i)
			{
				WtTemplateArgument arg = (WtTemplateArgument) argsValues.get(i);
				WtNode argNameNode = frame.expand(arg.getName());
				WtNode argValueNode = frame.expand(arg.getValue());
				if (argNameNode == null || argValueNode == null)
					continue;
				
				String argName;
				String argValue;
				try
				{
					argName = StringConverter.convert(argNameNode);
					argValue = StringConverter.convert(argValueNode);
				}
				catch (StringConversionException e)
				{
					continue;
				}
				
				if (!XmlGrammar.xmlName().matcher(argName).matches())
					continue;
				
				attribs.add(astXmlAttrib().withName(argName).withValue(argValue).build());
			}
			
			tagExt.setXmlAttributes(attribs);
			
			Toolbox.addRtData(tagExt);
			
			return frame.expand(tagExt);
		}
		
		private WtNode stripComments(WtNode n)
		{
			ListIterator<WtNode> i = n.listIterator();
			while (i.hasNext())
			{
				WtNode child = i.next();
				switch (child.getNodeType())
				{
					case AstNodeTypes.NT_XML_COMMENT:
					case AstNodeTypes.NT_IGNORED:
						i.remove();
						break;
					default:
						if (!child.isEmpty())
							stripComments(child);
				}
			}
			return n;
		}
	}
}

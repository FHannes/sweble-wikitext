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

package org.sweble.wikitext.lazy.utils;

import java.util.ArrayList;
import java.util.Arrays;

import org.sweble.wikitext.lazy.AstNodeTypes;
import org.sweble.wikitext.lazy.parser.RtData;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.Text;
import de.fau.cs.osr.utils.StringUtils;

public final class TextUtils
{
	public static NodeList stringToAst(String text)
	{
		return stringToAst(text, true);
	}
	
	public static NodeList stringToAst(String text, boolean forAttribute)
	{
		NodeList list = new NodeList();
		
		if (text == null)
			return list;
		
		int n = text.length();
		int i = 0;
		int j = 0;
		for (; j < n; ++j)
		{
			char ch = text.charAt(j);
			switch (ch)
			{
				case ' ':
				case '\n':
				case '\t':
					break;
				case '<':
					if (j > i)
						list.add(new Text(text.substring(i, j)));
					list.add(xmlEntity("lt", "<"));
					i = j + 1;
					break;
				case '>':
					if (!forAttribute)
						break;
					if (j > i)
						list.add(new Text(text.substring(i, j)));
					list.add(xmlEntity("gt", ">"));
					i = j + 1;
					break;
				case '&':
					if (j > i)
						list.add(new Text(text.substring(i, j)));
					list.add(xmlEntity("amp", "&"));
					i = j + 1;
					break;
				case '\'':
					// &apos; cannot safely be used, see wikipedia
					if (j > i)
						list.add(new Text(text.substring(i, j)));
					list.add(xmlCharRef(39));
					i = j + 1;
					break;
				case '"':
					if (!forAttribute)
						break;
					if (j > i)
						list.add(new Text(text.substring(i, j)));
					list.add(xmlEntity("quot", "\""));
					i = j + 1;
					break;
				default:
					if ((ch >= 0 && ch < 0x20) || (ch == 0xFE))
					{
						if (j > i)
							list.add(new Text(text.substring(i, j)));
						list.add(xmlCharRef(ch));
						i = j + 1;
						continue;
					}
					else if (Character.isHighSurrogate(ch))
					{
						++i;
						if (i < n)
						{
							char ch2 = text.charAt(i);
							if (Character.isLowSurrogate(ch2))
							{
								int codePoint = Character.toCodePoint(ch, ch2);
								switch (Character.getType(codePoint))
								{
									case Character.CONTROL:
									case Character.PRIVATE_USE:
									case Character.UNASSIGNED:
										if (j > i)
											list.add(new Text(text.substring(i, j)));
										list.add(xmlCharRef(codePoint));
										i = j + 1;
										break;
									
									default:
										break;
								}
								
								continue;
							}
						}
					}
					else if (!Character.isLowSurrogate(ch))
					{
						continue;
					}
					
					// No low surrogate followed or only low surrogate
					throw new IllegalArgumentException("String contains isolated surrogates!");
			}
		}
		
		if (i != j)
			list.add(new Text(text.substring(i, j)));
		
		return list;
	}
	
	// =========================================================================
	
	public static XmlCharRef xmlCharRef(int codePoint)
	{
		XmlCharRef xmlCharRef = new XmlCharRef(codePoint);
		setXmlCharRef(xmlCharRef, codePoint);
		return xmlCharRef;
	}
	
	public static void setXmlCharRef(XmlCharRef xmlCharRef, int codePoint)
	{
		xmlCharRef.setCodePoint(codePoint);
		addRtData(xmlCharRef, joinRt(StringUtils.hexCharRef(codePoint)));
	}
	
	public static XmlEntityRef xmlEntity(String name, String resolved)
	{
		XmlEntityRef xmlEntityRef = new XmlEntityRef(name, resolved);
		setXmlEntityRef(xmlEntityRef, name);
		return xmlEntityRef;
	}
	
	private static void setXmlEntityRef(XmlEntityRef xmlEntityRef, String name)
	{
		xmlEntityRef.setName(name);
		addRtData(xmlEntityRef, joinRt(StringUtils.entityRef(name)));
	}
	
	// =========================================================================
	
	public static NodeList trim(NodeList nodes)
	{
		ArrayList<AstNode> result = new ArrayList<AstNode>(nodes);
		
		trimLeft(result);
		trimRight(result);
		
		return new NodeList(result);
	}
	
	public static NodeList trimLeft(NodeList nodes)
	{
		ArrayList<AstNode> result = new ArrayList<AstNode>(nodes);
		
		trimLeft(result);
		
		return new NodeList(result);
	}
	
	public static NodeList trimRight(NodeList nodes)
	{
		ArrayList<AstNode> result = new ArrayList<AstNode>(nodes);
		
		trimRight(result);
		
		return new NodeList(result);
	}
	
	public static NodeList trimAndPad(NodeList nodes, int spaces)
	{
		ArrayList<AstNode> result = new ArrayList<AstNode>(nodes);
		
		trimLeft(result);
		trimRight(result);
		
		if (spaces <= 0)
			return new NodeList(result);
		
		return pad(result, spaces);
	}
	
	public static void trimLeft(ArrayList<AstNode> result)
	{
		int i = 0;
		while (i < result.size())
		{
			switch (result.get(i).getNodeType())
			{
				case AstNode.NT_TEXT:
				{
					Text stringNode = (Text) result.get(i);
					String trimmed = StringUtils.trimLeft(stringNode.getContent());
					if (trimmed != stringNode.getContent())
					{
						if (trimmed.isEmpty())
						{
							result.remove(i);
							continue;
						}
						else
						{
							result.set(i, new Text(trimmed));
							break;
						}
					}
					else
					{
						break;
					}
				}
				
				case AstNodeTypes.NT_XML_COMMENT:
					++i;
					continue;
					
				default:
					break;
			}
			
			break;
		}
	}
	
	public static void trimRight(ArrayList<AstNode> result)
	{
		int i = result.size() - 1;
		while (i >= 0)
		{
			switch (result.get(i).getNodeType())
			{
				case AstNode.NT_TEXT:
				{
					Text stringNode = (Text) result.get(i);
					String trimmed = StringUtils.trimRight(stringNode.getContent());
					if (trimmed != stringNode.getContent())
					{
						if (trimmed.isEmpty())
						{
							result.remove(i--);
							continue;
						}
						else
						{
							result.set(i, new Text(trimmed));
							break;
						}
					}
					else
					{
						break;
					}
				}
				
				case AstNodeTypes.NT_XML_COMMENT:
					--i;
					continue;
					
				default:
					break;
			}
			
			break;
		}
	}
	
	// =========================================================================
	
	public static NodeList pad(ArrayList<AstNode> result, int spaces)
	{
		if (spaces <= 0)
			return new NodeList(result);
		
		if (result.isEmpty())
		{
			result.add(new Text(StringUtils.strrep(' ', spaces * 2)));
		}
		else
		{
			String spaced;
			String spacesString = StringUtils.strrep(' ', spaces);
			
			// -- before
			
			Text before = null;
			if (result.get(0).isNodeType(AstNode.NT_TEXT))
				before = (Text) result.remove(0);
			spaced = "";
			if (before != null)
				spaced = before.getContent();
			
			spaced = spacesString + spaced;
			result.add(0, new Text(spaced));
			
			// -- after
			
			Text after = null;
			int i = result.size() - 1;
			if (result.get(i).isNodeType(AstNode.NT_TEXT))
				after = (Text) result.remove(i);
			spaced = "";
			if (after != null)
				spaced = after.getContent();
			
			spaced = spaced + spacesString;
			result.add(new Text(spaced));
		}
		
		return new NodeList(result);
	}
	
	// =========================================================================
	
	public static RtData addRtData(AstNode yyValue, Object[]... rts)
	{
		if (rts.length != yyValue.size() + 1)
			rts = Arrays.copyOf(rts, yyValue.size() + 1);
		RtData data = new RtData(rts);
		yyValue.setAttribute("RTD", data);
		return data;
	}
	
	public static Object[] joinRt(Object... objects)
	{
		ArrayList<Object> result = new ArrayList<Object>();
		for (Object o : objects)
		{
			if (o instanceof AstNode)
			{
				AstNode a = (AstNode) o;
				switch (a.getNodeType())
				{
					case AstNode.NT_NODE_LIST:
						for (AstNode c : (NodeList) a)
						{
							if (c.getNodeType() == AstNode.NT_TEXT)
							{
								rtAddString(result, ((Text) c).getContent());
							}
							else
							{
								result.add(c);
							}
						}
						break;
					
					case AstNode.NT_TEXT:
						rtAddString(result, ((Text) a).getContent());
						break;
					
					default:
						result.add(a);
						break;
				}
			}
			else
			{
				if (o == null)
				{
				}
				else if (o instanceof Character)
				{
					rtAddString(result, String.valueOf((Character) o));
				}
				else
				{
					rtAddString(result, (String) o);
				}
			}
		}
		return result.toArray();
	}
	
	public static void rtAddString(ArrayList<Object> result, String so)
	{
		int last = result.size() - 1;
		if (last >= 0 && result.get(last) instanceof String)
		{
			result.set(last, result.get(last) + so);
		}
		else
		{
			result.add(so);
		}
	}
	
	public static void prependRtData(AstNode n, String data)
	{
		RtData rtd = (RtData) n.getAttribute("RTD");
		if (rtd == null || rtd.getRts().length == 0)
		{
			addRtData(n, joinRt(data));
		}
		else
		{
			Object[] rtd0 = rtd.getRts()[0];
			if (rtd0 == null || rtd0.length == 0)
			{
				rtd0 = new Object[] { data };
			}
			else if (rtd0[0] instanceof String)
			{
				rtd0 = rtd0.clone();
				rtd0[0] = data + rtd0[0];
			}
			else
			{
				Object[] rtd0_ = new Object[rtd0.length + 1];
				rtd0_[0] = data;
				System.arraycopy(rtd0, 0, rtd0_, 1, rtd0.length);
			}
			
			Object[][] rts = rtd.getRts().clone();
			rts[0] = rtd0;
			n.setAttribute("RTD", new RtData(rts));
		}
	}
}

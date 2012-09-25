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

import static org.sweble.wikitext.parser.utils.AstBuilder.*;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.IllegalArgumentsWarning;
import org.sweble.wikitext.engine.InvalidNameWarning;
import org.sweble.wikitext.engine.InvalidPagenameWarning;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.PfnArgumentMode;
import org.sweble.wikitext.engine.UrlType;
import org.sweble.wikitext.engine.config.Namespace;
import org.sweble.wikitext.engine.config.ParserFunctionGroup;
import org.sweble.wikitext.engine.utils.UrlEncoding;
import org.sweble.wikitext.parser.preprocessor.Template;
import org.sweble.wikitext.parser.LinkTargetException;
import org.sweble.wikitext.parser.WarningSeverity;
import org.sweble.wikitext.parser.utils.StringConversionException;
import org.sweble.wikitext.parser.utils.StringConverter;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.Text;

public class CorePfnFunctionsUrlData
		extends
			ParserFunctionGroup
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	protected CorePfnFunctionsUrlData()
	{
		super("Core - Parser Functions - URL data");
		addParserFunction(new FullurlPfn());
		addParserFunction(new FilepathPfn());
		addParserFunction(new UrlencodePfn());
	}
	
	public static CorePfnFunctionsUrlData group()
	{
		return new CorePfnFunctionsUrlData();
	}
	
	// =========================================================================
	// ==
	// == TODO: {{localurl:page name}}
	// ==       {{localurl:page name|query_string}}
	// ==
	// =========================================================================
	
	// =========================================================================
	// ==
	// == {{fullurl:page name}}
	// == {{fullurl:page name|query_string}}
	// == {{fullurl:interwiki:remote page name|query_string}}
	// ==
	// =========================================================================
	
	public static final class FullurlPfn
			extends
				CorePfnFunction
	{
		private static final long serialVersionUID = 1L;
		
		public FullurlPfn()
		{
			super(PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "fullurl");
		}
		
		@Override
		public AstNode invoke(
				Template pfn,
				ExpansionFrame frame,
				List<? extends AstNode> argsValues)
		{
			if (argsValues.size() < 1)
			{
				frame.fileWarning(
						new IllegalArgumentsWarning(
								WarningSeverity.NORMAL,
								getClass(),
								"Parser function was called with too few arguments!",
								pfn));
				return pfn;
			}
			else if (argsValues.size() > 2)
			{
				frame.fileWarning(
						new IllegalArgumentsWarning(
								WarningSeverity.NONE,
								getClass(),
								"Parser function was called with too many arguments!",
								pfn));
			}
			
			AstNode titleNode = argsValues.get(0);
			
			String titleStr;
			try
			{
				titleStr = StringConverter.convert(titleNode);
			}
			catch (StringConversionException e1)
			{
				frame.fileWarning(
						new InvalidNameWarning(
								WarningSeverity.NORMAL,
								getClass(),
								titleNode));
				return pfn;
			}
			
			PageTitle title;
			try
			{
				title = PageTitle.make(frame.getWikiConfig(), titleStr);
			}
			catch (LinkTargetException e)
			{
				try
				{
					titleStr = URLDecoder.decode(titleStr, "UTF-8");
					title = PageTitle.make(frame.getWikiConfig(), titleStr);
				}
				catch (LinkTargetException e2)
				{
					frame.fileWarning(
							new InvalidPagenameWarning(
									WarningSeverity.NORMAL,
									getClass(),
									titleNode,
									titleStr));
					return pfn;
				}
				catch (UnsupportedEncodingException e2)
				{
					frame.fileWarning(
							new InvalidNameWarning(
									WarningSeverity.NORMAL,
									getClass(),
									titleNode));
					return pfn;
				}
			}
			
			String queryStr = null;
			if (argsValues.size() >= 2)
			{
				AstNode queryNode = argsValues.get(1);
				
				try
				{
					queryStr = StringConverter.convert(queryNode);
				}
				catch (StringConversionException e)
				{
					frame.fileWarning(
							new InvalidNameWarning(
									WarningSeverity.NORMAL,
									getClass(),
									queryNode));
				}
			}
			
			Namespace ns = title.getNamespace();
			if (ns.isMediaNs())
				title = title.newWithNamespace(frame.getWikiConfig().getFileNamespace());
			
			URL titleUrl;
			try
			{
				titleUrl = title.getUrl(queryStr);
			}
			catch (MalformedURLException e)
			{
				// Try without query string ...
				titleUrl = title.getUrl();
				
				frame.fileWarning(
						new InvalidNameWarning(
								WarningSeverity.NORMAL,
								getClass(),
								pfn));
			}
			
			URL url = frame.getUrlService().convertUrl(
					UrlType.FULL,
					titleUrl);
			
			return new Text(url.toExternalForm());
		}
	}
	
	// =========================================================================
	// ==
	// == TODO: {{canonicalurl:page name}}
	// ==       {{canonicalurl:page name|query_string}}
	// ==       {{canonicalurl:interwiki:remote page name|query_string}}
	// ==
	// =========================================================================
	
	// =========================================================================
	// ==
	// == {{filepath:file name}}
	// == {{filepath:file name|nowiki}}
	// == {{filepath:file name|thumbnail_size}}
	// ==
	// =========================================================================
	
	public static final class FilepathPfn
			extends
				CorePfnFunction
	{
		private static final long serialVersionUID = 1L;
		
		public FilepathPfn()
		{
			super("filepath");
		}
		
		@Override
		public AstNode invoke(
				Template pfn,
				ExpansionFrame frame,
				List<? extends AstNode> args)
		{
			if (args.size() < 1)
				return pfn;
			
			PageTitle title;
			try
			{
				String titleStr = StringConverter.convert(args.get(0)).trim();
				
				title = PageTitle.make(frame.getWikiConfig(), titleStr);
				
				title = title.newWithNamespace(frame.getWikiConfig().getFileNamespace());
			}
			catch (StringConversionException e1)
			{
				return pfn;
			}
			catch (LinkTargetException e)
			{
				return pfn;
			}
			
			int size = -1;
			boolean nowiki = false;
			if (args.size() > 1)
			{
				try
				{
					String opt1 = StringConverter.convert(args.get(1)).trim();
					
					String opt2 = null;
					if (args.size() > 2)
						opt2 = StringConverter.convert(args.get(2)).trim();
					
					String sizeStr = opt1;
					if ("nowiki".equals(opt1))
					{
						nowiki = true;
						sizeStr = opt2;
					}
					else if ("nowiki".equals(opt2))
					{
						nowiki = true;
					}
					
					if (sizeStr != null)
						size = Integer.parseInt(sizeStr);
				}
				catch (StringConversionException e)
				{
				}
				catch (NumberFormatException e)
				{
				}
			}
			
			String url;
			try
			{
				url = frame.getCallback().fileUrl(title, size, -1);
			}
			catch (Exception e)
			{
				return pfn;
			}
			
			if (url == null)
				return astText("");
			
			return nowiki ? astProtected(url) : astText(url);
		}
	}
	
	// =========================================================================
	// ==
	// == {{urlencode:string}} (or {{urlencode:string|QUERY}})
	// == {{urlencode:string|WIKI}}
	// == {{urlencode:string|PATH}}
	// ==
	// =========================================================================
	
	public static final class UrlencodePfn
			extends
				CorePfnFunction
	{
		private static final long serialVersionUID = 1L;
		
		public UrlencodePfn()
		{
			super("urlencode");
		}
		
		@Override
		public AstNode invoke(
				Template pfn,
				ExpansionFrame frame,
				List<? extends AstNode> args)
		{
			if (args.size() < 1)
				return pfn;
			
			String text;
			try
			{
				text = StringConverter.convert(args.get(0)).trim();
			}
			catch (StringConversionException e1)
			{
				return pfn;
			}
			
			UrlEncoding encoder = UrlEncoding.QUERY;
			if (args.size() > 1)
			{
				try
				{
					String encoderName = StringConverter.convert(args.get(1)).trim();
					
					encoder = UrlEncoding.valueOf(encoderName.toUpperCase());
				}
				catch (StringConversionException e)
				{
				}
				catch (IllegalArgumentException e)
				{
				}
			}
			
			return astText(encoder.encode(text));
		}
	}
	
	// =========================================================================
	// ==
	// == TODO: {{anchorencode:string}}
	// ==
	// =========================================================================
	
}
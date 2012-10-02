package org.sweble.wikitext.parser.nodes;

/**
 * <h1>Link Option: AltText</h1> <h2>Grammar</h2>
 * <ul>
 * <li>
 * <p>
 * LinkOptionAltText ::= Ws* 'alt=' LinkTitleContent{ALT}*
 * </p>
 * </li>
 * </ul>
 */
public class LinkOptionAltText
		extends
			WtContentNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public LinkOptionAltText()
	{
		super();
		
	}
	
	public LinkOptionAltText(WtList content)
	{
		super(content);
		
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_LINK_OPTION_ALT_TEXT;
	}
}
/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Node;

public interface AttributeDescriptor
{
	//	/**
	//	 * Check an attribute's value for well-formed-ness.
	//	 * 
	//	 * @param parent
	//	 *            The parent node for which to verify the attribute.
	//	 * @param value
	//	 *            The value to check.
	//	 * @return The value to set the attribute to. Most of the time this will be
	//	 *         the input parameter "value". But if the value needs further
	//	 *         normalization (apart from XML normalization of attributes), this
	//	 *         method can return a normalized string to use as value.
	//	 * @throws IllegalArgumentException
	//	 *             Thrown if the given value is not well-formed.
	//	 */
	//	public abstract String verify(WomNode parent, String value) throws IllegalArgumentException;
	
	/**
	 * @param parent
	 *            The node that owns or will own the attribute.
	 * @param verified
	 *            Write the string value to convert into this object. The
	 *            converted and verified values will be written into this object
	 *            as well.
	 * @return {@code true} when the attribute should be kept, {@code false} if
	 *         the attributes should be removed.
	 */
	public abstract boolean verifyAndConvert(
			Backbone parent,
			NativeAndStringValuePair verified);
	
	/**
	 * Ask whether this attribute can be removed from its parent node.
	 */
	public abstract boolean isRemovable();
	
	/**
	 * Return the normalization mode for the attribute.
	 * 
	 * @return The normalization mode.
	 */
	public abstract Normalization getNormalizationMode();
	
	/**
	 * Called after the attribute was set to perform custom alterations on WOM
	 * or AST.
	 * 
	 * @param parent
	 *            The parent node for which to verify the attribute.
	 * @param oldAttr
	 *            The old attribute node.
	 * @param newAttr
	 *            The new attribute node or <code>null</code> if the old
	 *            attribute was removed.
	 */
	public abstract void customAction(
			Wom3Node parent,
			AttributeBase oldAttr,
			AttributeBase newAttr);
	
	// =========================================================================
	
	public enum Normalization
	{
		/**
		 * Only convert the attribute value's WtNodeList into a string. Don't
		 * post-process the resulting string.
		 */
		NONE,
		
		/**
		 * Normalize the attribute's value after conversion according to these
		 * rules: http://www.w3.org/TR/REC-xml/#AVNormalize.
		 */
		CDATA,
		
		/**
		 * Normalize the attribute's value after conversion according to these
		 * rules: http://www.w3.org/TR/REC-xml/#AVNormalize.
		 * 
		 * Unlike the CDATA normalization, NON_CDATA normalization also
		 * collapses sequences of spaces into a single space and removes leading
		 * and trailing spaces.
		 */
		NON_CDATA
	}
}

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
package org.sweble.wikitext.engine.wom;

/**
 * An attribute of a tag extension.
 * 
 * Corresponds to the WXML 1.0 element "attr".
 * 
 * <b>Child elements:</b> -
 */
public interface WomAttr
        extends
            WomNode
{
	/**
	 * Get the name of the attribute. Attribute names are case-insensitive.
	 * 
	 * Corresponds to the XWML 1.0 attribute "name".
	 * 
	 * @return The name of the attribute.
	 */
	public String getName();
	
	/**
	 * Set the name of the attribute. Attribute names are case-insensitive.
	 * 
	 * Corresponds to the XWML 1.0 attribute "name".
	 * 
	 * @param name
	 *            The new name of the attribute.
	 * @return The old name of the attribute.
	 * @throws IllegalArgumentException
	 *             If an attribute with the given name already exists or the
	 *             given name was empty or not a valid XML name.
	 * @throws NullPointerException
	 *             Thrown if the <code>null</code> was specified as name.
	 */
	public String setName(String name) throws IllegalArgumentException, NullPointerException;
	
	/**
	 * Retrieve the value of the attribute.
	 * 
	 * Corresponds to the XWML 1.0 attribute "value".
	 * 
	 * @return The value of the attribute.
	 */
	public String getAttrValue();
	
	/**
	 * Retrieve the value of the attribute.
	 * 
	 * Corresponds to the XWML 1.0 attribute "value".
	 * 
	 * @param value
	 *            The new value of the attriute.
	 * @return The old value of the attribute.
	 * @throws NullPointerException
	 *             Thrown when <code>null</code> is passed as value.
	 */
	public String setAttrValue(String value) throws NullPointerException;
}

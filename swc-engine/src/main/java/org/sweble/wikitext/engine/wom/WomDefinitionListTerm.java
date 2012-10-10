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

import java.util.Collection;

/**
 * Denotes a definition list term.
 * 
 * While in HTML a definition list is a loose collection of terms and
 * definitions, the WOM interfaces also offer a more abstract view on a
 * definition list in which one or more definitions belong to a term.
 * 
 * Therefore, if one retrieves a term from a definition list, the definitions
 * that follow the term can also be addressed and altered through the term
 * interface (this interface).
 * 
 * However, the definitions that are considered part of a term are still
 * <b>not</b> children of that term! They are only attached to the term
 * "virtually". Altering definitions through the term interface will actually
 * alter the definition list to which the term belongs. If a term does not yet
 * belong to a definition list, definitions that get attached to the term will
 * be temporarily stored in that term (but not as its children) and will be
 * attached to the definition list once the term is attached to a definition
 * list.
 * 
 * Individual definitions are addressed using a zero-based integer index where
 * <code>0</code> denotes the first definition that follows this term in the
 * definition list. If this term is followed by another term in the definition
 * list and there are not definitions between the two terms, than this term does
 * not have any definitions.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "dt".
 * 
 * <b>Child elements:</b> Mixed, [Inline elements]*
 */
public interface WomDefinitionListTerm
		extends
			WomDefinitionListItem
{
	/**
	 * Get the number of definitions of this term.
	 * 
	 * @return The number of definitions of this term.
	 */
	public int getDefNum();
	
	/**
	 * Get a collection containing all definitions of this term.
	 * 
	 * @return A collection containing all definitions of this term.
	 */
	public Collection<WomDefinitionListDef> getDefs();
	
	/**
	 * Get a certain definition of this term.
	 * 
	 * @param index
	 *            The zero-based index of the definition to retrieve.
	 * @return The definition with the given index.
	 * @throws IndexOutOfBoundsException
	 *             If the given index is out of range.
	 */
	public WomDefinitionListDef getDef(int index) throws IndexOutOfBoundsException;
	
	/**
	 * Replace a definition of this term.
	 * 
	 * @param index
	 *            The zero-based index of the definition to replace.
	 * @param def
	 *            The replacement definition.
	 * @return The replaced definition.
	 * @throws IndexOutOfBoundsException
	 *             If the given index is out of range.
	 */
	public WomDefinitionListDef replaceDef(int index, WomDefinitionListDef def) throws IndexOutOfBoundsException;
	
	/**
	 * Remove a definition if this term.
	 * 
	 * @param index
	 *            The zero-based index of the definition to remove.
	 * @return The removed definition.
	 * @throws IndexOutOfBoundsException
	 *             If the given index is out of range.
	 */
	public WomDefinitionListDef removeDef(int index) throws IndexOutOfBoundsException;
	
	/**
	 * Append a definition to the term.
	 * 
	 * @param def
	 *            The item to append.
	 */
	public void appendDef(WomDefinitionListDef def);
	
	/**
	 * Insert a definition at the given index into the list of definitions of
	 * this term.
	 * 
	 * @param beforeIndex
	 *            The index of the definition in front of which the new
	 *            definition is to be inserted.
	 * @param def
	 *            The definition to insert. The definition will have the given
	 *            index <code>beforeIndex</code> after insertion.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if <code>0 <= beforeIndex <= getDefNum()</code> does
	 *             not hold.
	 */
	public void insertItem(int beforeIndex, WomDefinitionListDef def) throws IndexOutOfBoundsException;
}

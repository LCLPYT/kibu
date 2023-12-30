/*
 * JNBT License
 *
 * Copyright (c) 2010 Graham Edgecombe
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 *     * Neither the name of the JNBT team nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package work.lclpnet.kibu.jnbt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * The <code>TAG_List</code> tag.
 * 
 * @author Graham Edgecombe
 * 
 */
public final class ListTag implements Tag, Iterable<Tag> {
	
	/**
	 * The type.
	 */
	private final Class<? extends Tag> listType;
	
	/**
	 * The value.
	 */
	private final List<Tag> value;

	public ListTag() {
		this(EndTag.class);
	}

	public ListTag(final int listType) {
		this(NBTUtils.getTypeClass(listType));
	}

	/**
	 * Creates the tag.
	 *
	 * @param listType
	 *            The type of item in the list.
	 */
	public ListTag(final Class<? extends Tag> listType) {
		this(listType, new ArrayList<>());
	}

	/**
	 * Creates the tag.
	 *
	 * @param listType
	 *            The type of item in the list.
	 * @param value
	 *            The value.
	 */
	public ListTag(final Class<? extends Tag> listType, final List<Tag> value) {
		this.listType = Objects.requireNonNull(listType);
		this.value = Objects.requireNonNull(value);
	}
	
	/**
	 * Gets the type of item in this list.
	 * 
	 * @return The type of item in this list.
	 */
	public Class<? extends Tag> getListType() {
		return listType;
	}
	
	@Override
	public List<Tag> getValue() {
		return value;
	}

	@Override
	public int getType() {
		return NBTConstants.TYPE_LIST;
	}

	public void add(Tag tag) {
		if (!listType.isAssignableFrom(tag.getClass())) {
			throw new IllegalArgumentException("Invalid tag type %s".formatted(tag.getClass().getSimpleName()));
		}

		value.add(tag);
	}

	public void addAll(Iterable<? extends Tag> tags) {
		for (var tag : tags) {
			add(tag);
		}
	}

	public void remove(Tag tag) {
		value.remove(tag);
	}

	public void removeAll(Iterable<? extends Tag> tags) {
		for (var tag : tags) {
			remove(tag);
		}
	}

	public boolean contains(Tag tag) {
		return value.contains(tag);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();

		builder.append("TAG_List: ")
				.append(value.size())
				.append(" entries of type ")
				.append(NBTUtils.getTypeName(listType))
				.append("\r\n{\r\n");

		for (final Tag t : value) {
			builder.append("   ")
					.append(t.toString().replaceAll("\r\n", "\r\n   "))
					.append("\r\n");
		}

		builder.append("}");

		return builder.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ListTag listTag = (ListTag) o;
		return listType.equals(listTag.listType) && value.equals(listTag.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(listType, value);
	}

	@Override
	public Iterator<Tag> iterator() {
		return value.iterator();
	}
}

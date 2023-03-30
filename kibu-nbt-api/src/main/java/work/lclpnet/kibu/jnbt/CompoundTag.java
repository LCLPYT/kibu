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

import javax.annotation.Nonnull;
import java.util.*;

import static work.lclpnet.kibu.jnbt.NBTConstants.*;

/**
 * The <code>TAG_Compound</code> tag.
 * Made mutable and enriched with comfort methods to put get entries.
 * 
 * @author Graham Edgecombe
 * 
 */
public final class CompoundTag implements Tag {
	
	/**
	 * The value.
	 */
	private final Map<String, Tag> map;

	public CompoundTag() {
		this(new HashMap<>());
	}

	/**
	 * Creates the tag.
	 * 
	 * @param map
	 *            The value.
	 */
	public CompoundTag(final Map<String, Tag> map) {
		this.map = Objects.requireNonNull(map);
	}
	
	@Override
	public Map<String, Tag> getValue() {
		return map;
	}

	@Override
	public int getType() {
		return TYPE_COMPOUND;
	}

	public void put(String name, Tag tag) {
		map.put(name, Objects.requireNonNull(tag));
	}

	public Tag getNullable(String name) {
		return map.get(name);
	}

	@SuppressWarnings("unchecked")
	public <T extends Tag> T getNullable(String name, Class<T> type) {
		var tag = getNullable(name);
		if (tag == null) return null;

		if (type.isAssignableFrom(tag.getClass())) return (T) tag;

		throw new IllegalStateException("Tag %s is not of type %s".formatted(name, type.getSimpleName()));
	}

	@Nonnull
	public <T extends Tag> T get(String name, Class<T> type) {
		var tag = getNullable(name, type);
		if (tag != null) return tag;

		throw new IllegalStateException("Tag %s does not exist".formatted(name));
	}

	public boolean contains(String name) {
		var tag = getNullable(name);
		return tag != null;
	}

	public boolean contains(String name, int type) {
		var tag = getNullable(name);
		if (tag == null) return false;

		return type == tag.getType();
	}

	public void putByteArray(String name, byte[] value) {
		this.map.put(name, new ByteArrayTag(value));
	}

	public byte[] getByteArray(String name) {
		if (!contains(name, TYPE_BYTE_ARRAY)) return new byte[0];

		return get(name, ByteArrayTag.class).getValue();
	}

	public void putByte(String name, byte value) {
		this.map.put(name, new ByteTag(value));
	}

	public byte getByte(String name) {
		if (!contains(name, TYPE_BYTE)) return 0;

		return get(name, ByteTag.class).getValue();
	}

	public void putDouble(String name, double value) {
		this.map.put(name, new DoubleTag(value));
	}

	public double getDouble(String name) {
		if (!contains(name, TYPE_DOUBLE)) return 0;

		return get(name, DoubleTag.class).getValue();
	}

	public void putFloat(String name, float value) {
		this.map.put(name, new FloatTag(value));
	}

	public float getFloat(String name) {
		if (!contains(name, TYPE_FLOAT)) return 0;

		return get(name, FloatTag.class).getValue();
	}

	public void putIntArray(String name, int[] value) {
		this.map.put(name, new IntArrayTag(value));
	}

	public int[] getIntArray(String name) {
		if (!contains(name, TYPE_INT_ARRAY)) return new int[0];

		return get(name, IntArrayTag.class).getValue();
	}

	public void putInt(String name, int value) {
		this.map.put(name, new IntTag(value));
	}

	public int getInt(String name) {
		if (!contains(name, TYPE_INT)) return 0;

		return get(name, IntTag.class).getValue();
	}

	public void putLongArray(String name, long[] value) {
		this.map.put(name, new LongArrayTag(value));
	}

	public long[] getLongArray(String name) {
		if (!contains(name, TYPE_LONG_ARRAY)) return new long[0];

		return get(name, LongArrayTag.class).getValue();
	}

	public void putLong(String name, long value) {
		this.map.put(name, new LongTag(value));
	}

	public long getLong(String name) {
		if (!contains(name, TYPE_LONG)) return 0;

		return get(name, LongTag.class).getValue();
	}

	public void putShort(String name, short value) {
		this.map.put(name, new ShortTag(value));
	}

	public short getShort(String name) {
		if (!contains(name, TYPE_SHORT)) return 0;

		return get(name, ShortTag.class).getValue();
	}

	public void putString(String name, String value) {
		this.map.put(name, new StringTag(value));
	}

	public String getString(String name) {
		if (!contains(name, TYPE_STRING)) return "";

		return get(name, StringTag.class).getValue();
	}

	public CompoundTag getCompound(String name) {
		if (!contains(name, TYPE_COMPOUND)) return new CompoundTag();

		return get(name, CompoundTag.class);
	}

	public ListTag getList(String name) {
		if (!contains(name, TYPE_LIST)) return new ListTag();

		return get(name, ListTag.class);
	}

	public ListTag getList(String name, int type) {
		final var typeClass = NBTUtils.getTypeClass(type);

		if (contains(name, TYPE_LIST)) {
			var listTag = get(name, ListTag.class);

			if (typeClass.equals(listTag.getListType())) {
				return listTag;
			}
		}

		return new ListTag(typeClass);
	}

	public void putBoolean(String name, boolean value) {
		this.putByte(name, (byte) (value ? 1 : 0));
	}

	public boolean getBoolean(String name) {
		return getByte(name) == 1;
	}

	public void putUuid(String name, UUID value) {
		var ints = NBTUtils.splitUuid(value);
		this.putIntArray(name, ints);
	}

	public UUID getUuid(String name) {
		var ints = getIntArray(name);
		return NBTUtils.joinUuid(ints);
	}

	public int getType(String name) {
		var tag = getNullable(name);
		if (tag == null) return TYPE_END;

		return tag.getType();
	}

	public Set<String> keySet() {
		return this.map.keySet();
	}

	public void remove(String name) {
		this.map.remove(name);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("TAG_Compound: ").append(map.size()).append(" entries\r\n{\r\n");

		for (final var entry : map.entrySet()) {
			builder.append("   ")
					.append(entry.getKey())
					.append("=")
					.append(entry.getValue().toString().replaceAll("\r\n", "\r\n   "))
					.append("\r\n");
		}

		builder.append("}");

		return builder.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CompoundTag that = (CompoundTag) o;
		return map.equals(that.map);
	}

	@Override
	public int hashCode() {
		return Objects.hash(map);
	}
}

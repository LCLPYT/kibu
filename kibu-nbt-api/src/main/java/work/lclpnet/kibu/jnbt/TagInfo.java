package work.lclpnet.kibu.jnbt;

/**
 * A NBT tag with additional information, such as name.
 * Separated from {@link Tag}s to save memory.
 * Change inspired by <code>worldedit-core</code>'s <a href="https://github.com/EngineHub/WorldEdit/blob/df3f7b2ae66de0cf34215c012e7abe4fc61210fc/worldedit-core/src/main/java/com/sk89q/jnbt/NamedTag.java">NamedTag</a>.
 */
public record TagInfo(String name, Tag tag) {

    public CompoundTag rootTag() {
        var root = new CompoundTag();
        root.put(name, tag);

        return root;
    }
}

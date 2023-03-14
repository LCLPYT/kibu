package work.lclpnet.kibu.cmd;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import java.util.Map;

class CommandInternals {

    public static <S> Map<String, CommandNode<S>> getChildren(CommandNode<S> node) throws ReflectiveOperationException {
        return get("children", node);
    }

    public static <S> Map<String, LiteralCommandNode<S>> getLiterals(CommandNode<S> node) throws ReflectiveOperationException {
        return get("literals", node);
    }

    public static <S> void setRedirect(CommandNode<S> node, CommandNode<S> redirect) throws ReflectiveOperationException {
        set("redirect", node, redirect);
    }

    private static <F, C, I extends C> F get(String fieldName, I instance) throws NoSuchFieldException, IllegalAccessException {
        var field = CommandNode.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        @SuppressWarnings("unchecked")
        var val = (F) field.get(instance);

        return val;
    }

    private static <F, C, I extends C> void set(String fieldName, I instance, F value) throws NoSuchFieldException, IllegalAccessException {
        var field = CommandNode.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        field.set(instance, value);
    }
}

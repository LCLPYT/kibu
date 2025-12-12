package work.lclpnet.kibu.inv.prompt;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringUtil;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.kibu.hook.player.PlayerInventoryHooks;
import work.lclpnet.kibu.inv.mixin.ScreenHandlerAccessor;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class TextPrompt {

    private TextPrompt() {}

    public static CompletableFuture<Optional<String>> open(ServerPlayer player, Component title, String initial, Predicate<String> validator) {
        var future = new CompletableFuture<Optional<String>>();

        MenuConstructor factory = (syncId, inv, p) -> {
            var handler = new TextInputHandler(syncId, inv, future, validator);
            handler.setInitial(initial);

            return handler;
        };

        player.openMenu(new SimpleMenuProvider(factory, title));

        return future;
    }

    public interface Handler {
        void onClick(PlayerInventoryHooks.ClickEvent event);
    }

    private static class TextInputHandler extends AnvilMenu implements Handler {

        private final ItemStack EMPTY_INEQUALITY = new ItemStack(Items.POISONOUS_POTATO);  // ¯\_(ツ)_/¯
        private final CompletableFuture<Optional<String>> future;
        private final Predicate<String> validator;
        private @Nullable String value = null;
        private boolean changed = false;

        protected TextInputHandler(int syncId, Inventory inventory,
                                   CompletableFuture<Optional<String>> future,
                                   Predicate<String> validator) {
            super(syncId, inventory);
            this.future = future;
            this.validator = validator;
        }

        @Override
        public void broadcastChanges() {
            if (changed) {
                changed = false;

                // some stack that doesn't equal the new output stack so that the cache will miss
                ItemStack invalidateStack;

                if (value != null) {
                    resultSlots.setItem(0, textStack(value));
                    invalidateStack = ItemStack.EMPTY;
                } else {
                    resultSlots.setItem(0, ItemStack.EMPTY);
                    invalidateStack = EMPTY_INEQUALITY;
                }

                // set no level cost
                setData(0, 0);

                // invalidate tracked data
                ((ScreenHandlerAccessor) this).getRemoteDataSlots().set(0, 1);
                setRemoteSlot(2, invalidateStack);
            }

            super.broadcastChanges();
        }

        @Override
        public boolean setItemName(String newItemName) {
            value = validate(newItemName);
            changed = true;

            return super.setItemName(newItemName);
        }

        public void setInitial(String value) {
            ItemStack stack = textStack(value);

            inputSlots.setItem(0, stack);

            if (validate(value) != null) {
                resultSlots.setItem(0, stack);
            }

            this.value = validate(value);
        }

        public @Nullable String validate(String str) {
            String sanitized = StringUtil.filterText(str);

            if (sanitized.length() > 50 || !validator.test(sanitized)) {
                return null;
            }

            return sanitized;
        }

        private @NotNull ItemStack textStack(String value) {
            ItemStack stack = new ItemStack(Items.PAPER);
            stack.set(DataComponents.ITEM_NAME, Component.literal(value));
            return stack;
        }

        @Override
        public void onClick(PlayerInventoryHooks.ClickEvent event) {
            if (event.slot() != 2 || value == null) return;

            future.complete(Optional.of(value));

            if (player.containerMenu == this) {
                event.player().closeContainer();
            }
        }

        @Override
        public void removed(Player player) {
            super.removed(player);

            future.complete(Optional.empty());
        }
    }
}

package work.lclpnet.kibu.inv.prompt;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.kibu.hook.player.PlayerInventoryHooks;
import work.lclpnet.kibu.inv.mixin.ScreenHandlerAccessor;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class TextPrompt {

    private TextPrompt() {}

    public static CompletableFuture<Optional<String>> open(ServerPlayerEntity player, Text title, String initial, Predicate<String> validator) {
        var future = new CompletableFuture<Optional<String>>();

        ScreenHandlerFactory factory = (syncId, inv, p) -> {
            var handler = new TextInputHandler(syncId, inv, future, validator);
            handler.setInitial(initial);

            return handler;
        };

        player.openHandledScreen(new SimpleNamedScreenHandlerFactory(factory, title));

        return future;
    }

    public interface Handler {
        void onClick(PlayerInventoryHooks.ClickEvent event);
    }

    private static class TextInputHandler extends AnvilScreenHandler implements Handler {

        private final ItemStack EMPTY_INEQUALITY = new ItemStack(Items.POISONOUS_POTATO);  // ¯\_(ツ)_/¯
        private final CompletableFuture<Optional<String>> future;
        private final Predicate<String> validator;
        private @Nullable String value = null;
        private boolean changed = false;

        protected TextInputHandler(int syncId, PlayerInventory inventory,
                                   CompletableFuture<Optional<String>> future,
                                   Predicate<String> validator) {
            super(syncId, inventory);
            this.future = future;
            this.validator = validator;
        }

        @Override
        public void sendContentUpdates() {
            if (changed) {
                changed = false;

                // some stack that doesn't equal the new output stack so that the cache will miss
                ItemStack invalidateStack;

                if (value != null) {
                    output.setStack(0, textStack(value));
                    invalidateStack = ItemStack.EMPTY;
                } else {
                    output.setStack(0, ItemStack.EMPTY);
                    invalidateStack = EMPTY_INEQUALITY;
                }

                // set no level cost
                setProperty(0, 0);

                // invalidate tracked data
                ((ScreenHandlerAccessor) this).getTrackedPropertyValues().set(0, 1);
                setReceivedStack(2, invalidateStack);
            }

            super.sendContentUpdates();
        }

        @Override
        public boolean setNewItemName(String newItemName) {
            value = validate(newItemName);
            changed = true;

            return super.setNewItemName(newItemName);
        }

        public void setInitial(String value) {
            ItemStack stack = textStack(value);

            input.setStack(0, stack);

            if (validate(value) != null) {
                output.setStack(0, stack);
            }

            this.value = validate(value);
        }

        public @Nullable String validate(String str) {
            String sanitized = StringHelper.stripInvalidChars(str);

            if (sanitized.length() > 50 || !validator.test(sanitized)) {
                return null;
            }

            return sanitized;
        }

        private @NotNull ItemStack textStack(String value) {
            ItemStack stack = new ItemStack(Items.PAPER);
            stack.set(DataComponentTypes.ITEM_NAME, Text.literal(value));
            return stack;
        }

        @Override
        public void onClick(PlayerInventoryHooks.ClickEvent event) {
            if (event.slot() != 2 || value == null) return;

            future.complete(Optional.of(value));

            if (player.currentScreenHandler == this) {
                event.player().closeHandledScreen();
            }
        }

        @Override
        public void onClosed(PlayerEntity player) {
            super.onClosed(player);

            future.complete(Optional.empty());
        }
    }
}

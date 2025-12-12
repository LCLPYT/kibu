package work.lclpnet.kibu.inv.prompt;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.kibu.hook.player.PlayerInventoryHooks;
import work.lclpnet.kibu.inv.type.RestrictedInventory;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class OptionPrompt {

    public static <T> CompletableFuture<Optional<T>> open(ServerPlayer player, Component title, Collection<T> options, Function<T, ItemStack> iconFactory) {
        var future = new CompletableFuture<Optional<T>>();

        RestrictedInventory inventory = createInventory(title, options, iconFactory, future);

        player.openMenu(inventory);

        return future;
    }

    public static <T> RestrictedInventory createInventory(Component title, Collection<T> options, Function<T, ItemStack> iconFactory, CompletableFuture<Optional<T>> future) {
        int rows = Math.max(1, Math.min(6, (int) Math.ceil(options.size() / 9d)));

        var inv = new ChooserInventory<>(rows, title, options, future);

        int capacity = rows * 9;
        int i = 0;

        for (T item : options) {
            if (i >= capacity) break;

            ItemStack icon = iconFactory.apply(item);

            inv.setItem(i++, icon);
        }

        return inv;
    }

    public interface Handler {
        void onClick(PlayerInventoryHooks.ClickEvent event);
    }

    private static class ChooserInventory<T> extends RestrictedInventory implements Handler {
        private final Int2ObjectMap<T> items;
        private final CompletableFuture<Optional<T>> future;

        private ChooserInventory(int rows, Component title, Collection<T> items, CompletableFuture<Optional<T>> future) {
            super(rows, title);

            this.future = future;

            int size = items.size();
            this.items = new Int2ObjectArrayMap<>(size);

            int i = 0;

            for (T item : items) {
                this.items.put(i++, item);
            }
        }

        @Nullable
        public T get(int i) {
            return items.get(i);
        }

        @Override
        public void onClick(PlayerInventoryHooks.ClickEvent event) {
            Slot slot = event.handlerSlot();

            if (slot == null) return;

            int slotIndex = slot.getContainerSlot();

            T option = get(slotIndex);

            if (option == null) return;

            ServerPlayer player = event.player();

            future.complete(Optional.of(option));

            if (player.containerMenu instanceof ChestMenu handler && handler.getContainer() == this) {
                player.closeContainer();
            }
        }

        @Override
        public void stopOpen(ContainerUser user) {
            super.stopOpen(user);

            future.complete(Optional.empty());
        }
    }
}

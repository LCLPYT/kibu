package work.lclpnet.kibu.inv.type;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public class KibuInventory extends SimpleContainer implements MenuProvider {

    private final int rows;
    private final Component title;

    public KibuInventory(int rows, Component title) {
        super(9 * validateRowCount(rows));
        this.rows = rows;
        this.title = title;
    }

    public void open(ServerPlayer player) {
        player.openMenu(this);
    }

    @Override
    public Component getDisplayName() {
        return title;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        MenuType<ChestMenu> type = switch (rows) {
            case 1 -> MenuType.GENERIC_9x1;
            case 2 -> MenuType.GENERIC_9x2;
            case 3 -> MenuType.GENERIC_9x3;
            case 4 -> MenuType.GENERIC_9x4;
            case 5 -> MenuType.GENERIC_9x5;
            case 6 -> MenuType.GENERIC_9x6;
            default -> throw new IllegalArgumentException("Invalid row count");
        };

        return new ChestMenu(type, syncId, playerInventory, this, rows);
    }

    private static int validateRowCount(int rows) {
        if (rows < 1 || rows > 6) throw new IllegalArgumentException("Row count must be within [1,6]");
        return rows;
    }
}
package work.lclpnet.kibu.inv.type;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class KibuInventory extends SimpleInventory implements NamedScreenHandlerFactory {

    private final int rows;
    private final Text title;

    public KibuInventory(int rows, Text title) {
        super(9 * validateRowCount(rows));
        this.rows = rows;
        this.title = title;
    }

    public void open(ServerPlayerEntity player) {
        player.openHandledScreen(this);
    }

    @Override
    public Text getDisplayName() {
        return title;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        ScreenHandlerType<GenericContainerScreenHandler> type = switch (rows) {
            case 1 -> ScreenHandlerType.GENERIC_9X1;
            case 2 -> ScreenHandlerType.GENERIC_9X2;
            case 3 -> ScreenHandlerType.GENERIC_9X3;
            case 4 -> ScreenHandlerType.GENERIC_9X4;
            case 5 -> ScreenHandlerType.GENERIC_9X5;
            case 6 -> ScreenHandlerType.GENERIC_9X6;
            default -> throw new IllegalArgumentException("Invalid row count");
        };

        return new GenericContainerScreenHandler(type, syncId, playerInventory, this, rows);
    }

    private static int validateRowCount(int rows) {
        if (rows < 1 || rows > 6) throw new IllegalArgumentException("Row count must be within [1,6]");
        return rows;
    }
}
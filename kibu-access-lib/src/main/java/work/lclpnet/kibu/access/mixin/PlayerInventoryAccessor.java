package work.lclpnet.kibu.access.mixin;

import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerInventory.class)
public interface PlayerInventoryAccessor {

    @Accessor
    int getSelectedSlot();

    @Accessor
    void setSelectedSlot(int selectedSlot);
}

package work.lclpnet.test;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import work.lclpnet.kibu.inv.item.FireworkExplosion;
import work.lclpnet.kibu.inv.item.FireworkUtil;

public class FireworkCommand {

    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(command());
    }

    private LiteralArgumentBuilder<ServerCommandSource> command() {
        return CommandManager.literal("kibu:firework")
                .requires(s -> s.hasPermissionLevel(2))
                .executes(context -> {
                    ItemStack stack = new ItemStack(Items.FIREWORK_ROCKET);
                    FireworkUtil.setFlight(stack, (byte) 3);
                    FireworkUtil.setExplosions(stack,
                            new FireworkExplosion(FireworkRocketItem.Type.CREEPER, true, false, new int[] {0x00ff00}, new int[] {0x005500}),
                            new FireworkExplosion(FireworkRocketItem.Type.LARGE_BALL, false, true, new int[] {0xffff00}, new int[] {0x0000ff}));

                    ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                    player.getInventory().insertStack(stack);
                    return 1;
                });
    }
}

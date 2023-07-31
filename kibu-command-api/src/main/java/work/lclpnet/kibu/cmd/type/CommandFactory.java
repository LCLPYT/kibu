package work.lclpnet.kibu.cmd.type;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

public interface CommandFactory<S> {

    LiteralArgumentBuilder<S> create(CommandRegistrationContext context);
}

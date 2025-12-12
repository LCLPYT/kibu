package work.lclpnet.kibu.translate.text;

import net.minecraft.network.chat.Component;

public interface TextTranslatable {

    Component translateTo(String language);
}

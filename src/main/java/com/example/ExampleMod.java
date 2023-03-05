package com.example;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {

	public static final String MOD_ID = "testmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initialized.");
	}

	public static Identifier identifier(String path) {
		return new Identifier(MOD_ID, path);
	}

	public static Identifier identifier(String format, Object... substitutes) {
		return identifier(String.format(format, substitutes));
	}
}
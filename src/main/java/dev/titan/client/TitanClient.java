package dev.titan.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public final class TitanClient implements ClientModInitializer {
    private static final KeyMapping.Category CATEGORY =
        KeyMapping.Category.register(Identifier.fromNamespaceAndPath("titanclient", "main"));
    private KeyMapping openGuiKey;
    private KeyMapping performanceKey;
    private boolean performanceMode;
    private int previousRenderDistance;

    @Override
    public void onInitializeClient() {
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.titanclient.open_gui", InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT, CATEGORY
        ));
        performanceKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.titanclient.performance", InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_P, CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.consumeClick()) {
                if (client.screen == null) client.setScreen(new TitanClickGui());
            }
            while (performanceKey.consumeClick()) {
                togglePerformanceMode(client);
            }
        });
    }

    public void togglePerformanceMode(net.minecraft.client.Minecraft client) {
        if (client.options == null) return;
        if (!performanceMode) {
            previousRenderDistance = client.options.renderDistance().get();
            client.options.renderDistance().set(Math.min(previousRenderDistance, 6));
            performanceMode = true;
            client.player.displayClientMessage(Component.literal("TITAN: Performance mode ON (render distance 6)"), true);
        } else {
            client.options.renderDistance().set(previousRenderDistance);
            performanceMode = false;
            client.player.displayClientMessage(Component.literal("TITAN: Performance mode OFF"), true);
        }
        client.options.save();
    }

    public boolean isPerformanceMode() { return performanceMode; }
}

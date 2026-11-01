package dev.titan.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public final class TitanClient implements ClientModInitializer {
    private static final KeyMapping.Category CATEGORY =
        KeyMapping.Category.register(Identifier.fromNamespaceAndPath("titanclient", "main"));
    private static KeyMapping openGuiKey;
    private static KeyMapping performanceKey;
    private static boolean performanceMode;
    private static int previousRenderDistance;

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

    public static void togglePerformanceMode(Minecraft client) {
        if (client == null || client.options == null) return;
        if (!performanceMode) {
            previousRenderDistance = client.options.renderDistance().get();
            client.options.renderDistance().set(Math.min(previousRenderDistance, 6));
            performanceMode = true;
            notifyPlayer(client, "TITAN: Performance mode ON - 6 chunks");
        } else {
            client.options.renderDistance().set(previousRenderDistance);
            performanceMode = false;
            notifyPlayer(client, "TITAN: Performance mode OFF");
        }
        client.options.save();
    }

    private static void notifyPlayer(Minecraft client, String message) {
        if (client.player != null) client.player.displayClientMessage(Component.literal(message), true);
    }

    public static boolean isPerformanceMode() { return performanceMode; }
}

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
    private static KeyMapping sprintToggleKey;
    private static boolean performanceMode;
    private static boolean sprintToggle;
    private static boolean originalSaved;
    private static boolean warnedForMultiplayer;
    private static int previousRenderDistance;

    @Override
    public void onInitializeClient() {
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.titanclient.open_gui", InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT, CATEGORY));
        performanceKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.titanclient.performance", InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_P, CATEGORY));
        sprintToggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.titanclient.toggle_sprint", InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G, CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            boolean multiplayer = client.getConnection() != null && !client.hasSingleplayerServer();
            if (!multiplayer) {
                warnedForMultiplayer = false;
            } else {
                if (!warnedForMultiplayer) {
                    notifyPlayer(client, "TITAN SAFETY: Multiplayer detected. Use only server-approved features.");
                    warnedForMultiplayer = true;
                }
                if (sprintToggle) {
                    sprintToggle = false;
                    notifyPlayer(client, "TITAN SAFETY: Toggle Sprint auto-disabled in multiplayer.");
                }
            }

            while (openGuiKey.consumeClick()) {
                if (client.screen == null) client.setScreen(new TitanClickGui());
            }
            while (performanceKey.consumeClick()) togglePerformanceMode(client);
            while (sprintToggleKey.consumeClick()) {
                if (multiplayer) {
                    notifyPlayer(client, "TITAN SAFETY: Toggle Sprint is restricted to singleplayer.");
                } else {
                    setSprintToggle(!sprintToggle, client);
                }
            }
            if (sprintToggle && client.player != null && client.screen == null
                && !client.player.isFallFlying() && !client.player.isPassenger()
                && !client.player.isCrouching() && client.player.getFoodData().getFoodLevel() > 6) {
                client.player.setSprinting(true);
            }
        });
    }

    public static void setSprintToggle(boolean enabled, Minecraft client) {
        if (enabled && client != null && client.getConnection() != null && !client.hasSingleplayerServer()) {
            sprintToggle = false;
            notifyPlayer(client, "TITAN SAFETY: Toggle Sprint is restricted to singleplayer.");
            return;
        }
        sprintToggle = enabled;
        notifyPlayer(client, "TITAN: Toggle Sprint " + (enabled ? "ON" : "OFF"));
    }

    public static boolean isSprintToggle() { return sprintToggle; }

    public static void applyRenderPreset(Minecraft client, int chunks) {
        if (client == null || client.options == null) return;
        if (!originalSaved) {
            previousRenderDistance = client.options.renderDistance().get();
            originalSaved = true;
        }
        client.options.renderDistance().set(chunks);
        performanceMode = chunks <= 6;
        client.options.save();
        notifyPlayer(client, "TITAN: Render distance set to " + chunks + " chunks");
    }

    public static void restoreRenderDistance(Minecraft client) {
        if (client == null || client.options == null) return;
        if (originalSaved) {
            client.options.renderDistance().set(previousRenderDistance);
            client.options.save();
            originalSaved = false;
        }
        performanceMode = false;
        notifyPlayer(client, "TITAN: Original render distance restored");
    }

    public static void togglePerformanceMode(Minecraft client) {
        if (performanceMode) restoreRenderDistance(client);
        else applyRenderPreset(client, 6);
    }

    private static void notifyPlayer(Minecraft client, String message) {
        if (client != null && client.player != null)
            client.player.displayClientMessage(Component.literal(message), true);
    }

    public static boolean isPerformanceMode() { return performanceMode; }
}

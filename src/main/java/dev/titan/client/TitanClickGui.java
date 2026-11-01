package dev.titan.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class TitanClickGui extends Screen {
    public TitanClickGui() {
        super(Component.literal("TITAN CLIENT"));
    }

    @Override
    protected void init() {
        int buttonWidth = Math.min(220, Math.max(150, width - 32));
        int left = (width - buttonWidth) / 2;
        int top = Math.max(55, height / 2 - 30);

        addRenderableWidget(Button.builder(performanceLabel(), button -> {
            TitanClient.togglePerformanceMode(Minecraft.getInstance());
            button.setMessage(performanceLabel());
        }).bounds(left, top, buttonWidth, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Close"), button -> onClose())
            .bounds(left, top + 28, buttonWidth, 20).build());
    }

    private static Component performanceLabel() {
        return Component.literal("Performance Mode: " +
            (TitanClient.isPerformanceMode() ? "ON" : "OFF"));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        graphics.fill(0, 0, width, height, 0xE9101018);
        graphics.fill(width / 2 - 118, 18, width / 2 + 118, 42, 0xFF7A1018);
        graphics.drawCenteredString(font, "TITAN CLIENT", width / 2, 25, 0xFFFFFFFF);
        graphics.drawCenteredString(font, "PC + PojavLauncher | Fabric 1.21.11",
            width / 2, 46, 0xFFCCCCCC);
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

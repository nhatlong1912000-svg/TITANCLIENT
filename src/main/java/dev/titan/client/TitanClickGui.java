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
        int buttonWidth = Math.min(220, Math.max(140, width - 24));
        int left = (width - buttonWidth) / 2;
        int buttonHeight = 20;
        int gap = 5;
        int totalHeight = buttonHeight * 5 + gap * 4;
        int top = Math.max(58, (height - totalHeight) / 2);

        addRenderableWidget(Button.builder(Component.literal("ULTRA FPS  |  4 CHUNKS"),
            button -> TitanClient.applyRenderPreset(Minecraft.getInstance(), 4))
            .bounds(left, top, buttonWidth, buttonHeight).build());

        addRenderableWidget(Button.builder(Component.literal("PERFORMANCE  |  6 CHUNKS"),
            button -> TitanClient.applyRenderPreset(Minecraft.getInstance(), 6))
            .bounds(left, top + 25, buttonWidth, buttonHeight).build());

        addRenderableWidget(Button.builder(Component.literal("BALANCED  |  8 CHUNKS"),
            button -> TitanClient.applyRenderPreset(Minecraft.getInstance(), 8))
            .bounds(left, top + 50, buttonWidth, buttonHeight).build());

        addRenderableWidget(Button.builder(Component.literal("RESTORE ORIGINAL"),
            button -> TitanClient.restoreRenderDistance(Minecraft.getInstance()))
            .bounds(left, top + 75, buttonWidth, buttonHeight).build());

        addRenderableWidget(Button.builder(Component.literal("CLOSE"),
            button -> onClose())
            .bounds(left, top + 100, buttonWidth, buttonHeight).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        graphics.fill(0, 0, width, height, 0xE9101018);
        graphics.fill(width / 2 - 122, 14, width / 2 + 122, 39, 0xFF7A1018);
        graphics.drawCenteredString(font, "TITAN CLIENT", width / 2, 22, 0xFFFFFFFF);
        graphics.drawCenteredString(font, "PERFORMANCE PRESETS  |  FABRIC 1.21.11",
            width / 2, 45, 0xFFCCCCCC);
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

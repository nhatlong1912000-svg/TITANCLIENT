package dev.titan.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class TitanClickGui extends Screen {
    private enum Page { PERFORMANCE, HUD, VISUAL, UTILITY }
    private Page page = Page.PERFORMANCE;

    public TitanClickGui() {
        super(Component.literal("TITAN CLIENT"));
    }

    @Override
    protected void init() {
        clearWidgets();
        int panelWidth = Math.min(390, width - 16);
        int panelHeight = Math.min(250, height - 20);
        int left = (width - panelWidth) / 2;
        int top = (height - panelHeight) / 2;
        int sideWidth = Math.max(76, Math.min(104, panelWidth / 3));
        int contentLeft = left + sideWidth + 10;
        int contentWidth = panelWidth - sideWidth - 20;

        addRenderableWidget(tab("PERF", Page.PERFORMANCE, left + 6, top + 43, sideWidth - 12));
        addRenderableWidget(tab("HUD", Page.HUD, left + 6, top + 69, sideWidth - 12));
        addRenderableWidget(tab("VISUAL", Page.VISUAL, left + 6, top + 95, sideWidth - 12));
        addRenderableWidget(tab("UTILITY", Page.UTILITY, left + 6, top + 121, sideWidth - 12));

        if (page == Page.PERFORMANCE) {
            addRenderableWidget(action("ULTRA FPS  ·  4 CHUNKS", contentLeft, top + 48,
                contentWidth, () -> TitanClient.applyRenderPreset(Minecraft.getInstance(), 4)));
            addRenderableWidget(action("PERFORMANCE  ·  6 CHUNKS", contentLeft, top + 76,
                contentWidth, () -> TitanClient.applyRenderPreset(Minecraft.getInstance(), 6)));
            addRenderableWidget(action("BALANCED  ·  8 CHUNKS", contentLeft, top + 104,
                contentWidth, () -> TitanClient.applyRenderPreset(Minecraft.getInstance(), 8)));
            addRenderableWidget(action("RESTORE ORIGINAL", contentLeft, top + 132,
                contentWidth, () -> TitanClient.restoreRenderDistance(Minecraft.getInstance())));
        } else if (page == Page.UTILITY) {
            addRenderableWidget(action("TOGGLE SPRINT: " + (TitanClient.isSprintToggle() ? "ON" : "OFF"),
                contentLeft, top + 48, contentWidth,
                () -> {
                    TitanClient.setSprintToggle(!TitanClient.isSprintToggle(), Minecraft.getInstance());
                    rebuildWidgets();
                }));
            addRenderableWidget(Button.builder(Component.literal("WAYPOINTS  ·  COMING SOON"), b -> {})
                .bounds(contentLeft, top + 76, contentWidth, 20).build());
            addRenderableWidget(Button.builder(Component.literal("SCREENSHOT MANAGER  ·  COMING SOON"), b -> {})
                .bounds(contentLeft, top + 104, contentWidth, 20).build());
            addRenderableWidget(Button.builder(Component.literal("MODULE SEARCH  ·  COMING SOON"), b -> {})
                .bounds(contentLeft, top + 132, contentWidth, 20).build());
        } else {
            String[] labels = switch (page) {
                case HUD -> new String[] {"FPS COUNTER", "COORDINATES HUD", "KEYSTROKES", "ARMOR HUD"};
                case VISUAL -> new String[] {"CUSTOM CROSSHAIR", "ZOOM", "FULLBRIGHT", "DAMAGE TINT"};
                case UTILITY -> new String[] {"WAYPOINTS", "SCREENSHOT MANAGER", "MODULE SEARCH"};
                default -> new String[0];
            };
            for (int i = 0; i < labels.length; i++) {
                final int index = i;
                addRenderableWidget(Button.builder(Component.literal(labels[i] + "  ·  COMING SOON"),
                    b -> {}).bounds(contentLeft, top + 48 + i * 28, contentWidth, 20)
                    .build());
            }
        }

        addRenderableWidget(action("CLOSE", contentLeft, top + panelHeight - 31,
            contentWidth, this::onClose));
    }

    private Button tab(String label, Page target, int x, int y, int w) {
        String shown = page == target ? "› " + label : label;
        return Button.builder(Component.literal(shown), b -> {
            page = target;
            rebuildWidgets();
        }).bounds(x, y, w, 20).build();
    }

    private Button action(String label, int x, int y, int w, Runnable task) {
        return Button.builder(Component.literal(label), b -> task.run())
            .bounds(x, y, w, 20).build();
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        int panelWidth = Math.min(390, width - 16);
        int panelHeight = Math.min(250, height - 20);
        int left = (width - panelWidth) / 2;
        int top = (height - panelHeight) / 2;
        int sideWidth = Math.max(76, Math.min(104, panelWidth / 3));

        g.fill(0, 0, width, height, 0x99000000);
        g.fill(left, top, left + panelWidth, top + panelHeight, 0xF014151B);
        g.fill(left, top, left + panelWidth, top + 29, 0xFF861322);
        g.fill(left, top + 29, left + sideWidth, top + panelHeight, 0xFF1B1C24);
        g.fill(left + sideWidth, top + 29, left + sideWidth + 1, top + panelHeight, 0xFF8C1B2B);
        g.drawString(font, "TITAN CLIENT", left + 10, top + 10, 0xFFFFFFFF, false);
        g.drawString(font, page.name(), left + sideWidth + 12, top + 37, 0xFFFF6677, false);
        g.drawString(font, "MODS  /  1.21.11", left + 9, top + panelHeight - 17, 0xFF888A96, false);
        super.render(g, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

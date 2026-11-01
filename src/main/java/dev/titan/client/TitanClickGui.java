package dev.titan.client;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class TitanClickGui extends Screen {
    private enum Page { PERFORMANCE, HUD, VISUAL, UTILITY }
    private Page page = Page.HUD;

    public TitanClickGui() {
        super(Component.literal("TITAN CLIENT"));
    }

    @Override
    protected void init() {
        clearWidgets();
        int panelWidth = Math.min(410, width - 16);
        int panelHeight = Math.min(285, height - 16);
        int left = (width - panelWidth) / 2;
        int top = (height - panelHeight) / 2;
        int sideWidth = 104;
        int contentLeft = left + sideWidth + 12;
        int contentWidth = panelWidth - sideWidth - 22;

        addRenderableWidget(tab("PERF", Page.PERFORMANCE, left + 6, top + 46, sideWidth - 12));
        addRenderableWidget(tab("HUD", Page.HUD, left + 6, top + 73, sideWidth - 12));
        addRenderableWidget(tab("VISUAL", Page.VISUAL, left + 6, top + 100, sideWidth - 12));
        addRenderableWidget(tab("UTILITY", Page.UTILITY, left + 6, top + 127, sideWidth - 12));

        List<TitanModules.Entry> entries = TitanModules.ALL.stream()
            .filter(entry -> entry.group().equals(page.name()))
            .toList();

        for (int i = 0; i < entries.size(); i++) {
            TitanModules.Entry entry = entries.get(i);
            String state = TitanModules.isEnabled(entry.id()) ? "ON" : "OFF";
            addRenderableWidget(Button.builder(
                Component.literal(entry.name() + "  [" + state + "]"),
                button -> {
                    TitanModules.toggle(entry.id());
                    rebuildWidgets();
                }
            ).bounds(contentLeft, top + 49 + i * 25, contentWidth, 20).build());
        }

        if (page == Page.UTILITY) {
            addRenderableWidget(Button.builder(Component.literal("SAVE CURRENT POSITION AS WAYPOINT"),
                button -> TitanModules.setWaypoint(Minecraft.getInstance()))
                .bounds(contentLeft, top + 205, contentWidth, 20).build());
        }

        addRenderableWidget(Button.builder(Component.literal("CLOSE"), button -> onClose())
            .bounds(contentLeft, top + panelHeight - 30, contentWidth, 20).build());
    }

    private Button tab(String label, Page target, int x, int y, int w) {
        String shown = page == target ? "› " + label : label;
        return Button.builder(Component.literal(shown), button -> {
            page = target;
            rebuildWidgets();
        }).bounds(x, y, w, 20).build();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        int panelWidth = Math.min(410, width - 16);
        int panelHeight = Math.min(285, height - 16);
        int left = (width - panelWidth) / 2;
        int top = (height - panelHeight) / 2;
        int sideWidth = 104;

        graphics.fill(0, 0, width, height, 0x99000000);
        graphics.fill(left, top, left + panelWidth, top + panelHeight, 0xF014151B);
        graphics.fill(left, top, left + panelWidth, top + 30, 0xFF861322);
        graphics.fill(left, top + 30, left + sideWidth, top + panelHeight, 0xFF1B1C24);
        graphics.fill(left + sideWidth, top + 30, left + sideWidth + 1, top + panelHeight, 0xFF8C1B2B);
        graphics.drawString(font, "TITAN CLIENT  /  24 MODULES", left + 10, top + 11, 0xFFFFFFFF, false);
        graphics.drawString(font, page.name(), left + sideWidth + 12, top + 36, 0xFFFF6677, false);
        graphics.drawString(font, "RIGHT SHIFT", left + 10, top + panelHeight - 17, 0xFF888A96, false);
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

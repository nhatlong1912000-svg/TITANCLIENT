package dev.titan.client;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class TitanClickGui extends Screen {
    private enum Page {
        HUD("HUD"), VISUAL("VISUAL"), PERFORMANCE("PERFORMANCE"), UTILITY("UTILITY");
        final String label;
        Page(String label) { this.label = label; }
    }

    private Page page = Page.HUD;
    private int scroll;
    private static final int RED = 0xFFFF2948;
    private static final int PANEL = 0xF0101118;
    private static final int CARD = 0xFF191A22;
    private static final int MUTED = 0xFF9699A6;

    public TitanClickGui() {
        super(Component.literal("TITAN CLIENT"));
    }

    @Override
    protected void init() {
        // Drawn manually so the interface is consistent across desktop and touch layouts.
    }

    private int panelWidth() { return Math.min(820, width - 20); }
    private int panelHeight() { return Math.min(520, height - 20); }
    private int left() { return (width - panelWidth()) / 2; }
    private int top() { return (height - panelHeight()) / 2; }
    private int sideWidth() { return Math.max(112, Math.min(154, panelWidth() / 4)); }
    private int contentLeft() { return left() + sideWidth() + 18; }
    private int contentWidth() { return panelWidth() - sideWidth() - 30; }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        g.fill(0, 0, width, height, 0x9905070D);
        int x = left(), y = top(), w = panelWidth(), h = panelHeight(), side = sideWidth();
        g.fill(x, y, x + w, y + h, PANEL);
        g.fill(x, y, x + w, y + 5, RED);
        g.fill(x, y, x + w, y + 76, 0xFF14151D);
        g.fill(x, y + 75, x + w, y + 77, 0xFF54202C);
        g.fill(x, y + 76, x + side, y + h, 0xFF12131A);
        g.fill(x + side, y + 76, x + side + 1, y + h, 0xFF792033);

        g.drawString(font, "✦", x + 18, y + 19, RED, false);
        g.drawString(font, "TITAN CLIENT", x + 42, y + 17, 0xFFFFFFFF, false);
        g.drawString(font, "BETTER  •  FASTER  •  STRONGER", x + 42, y + 37, 0xFFFF7183, false);
        g.drawString(font, "24 MODULES", x + w - 104, y + 27, 0xFFB8BAC4, false);

        Page[] pages = Page.values();
        String[] labels = {"HUD", "VISUAL", "PERFORMANCE", "UTILITY"};
        for (int i = 0; i < pages.length; i++) {
            int ty = y + 102 + i * 48;
            boolean active = page == pages[i];
            if (active) {
                g.fill(x + 9, ty - 8, x + side - 10, ty + 29, 0xFF501321);
                g.fill(x + 9, ty - 8, x + 13, ty + 29, RED);
            }
            g.drawString(font, labels[i], x + 25, ty + 4, active ? 0xFFFFFFFF : MUTED, false);
        }
        g.drawString(font, "SETTINGS", x + 24, y + h - 56, MUTED, false);
        g.drawString(font, "RIGHT SHIFT  •  CLOSE", x + 18, y + h - 28, 0xFF747783, false);

        int cx = contentLeft();
        int cw = contentWidth();
        g.drawString(font, page.label + "  /  MODULES", cx, y + 91, RED, false);
        List<TitanModules.Entry> entries = TitanModules.ALL.stream()
            .filter(e -> e.group().equals(page.name()))
            .toList();

        int gap = 10;
        int cardW = (cw - gap) / 2;
        int rowH = 58;
        for (int i = 0; i < entries.size(); i++) {
            TitanModules.Entry entry = entries.get(i);
            int col = i % 2;
            int row = i / 2;
            int bx = cx + col * (cardW + gap);
            int by = y + 118 + row * (rowH + 8) - scroll;
            if (by + rowH < y + 112 || by > y + h - 14) continue;
            boolean enabled = TitanModules.isEnabled(entry.id());
            boolean hover = mouseX >= bx && mouseX <= bx + cardW && mouseY >= by && mouseY <= by + rowH;
            g.fill(bx, by, bx + cardW, by + rowH, hover ? 0xFF252630 : CARD);
            g.fill(bx, by, bx + cardW, by + 1, 0xFF48202B);
            g.drawString(font, entry.name(), bx + 12, by + 13, 0xFFF4F4F7, false);
            g.drawString(font, "TITAN MODULE", bx + 12, by + 32, MUTED, false);
            int sx = bx + cardW - 48;
            int sy = by + 18;
            g.fill(sx, sy, sx + 34, sy + 18, enabled ? RED : 0xFF555761);
            g.fill(enabled ? sx + 18 : sx + 2, sy + 2, enabled ? sx + 32 : sx + 16, sy + 16, 0xFFFFFFFF);
        }
        super.render(g, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double mouseX = event.x();
        double mouseY = event.y();
        if (event.button() != 0) return super.mouseClicked(event, doubleClick);
        int x = left(), y = top(), h = panelHeight(), side = sideWidth();
        for (int i = 0; i < Page.values().length; i++) {
            int ty = y + 94 + i * 48;
            if (mouseX >= x + 8 && mouseX <= x + side - 8 && mouseY >= ty && mouseY <= ty + 38) {
                page = Page.values()[i];
                scroll = 0;
                return true;
            }
        }
        int cx = contentLeft(), cw = contentWidth(), gap = 10;
        int cardW = (cw - gap) / 2, rowH = 58;
        List<TitanModules.Entry> entries = TitanModules.ALL.stream()
            .filter(e -> e.group().equals(page.name())).toList();
        for (int i = 0; i < entries.size(); i++) {
            int col = i % 2, row = i / 2;
            int bx = cx + col * (cardW + gap);
            int by = y + 118 + row * (rowH + 8) - scroll;
            if (mouseX >= bx && mouseX <= bx + cardW && mouseY >= by && mouseY <= by + rowH) {
                TitanModules.toggle(entries.get(i).id());
                if (entries.get(i).id().equals("waypoint")) TitanModules.setWaypoint(Minecraft.getInstance());
                return true;
            }
        }
        if (mouseX < x || mouseX > x + panelWidth() || mouseY < y || mouseY > y + h) onClose();
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int rows = (int) Math.ceil(TitanModules.ALL.stream().filter(e -> e.group().equals(page.name())).count() / 2.0);
        int max = Math.max(0, rows * 66 - (panelHeight() - 145));
        scroll = Math.max(0, Math.min(max, scroll - (int)(verticalAmount * 42)));
        return true;
    }


    @Override
    public boolean isPauseScreen() { return false; }
}

package dev.titan.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class TitanClickGui extends Screen {
    public TitanClickGui() { super(Component.literal("TITAN CLIENT")); }

    @Override
    protected void init() {
        int left = this.width / 2 - 105;
        int top = 38;
        int row = 23;
        for (int i = 0; i < ModuleManager.MODULES.size(); i++) {
            Module module = ModuleManager.MODULES.get(i);
            int y = top + i * row;
            if (y + 20 > this.height - 28) break;
            addRenderableWidget(Button.builder(label(module), b -> {
                module.toggle();
                b.setMessage(label(module));
            }).bounds(left, y, 210, 20).build());
        }
        addRenderableWidget(Button.builder(Component.literal("Close"), b -> onClose())
            .bounds(left, this.height - 25, 210, 20).build());
    }

    private static Component label(Module module) {
        return Component.literal((module.isEnabled() ? "[ON] " : "[OFF] ") + module.getName());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics, mouseX, mouseY, delta);
        graphics.drawCenteredString(font, "TITAN CLIENT | RIGHT SHIFT", width / 2, 17, 0x55CCFF);
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}

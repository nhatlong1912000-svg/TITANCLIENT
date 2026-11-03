package dev.titan.client;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public final class TitanModules {
    public record Entry(String id, String name, String group) {}

    public static final List<Entry> ALL = List.of(
        new Entry("fps", "FPS Counter", "HUD"),
        new Entry("coords", "Coordinates HUD", "HUD"),
        new Entry("chunk", "Chunk Coordinates", "HUD"),
        new Entry("direction", "Direction HUD", "HUD"),
        new Entry("clock", "Local Clock", "HUD"),
        new Entry("day", "World Day Counter", "HUD"),
        new Entry("health", "Health Display", "VISUAL"),
        new Entry("hunger", "Hunger Display", "VISUAL"),
        new Entry("armor", "Armor Status", "VISUAL"),
        new Entry("xp", "Experience Display", "VISUAL"),
        new Entry("held", "Held Item Display", "VISUAL"),
        new Entry("effects", "Status Effects Count", "VISUAL"),
        new Entry("speed", "Movement Speed", "PERFORMANCE"),
        new Entry("sprint", "Sprint Status", "PERFORMANCE"),
        new Entry("sneak", "Sneak Status", "PERFORMANCE"),
        new Entry("ground", "Ground Status", "PERFORMANCE"),
        new Entry("light", "Local Light Level", "PERFORMANCE"),
        new Entry("ping", "Network Ping", "PERFORMANCE"),
        new Entry("dimension", "Dimension Display", "UTILITY"),
        new Entry("keys", "Keystrokes HUD", "UTILITY"),
        new Entry("target_entity", "Target Entity Info", "UTILITY"),
        new Entry("target_block", "Target Block Coordinates", "UTILITY"),
        new Entry("waypoint", "Personal Waypoint", "UTILITY"),
        new Entry("session", "Session Timer", "UTILITY"),
        new Entry("durability", "Tool Durability", "RAID"),
        new Entry("totems", "Totem Counter", "RAID"),
        new Entry("crystals", "End Crystal Counter", "RAID"),
        new Entry("obsidian", "Obsidian Counter", "RAID"),
        new Entry("pearls", "Ender Pearl Counter", "RAID"),
        new Entry("golden_apples", "Golden Apple Counter", "RAID"),
        new Entry("shulkers", "Shulker Box Counter", "RAID"),
        new Entry("rockets", "Firework Counter", "RAID"),
        new Entry("arrows", "Arrow Counter", "RAID"),
        new Entry("raid_timer", "Raid Session Timer", "RAID"),
        new Entry("sus_chunk_finder", "Sus Chunk Finder", "RAID"),
        new Entry("base_finder", "Base Finder", "RAID"),
        new Entry("amethyst_esp", "Amethyst ESP", "RAID"),
        new Entry("chunk_finder", "Chunk Finder", "RAID")
    );

    private static final Map<String, Boolean> ENABLED = new LinkedHashMap<>();
    private static BlockPos waypoint;
    private static int nearbyAmethystCount;
    private static long lastAmethystScan;
    private static final long sessionStart = System.currentTimeMillis();

    static {
        for (Entry entry : ALL) ENABLED.put(entry.id(), false);
        ENABLED.put("fps", true);
        ENABLED.put("coords", true);
    }

    private TitanModules() {}

    public static boolean isEnabled(String id) {
        return ENABLED.getOrDefault(id, false);
    }

    public static void toggle(String id) {
        ENABLED.computeIfPresent(id, (key, value) -> !value);
    }

    public static void setWaypoint(Minecraft client) {
        if (client.player == null) return;
        waypoint = client.player.blockPosition();
        client.player.displayClientMessage(Component.literal(
            "TITAN: Waypoint saved at " + waypoint.getX() + ", " + waypoint.getY() + ", " + waypoint.getZ()), true);
    }

    public static void registerHud() {
        HudElementRegistry.addLast(
            net.minecraft.resources.Identifier.fromNamespaceAndPath("titanclient", "module_hud"),
            TitanModules::renderHud
        );
    }

    private static void renderHud(GuiGraphics graphics, net.minecraft.client.DeltaTracker tickCounter) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null || client.options.hideGui) return;
        int x = 8;
        int y = 8;
        for (Entry entry : ALL) {
            if (!isEnabled(entry.id())) continue;
            String value = value(entry.id(), client);
            if (value == null || value.isBlank()) continue;
            String line = entry.name() + ": " + value;
            graphics.fill(x - 3, y - 2, x + client.font.width(line) + 4, y + 11, 0x990D0D12);
            graphics.drawString(client.font, line, x, y, 0xFFFF6677, false);
            y += 12;
        }
    }

    private static String value(String id, Minecraft client) {
        var player = client.player;
        var level = client.level;
        return switch (id) {
            case "fps" -> Integer.toString(client.getFps());
            case "coords" -> String.format("%.1f / %.1f / %.1f", player.getX(), player.getY(), player.getZ());
            case "chunk" -> {
                BlockPos p = player.blockPosition();
                yield (p.getX() >> 4) + ", " + (p.getZ() >> 4);
            }
            case "direction" -> player.getDirection().toString();
            case "clock" -> LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            case "day" -> Long.toString(level.getDayTime() / 24000L);
            case "health" -> String.format("%.1f / %.1f", player.getHealth(), player.getMaxHealth());
            case "hunger" -> Integer.toString(player.getFoodData().getFoodLevel());
            case "armor" -> Integer.toString(player.getArmorValue());
            case "xp" -> Integer.toString(player.experienceLevel);
            case "held" -> player.getMainHandItem().getHoverName().getString();
            case "effects" -> Integer.toString(player.getActiveEffects().size());
            case "speed" -> String.format("%.2f m/s", player.getDeltaMovement().horizontalDistance() * 20.0);
            case "sprint" -> player.isSprinting() ? "ON" : "OFF";
            case "sneak" -> player.isCrouching() ? "ON" : "OFF";
            case "ground" -> player.onGround() ? "ON" : "AIR";
            case "light" -> Integer.toString(level.getMaxLocalRawBrightness(player.blockPosition()));
            case "ping" -> {
                var connection = client.getConnection();
                var info = connection == null ? null : connection.getPlayerInfo(player.getUUID());
                yield info == null ? "N/A" : info.getLatency() + " ms";
            }
            case "dimension" -> level.dimension().identifier().toString();
            case "keys" -> "W:" + key(client.options.keyUp.isDown()) +
                " A:" + key(client.options.keyLeft.isDown()) +
                " S:" + key(client.options.keyDown.isDown()) +
                " D:" + key(client.options.keyRight.isDown()) +
                " J:" + key(client.options.keyJump.isDown());
            case "target_entity" -> client.crosshairPickEntity == null ? "None" :
                client.crosshairPickEntity.getName().getString();
            case "target_block" -> client.hitResult instanceof BlockHitResult hit
                ? hit.getBlockPos().getX() + ", " + hit.getBlockPos().getY() + ", " + hit.getBlockPos().getZ()
                : "None";
            case "waypoint" -> waypoint == null ? "Not set" :
                waypoint.getX() + ", " + waypoint.getY() + ", " + waypoint.getZ() +
                String.format(" (%.1fm)", Math.sqrt(player.blockPosition().distSqr(waypoint)));
            case "session", "raid_timer" -> {
                long seconds = (System.currentTimeMillis() - sessionStart) / 1000L;
                yield String.format("%02d:%02d:%02d", seconds / 3600, (seconds / 60) % 60, seconds % 60);
            }
            case "durability" -> {
                var stack = player.getMainHandItem();
                yield stack.isEmpty() ? "Empty hand" : stack.isDamageableItem()
                    ? (stack.getMaxDamage() - stack.getDamageValue()) + " / " + stack.getMaxDamage()
                    : "Not damageable";
            }
            case "totems" -> Integer.toString(countItem(player, Items.TOTEM_OF_UNDYING));
            case "crystals" -> Integer.toString(countItem(player, Items.END_CRYSTAL));
            case "obsidian" -> Integer.toString(countItem(player, Items.OBSIDIAN));
            case "pearls" -> Integer.toString(countItem(player, Items.ENDER_PEARL));
            case "golden_apples" -> Integer.toString(countItem(player, Items.GOLDEN_APPLE) + countItem(player, Items.ENCHANTED_GOLDEN_APPLE));
            case "shulkers" -> Integer.toString(countItem(player, Items.SHULKER_BOX));
            case "rockets" -> Integer.toString(countItem(player, Items.FIREWORK_ROCKET));
            case "arrows" -> Integer.toString(countItem(player, Items.ARROW));
            default -> "";
        };
    }

    private static int countNearbyAmethyst(Minecraft client) {\n        var player = client.player;\n        var level = client.level;\n        if (player == null || level == null) return 0;\n        BlockPos center = player.blockPosition();\n        int count = 0;\n        for (int x = -8; x <= 8; x++) {\n            for (int y = -8; y <= 8; y++) {\n                for (int z = -8; z <= 8; z++) {\n                    var state = level.getBlockState(center.offset(x, y, z));\n                    if (state.is(Blocks.AMETHYST_CLUSTER) || state.is(Blocks.SMALL_AMETHYST_BUD)\n                        || state.is(Blocks.MEDIUM_AMETHYST_BUD) || state.is(Blocks.LARGE_AMETHYST_BUD)) count++;\n                }\n            }\n        }\n        return count;\n    }\n\n    private static int countItem(net.minecraft.world.entity.player.Player player, Item item) {
        int total = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            var stack = player.getInventory().getItem(i);
            if (stack.is(item)) total += stack.getCount();
        }
        return total;
    }

    private static String key(boolean down) {
        return down ? "§a●" : "§7○";
    }
}

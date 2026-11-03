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
        new Entry("day", "World Day Counter", "HUD"),
        new Entry("health", "Health Display", "VISUAL"),
        new Entry("hunger", "Hunger Display", "VISUAL"),
        new Entry("armor", "Armor Status", "VISUAL"),
        new Entry("xp", "Experience Display", "VISUAL"),
        new Entry("held", "Held Item Display", "VISUAL"),
        new Entry("effects", "Status Effects Count", "VISUAL"),
        new Entry("speed", "Movement Speed", "PERFORMANCE"),
        new Entry("sprint", "Sprint Status", "PERFORMANCE"),\n        new Entry("freecam", "Freecam", "UTILITY"),
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
        new Entry("chunk_finder", "Chunk Finder", "RAID"),
        new Entry("target_health", "Target Health", "COMBAT"),
        new Entry("target_armor", "Target Armor", "COMBAT"),
        new Entry("target_distance", "Target Distance", "COMBAT"),
        new Entry("target_name", "Target Name", "COMBAT"),
        new Entry("attack_indicator", "Attack Indicator", "COMBAT"),
        new Entry("combat_timer", "Combat Timer", "COMBAT"),
        new Entry("hit_counter", "Hit Counter", "COMBAT"),
        new Entry("damage_indicator", "Damage Indicator", "COMBAT"),
        new Entry("totem_pop_counter", "Totem Pop Counter", "COMBAT"),
        new Entry("combat_hud", "Combat HUD", "COMBAT"),
        new Entry("weapon_info", "Weapon Info", "COMBAT"),
        new Entry("armor_warning", "Armor Warning", "COMBAT"),
        new Entry("combat_log", "Combat Log", "COMBAT"),
        new Entry("crystal_warning", "Crystal Warning", "COMBAT"),
        new Entry("totem_tracker", "Totem Tracker", "COMBAT"),
        new Entry("auto_eat", "Auto Eat", "MISC"),
        new Entry("auto_craft", "Auto Craft", "MISC"),
        new Entry("auto_fish", "Auto Fish", "MISC"),
        new Entry("auto_log", "Auto Log", "MISC"),
        new Entry("discord_presence", "Discord Presence", "MISC"),
        new Entry("friends", "Friends", "MISC"),
        new Entry("calculator", "Calculator", "MISC"),
        new Entry("screenshot_tool", "Screenshot Tool", "MISC"),
        new Entry("module_search", "Module Search", "MISC"),
        new Entry("auto_reconnect", "Auto Reconnect", "MISC"),
        new Entry("chat_timestamps", "Chat Timestamps", "MISC"),
        new Entry("chat_tweaks", "Chat Tweaks", "MISC"),
        new Entry("inventory_profiles", "Inventory Profiles", "MISC"),
        new Entry("item_highlight", "Item Highlight", "MISC"),
        new Entry("anti_afk", "Anti AFK", "MISC"),
        new Entry("jump_counter", "Jump Counter", "MOVEMENT"),
        new Entry("fall_distance", "Fall Distance", "MOVEMENT"),
        new Entry("movement_timer", "Movement Timer", "MOVEMENT"),
        new Entry("motion_hud", "Motion HUD", "MOVEMENT"),
        new Entry("step_counter", "Step Counter", "MOVEMENT"),
        new Entry("elytra_status", "Elytra Status", "MOVEMENT"),
        new Entry("free_look", "Free Look", "MOVEMENT"),
        new Entry("step", "Step", "MOVEMENT"),
        new Entry("sprint_toggle", "Sprint Toggle", "MOVEMENT"),
        new Entry("safe_walk", "Safe Walk", "MOVEMENT"),
        new Entry("elytra_hud", "Elytra HUD", "MOVEMENT"),
        new Entry("boat_fly_status", "Vehicle Status", "MOVEMENT"),
        new Entry("velocity_display", "Velocity Display", "MOVEMENT"),
        new Entry("distance_traveled", "Distance Traveled", "MOVEMENT"),
        new Entry("movement_stats", "Movement Stats", "MOVEMENT"),
        new Entry("seed_info", "Seed Info", "WORLD"),
        new Entry("biome_info", "Biome Info", "WORLD"),
        new Entry("world_time", "World Time", "WORLD"),
        new Entry("world_day", "World Day", "WORLD"),
        new Entry("personal_waypoints", "Personal Waypoints", "WORLD"),
        new Entry("waypoint_distance", "Waypoint Distance", "WORLD"),
        new Entry("compass", "Compass", "WORLD"),
        new Entry("local_map_info", "Local Map Info", "WORLD"),
        new Entry("world_hud", "World HUD", "WORLD"),
        new Entry("block_counter", "Block Counter", "WORLD"),
        new Entry("region_info", "Region Info", "WORLD"),
        new Entry("structure_notes", "Structure Notes", "WORLD"),
        new Entry("spawn_info", "Spawn Info", "WORLD"),
        new Entry("slime_chunk_info", "Slime Chunk Info", "WORLD"),
        new Entry("map_markers", "Map Markers", "WORLD"),
        new Entry("entity_info", "Entity Info", "RENDER"),
        new Entry("entity_nametags", "Entity Nametags", "RENDER"),
        new Entry("mob_counter", "Mob Counter", "RENDER"),
        new Entry("item_esp", "Item ESP", "RENDER"),
        new Entry("block_highlight", "Block Highlight", "RENDER"),
        new Entry("chest_highlight", "Chest Highlight", "RENDER"),
        new Entry("ore_info", "Ore Info", "RENDER"),
        new Entry("cave_info", "Cave Info", "RENDER"),
        new Entry("weather_visuals", "Weather Visuals", "RENDER"),
        new Entry("time_changer", "Time Changer", "RENDER"),
        new Entry("fullbright", "Fullbright", "RENDER"),
        new Entry("camera_hud", "Camera HUD", "RENDER"),
        new Entry("particle_control", "Particle Control", "RENDER"),
        new Entry("screenshot_mode", "Screenshot Mode", "RENDER"),
        new Entry("jump_circles", "Jump Circles", "RENDER"),
        new Entry("storage_esp", "Storage ESP", "RENDER"),
        new Entry("block_esp", "Block ESP", "RENDER"),
        new Entry("tracers", "Tracers", "RENDER"),
        new Entry("entity_owner_info", "Entity Info Labels", "RENDER"),
        new Entry("player_model", "Player Model", "PLAYER"),
        new Entry("air_hud", "Air HUD", "PLAYER"),
        new Entry("temperature_display", "Temperature Display", "PLAYER"),
        new Entry("player_statistics", "Player Statistics", "PLAYER"),
        new Entry("health_hud", "Health HUD", "PLAYER"),
        new Entry("hunger_hud", "Hunger HUD", "PLAYER"),
        new Entry("armor_hud", "Armor HUD", "PLAYER"),
        new Entry("xp_hud", "XP HUD", "PLAYER"),
        new Entry("effects_hud", "Effects HUD", "PLAYER"),
        new Entry("durability_hud", "Durability HUD", "PLAYER"),
        new Entry("totem_counter", "Totem Counter", "INVENTORY"),
        new Entry("crystal_counter", "Crystal Counter", "INVENTORY"),
        new Entry("obsidian_counter", "Obsidian Counter", "INVENTORY"),
        new Entry("ender_pearl_counter", "Ender Pearl Counter", "INVENTORY"),
        new Entry("golden_apple_counter", "Golden Apple Counter", "INVENTORY"),
        new Entry("shulker_counter", "Shulker Counter", "INVENTORY"),
        new Entry("rocket_counter", "Rocket Counter", "INVENTORY"),
        new Entry("arrow_counter", "Arrow Counter", "INVENTORY"),
        new Entry("item_counter", "Item Counter", "INVENTORY"),
        new Entry("inventory_hud", "Inventory HUD", "INVENTORY"),
        new Entry("cps_counter", "CPS Counter", "UTILITY"),
        new Entry("ping_display", "Ping Display", "UTILITY"),
        new Entry("keystrokes", "Keystrokes", "UTILITY"),
        new Entry("session_timer", "Session Timer", "UTILITY"),
        new Entry("target_info", "Target Info", "UTILITY"),
        new Entry("fps_optimizer", "FPS Optimizer", "PERFORMANCE"),
        new Entry("render_distance_preset", "Render Distance Preset", "PERFORMANCE"),
        new Entry("particle_reduction", "Particle Reduction", "PERFORMANCE"),
        new Entry("animation_reduction", "Animation Reduction", "PERFORMANCE"),
        new Entry("entity_render_limit", "Entity Render Limit", "PERFORMANCE"),
        new Entry("hud_optimizer", "HUD Optimizer", "PERFORMANCE"),
        new Entry("memory_display", "Memory Display", "PERFORMANCE"),
        new Entry("performance_monitor", "Performance Monitor", "PERFORMANCE"),
        new Entry("raid_hud", "Raid HUD", "RAID"),
        new Entry("supply_counter", "Supply Counter", "RAID"),
        new Entry("raid_block_counter", "Raid Block Counter", "RAID"),
        new Entry("session_statistics", "Session Statistics", "RAID"),
        new Entry("sus_chunk_notes", "Sus Chunk Notes", "RAID"),
        new Entry("amethyst_counter", "Amethyst Counter", "RAID"),
        new Entry("chunk_coordinates_tool", "Chunk Coordinates Tool", "RAID"),
        new Entry("base_waypoint_notes", "Base Waypoint Notes", "RAID"),
        new Entry("raid_timer_hud", "Raid Timer HUD", "RAID"),
        new Entry("durability_warning", "Durability Warning", "RAID")
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
            case "dimension" -> level.dimension().identifier().toString();\n            case "freecam" -> "Toggle available in singleplayer only";
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
            case "arrows" -> Integer.toString(countItem(player, Items.ARROW));\n            case "sus_chunk_finder" -> "Inhabited: " + level.getChunk(player.blockPosition()).getInhabitedTime();\n            case "base_finder" -> waypoint == null ? "Save a personal waypoint first" : "Waypoint " + waypoint.getX() + ", " + waypoint.getZ();\n            case "chunk_finder" -> { BlockPos p = player.blockPosition(); yield "Chunk " + (p.getX() >> 4) + ", " + (p.getZ() >> 4); }\n            case "amethyst_esp" -> {\n                long now = System.currentTimeMillis();\n                if (now - lastAmethystScan > 1000) { nearbyAmethystCount = countNearbyAmethyst(client); lastAmethystScan = now; }\n                yield nearbyAmethystCount + " nearby blocks";\n            }
            default -> "";
        };
    }

    private static int countNearbyAmethyst(Minecraft client) {
        var player = client.player;
        var level = client.level;
        if (player == null || level == null) return 0;
        BlockPos center = player.blockPosition();
        int count = 0;
        for (int x = -8; x <= 8; x++) {
            for (int y = -8; y <= 8; y++) {
                for (int z = -8; z <= 8; z++) {
                    var state = level.getBlockState(center.offset(x, y, z));
                    if (state.is(Blocks.AMETHYST_CLUSTER) || state.is(Blocks.SMALL_AMETHYST_BUD)
                        || state.is(Blocks.MEDIUM_AMETHYST_BUD) || state.is(Blocks.LARGE_AMETHYST_BUD)) count++;
                }
            }
        }
        return count;
    }

    private static int countItem(net.minecraft.world.entity.player.Player player, Item item) {
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

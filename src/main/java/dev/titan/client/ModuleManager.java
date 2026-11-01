package dev.titan.client;

import java.util.List;

public final class ModuleManager {
    public static final List<Module> MODULES = List.of(
        new Module("Sus Chunk", "Local chunk notes for worlds you control"),
        new Module("Chunk Finder", "Local exploration helper"),
        new Module("Amethyst ESP", "Local amethyst visibility helper"),
        new Module("Entity ESP", "Entity highlighting in permitted worlds"),
        new Module("Storage Finder", "Markers for your own worlds"),
        new Module("Cave Finder", "Cave exploration helper"),
        new Module("Coordinates HUD", "Coordinate display"),
        new Module("FPS Counter", "Performance display"),
        new Module("Keystrokes", "Key display"),
        new Module("Armor HUD", "Armor status display"),
        new Module("Toggle Sprint", "Sprint convenience"),
        new Module("Fullbright", "Brightness convenience"),
        new Module("Zoom", "Zoom convenience")
    );
    private ModuleManager() {}
}

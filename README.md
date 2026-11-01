# TITAN CLIENT

Starter project for a Minecraft Java 1.21.11 Fabric client-side utility mod.

## Features in this starter
- Right Shift opens the ClickGUI.
- Module list with toggle state for utility placeholders.
- Intended for local worlds and permitted client-side use.

The listed finder/ESP modules are placeholders in this starter: their actual rendering and scanning behavior has not yet been implemented. This project does not access hidden server data or bypass server permissions.

## Build
Requires Java 21 and Gradle 9.2.1.

```bash
gradle build
```

The remapped mod JAR should be in `build/libs/` (choose the JAR without a `-dev` suffix).

## Install
Use a Fabric 1.21.11 instance, install the matching Fabric API, and place the built JAR in the instance's `mods` folder. Press Right Shift in-game to open the GUI.

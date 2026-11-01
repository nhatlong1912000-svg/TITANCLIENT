package dev.titan.client;

import java.util.List;

public final class ModuleManager {
    public static final List<Module> MODULES = TitanModules.ALL.stream()
        .map(entry -> new Module(entry.name(), entry.group() + " module"))
        .toList();

    private ModuleManager() {}
}

package dev.titan.client;

public final class Module {
    private final String name;
    private final String description;
    private boolean enabled;

    public Module(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isEnabled() { return enabled; }
    public void toggle() { enabled = !enabled; }
}

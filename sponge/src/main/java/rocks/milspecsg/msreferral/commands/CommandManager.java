package rocks.milspecsg.msreferral.commands;

@FunctionalInterface
public interface CommandManager {

    void register(Object plugin);
}

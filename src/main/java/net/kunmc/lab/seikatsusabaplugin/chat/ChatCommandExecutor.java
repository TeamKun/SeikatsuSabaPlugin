package net.kunmc.lab.seikatsusabaplugin.chat;

public interface ChatCommandExecutor
{
    String getName();

    void onExecute(ChatCommandComponent component);
}

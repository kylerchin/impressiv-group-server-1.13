package net.minecraft.server;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import java.util.function.Predicate;

public class CommandTime {

    public static void a(com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> com_mojang_brigadier_commanddispatcher) {
        com_mojang_brigadier_commanddispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) CommandDispatcher.a("time").requires((commandlistenerwrapper) -> {
            return commandlistenerwrapper.hasPermission(2);
        })).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) CommandDispatcher.a("set").then(CommandDispatcher.a("day").executes((commandcontext) -> {
            return a((CommandListenerWrapper) commandcontext.getSource(), 1000);
        }))).then(CommandDispatcher.a("noon").executes((commandcontext) -> {
            return a((CommandListenerWrapper) commandcontext.getSource(), 6000);
        }))).then(CommandDispatcher.a("night").executes((commandcontext) -> {
            return a((CommandListenerWrapper) commandcontext.getSource(), 13000);
        }))).then(CommandDispatcher.a("midnight").executes((commandcontext) -> {
            return a((CommandListenerWrapper) commandcontext.getSource(), 18000);
        }))).then(CommandDispatcher.a("time", (ArgumentType) IntegerArgumentType.integer(0)).executes((commandcontext) -> {
            return a((CommandListenerWrapper) commandcontext.getSource(), IntegerArgumentType.getInteger(commandcontext, "time"));
        })))).then(CommandDispatcher.a("add").then(CommandDispatcher.a("time", (ArgumentType) IntegerArgumentType.integer(0)).executes((commandcontext) -> {
            return b((CommandListenerWrapper) commandcontext.getSource(), IntegerArgumentType.getInteger(commandcontext, "time"));
        })))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) CommandDispatcher.a("query").then(CommandDispatcher.a("daytime").executes((commandcontext) -> {
            return c((CommandListenerWrapper) commandcontext.getSource(), a(((CommandListenerWrapper) commandcontext.getSource()).getWorld()));
        }))).then(CommandDispatcher.a("gametime").executes((commandcontext) -> {
            return c((CommandListenerWrapper) commandcontext.getSource(), (int) (((CommandListenerWrapper) commandcontext.getSource()).getWorld().getTime() % 2147483647L));
        }))).then(CommandDispatcher.a("day").executes((commandcontext) -> {
            return c((CommandListenerWrapper) commandcontext.getSource(), (int) (((CommandListenerWrapper) commandcontext.getSource()).getWorld().getDayTime() / 24000L % 2147483647L));
        }))));
    }

    private static int a(WorldServer worldserver) {
        return (int) (worldserver.getDayTime() % 24000L);
    }

    private static int c(CommandListenerWrapper commandlistenerwrapper, int i) {
        commandlistenerwrapper.sendMessage(new ChatMessage("commands.time.query", new Object[] { Integer.valueOf(i)}), false);
        return i;
    }

    public static int a(CommandListenerWrapper commandlistenerwrapper, int i) {
        WorldServer[] aworldserver = commandlistenerwrapper.getServer().worldServer;
        int j = aworldserver.length;

        for (int k = 0; k < j; ++k) {
            WorldServer worldserver = aworldserver[k];

            worldserver.setDayTime((long) i);
        }

        commandlistenerwrapper.sendMessage(new ChatMessage("commands.time.set", new Object[] { Integer.valueOf(i)}), true);
        return a(commandlistenerwrapper.getWorld());
    }

    public static int b(CommandListenerWrapper commandlistenerwrapper, int i) {
        WorldServer[] aworldserver = commandlistenerwrapper.getServer().worldServer;
        int j = aworldserver.length;

        for (int k = 0; k < j; ++k) {
            WorldServer worldserver = aworldserver[k];

            worldserver.setDayTime(worldserver.getDayTime() + (long) i);
        }

        int l = a(commandlistenerwrapper.getWorld());

        commandlistenerwrapper.sendMessage(new ChatMessage("commands.time.set", new Object[] { Integer.valueOf(l)}), true);
        return l;
    }
}

package com.buuz135.raidmeter.command;

import com.buuz135.raidmeter.meter.RaidMeterObject;
import com.buuz135.raidmeter.storage.RaidMeterWorldSavedData;
import com.buuz135.raidmeter.util.MeterPosition;
import com.buuz135.raidmeter.util.MeterRenderType;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.network.chat.TextComponent;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class RaidMeterCommandHandler {


    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("raidmeter")
                        .then(getAdd())
                        .then(getRemove())
                        .then(getModify())
                        .then(getInfo())
        );
    }

    public static LiteralArgumentBuilder<CommandSourceStack> getAdd() {
        return Commands.literal("add")
                .then(Commands.argument("id", StringArgumentType.word())
                        .then(Commands.argument("display_name", StringArgumentType.string())
                                .then(Commands.argument("max_amount", IntegerArgumentType.integer(0, Integer.MAX_VALUE))
                                        .then(Commands.argument("current_amount", IntegerArgumentType.integer(0, Integer.MAX_VALUE))
                                                .then(Commands.argument("position", StringArgumentType.word()).suggests((context, builder) -> SharedSuggestionProvider.suggest(Arrays.stream(MeterPosition.values()).map(Enum::name).collect(Collectors.toList()), builder))
                                                        .then(Commands.argument("type", StringArgumentType.word()).suggests((context, builder) -> SharedSuggestionProvider.suggest(Arrays.stream(MeterRenderType.values()).map(Enum::name).collect(Collectors.toList()), builder))
                                                                .executes(RaidMeterCommandHandler::add)
                                                        )
                                                )
                                        )
                                )
                        )
                );
    }

    public static LiteralArgumentBuilder<CommandSourceStack> getRemove() {
        return Commands.literal("remove")
                .then(Commands.argument("id", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(RaidMeterWorldSavedData.getInstance(context.getSource().getLevel()).map(raidMeterWorldSavedData -> raidMeterWorldSavedData.getMeters().keySet()).orElse(Collections.EMPTY_SET), builder))
                        .executes(RaidMeterCommandHandler::remove)
                );
    }

    public static LiteralArgumentBuilder<CommandSourceStack> getModify() {
        return Commands.literal("modify")
                .then(Commands.argument("id", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(RaidMeterWorldSavedData.getInstance(context.getSource().getLevel()).map(raidMeterWorldSavedData -> raidMeterWorldSavedData.getMeters().keySet()).orElse(Collections.EMPTY_SET), builder))
                        .then(Commands.literal("max_amount").then(Commands.argument("max_amount", IntegerArgumentType.integer(0, Integer.MAX_VALUE)).executes(context -> modify(context, ModifyType.MAX_AMOUNT))))
                        .then(Commands.literal("current_amount").then(Commands.argument("amount", IntegerArgumentType.integer(0, Integer.MAX_VALUE)).executes(context -> modify(context, ModifyType.CURRENT_AMOUNT))))
                        .then(Commands.literal("add").then(Commands.argument("amount", IntegerArgumentType.integer(Integer.MIN_VALUE, Integer.MAX_VALUE)).executes(context -> modify(context, ModifyType.ADD))))
                        .then(Commands.literal("set").then(Commands.argument("amount", IntegerArgumentType.integer(Integer.MIN_VALUE, Integer.MAX_VALUE)).executes(context -> modify(context, ModifyType.ADD))))
                        .then(Commands.literal("position").then(Commands.argument("position", StringArgumentType.word()).suggests((context, builder) -> SharedSuggestionProvider.suggest(Arrays.stream(MeterPosition.values()).map(Enum::name).collect(Collectors.toList()), builder)).executes(context -> modify(context, ModifyType.POSITION))))
                        .then(Commands.literal("type").then(Commands.argument("type", StringArgumentType.word()).suggests((context, builder) -> SharedSuggestionProvider.suggest(Arrays.stream(MeterRenderType.values()).map(Enum::name).collect(Collectors.toList()), builder)).executes(context -> modify(context, ModifyType.TYPE))))
                        .then(Commands.literal("name").then(Commands.argument("name", StringArgumentType.string()).executes(context -> modify(context, ModifyType.NAME))))
                        .then(Commands.literal("color").then(Commands.argument("color", StringArgumentType.string()).suggests((context, builder) -> SharedSuggestionProvider.suggest(Arrays.stream(DyeColor.values()).map(Enum::name).collect(Collectors.toList()), builder)).executes(context -> modify(context, ModifyType.COLOR))))
                        .then(Commands.literal("display_add").then(Commands.argument("player", EntityArgument.players()).executes(context -> modify(context, ModifyType.DISPLAY_ADD))))
                        .then(Commands.literal("display_for").then(Commands.argument("time", IntegerArgumentType.integer(-1, Integer.MAX_VALUE)).executes(context -> modify(context, ModifyType.DISPLAY_FOR))))
                        .then(Commands.literal("display_remove").then(Commands.argument("player", EntityArgument.players()).executes(context -> modify(context, ModifyType.DISPLAY_REMOVE))))
                );
    }

    public static LiteralArgumentBuilder<CommandSourceStack> getInfo() {
        return Commands.literal("info")
                .then(Commands.argument("id", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(RaidMeterWorldSavedData.getInstance(context.getSource().getLevel()).map(raidMeterWorldSavedData -> raidMeterWorldSavedData.getMeters().keySet()).orElse(Collections.EMPTY_SET), builder))
                        .then(Commands.literal("max_amount").executes(context -> info(context, ModifyType.MAX_AMOUNT)))
                        .then(Commands.literal("current_amount").executes(context -> info(context, ModifyType.CURRENT_AMOUNT)))
                        .then(Commands.literal("position").executes(context -> info(context, ModifyType.POSITION)))
                        .then(Commands.literal("type").executes(context -> info(context, ModifyType.TYPE)))
                        .then(Commands.literal("name").executes(context -> info(context, ModifyType.NAME)))
                        .then(Commands.literal("color").executes(context -> info(context, ModifyType.COLOR)))
                );
    }

    private static int remove(CommandContext<CommandSourceStack> context) {
        RaidMeterWorldSavedData data = RaidMeterWorldSavedData.getInstance(context.getSource().getLevel()).orElse(null);
        if (data != null) {
            data.getMeters().remove(context.getArgument("id", String.class));
            data.markDirty(context.getSource().getLevel());
            return 1;
        }
        return 0;
    }

    private static int modify(CommandContext<CommandSourceStack> context, ModifyType type) throws CommandSyntaxException {
        RaidMeterWorldSavedData data = RaidMeterWorldSavedData.getInstance(context.getSource().getLevel()).orElse(null);
        if (data != null) {
            RaidMeterObject meterObject = data.getMeters().get(context.getArgument("id", String.class));
            if (meterObject != null) {
                if (type == ModifyType.MAX_AMOUNT) {
                    meterObject.setMaxProgress(context.getArgument("max_amount", Integer.class));
                }
                if (type == ModifyType.CURRENT_AMOUNT) {
                    meterObject.setCurrentProgress(context.getArgument("amount", Integer.class));
                }
                if (type == ModifyType.ADD) {
                    meterObject.add(context.getArgument("amount", Integer.class));
                }
                if (type == ModifyType.SET) {
                    meterObject.setCurrentProgress(context.getArgument("amount", Integer.class));
                }
                if (type == ModifyType.POSITION) {
                    meterObject.setMeterPosition(MeterPosition.valueOf(context.getArgument("position", String.class)));
                }
                if (type == ModifyType.TYPE) {
                    meterObject.setMeterRenderType(MeterRenderType.valueOf(context.getArgument("type", String.class)));
                }
                if (type == ModifyType.NAME) {
                    meterObject.setName(context.getArgument("name", String.class));
                }
                if (type == ModifyType.COLOR) {
                    meterObject.setColor(DyeColor.valueOf(context.getArgument("color", String.class)).getTextColor());
                }
                if (type == ModifyType.DISPLAY_ADD) {
                    for (ServerPlayer player : context.getArgument("player", EntitySelector.class).findPlayers(context.getSource())) {
                        String uuid = player.getUUID().toString();
                        if (!meterObject.getVisibleToPlayers().contains(uuid)) {
                            meterObject.getVisibleToPlayers().add(uuid);
                        }
                    }
                }
                if (type == ModifyType.DISPLAY_FOR) {
                    meterObject.setDisplayFor(context.getArgument("time", Integer.class));
                }
                if (type == ModifyType.DISPLAY_REMOVE) {
                    for (ServerPlayer player : context.getArgument("player", EntitySelector.class).findPlayers(context.getSource())) {
                        String uuid = player.getUUID().toString();
                        meterObject.getVisibleToPlayers().remove(uuid);
                    }
                }
                data.markDirty(context.getSource().getLevel());
            }
            return 1;
        }
        return 0;
    }

    private static int info(CommandContext<CommandSourceStack> context, ModifyType type) throws CommandSyntaxException {
        RaidMeterWorldSavedData data = RaidMeterWorldSavedData.getInstance(context.getSource().getLevel()).orElse(null);
        if (data != null) {
            RaidMeterObject meterObject = data.getMeters().get(context.getArgument("id", String.class));
            if (meterObject != null) {
                if (type == ModifyType.MAX_AMOUNT) {
                    context.getSource().getPlayerOrException().displayClientMessage(new TextComponent("Max amount: " + meterObject.getMaxProgress()), false);
                }
                if (type == ModifyType.CURRENT_AMOUNT) {
                    context.getSource().getPlayerOrException().displayClientMessage(new TextComponent("Current amount: " + meterObject.getCurrentProgress()), false);
                }
                if (type == ModifyType.POSITION) {
                    context.getSource().getPlayerOrException().displayClientMessage(new TextComponent("Position: " + meterObject.getMeterPosition().name()), false);
                }
                if (type == ModifyType.TYPE) {
                    context.getSource().getPlayerOrException().displayClientMessage(new TextComponent("Type: " + meterObject.getMeterRenderType().name()), false);
                }
                if (type == ModifyType.NAME) {
                    context.getSource().getPlayerOrException().displayClientMessage(new TextComponent("Name: " + meterObject.getName()), false);
                }
                if (type == ModifyType.COLOR) {
                    context.getSource().getPlayerOrException().displayClientMessage(new TextComponent("Color: " + meterObject.getColor()), false);
                }
                if (type == ModifyType.DISPLAY_FOR) {
                    context.getSource().getPlayerOrException().displayClientMessage(new TextComponent("Display For: " + meterObject.getDisplayFor() + " ticks"), false);
                }
                data.markDirty(context.getSource().getLevel());
            }
            return 1;
        }
        return 0;
    }

    private static int add(CommandContext<CommandSourceStack> context) {
        RaidMeterWorldSavedData data = RaidMeterWorldSavedData.getInstance(context.getSource().getLevel()).orElse(null);
        if (data != null) {
            RaidMeterObject object = new RaidMeterObject(
                    context.getArgument("id", String.class),
                    context.getArgument("display_name", String.class),
                    context.getArgument("max_amount", Integer.class),
                    context.getArgument("current_amount", Integer.class),
                    MeterPosition.valueOf(context.getArgument("position", String.class)),
                    MeterRenderType.valueOf(context.getArgument("type", String.class))
            );
            try {
                object.getVisibleToPlayers().add(context.getSource().getPlayerOrException().getUUID().toString());
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
            data.getMeters().put(context.getArgument("id", String.class), object);
            data.markDirty(context.getSource().getLevel());
            return 1;
        }
        return 0;
    }

    public static enum ModifyType {
        MAX_AMOUNT,
        CURRENT_AMOUNT,
        ADD,
        POSITION,
        TYPE,
        NAME,
        COLOR,
        SET,
        DISPLAY_ADD,
        DISPLAY_REMOVE,
        DISPLAY_FOR;
    }

}

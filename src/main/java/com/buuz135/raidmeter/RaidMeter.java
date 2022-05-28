package com.buuz135.raidmeter;

import com.buuz135.raidmeter.client.ModOverlayRegistry;
import com.buuz135.raidmeter.command.RaidMeterCommandHandler;
import com.buuz135.raidmeter.network.RaidMeterSyncMessage;
import com.buuz135.raidmeter.storage.ClientRaidMeterData;
import com.buuz135.raidmeter.storage.RaidMeterWorldSavedData;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("raidmeter")
public class RaidMeter {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "raidmeter";
    public static IIngameOverlay METER_ELEMENT;

    public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("raidmeter", "network"),
            () -> "1.0",
            s -> true,
            s -> true
    );

    public RaidMeter() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        NETWORK.registerMessage(0, RaidMeterSyncMessage.class, RaidMeterSyncMessage::toBytes, packetBuffer -> new RaidMeterSyncMessage().fromBytes(packetBuffer), RaidMeterSyncMessage::handle);
    }

    private void setup(final FMLCommonSetupEvent event) {

    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ModOverlayRegistry.register();
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {

    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(RegisterCommandsEvent event) {
        RaidMeterCommandHandler.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onPlayerLogged(PlayerEvent.PlayerLoggedInEvent event){
        RaidMeterWorldSavedData data = RaidMeterWorldSavedData.getInstance(event.getPlayer().level).orElse(null);
        if (data != null && event.getPlayer() instanceof ServerPlayer) {
            RaidMeter.NETWORK.sendTo(new RaidMeterSyncMessage(data.save(new CompoundTag())), ((ServerPlayer)event.getPlayer()).connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
        }
    }

}

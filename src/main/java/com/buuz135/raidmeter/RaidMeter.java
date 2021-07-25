package com.buuz135.raidmeter;

import com.buuz135.raidmeter.command.RaidMeterCommandHandler;
import com.buuz135.raidmeter.network.RaidMeterSyncMessage;
import com.buuz135.raidmeter.storage.RaidMeterWorldSavedData;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("raidmeter")
public class RaidMeter {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "raidmeter";

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

    }

    private void enqueueIMC(final InterModEnqueueEvent event) {

    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        RaidMeterCommandHandler.register(event.getServer().getCommandManager().getDispatcher());
    }

    @SubscribeEvent
    public void onPlayerLogged(PlayerEvent.PlayerLoggedInEvent event){
        RaidMeterWorldSavedData data = RaidMeterWorldSavedData.getInstance(event.getPlayer().world).orElse(null);
        if (data != null && event.getPlayer() instanceof ServerPlayerEntity) {
            RaidMeter.NETWORK.sendTo(new RaidMeterSyncMessage(data.write(new CompoundNBT())), ((ServerPlayerEntity)event.getPlayer()).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        }
    }

}

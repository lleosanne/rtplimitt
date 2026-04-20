package com.rtplimit;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.CommandEvent;

public class RtpCommandListener {

    private static final String NBT_KEY = "rtplimit_used";

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onCommand(CommandEvent event) {
        String input = event.getParseResults().getReader().getString().trim();

        // Match /rtp (with or without leading slash, and ignore extra args)
        if (!input.equalsIgnoreCase("rtp") && !input.toLowerCase().startsWith("rtp ")) {
            return;
        }

        var source = event.getParseResults().getContext().getSource();

        // Only apply to actual players
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        // Ops/admins bypass the limit
        if (player.hasPermissions(2)) {
            return;
        }

        CompoundTag persistedData = player.getPersistentData();

        if (persistedData.getBoolean(NBT_KEY)) {
            // Already used — cancel the command
            event.setCanceled(true);
            player.sendSystemMessage(Component.literal(
                "§cYou have already used /rtp. It can only be used once."
            ));
            RtpLimitMod.LOGGER.info("Blocked /rtp for player {} - already used.", player.getName().getString());
        } else {
            // First use — mark as used
            persistedData.putBoolean(NBT_KEY, true);
            RtpLimitMod.LOGGER.info("Player {} used /rtp for the first (and last) time.", player.getName().getString());
        }
    }
}

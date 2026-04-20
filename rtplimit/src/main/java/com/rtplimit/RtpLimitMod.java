package com.rtplimit;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(RtpLimitMod.MOD_ID)
public class RtpLimitMod {

    public static final String MOD_ID = "rtplimit";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RtpLimitMod(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(RtpCommandListener.class);
        LOGGER.info("RtpLimit mod loaded - /rtp is limited to one use per player.");
    }
}

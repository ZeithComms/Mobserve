package org.zeith.comms.c19mobserve;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.zeith.comms.c19mobserve.advancements.CriteriaTriggersMS;

import static org.zeith.comms.c19mobserve.Mobserve.MOD_ID;

@Mod(MOD_ID)
public class Mobserve
{
	public static final String MOD_ID = "mobserve";

	public Mobserve()
	{
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;

		modBus.addListener(this::setup);
		forgeBus.addListener(this::tickPlayer);
	}

	public void setup(FMLCommonSetupEvent e)
	{
		CriteriaTriggersMS.setup();
	}

	public void tickPlayer(TickEvent.PlayerTickEvent e)
	{
		if(e.side == LogicalSide.SERVER && e.phase == TickEvent.Phase.END && e.player instanceof ServerPlayerEntity && e.player.tickCount % 5 == 0)
		{
			ServerPlayerEntity player = (ServerPlayerEntity) e.player;

			Vector3d from = player.getEyePosition(1F);
			Vector3d to = from.add(player.getLookAngle().scale(128));

			for(Entity entity : player.level.getEntities(player, new AxisAlignedBB(from, to)))
			{
				CriteriaTriggersMS.OBSERVE_ENTITY.trigger(player, entity);
			}
		}
	}
}
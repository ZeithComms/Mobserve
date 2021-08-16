package org.zeith.comms.c19mobserve.advancements;

import org.zeith.comms.c19mobserve.advancements.criterion.ObserveEntityTrigger;

import static net.minecraft.advancements.CriteriaTriggers.register;

public class CriteriaTriggersMS
{
	public static final ObserveEntityTrigger OBSERVE_ENTITY = register(new ObserveEntityTrigger());

	public static void setup()
	{
	}
}
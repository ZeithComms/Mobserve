package org.zeith.comms.c19mobserve.advancements.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import org.zeith.comms.c19mobserve.Mobserve;

public class ObserveEntityTrigger
		extends AbstractCriterionTrigger<ObserveEntityTrigger.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation(Mobserve.MOD_ID, "observe_entity");

	@Override
	public ResourceLocation getId()
	{
		return ID;
	}

	@Override
	protected Instance createInstance(JsonObject json, EntityPredicate.AndPredicate player, ConditionArrayParser parser)
	{
		EntityPredicate entity = EntityPredicate.fromJson(json.get("entity"));
		double distance = !json.has("distance") ? 32 : json.get("distance").getAsDouble();

		return new Instance(player, entity, distance);
	}

	public void trigger(ServerPlayerEntity player, Entity entity)
	{
		this.trigger(player, inst -> inst.matches(player, entity));
	}

	public static class Instance
			extends CriterionInstance
	{
		private final EntityPredicate entity;
		private final double distance;

		public Instance(EntityPredicate.AndPredicate player, EntityPredicate entity, double distance)
		{
			super(ObserveEntityTrigger.ID, player);
			this.entity = entity;
			this.distance = distance;
		}

		private boolean isLookingAtEntity(ServerPlayerEntity player, Entity entity)
		{
			Vector3d eye = player.getEyePosition(1.0F);
			Vector3d dir = player.getViewVector(1F);
			Vector3d end = eye.add(dir.scale(distance));
			return entity.getBoundingBox().clip(eye, end).map(v -> v.distanceTo(eye) < distance).orElse(false) && player.canSee(entity);
		}

		public boolean matches(ServerPlayerEntity player, Entity entity)
		{
			return this.entity.matches(player, entity) && isLookingAtEntity(player, entity);
		}

		@Override
		public JsonObject serializeToJson(ConditionArraySerializer serializer)
		{
			JsonObject json = super.serializeToJson(serializer);
			json.add("entity", this.entity.serializeToJson());
			json.addProperty("distance", this.distance);
			return json;
		}
	}
}
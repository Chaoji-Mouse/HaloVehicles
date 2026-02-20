package com.cmhh.halovecs.entity.vehicle;

import com.atsuishio.superbwarfare.entity.vehicle.base.GeoVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.damage.DamageModifier;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class M274mEntity extends GeoVehicleEntity {

    private static final float CUSTOM_RUDDER_LIMIT = 2f;

    public M274mEntity(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    public DamageModifier getDamageModifier() {
        return super.getDamageModifier()
                .custom((source, damage) -> getSourceAngle(source, 0.25f) * damage);
    }

    @Override
    public void setRudderRot(float rudderRot) {
        float newRudderRot = rudderRot - this.getEntityData().get(DELTA_ROT);
        
        float clampedRudder = Mth.clamp(
            newRudderRot, 
            -CUSTOM_RUDDER_LIMIT,
            CUSTOM_RUDDER_LIMIT
        );
        
        float scaledRudder = clampedRudder * 0.75f;
        
        super.setRudderRot(scaledRudder);
    }
}

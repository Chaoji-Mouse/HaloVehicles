package com.cmhh.halovecs.client.renderer.entity;

import com.atsuishio.superbwarfare.client.renderer.entity.VehicleRenderer;
import com.cmhh.halovecs.client.model.entity.M12hmgModel;
import com.cmhh.halovecs.entity.vehicle.M12hmgEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class M12hmgRenderer extends VehicleRenderer<M12hmgEntity> {

    public M12hmgRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new M12hmgModel());
        this.shadowRadius = 0.8f;
    }
}

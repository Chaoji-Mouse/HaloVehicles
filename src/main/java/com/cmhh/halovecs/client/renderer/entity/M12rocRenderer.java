package com.cmhh.halovecs.client.renderer.entity;

import com.atsuishio.superbwarfare.client.renderer.entity.VehicleRenderer;
import com.cmhh.halovecs.client.model.entity.M12rocModel;
import com.cmhh.halovecs.entity.vehicle.M12rocEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class M12rocRenderer extends VehicleRenderer<M12rocEntity> {

    public M12rocRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new M12rocModel());
        this.shadowRadius = 0.8f;
    }
}

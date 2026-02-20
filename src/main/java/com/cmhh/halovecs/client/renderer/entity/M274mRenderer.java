package com.cmhh.halovecs.client.renderer.entity;

import com.atsuishio.superbwarfare.client.renderer.entity.VehicleRenderer;
import com.cmhh.halovecs.client.model.entity.M274mModel;
import com.cmhh.halovecs.entity.vehicle.M274mEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class M274mRenderer extends VehicleRenderer<M274mEntity> {

    public M274mRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new M274mModel());
        this.shadowRadius = 0.8f;
    }
}

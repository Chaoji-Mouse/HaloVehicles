package com.cmhh.halovecs.client.renderer.entity;

import com.atsuishio.superbwarfare.client.renderer.entity.VehicleRenderer;
import com.cmhh.halovecs.client.model.entity.M12gauModel;
import com.cmhh.halovecs.entity.vehicle.M12gauEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class M12gauRenderer extends VehicleRenderer<M12gauEntity> {

    public M12gauRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new M12gauModel());
        this.shadowRadius = 0.8f;
    }
}

package com.cmhh.halovecs.client.renderer.entity;

import com.atsuishio.superbwarfare.client.renderer.entity.VehicleRenderer;
import com.cmhh.halovecs.client.model.entity.M12traModel;
import com.cmhh.halovecs.entity.vehicle.M12traEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class M12traRenderer extends VehicleRenderer<M12traEntity> {

    public M12traRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new M12traModel());
        this.shadowRadius = 0.8f;
    }
}

package com.cmhh.halovecs.client.renderer.entity;

import com.atsuishio.superbwarfare.client.renderer.entity.VehicleRenderer;
import com.cmhh.halovecs.client.model.entity.M12Model;
import com.cmhh.halovecs.entity.vehicle.M12Entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class M12Renderer extends VehicleRenderer<M12Entity> {

    public M12Renderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new M12Model());
        this.shadowRadius = 0.8f;
    }
}

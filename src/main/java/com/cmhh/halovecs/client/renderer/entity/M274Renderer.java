package com.cmhh.halovecs.client.renderer.entity;

import com.atsuishio.superbwarfare.client.renderer.entity.VehicleRenderer;
import com.cmhh.halovecs.client.model.entity.M274Model;
import com.cmhh.halovecs.entity.vehicle.M274Entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class M274Renderer extends VehicleRenderer<M274Entity> {

    public M274Renderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new M274Model());
        this.shadowRadius = 0.8f;
    }
}

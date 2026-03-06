package com.cmhh.halovecs.client.renderer.entity;

import com.atsuishio.superbwarfare.client.renderer.entity.VehicleRenderer;
import com.cmhh.halovecs.client.model.entity.M808Model;
import com.cmhh.halovecs.entity.vehicle.M808Entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class M808Renderer extends VehicleRenderer<M808Entity> {

    public M808Renderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new M808Model());
        this.shadowRadius = 0.8f;
    }
}

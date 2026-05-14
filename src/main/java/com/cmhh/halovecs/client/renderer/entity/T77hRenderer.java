package com.cmhh.halovecs.client.renderer.entity;

import com.atsuishio.superbwarfare.client.renderer.entity.VehicleRenderer;
import com.cmhh.halovecs.client.model.entity.T77hModel;
import com.cmhh.halovecs.entity.vehicle.T77hEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class T77hRenderer extends VehicleRenderer<T77hEntity> {

    public T77hRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new T77hModel());
        this.shadowRadius = 0.8f;
    }
}

/*
 * Copyright (c) 2017 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration. All Rights Reserved.
 */

package gov.nasa.worldwind.shape;

import gov.nasa.worldwind.draw.DrawableDSFUN90ScreenPoint;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec3;
import gov.nasa.worldwind.render.AbstractRenderable;
import gov.nasa.worldwind.render.RenderContext;

public class RTWDSFUN90Point extends AbstractRenderable {

    protected Position position;

    protected Vec3 modelCoords = new Vec3();

    protected DrawableDSFUN90ScreenPoint drawable;

    public RTWDSFUN90Point(Position position) {
        this.position = position;
    }

    @Override
    protected void doRender(RenderContext rc) {

        if (this.drawable == null) {
            this.drawable = new DrawableDSFUN90ScreenPoint(rc.resources);
        }

        rc.globe.geographicToCartesian(this.position.latitude, this.position.longitude, this.position.altitude, this.modelCoords);
        this.drawable.setPosition(this.modelCoords);
        rc.offerShapeDrawable(drawable, rc.cameraPoint.distanceTo(this.modelCoords));

    }

}

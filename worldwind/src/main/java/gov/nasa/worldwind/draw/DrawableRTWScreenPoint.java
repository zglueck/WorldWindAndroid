/*
 * Copyright (c) 2017 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration. All Rights Reserved.
 */

package gov.nasa.worldwind.draw;

import android.content.res.Resources;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

import gov.nasa.worldwind.geom.Vec3;
import gov.nasa.worldwind.render.BufferObject;
import gov.nasa.worldwind.render.SimpleShaderProgram;

/**
 * Created by zach on 9/11/17.
 */

public class DrawableRTWScreenPoint implements Drawable {

    protected Vec3 position;

    protected float size = 6f;

    protected SimpleShaderProgram program;

    protected FloatBuffer buffer = FloatBuffer.allocate(3);

    protected BufferObject vbo = new BufferObject(GLES20.GL_ARRAY_BUFFER, 3 * 4, this.buffer);

    protected boolean updateBuffer = false;

    public DrawableRTWScreenPoint(Resources resources) {
        this.program = new SimpleShaderProgram(resources);
    }

    public Vec3 getPosition() {
        return position;
    }

    public void setPosition(Vec3 position) {
        this.position = position;
        this.updateBuffer = true;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
        this.updateBuffer = true;
    }

    @Override
    public void recycle() {

    }

    @Override
    public void draw(DrawContext dc) {

        if (dc.pickMode) {
            return; // picking not supported
        }

        if (this.program == null || !this.program.useProgram(dc)) {
            System.out.println("error using program");
            return;
        }

        if (this.position == null) {
            return; // no position specified yet
        }

        if (this.updateBuffer) {
            this.updateBuffer = false;
            this.buffer.position(0);
            this.buffer.put((float) this.position.x);
            this.buffer.put((float) this.position.y);
            this.buffer.put((float) this.position.z);
            this.buffer.flip();
        }

        this.vbo.bindBuffer(dc);

        this.program.loadModelview(dc.modelview);
        this.program.loadProjection(dc.projection);

        int verticesId = GLES20.glGetAttribLocation(this.program.getProgramId(), "vertexPoint");

        GLES20.glEnableVertexAttribArray(verticesId);
        GLES20.glVertexAttribPointer(verticesId, 3, GLES20.GL_FLOAT, false, 3 * 4, 0);

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);

    }
}

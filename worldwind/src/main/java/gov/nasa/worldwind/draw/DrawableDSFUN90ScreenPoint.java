/*
 * Copyright (c) 2017 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration. All Rights Reserved.
 */

package gov.nasa.worldwind.draw;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.FloatBuffer;

import gov.nasa.worldwind.geom.Vec3;
import gov.nasa.worldwind.render.BufferObject;
import gov.nasa.worldwind.render.DSFUN90SimpleShaderProgram;

public class DrawableDSFUN90ScreenPoint implements Drawable {

    protected Vec3 position;

    protected float size = 6f;

    protected DSFUN90SimpleShaderProgram program;

    protected FloatBuffer buffer = FloatBuffer.allocate(6);

    protected BufferObject vbo = new BufferObject(GLES20.GL_ARRAY_BUFFER, 6 * 4, this.buffer);

    protected boolean updateBuffer = false;

    public static final float[] SCRATCH = new float[2];

    public static final float[] SCRATCH_HIGH = new float[3];

    public static final float[] SCRATCH_LOW = new float[3];

    public DrawableDSFUN90ScreenPoint(Resources resources) {
        this.program = new DSFUN90SimpleShaderProgram(resources);
    }

    public void setPosition(Vec3 position) {
        this.position = position;
        this.updateBuffer = true;
    }

    @Override
    public void recycle() {
        // TODO
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

            DSFUN90SimpleShaderProgram.doubleToTwoFloats(this.position.x, SCRATCH);
            SCRATCH_HIGH[0] = SCRATCH[0];
            SCRATCH_LOW[0] = SCRATCH[1];
            DSFUN90SimpleShaderProgram.doubleToTwoFloats(this.position.y, SCRATCH);
            SCRATCH_HIGH[1] = SCRATCH[0];
            SCRATCH_LOW[1] = SCRATCH[1];
            DSFUN90SimpleShaderProgram.doubleToTwoFloats(this.position.z, SCRATCH);
            SCRATCH_HIGH[2] = SCRATCH[0];
            SCRATCH_LOW[2] = SCRATCH[1];

            this.buffer.put(SCRATCH_HIGH);
            this.buffer.put(SCRATCH_LOW);

            this.buffer.flip();
        }

        this.vbo.bindBuffer(dc);

        this.program.loadModelview(dc.modelview);
        this.program.loadProjection(dc.projection);
        this.program.setCamera(dc.eyePoint);

        int positionHighId = GLES20.glGetAttribLocation(this.program.getProgramId(), "positionHigh");
        int positionLowId = GLES20.glGetAttribLocation(this.program.getProgramId(), "positionLow");

        GLES20.glEnableVertexAttribArray(positionHighId);
        GLES20.glVertexAttribPointer(positionHighId, 3, GLES20.GL_FLOAT, false, 3 * 4, 0);
        GLES20.glEnableVertexAttribArray(positionLowId);
        GLES20.glVertexAttribPointer(positionLowId, 3, GLES20.GL_FLOAT, false, 3 * 4, 3);

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);

    }
}

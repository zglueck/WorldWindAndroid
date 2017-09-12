/*
 * Copyright (c) 2017 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration. All Rights Reserved.
 */

package gov.nasa.worldwind.render;

import android.content.res.Resources;
import android.opengl.GLES20;

import gov.nasa.worldwind.R;
import gov.nasa.worldwind.draw.DrawContext;
import gov.nasa.worldwind.geom.Matrix4;
import gov.nasa.worldwind.geom.Vec3;
import gov.nasa.worldwind.util.Logger;
import gov.nasa.worldwind.util.WWUtil;

public class DSFUN90SimpleShaderProgram extends SimpleShaderProgram {

    protected int cameraHighId;

    protected int cameraLowId;

    protected float[] cameraHigh = new float[3];

    protected float[] cameraLow = new float[3];

    private static final float[] SCRATCH = new float[2];

    public DSFUN90SimpleShaderProgram(Resources resources) {
        super(resources);
    }

    @Override
    protected void init(Resources resources) {
        try {
            String vs = WWUtil.readResourceAsText(resources, R.raw.gov_nasa_worldwind_dsfun90_vert);
            String fs = WWUtil.readResourceAsText(resources, R.raw.gov_nasa_worldwind_dsfun90_frag);
            this.setProgramSources(vs, fs);
            this.setAttribBindings("positionHigh", "positionLow");
        } catch (Exception logged) {
            Logger.logMessage(Logger.ERROR, "DSFUN90SimpleShaderProgram", "constructor", "errorReadingProgramSource", logged);
        }
    }

    @Override
    protected void initProgram(DrawContext dc) {
        super.initProgram(dc);

        this.cameraHighId = GLES20.glGetUniformLocation(this.programId, "cameraHigh");
        this.cameraLowId = GLES20.glGetUniformLocation(this.programId, "cameraLow");
    }

    public void setCamera(Vec3 cameraPosition) {

        doubleToTwoFloats(cameraPosition.x, SCRATCH);
        this.cameraHigh[0] = SCRATCH[0];
        this.cameraLow[0] = SCRATCH[1];

        doubleToTwoFloats(cameraPosition.y, SCRATCH);
        this.cameraHigh[1] = SCRATCH[0];
        this.cameraLow[1] = SCRATCH[1];

        doubleToTwoFloats(cameraPosition.z, SCRATCH);
        this.cameraHigh[2] = SCRATCH[0];
        this.cameraLow[2] = SCRATCH[1];

        GLES20.glUniform3fv(this.cameraHighId, 1, this.cameraHigh, 0);
        GLES20.glUniform3fv(this.cameraLowId, 1, this.cameraLow, 0);
    }

    public static void doubleToTwoFloats(double value, float[] split) {
        split[0] = (float) value;
        split[1] = (float) (value - split[0]);
    }

    @Override
    public void loadModelview(Matrix4 matrix) {
        matrix.transposeToArray(this.array, 0);
        GLES20.glUniformMatrix4fv(this.mvMatrixId, 1, false, this.array, 0);
    }

    @Override
    public void loadProjection(Matrix4 matrix) {
        matrix.transposeToArray(this.array, 0);
        GLES20.glUniformMatrix4fv(this.pMatrixId, 1, false, this.array, 0);
    }
}

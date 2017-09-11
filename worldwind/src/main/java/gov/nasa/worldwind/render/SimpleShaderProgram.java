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
import gov.nasa.worldwind.util.Logger;
import gov.nasa.worldwind.util.WWUtil;

/**
 * Created by zach on 9/11/17.
 */

public class SimpleShaderProgram extends ShaderProgram {

    protected Matrix4 mvMatrix = new Matrix4();

    protected Matrix4 pMatrix = new Matrix4();

    protected int mvMatrixId;

    protected int pMatrixId;

    private float[] array = new float[16];

    public SimpleShaderProgram(Resources resources) {
        try {
            String vs = WWUtil.readResourceAsText(resources, R.raw.gov_nasa_worldwind_simple_vert);
            String fs = WWUtil.readResourceAsText(resources, R.raw.gov_nasa_worldwind_simple_frag);
            this.setProgramSources(vs, fs);
            this.setAttribBindings("vertexPoint");
        } catch (Exception logged) {
            Logger.logMessage(Logger.ERROR, "SimpleShaderProgram", "constructor", "errorReadingProgramSource", logged);
        }
    }

    @Override
    protected void initProgram(DrawContext dc) {
        this.mvMatrixId = GLES20.glGetUniformLocation(this.programId, "mvMatrix");
        this.mvMatrix.transposeToArray(this.array, 0);
        GLES20.glUniformMatrix4fv(this.mvMatrixId, 1, false, this.array, 0);

        this.pMatrixId = GLES20.glGetUniformLocation(this.programId, "pMatrix");
        this.pMatrix.transposeToArray(this.array, 0);
        GLES20.glUniformMatrix4fv(this.pMatrixId, 1, false, this.array, 0);
    }

    public void loadModelview(Matrix4 matrix) {
        matrix.transposeToArray(this.array, 0);
        GLES20.glUniformMatrix4fv(this.mvMatrixId, 1, false, this.array, 0);
    }

    public void loadProjection(Matrix4 matrix) {
        matrix.transposeToArray(this.array, 0);
        GLES20.glUniformMatrix4fv(this.pMatrixId, 1, false, this.array, 0);
    }

    public int getProgramId() {
        return this.programId;
    }
}

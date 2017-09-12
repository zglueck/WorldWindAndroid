/*
 * Copyright (c) 2016 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration. All Rights Reserved.
 */

uniform vec3 cameraHigh;
uniform vec3 cameraLow;
uniform mat4 mvMatrix;
uniform mat4 pMatrix;

attribute vec3 positionHigh;
attribute vec3 positionLow;

void main() {
    gl_PointSize = 9.0;

    vec3 highDifference = positionHigh - cameraHigh;
    vec3 lowDifference = positionLow - cameraLow;
//    vec3 t1 = positionLow - cameraLow;
//    vec3 e = t1 - positionLow;
//    vec3 t2 = ((-cameraLow - e) + (positionLow - (t1 - e))) + positionHigh - cameraHigh;
//    vec3 highDifference = t1 + t2;
//    vec3 lowDifference = t2 - (highDifference - t1);

    /* Transform the vertex position by the modelview-projection matrix. */
    gl_Position = pMatrix * mvMatrix * vec4(highDifference + lowDifference, 1.0);
}
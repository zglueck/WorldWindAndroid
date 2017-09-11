/*
 * Copyright (c) 2016 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration. All Rights Reserved.
 */

uniform mat4 mvMatrix;
uniform mat4 pMatrix;

attribute vec3 vertexPoint;

void main() {
    gl_PointSize = 9.0;

    /* Transform the vertex position by the modelview-projection matrix. */
    gl_Position = pMatrix * mvMatrix * vec4(vertexPoint, 1.0);
}
/*
GLSL conversion of Michael Horsch water demo
http://www.bonzaisoftware.com/wfs.html
Converted by Mars_999
8/20/2005
*/

uniform float m_time;
uniform mat4 g_WorldViewProjectionMatrix;

attribute vec4 inPosition;
attribute vec2 inTexCoord;

varying vec2 texCoord;
varying vec2 waterTex1;
varying vec2 waterTex2;
varying vec4 position;

void main(void) {

    vec2 t1 = vec2(0.0, -m_time);
    vec2 t2 = vec2(0.0, m_time);

    texCoord = inTexCoord;
    waterTex1 = inTexCoord + t1;
    waterTex2 = inTexCoord + t2;

    position = g_WorldViewProjectionMatrix * inPosition;
    gl_Position = position;
}

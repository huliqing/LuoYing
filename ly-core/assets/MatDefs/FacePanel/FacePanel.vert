uniform mat4 g_WorldViewProjectionMatrix;

attribute vec3 inPosition;
attribute vec2 inTexCoord;

varying vec2 texCoord;
varying vec2 pos;

void main(){
    pos = vec2(inPosition.x, inPosition.y);
    texCoord = inTexCoord;

    gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);

}
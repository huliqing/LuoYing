uniform mat4 g_WorldViewProjectionMatrix;

attribute vec3 inPosition;

#ifdef Texture
    attribute vec2 inTexCoord;
    varying vec2 texCoord;
#endif

void main(){

    #ifdef Texture
        texCoord = inTexCoord;
    #endif

    gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);

}
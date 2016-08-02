uniform mat4 g_WorldViewProjectionMatrix;
uniform mat4 g_WorldMatrix;
uniform mat4 m_CastViewProjectionMatrix;

attribute vec3 inPosition;

#ifdef TEXTURE
    attribute vec2 inTexCoord;
    varying vec2 texCoord;
#endif

varying vec4 projCoord;
const mat4 biasMat = mat4(0.5, 0.0, 0.0, 0.0,
                          0.0, 0.5, 0.0, 0.0,
                          0.0, 0.0, 0.5, 0.0,
                          0.0, 0.0, 0.0, 1.0);

void main(){

    #ifdef TEXTURE
        texCoord = inTexCoord;
    #endif
    gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);

    vec4 modelSpacePos = vec4(inPosition, 1.0);
    vec4 worldPos = vec4(0.0);
    worldPos = g_WorldMatrix * modelSpacePos;
    //projCoord = biasMat * m_CastViewProjectionMatrix * worldPos;
    projCoord = m_CastViewProjectionMatrix * worldPos;

}
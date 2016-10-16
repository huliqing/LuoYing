uniform mat4 m_LightViewProjectionMatrix0;
uniform mat4 g_WorldViewProjectionMatrix;
uniform mat4 g_WorldMatrix;

varying vec4 projCoord0;
varying vec4 projCoord1;
varying vec4 projCoord2;
varying vec4 projCoord3;

varying float shadowPosition;
varying vec2 texCoord;

attribute vec3 inPosition;


const mat4 biasMat = mat4(0.5, 0.0, 0.0, 0.0,
                          0.0, 0.5, 0.0, 0.0,
                          0.0, 0.0, 0.5, 0.0,
                          0.5, 0.5, 0.5, 1.0);


void main(){
   vec4 modelSpacePos = vec4(inPosition, 1.0);
  
    gl_Position = g_WorldViewProjectionMatrix * modelSpacePos;


    shadowPosition = gl_Position.z;
   
    vec4 worldPos=vec4(0.0);

    // get the vertex in world space
    worldPos = g_WorldMatrix * modelSpacePos;

    // populate the light view matrices array and convert vertex to light viewProj space
    projCoord0 = biasMat * m_LightViewProjectionMatrix0 * worldPos;
}
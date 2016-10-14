#import "Common/ShaderLib/Skinning.glsllib"

uniform mat4 g_WorldViewProjectionMatrix;
uniform float g_Time;
 
attribute vec3 inPosition;
attribute vec2 inTexCoord;

varying vec2 maskCoord;
varying vec2 texCoord;

#ifdef FOG
    varying float fog_z;
#endif

#if defined(FOG_SKY)
    varying vec3 I;
    uniform vec3 g_CameraPosition;
    uniform mat4 g_WorldMatrix;
#endif 


void main(){
    vec4 pos = vec4(inPosition, 1.0);

    #ifdef NUM_BONES
      Skinning_Compute(pos);
    #endif

    gl_Position = g_WorldViewProjectionMatrix * pos;

    maskCoord = inTexCoord;
    texCoord = inTexCoord;
    
    #if defined(FOG_SKY)
        vec3 worldPos = (g_WorldMatrix * pos).xyz;
        I = normalize(g_CameraPosition - worldPos).xyz;
    #endif

    #ifdef FOG
        fog_z = gl_Position.z;
    #endif
}
#import "Common/ShaderLib/Skinning.glsllib"

uniform mat4 g_WorldViewProjectionMatrix;
uniform float m_offsetY;

attribute vec3 inPosition;
attribute vec2 inTexCoord;
 
varying vec2 oriCoord;
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
    oriCoord = inTexCoord;
    texCoord = inTexCoord;

    #if defined (TEX_ANIM_Y) && !defined (TEX_CHANGE_DIR)
        texCoord.y += m_offsetY;
    #elif defined (TEX_ANIM_Y) && defined (TEX_CHANGE_DIR)
        texCoord.y -= m_offsetY;
    #endif

    #if defined(FOG_SKY)
        vec3 worldPos = (g_WorldMatrix * pos).xyz;
        I = normalize(g_CameraPosition - worldPos).xyz;
    #endif

    #ifdef FOG
        fog_z = gl_Position.z;
    #endif
}
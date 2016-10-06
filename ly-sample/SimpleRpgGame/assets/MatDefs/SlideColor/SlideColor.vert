#import "Common/ShaderLib/Skinning.glsllib"

uniform mat4 g_WorldViewProjectionMatrix;
uniform float g_Time;

uniform float m_TexScaleY;
uniform float m_TexScaleX;
#if defined (TexAnimY) || defined (TexAnimX)
    uniform float m_TexSpeed;
#endif

uniform float m_MaskScaleY;
uniform float m_MaskScaleX;
#if defined (MaskAnimY) || defined (MaskAnimX)
    uniform float m_MaskSpeed;
#endif
 
attribute vec3 inPosition;
attribute vec2 inTexCoord;
 
varying vec2 oriCoord;
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
    oriCoord = inTexCoord;

    maskCoord = inTexCoord;
    #if defined (MaskAnimY) || defined (MaskAnimX)
        float maskTime = g_Time * m_MaskSpeed;
    #endif

    #if defined (MaskScaleY)
        maskCoord.y *= m_MaskScaleY;
    #endif
    #if defined (MaskScaleX) 
        maskCoord.x *= m_MaskScaleX;
    #endif

    #if defined (MaskAnimY) && !defined (MaskChangeDir)
        maskCoord.y += maskTime;
    #elif defined (MaskAnimY) && defined (MaskChangeDir)
        maskCoord.y -= maskTime;
    #endif

    #if defined (MaskAnimX) && !defined (MaskChangeDir)
        maskCoord.x += maskTime;
    #elif defined (MaskAnimX) && defined (MaskChangeDir)
        maskCoord.x -= maskTime;
    #endif


    texCoord = inTexCoord;
    #if defined (TexAnimY) || defined (TexAnimX)
        float texTime = g_Time * m_TexSpeed;
    #endif

    #if defined (TexScaleY)
        texCoord.y *= m_TexScaleY;
    #endif

    #if defined (TexScaleX) 
        texCoord.x *= m_TexScaleX;
    #endif

    #if defined (TexAnimY) && !defined (TexChangeDir)
        texCoord.y += texTime;
    #elif defined (TexAnimY) && defined (TexChangeDir)
        texCoord.y -= texTime;
    #endif

    #if defined (TexAnimX) && !defined (TexChangeDir)
        texCoord.x += texTime;
    #elif defined (TexAnimX) && defined (TexChangeDir)
        texCoord.x -= texTime;
    #endif


    #if defined(FOG_SKY)
        vec3 worldPos = (g_WorldMatrix * pos).xyz;
        I = normalize(g_CameraPosition - worldPos).xyz;
    #endif

    #ifdef FOG
        fog_z = gl_Position.z;
    #endif
}
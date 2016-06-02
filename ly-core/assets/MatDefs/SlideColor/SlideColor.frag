
uniform sampler2D m_MaskMap;
uniform sampler2D m_TexMap;
uniform vec4 m_StartColor;
uniform vec4 m_EndColor;

varying vec2 oriCoord;
varying vec2 maskCoord;
varying vec2 texCoord;

#ifdef FOG
    varying float fog_z;
    uniform vec4 m_FogColor;
    vec4 fogColor;
    float fogFactor;
#endif

#ifdef FOG_SKY
#import "Common/ShaderLib/Optics.glsllib"
    uniform ENVMAP m_FogSkyBox;
    varying vec3 I;
#endif

void main(){

    vec4 color = mix(m_StartColor, m_EndColor, oriCoord.y);

    float mask = texture2D(m_MaskMap, maskCoord).r;
    vec3 tex = texture2D(m_TexMap, texCoord).rgb;

    //gl_FragColor.rgb = m_Color.rgb * mask * tex;
    gl_FragColor.rgb = color.rgb * mask * tex;

    #ifdef FOG
        fogColor = m_FogColor;

        #ifdef FOG_SKY
            fogColor.rgb = Optics_GetEnvColor(m_FogSkyBox, I).rgb;
        #endif

       float fogDistance = fogColor.a;
       float depth = (fog_z - fogDistance) / fogDistance;
       depth = max(depth, 0.0);
       fogFactor = exp2(-depth*depth);
       fogFactor = clamp(fogFactor, 0.05, 1.0);

       gl_FragColor.rgb = mix(fogColor.rgb,gl_FragColor.rgb,vec3(fogFactor));
    #endif

    if(mask < 0.015){
        discard;
    }

    gl_FragColor.a = mask;
}
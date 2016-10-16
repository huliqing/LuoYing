/*
GLSL conversion of Michael Horsch water demo
http://www.bonzaisoftware.com/wfs.html
Converted by Mars_999
8/20/2005
*/

uniform sampler2D m_water_normalmap;
uniform sampler2D m_water_reflection;
uniform sampler2D m_water_dudvmap;
uniform float m_distortionScale;
uniform float m_distortionMix;
uniform float m_texScale;

#ifdef WATER_COLOR
    uniform vec4 m_waterColor;
#endif

#ifdef FOAM_MAP
    uniform sampler2D m_foamMap;
    uniform vec2 m_foamScale;
#endif

#ifdef FOAM_MASK_MAP
    uniform sampler2D m_foamMaskMap;
    uniform vec2 m_foamMaskScale;
#endif

varying vec2 texCoord;
varying vec2 waterTex1;
varying vec2 waterTex2;
varying vec4 position; //for projection

 const vec4 two = vec4(2.0, 2.0, 2.0, 1.0);
 const vec4 ofive = vec4(0.5,0.5,0.5,1.0);

void main(void)
{
     vec2 texScale = vec2(m_texScale);

     vec4 disdis = texture2D(m_water_dudvmap, waterTex2 * texScale);
     vec4 fdist = texture2D(m_water_dudvmap, waterTex1 + vec2(disdis) * vec2(m_distortionMix));
     fdist = normalize( fdist * 2.0 - 1.0)* vec4(m_distortionScale);  

     //load normalmap     
     vec4 nmap = texture2D(m_water_normalmap, waterTex1 + vec2(disdis) * vec2(m_distortionMix));
     nmap = (nmap-ofive) * two;
     vec4 vNorm = normalize(nmap);
     
     vec4 projCoord = position / position.w;
     projCoord =(projCoord+1.0)*0.5 + fdist;
     projCoord = clamp(projCoord, 0.001, 0.999);

     //load reflection
     vec4 refl = texture2D(m_water_reflection, vec2(projCoord.x,1.0-projCoord.y));

     #ifdef WATER_COLOR
        refl *= m_waterColor;
     #endif

     #ifdef FOAM_MAP
        
        vec4 foam = texture2D(m_foamMap, waterTex2 * m_foamScale);

        #ifdef FOAM_MASK_MAP
            vec2 maskCoord = vec2(texCoord.x, texCoord.y) * m_foamMaskScale;
            // make the coord to the center.
            maskCoord.x -= (m_foamMaskScale.x - 1.0) * 0.5;
            maskCoord.y -= (m_foamMaskScale.y - 1.0) * 0.5;
            vec4 foamMask = texture2D(m_foamMaskMap, maskCoord);
            foam *= foamMask;
        #endif

        refl += foam;

     #endif

     gl_FragColor = refl;
}

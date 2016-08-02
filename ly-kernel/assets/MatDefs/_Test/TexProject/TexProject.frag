#define SHADOWMAP sampler2DShadow

uniform vec4 m_Splits;
uniform SHADOWMAP m_ShadowMap0;
uniform float m_ShadowIntensity;

out vec4 outFragColor;
in float shadowPosition;

in vec4 projCoord0;
in vec4 projCoord1;
in vec4 projCoord2;
in vec4 projCoord3;

float BorderCheck(in vec2 coord){
    // Fastest, "hack" method (uses 4-5 instructions)
    vec4 t = vec4(coord.xy, 0.0, 1.0);
    t = step(t.wwxy, t.xyzz);
    return dot(t,t);  
}

float Nearest(in SHADOWMAP tex, in vec4 projCoord){
    float border = BorderCheck(projCoord.xy);
    if (border > 0.0){
        return 1.0;
    }
    return textureProj(tex, projCoord);
}

float getLightShadows(in vec4 splits,in float shadowPosition,
                                    in SHADOWMAP shadowMap0,
                                    in vec4 projCoord0){    
    float shadow = 1.0;   
    if(shadowPosition < splits.x){
        shadow = Nearest(shadowMap0, projCoord0);   
    }
    return shadow;
}

void main(){

    float shadow = 1.0;
    
    shadow = getLightShadows(m_Splits, shadowPosition,m_ShadowMap0,projCoord0);

    shadow = shadow * m_ShadowIntensity + (1.0 - m_ShadowIntensity); 
    outFragColor =  vec4(shadow, shadow, shadow, 1.0);

    //if (shadow < 0.5) {
    //    outFragColor =  vec4(0.8, 0, 0, 1.0);
    //} else {
    //}
    
}
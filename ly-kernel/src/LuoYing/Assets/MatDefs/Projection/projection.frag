uniform float m_ProjLeftX;
uniform float m_ProjLeftY;
uniform float m_ProjWidth;
uniform float m_ProjHeight;

#ifdef TEXTURE
    uniform sampler2D m_Texture;
    varying vec2 texCoord;
#endif

#ifdef COLOR
    uniform vec4 m_Color;
#endif

varying vec4 projCoord;

void main(){

    float minX = m_ProjLeftX;
    float maxX = m_ProjLeftX + m_ProjWidth;
    float minY = m_ProjLeftY;
    float maxY = m_ProjLeftY + m_ProjHeight;
    bool on = projCoord.x >= minX && projCoord.x <= maxX && projCoord.y >= minY && projCoord.y <= maxY;
    if (!on) {
        discard;
    }

    vec4 color = vec4(1.0);
    #ifdef TEXTURE
        vec2 coord = vec2((projCoord.x - minX) / m_ProjWidth, 
                          (projCoord.y - minY) / m_ProjHeight);
        color = texture2D(m_Texture, coord);
    #endif
    
    #ifdef COLOR
        color = color * m_Color;
    #endif

    gl_FragColor = color;
    gl_FragColor.a = 1.0;
}
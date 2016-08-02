uniform vec4 m_Color;

#ifdef Texture
    uniform sampler2D m_Texture;
    varying vec2 texCoord;
#endif

void main(){

    #ifdef Texture
        vec4 tex = texture2D(m_Texture, texCoord);
        gl_FragColor = tex * m_Color;
    #else
        gl_FragColor = m_Color;
    #endif
    
}
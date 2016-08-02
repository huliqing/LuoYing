uniform sampler2D m_Texture;
uniform vec4 m_Color;
uniform vec4 m_BorderColor;
uniform float m_Radius;
uniform float m_BorderWidth;

varying vec2 texCoord;
varying vec2 pos;


void main(){

    float dis = distance(pos, vec2(m_Radius, m_Radius));
    
    if (dis > m_Radius) {
        discard;
    }
    vec4 tex = texture2D(m_Texture, texCoord);
    float innerRadius = m_Radius - m_BorderWidth;
    float centerBorder = m_Radius - m_BorderWidth * 0.5;


    if (dis > centerBorder) {
        gl_FragColor = m_BorderColor;
        gl_FragColor.a = (m_Radius - dis) * m_BorderColor.a;
    } else if (dis > innerRadius) {
        gl_FragColor = m_BorderColor;
        gl_FragColor.a = (dis - innerRadius) * m_BorderColor.a;
    } else {
        gl_FragColor = tex * m_Color;
    }
}
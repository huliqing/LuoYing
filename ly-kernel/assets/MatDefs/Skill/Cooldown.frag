uniform vec4 m_Color;
uniform float m_Percent;
varying vec2 pos;
const vec2 center = vec2(0.5, 0.5);
const vec2 start = vec2(0.5, 1.0);
const float pi2 = 6.2831;

void main(){
    if (m_Percent <= 0.0 || m_Percent > 1.0)
        discard;
    if (m_Color.a < 0.01)
        discard;

    vec2 a = normalize(start - center);
    vec2 b = normalize(pos - center);
    float angle = acos(dot(a, b));
    float percent = angle / pi2;
    if (pos.x < 0.5) {
        percent = 1.0 - percent;
    }

    if (percent < m_Percent) {
        discard;
    } else {
        gl_FragColor.rgba = m_Color.rgba;
    }

}
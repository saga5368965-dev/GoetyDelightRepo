#version 150

uniform vec4 ColorModulator;
uniform float iTime;

in vec2 texCoord;
out vec4 fragColor;

void main() {

    vec2 uv = texCoord;


    vec3 col = 0.5 + 0.5 * cos(iTime + uv.xyx + vec3(0, 2, 4));


    fragColor = vec4(col, 1.0) * ColorModulator;
}
#version 150

in vec3 Position;
in vec2 UV0;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform float iTime;
uniform float intensity;
uniform vec4 color;

out vec2 texCoord;
out float vTime;
out float vIntensity;
out vec4 vColor;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    texCoord = UV0;
    vTime = iTime;
    vIntensity = intensity;
    vColor = color;
}
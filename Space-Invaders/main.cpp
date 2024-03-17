#include <cstdio>
#include <cstdint>
#include <GL/glew.h>
#include <GLFW/glfw3.h>
// g++ -c main.cpp -I/opt/homebrew/Cellar/glfw/3.4/include -Iglew-2.2.0/include
// g++ main.o -o main.exe -L/opt/homebrew/Cellar/glfw/3.4/lib -Lglew-2.2.0/lib -lglfw -lglew -framework OpenGL 

void error_callback(int error, const char *description) {
    fprintf(stderr, "Error: %s\n", description);
}

void validateShader(GLuint shader, const char* file = 0) {
    static const unsigned int BUFFER_SIZE = 512;
    char buffer[BUFFER_SIZE];
    GLsizei length = 0;

    glGetShaderInfoLog(shader, BUFFER_SIZE, &length, buffer);

    if(length > 0) {
        printf("Shader %d(%s) compile error: %s \n", shader, (file ? file: ""), buffer);
    }
}

bool validateProgram(GLuint program) {
    static const GLsizei BUFFER_SIZE = 512;
    GLchar buffer[BUFFER_SIZE];
    GLsizei length = 0;

    glGetProgramInfoLog(program, BUFFER_SIZE, &length, buffer);

    if(length > 0) {
        printf("Program %d link error: %s \n", program, buffer);
        return false;
    }
    return true;
}

struct Buffer {
    size_t width, height;
    uint32_t* data;
};

struct Sprite {
    size_t width, height;
    uint8_t* data;
};


uint32_t rgbToUint32(uint8_t r, uint8_t g, uint8_t b) {
    return (r << 24) | (g << 16) | (b << 8) | 255;
}

void bufferClear(Buffer* buffer, uint32_t colour) {
    for (size_t i = 0; i < buffer->width * buffer->height; i++) {
        buffer->data[i] = colour;
    }
}

void bufferSpriteDraw(Buffer* buffer, const Sprite& sprite, size_t x, size_t y, uint32_t colour) {
    for (size_t xi = 0; xi < sprite.width; xi++) {
        for (size_t yi = 0; yi < sprite.height; yi++) {
            size_t sy = sprite.height - 1 + y - yi;
            size_t sx = x + xi;
            if (sprite.data[yi * sprite.width + xi] && sy < buffer->height && sx < buffer->width) {
                buffer->data[sy * buffer->width + sx] = colour;
            }
        }
    }
}


int main()
{
    const size_t buffer_width = 224;
    const size_t buffer_height = 256;

    glfwSetErrorCallback(error_callback);
    GLFWwindow *window;

    if (!glfwInit()) {
        return -1;
    }

    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

    window = glfwCreateWindow(640, 480, "Space Invaders", NULL, NULL);
    if (!window) {
        glfwTerminate();
        return -1;
    }

    glfwMakeContextCurrent(window);

    GLenum err = glewInit();
    if(err != GLEW_OK) {
        fprintf(stderr, "Error initializing GLEW.\n");
        return -1;
    }

    int glVersion[2] = {-1, 1};
    glGetIntegerv(GL_MAJOR_VERSION, &glVersion[0]);
    glGetIntegerv(GL_MINOR_VERSION, &glVersion[1]);

    printf("Using OpenGL: %d.%d\n", glVersion[0], glVersion[1]);

    glClearColor(1.0, 0.0, 0.0, 1.0);

    // Graphics buffer
    Buffer buffer;
    buffer.width = buffer_width;
    buffer.height = buffer_height;
    buffer.data = new uint32_t[buffer.width * buffer.height];
    bufferClear(&buffer, 0);

    GLuint bufferTexture;
    glGenTextures(1, &bufferTexture);

    glBindTexture(GL_TEXTURE_2D, bufferTexture);
    glTexImage2D(
        GL_TEXTURE_2D, 0, GL_RGB8,
        buffer.width, buffer.height, 0,
        GL_RGBA, GL_UNSIGNED_INT_8_8_8_8, buffer.data
    );
    // To tell the GPU not the apply any filtering when rendering 
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    // Use edge values when going beyond texture bounds
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);


    static const char* vertexShader =
        "\n"
        "#version 330 \n"
        "noperspective out vec2 TexCoord; \n"
        "\n"
        "void main(void) { \n"
        "\n"
        "   TexCoord.x = (gl_VertexID == 2) ? 2.0 : 0.0; \n"
        "   TexCoord.y = (gl_VertexID == 1) ? 2.0 : 0.0; \n"
        "\n"
        "   gl_Position = vec4(2.0 * TexCoord - 1.0, 0.0, 1.0); \n"
        "} \n";
    
    static const char* fragmentShader = 
        "\n"
        "#version 330 \n"
        "\n"
        "uniform sampler2D buffer; \n"
        "noperspective in vec2 TexCoord; \n"
        "\n"
        "out vec3 outColor; \n"
        "\n"
        "void main(void) { \n"
        "    outColor = texture(buffer, TexCoord).rgb; \n"
        "} \n";
    
    GLuint fullscreenTriangleVao;
    glGenVertexArrays(1, &fullscreenTriangleVao);
    glBindVertexArray(fullscreenTriangleVao);

    // To create into the program for GPU useage
    GLuint shaderID = glCreateProgram();
    
    // For creation of vertex shader
    {
        GLuint shaderVP = glCreateShader(GL_VERTEX_SHADER);

        glShaderSource(shaderVP, 1, &vertexShader, 0);
        glCompileShader(shaderVP);
        validateShader(shaderVP, vertexShader);
        glAttachShader(shaderID, shaderVP);

        glDeleteShader(shaderVP);
    }

    // For creation of fragment shader
    {
        GLuint shaderFP = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(shaderFP, 1, &fragmentShader, 0);
        glCompileShader(shaderFP);
        validateShader(shaderFP, fragmentShader);
        glAttachShader(shaderID, shaderFP);

        glDeleteShader(shaderFP);
    }

    glLinkProgram(shaderID);

    if(!validateProgram(shaderID)) {
        fprintf(stderr, "Error while validating shader. \n");
        glfwTerminate();
        glDeleteVertexArrays(1, &fullscreenTriangleVao);
        delete[] buffer.data;
        return -1;
    }

    glUseProgram(shaderID);

    // Just to attach the shader
    GLint location = glGetUniformLocation(shaderID, "buffer");
    glUniform1i(location, 0);

    glDisable(GL_DEPTH_TEST);
    glActiveTexture(GL_TEXTURE0);

    glBindVertexArray(fullscreenTriangleVao);

    Sprite alienSprite;
    alienSprite.width = 11;
    alienSprite.height = 8;
    alienSprite.data = new uint8_t[88] {
        0,0,1,0,0,0,0,0,1,0,0, // ..@.....@..
        0,0,0,1,0,0,0,1,0,0,0, // ...@...@...
        0,0,1,1,1,1,1,1,1,0,0, // ..@@@@@@@..
        0,1,1,0,1,1,1,0,1,1,0, // .@@.@@@.@@.
        1,1,1,1,1,1,1,1,1,1,1, // @@@@@@@@@@@
        1,0,1,1,1,1,1,1,1,0,1, // @.@@@@@@@.@
        1,0,1,0,0,0,0,0,1,0,1, // @.@.....@.@
        0,0,0,1,1,0,1,1,0,0,0  // ...@@.@@...
    };

    uint32_t clearColour = rgbToUint32(0, 128, 0);

    while(!glfwWindowShouldClose(window)) {
        glClear(GL_COLOR_BUFFER_BIT);
        // bufferClear(&buffer, clearColour);

        bufferSpriteDraw(&buffer, alienSprite, 112, 128, rgbToUint32(128, 0, 0));

        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0,
                        buffer.width, buffer.height,
                        GL_RGBA, GL_UNSIGNED_INT_8_8_8_8,
                        buffer.data);

        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    glfwDestroyWindow(window);
    glfwTerminate();

    delete[] alienSprite.data;
    delete[] buffer.data;
    
    return 0;
}
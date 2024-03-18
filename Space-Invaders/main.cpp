#include <cstdio>
#include <cstdint>
#include <GL/glew.h>
#include <GLFW/glfw3.h>
// g++ -c main.cpp -I/opt/homebrew/Cellar/glfw/3.4/include -Iglew-2.2.0/include
// g++ main.o -o main.exe -L/opt/homebrew/Cellar/glfw/3.4/lib -Lglew-2.2.0/lib -lglfw -lglew -framework OpenGL 

#define GAME_MAX_PROJECTILES 128

bool gameRunning = false; // Check to see if the ESC keys is pressed or the lives is at 0
int moveDirection = 0; // Direction of player movement (-1 for left, 1 for right);
bool firePressed = false;

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

void keyCallBack(GLFWwindow* window, int key, int scancode, int action, int mods) {
    switch (key) {
    case GLFW_KEY_ESCAPE:
        if(action == GLFW_PRESS)
            gameRunning = false;
        break;
    case GLFW_KEY_RIGHT:
        if(action == GLFW_PRESS)
            moveDirection += 1;
        else if(action == GLFW_RELEASE)
            moveDirection -= 1;
        break;
    case GLFW_KEY_LEFT:
        if(action == GLFW_PRESS)
            moveDirection -= 1;
        else if(action == GLFW_RELEASE)
            moveDirection += 1;
        break;
    case GLFW_KEY_SPACE:
        if(action == GLFW_RELEASE)
            firePressed = true;
        break;
    default:
        break;
    }
}

struct Buffer {
    size_t width, height;
    uint32_t* data;
};

struct Sprite {
    size_t width, height;
    uint8_t* data;
};

struct Alien {
    size_t x, y;
    uint8_t type;
};

struct Player {
    size_t x, y;
    size_t life;
};

struct Projectile {
    size_t x, y;
    int direction; // Towards aliens (+), Towards player (-)
};

struct Game {
    size_t width, height;
    size_t numAliens;
    size_t numProjectiles;
    Alien* aliens;
    Player player;
    Projectile projectiles[GAME_MAX_PROJECTILES];
};

struct SpriteAnimation {
    bool loop;
    size_t numFrames;
    size_t frameDuration;
    size_t time;
    Sprite** frames;
};

uint32_t rgbToUint32(uint8_t r, uint8_t g, uint8_t b) {
    return (r << 24) | (g << 16) | (b << 8) | 255;
}

void bufferClear(Buffer* buffer, uint32_t colour) {
    for(size_t i = 0; i < buffer->width * buffer->height; i++) {
        buffer->data[i] = colour;
    }
}

void bufferDrawSprite(Buffer* buffer, const Sprite& sprite, size_t x, size_t y, uint32_t colour) {
    for(size_t xi = 0; xi < sprite.width; xi++) {
        for(size_t yi = 0; yi < sprite.height; yi++) {
            size_t sy = sprite.height - 1 + y - yi;
            size_t sx = x + xi;
            if (sprite.data[yi * sprite.width + xi] && sy < buffer->height && sx < buffer->width) {
                buffer->data[sy * buffer->width + sx] = colour;
            }
        }
    }
}


int main() {
    const size_t buffer_width = 224;
    const size_t buffer_height = 256;

    glfwSetErrorCallback(error_callback);

    if (!glfwInit()) {
        return -1;
    }

    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

    GLFWwindow* window = glfwCreateWindow(640, 480, "Space Invaders", NULL, NULL);
    if (!window) {
        glfwTerminate();
        return -1;
    }

    glfwSetKeyCallback(window, keyCallBack);

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

    glfwSwapInterval(1);

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

    // To create into the program forGPU useage
    GLuint shaderID = glCreateProgram();
    
    // Forcreation of vertex shader
    {
        GLuint shaderVP = glCreateShader(GL_VERTEX_SHADER);

        glShaderSource(shaderVP, 1, &vertexShader, 0);
        glCompileShader(shaderVP);
        validateShader(shaderVP, vertexShader);
        glAttachShader(shaderID, shaderVP);

        glDeleteShader(shaderVP);
    }

    // Forcreation of fragment shader
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

    // Create Alien and Player sprites encoded as a bitmap
    Sprite alienSprite0;
    alienSprite0.width = 11;
    alienSprite0.height = 8;
    alienSprite0.data = new uint8_t[88]
    {
        0,0,1,0,0,0,0,0,1,0,0, // ..@.....@..
        0,0,0,1,0,0,0,1,0,0,0, // ...@...@...
        0,0,1,1,1,1,1,1,1,0,0, // ..@@@@@@@..
        0,1,1,0,1,1,1,0,1,1,0, // .@@.@@@.@@.
        1,1,1,1,1,1,1,1,1,1,1, // @@@@@@@@@@@
        1,0,1,1,1,1,1,1,1,0,1, // @.@@@@@@@.@
        1,0,1,0,0,0,0,0,1,0,1, // @.@.....@.@
        0,0,0,1,1,0,1,1,0,0,0  // ...@@.@@...
    };

    Sprite alienSprite1;
    alienSprite1.width = 11;
    alienSprite1.height = 8;
    alienSprite1.data = new uint8_t[88]
    {
        0,0,1,0,0,0,0,0,1,0,0, // ..@.....@..
        1,0,0,1,0,0,0,1,0,0,1, // @..@...@..@
        1,0,1,1,1,1,1,1,1,0,1, // @.@@@@@@@.@
        1,1,1,0,1,1,1,0,1,1,1, // @@@.@@@.@@@
        1,1,1,1,1,1,1,1,1,1,1, // @@@@@@@@@@@
        0,1,1,1,1,1,1,1,1,1,0, // .@@@@@@@@@.
        0,0,1,0,0,0,0,0,1,0,0, // ..@.....@..
        0,1,0,0,0,0,0,0,0,1,0  // .@.......@.
    };

    Sprite playerSprite;
    playerSprite.width = 11;
    playerSprite.height = 7;
    playerSprite.data = new uint8_t[77]
    {
        0,0,0,0,0,1,0,0,0,0,0, // .....@.....
        0,0,0,0,1,1,1,0,0,0,0, // ....@@@....
        0,0,0,0,1,1,1,0,0,0,0, // ....@@@....
        0,1,1,1,1,1,1,1,1,1,0, // .@@@@@@@@@.
        1,1,1,1,1,1,1,1,1,1,1, // @@@@@@@@@@@
        1,1,1,1,1,1,1,1,1,1,1, // @@@@@@@@@@@
        1,1,1,1,1,1,1,1,1,1,1, // @@@@@@@@@@@
    };

    Sprite projectileSprite;
    projectileSprite.width = 1;
    projectileSprite.height = 3;
    projectileSprite.data = new uint8_t[3]
    {
        1, // @
        1, // @
        1  // @
    };

    SpriteAnimation* alienAnimation = new SpriteAnimation;
    alienAnimation->loop = true;
    alienAnimation->numFrames = 2;
    alienAnimation->frameDuration = 10;
    alienAnimation->time = 0;

    alienAnimation->frames = new Sprite*[2];
    alienAnimation->frames[0] = &alienSprite0;
    alienAnimation->frames[1] = &alienSprite1;

    Game game;
    game.width = buffer_width;
    game.height = buffer_height;
    game.numAliens = 55;
    game.aliens = new Alien[game.numAliens];
    game.numProjectiles = 0;

    game.player.x = 112 - 5;
    game.player.y = 32;
    game.player.life = 3;

    for(size_t yi = 0; yi < 5; yi++) {
        for(size_t xi = 0; xi < 11; xi++) {
            game.aliens[yi * 11 + xi].x = 16 * xi + 20;
            game.aliens[yi * 11 + xi].y = 17 * yi + 128;
        }
    }
    

    uint32_t clearColour = rgbToUint32(0, 128, 0);
    int playerMoveDirection = 1;
    gameRunning = true;

    while(!glfwWindowShouldClose(window) && gameRunning) {
        bufferClear(&buffer, clearColour);

        // Draw Alien Sprites
        for(size_t ai = 0; ai < game.numAliens; ai++) {
            const Alien& alien = game.aliens[ai];
            size_t currentFrame = alienAnimation->time / alienAnimation->frameDuration;
            const Sprite& sprite = *alienAnimation->frames[currentFrame];
            bufferDrawSprite(&buffer, sprite, alien.x, alien.y, rgbToUint32(128, 0, 0));
        }

        // Draw Projectile Sprite
        for (size_t bi = 0; bi < game.numProjectiles; bi++) {
            const Projectile& projectile = game.projectiles[bi];
            const Sprite& sprite = projectileSprite;
            bufferDrawSprite(&buffer, sprite, projectile.x, projectile.y, rgbToUint32(128, 0, 0));
        }
        
        // Draw Player Sprite
        bufferDrawSprite(&buffer, playerSprite, game.player.x, game.player.y, rgbToUint32(128, 0, 0));

        // Update Animations
        alienAnimation->time++;
        if(alienAnimation->time == alienAnimation->numFrames * alienAnimation->frameDuration) {
            if(alienAnimation->loop)
                alienAnimation->time = 0;
            else {
                delete alienAnimation;
                alienAnimation = nullptr;
            }
        }

        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0,
                        buffer.width, buffer.height,
                        GL_RGBA, GL_UNSIGNED_INT_8_8_8_8,
                        buffer.data);

        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

        glfwSwapBuffers(window);

        for (size_t bi = 0; bi < game.numProjectiles;)
        {
            game.projectiles[bi].y += game.projectiles[bi].direction;
            if(game.projectiles[bi].y >= game.height || game.projectiles[bi].y < projectileSprite.height) {
                game.projectiles[bi] = game.projectiles[game.numProjectiles - 1];
                game.numProjectiles--;
                continue;
            }
            bi++;
        }
        

        // Player Movement
        playerMoveDirection = 2 * moveDirection;
        if(playerMoveDirection != 0) {
            if(game.player.x + playerSprite.width + playerMoveDirection >= game.width) {
                game.player.x = game.width - playerSprite.width;
            }
            else if ((int)game.player.x + playerMoveDirection <= 0) {
                game.player.x = 0;
            }
            else
                game.player.x += playerMoveDirection;
        }

        if(firePressed && game.numProjectiles < GAME_MAX_PROJECTILES) {
            game.projectiles[game.numProjectiles].x = game.player.x + playerSprite.width / 2;
            game.projectiles[game.numProjectiles].y = game.player.y + playerSprite.height;
            game.projectiles[game.numProjectiles].direction = 2;
            game.numProjectiles++;
        }
        firePressed = false;

        glfwPollEvents();
    }

    glfwDestroyWindow(window);
    glfwTerminate();

    glDeleteVertexArrays(1, &fullscreenTriangleVao);

    delete[] alienSprite0.data;
    delete[] alienSprite1.data;
    delete[] alienAnimation->frames;
    delete alienAnimation;

    delete[] buffer.data;
    delete[] game.aliens;
    
    return 0;
}
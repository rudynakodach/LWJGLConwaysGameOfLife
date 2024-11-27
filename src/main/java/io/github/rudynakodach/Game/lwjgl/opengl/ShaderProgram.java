package io.github.rudynakodach.Game.lwjgl.opengl;

import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
    private final int programId;

    public ShaderProgram() {
        this.programId = GL20.glCreateProgram();
        if(programId == 0) {
            throw new RuntimeException("Failed to create a shader program");
        }
    }

    public void attachVertexShader(String filename) {
        attachShader(filename, GL_VERTEX_SHADER);
    }

    public void attachFragmentShader(String filename) {
        attachShader(filename, GL_FRAGMENT_SHADER);
    }

    private void attachShader(String filepath, int shaderType) {
        String shaderCode = readFile(filepath);

        int shaderId = glCreateShader(shaderType);
        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if(glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            String err = glGetShaderInfoLog(shaderId);
            throw new RuntimeException("Shader compilation failed for shader " + filepath + ". \n" + err);
        }

        glAttachShader(programId, shaderId);
        glDeleteShader(shaderId);
    }

    public void link() {
        glLinkProgram(programId);

        if(glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            String err = glGetProgramInfoLog(programId);
            throw new RuntimeException("Shader link failed. " + err);
        }

        glValidateProgram(programId);
        if(glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            String err = glGetProgramInfoLog(programId);
            throw new RuntimeException("Shader validation failed. " + err);
        }
    }

    public void use() {
        GL20.glUseProgram(programId);
    }

    public void detach() {
        GL20.glUseProgram(0);
    }

    public void cleanup() {
        GL20.glDeleteProgram(programId);
    }

    private String readFile(String filename) {
        ClassLoader classLoader = ShaderProgram.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(filename)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Shader file not found: " + filename);
            }
            // Convert the InputStream to a String
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load shader file: " + filename, e);
        }
    }
}

package com.example.javasimplifier;

public class RequestBody {
    public String instructions;
    public String codeToRefactor;
    public String languageAndVersion;
    public String role;
    public String model;
    public int maxTokens;

    public RequestBody(String instructions, String languageAndVersion, String codeToRefactor, String role, String model, int maxTokens) {
        this.instructions = instructions;
        this.codeToRefactor = codeToRefactor;
        this.languageAndVersion = languageAndVersion;
        this.role = role;
        this.model = model;
        this.maxTokens = maxTokens;
    }
}
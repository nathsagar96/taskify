package dev.sagar.taskify.domain;

public record ErrorResponse(int status, String message, String details) {}

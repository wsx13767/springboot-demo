package com.example.sion.todo.controller.todo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "v2", description = "you only check todo list")
@RequestMapping("/v2/todo")
@RestController
public class TodoV2Controller {
    @GetMapping
    @Operation(summary = "get all todo list")
    public String todos() {
        return "NOT FOUND";
    }
}

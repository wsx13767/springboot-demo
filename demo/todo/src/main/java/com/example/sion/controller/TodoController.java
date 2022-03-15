package com.example.sion.controller;

import com.example.sion.model.TodoEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/todo")
@RestController
public class TodoController {
    private List<TodoEvent> todos = new ArrayList<>();

    @GetMapping
    public List<TodoEvent> todo() {
        return todos;
    }

    @GetMapping("{title}")
    public List<TodoEvent> todo(@PathVariable String title) {
        return todos.stream().filter(todo -> todo.getTitle().equalsIgnoreCase(title))
                .collect(Collectors.toList());
    }

    @PostMapping
    public String insert(TodoEvent event) {
        if (todos.stream().anyMatch(todoEvent -> todoEvent.getTitle().equalsIgnoreCase(event.getTitle()))) {
            throw new RuntimeException("");
        }

        return "success";
    }
}

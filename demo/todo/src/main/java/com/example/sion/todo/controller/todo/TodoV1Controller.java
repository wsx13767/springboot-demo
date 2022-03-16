package com.example.sion.todo.controller.todo;

import com.example.sion.todo.model.TodoEvent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Tag(name = "v1", description = "you can CRUD todo list")
@RequestMapping("/v1/todo")
@RestController
public class TodoV1Controller {
    private List<TodoEvent> todos = new ArrayList<>();
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();

    @Deprecated
    @Operation(summary = "get all todo list")
    @GetMapping
    public List<TodoEvent> todo() {
        try {
            readLock.lock();
            return todos;
        } finally {
            readLock.unlock();
        }
    }

    @Operation(summary = "get todo list by title")
    @GetMapping("/{title}")
    public List<TodoEvent> todo(@PathVariable String title) {
        try {
            readLock.lock();
            return todos.stream().filter(todo -> todo.getTitle().equalsIgnoreCase(title))
                    .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    @Operation(summary = "create todo list")
    @PostMapping
    public String insert(@RequestBody TodoEvent event) {

        if (event == null || !StringUtils.hasText(event.getTitle())) {
            throw new RuntimeException("title 不能為空");
        }

        try {
            writeLock.lock();
            if (todos.stream().anyMatch(todoEvent -> todoEvent.getTitle().equalsIgnoreCase(event.getTitle()))) {
                throw new RuntimeException("已有相同待辦事項");
            }
            todos.add(event);

        } finally {
            writeLock.unlock();
        }


        return "success";
    }

    @Operation(summary = "update todo list")
    @PutMapping
    public List<TodoEvent> update(@RequestBody TodoEvent todoEvent) {
        try {
            writeLock.lock();
            if (todoEvent != null && todos.stream().anyMatch(todo -> todo.getTitle().equalsIgnoreCase(todoEvent.getTitle()))) {
                todos = todos.stream().filter(todo -> !todo.getTitle().equalsIgnoreCase(todoEvent.getTitle())).collect(Collectors.toList());
                todos.add(todoEvent);
                return todos;
            }
        } finally {
            writeLock.unlock();
        }
        throw new RuntimeException("NOT FOUND");
    }

    @Operation(summary = "remove todo list")
    @DeleteMapping
    public List<TodoEvent> remove(@PathVariable String title) {
        try {
            writeLock.lock();
            if (todos.stream().anyMatch(todo -> todo.getTitle().equalsIgnoreCase(title))) {
                todos = todos.stream()
                        .filter(todo -> !todo.getTitle().equalsIgnoreCase(title)).collect(Collectors.toList());
                return todos;
            }
        } finally {
            writeLock.unlock();
        }

        throw new RuntimeException("NOT FOUND");
    }
}

package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @Autowired(required = false)
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public Collection<Mpa> findAll() {
        log.info("Получен запрос GET к эндпоинту: /mpa");
        return mpaService.getAllMpa();
    }

    @GetMapping("/{id}")
    public Mpa findGenre(@PathVariable String id) {
        log.info("Получен запрос GET к эндпоинту: /mpa/{}", id);
        return mpaService.getMpa(id);
    }
}

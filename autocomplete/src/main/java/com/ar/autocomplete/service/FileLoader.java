package com.ar.autocomplete.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.ar.autocomplete.entity.Name;
import com.ar.autocomplete.repo.NameRepo;

@Component
public class FileLoader {

    private static final String FILE_PATH = "BoyNames.txt";

    @Autowired
    private NameRepo nameRepository;


    @EventListener(ApplicationReadyEvent.class)
    public void loadFileToDB() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(FILE_PATH)))) {
            List<String> names = br.lines()
                                   .map(String::trim)
                                   .filter(line -> !line.isEmpty())
                                   .collect(Collectors.toList());

            List<Name> nameEntities = names.stream()
                                           .map(name -> {
                                               Name entity = new Name();
                                               entity.setName(name);
                                               return entity;
                                           }).collect(Collectors.toList());
            nameRepository.saveAll(nameEntities);
        } catch (IOException e) {
            throw new RuntimeException("Error loading file: " + FILE_PATH, e);
        }
    }
}

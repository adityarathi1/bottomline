package com.ar.autocomplete.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ar.autocomplete.entity.Name;
import com.ar.autocomplete.repo.NameRepo;

@Service
public class TrieService {

    private final TrieNode root = new TrieNode();
    private final Map<String, String> originalCaseMap = new HashMap<>();
    private static boolean isTrieLoaded = false;

    @Autowired
    private NameRepo nameRepository;

    public static class TrieNode {
        private final Map<Character, TrieNode> children = new HashMap<>();
        private boolean isEndOfWord;
    }


    public void loadNamesToTrie() {
    	if(!isTrieLoaded) {
    		System.out.println("Loading names into Trie...");
            List<String> names = nameRepository.findAll()
                                               .stream()
                                               .map(Name::getName)
                                               .collect(Collectors.toList());
            names.forEach(this::insertName);
            System.out.println("Loaded " + names.size());
            isTrieLoaded = true;
    	}
    	
    }
    

    public void insertName(String name) {
        String lowerCaseName = name.toLowerCase();
        originalCaseMap.put(lowerCaseName, name);

        TrieNode current = root;
        for (char c : lowerCaseName.toCharArray()) {
            current.children.putIfAbsent(c, new TrieNode());
            current = current.children.get(c);
        }
        current.isEndOfWord = true;
    }

    public List<String> searchByPrefix(String prefix) {
    	loadNamesToTrie();
        TrieNode current = root;
        String lowerCasePrefix = prefix.toLowerCase();

        for (char c : lowerCasePrefix.toCharArray()) {
            current = current.children.get(c);
            if (current == null) {
                return Collections.emptyList();
            }
        }

        List<String> results = collectWords(current, lowerCasePrefix);
        return results.stream()
                      .map(originalCaseMap::get)
                      .collect(Collectors.toList());
    }

    private List<String> collectWords(TrieNode node, String prefix) {
        List<String> results = new ArrayList<>();
        if (node.isEndOfWord) {
            results.add(prefix);
        }
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            results.addAll(collectWords(entry.getValue(), prefix + entry.getKey()));
        }
        return results;
    }
}


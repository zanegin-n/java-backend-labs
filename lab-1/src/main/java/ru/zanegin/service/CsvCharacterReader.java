package ru.zanegin.service;

import ru.zanegin.model.RmCharacter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Vector;

public class CsvCharacterReader {

    public Vector<RmCharacter> readFromResource(String fileName) {
        Vector<RmCharacter> characters = new Vector<>();
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(fileName);
        
        if (resourceAsStream == null) {
            throw new RuntimeException("Файл не найден в ресурсах: " + fileName);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8))) {
            String line = br.readLine(); // Пропускаем заголовок
            while ((line = br.readLine()) != null) {
                characters.add(parseLine(line));
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
            throw new RuntimeException("Не удалось прочитать файл " + fileName, e);
        }
        return characters;
    }

    private RmCharacter parseLine(String line) {
        String[] parts = line.split(",", -1);
        RmCharacter character = new RmCharacter();
        character.setId(Integer.parseInt(parts[0].trim()));
        character.setName(parts[1].trim());
        character.setStatus(parts[2].trim());
        character.setSpecies(parts[3].trim());
        character.setType(parts[4].trim());
        character.setGender(parts[5].trim());
        character.setOriginName(parts[6].trim());
        character.setLocationName(parts[7].trim());
        character.setCreated(parts[8].trim());
        return character;
    }
}
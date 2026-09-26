package ru.zanegin.service;

import ru.zanegin.model.RmCharacter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CsvCharacterRepository implements CharacterRepository {
    private final String filePath;
    private final List<RmCharacter> characters;

    public CsvCharacterRepository(String filePath) {
        this.filePath = filePath;
        this.characters = loadCharacters();
    }

    private List<RmCharacter> loadCharacters() {
        List<RmCharacter> list = new ArrayList<>();
        if (!Files.exists(Paths.get(filePath))) {
            return list;
        }
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    list.add(parseLine(line));
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
            throw new RuntimeException("Не удалось прочитать файл " + filePath, e);
        }
        return list;
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

    @Override
    public List<RmCharacter> findAll() {
        return new ArrayList<>(characters);
    }

    @Override
    public Optional<RmCharacter> findById(int id) {
        return characters.stream().filter(c -> c.getId() == id).findFirst();
    }

    @Override
    public void save(RmCharacter character) {
        int maxId = characters.stream().mapToInt(RmCharacter::getId).max().orElse(0);
        character.setId(maxId + 1);
        characters.add(character);
        persistToFile();
    }

    @Override
    public void update(RmCharacter character) {
        Optional<RmCharacter> existing = findById(character.getId());
        if (existing.isEmpty()) {
            throw new RuntimeException("Персонаж с id " + character.getId() + " не найден для обновления");
        }
        RmCharacter toUpdate = existing.get();
        toUpdate.setName(character.getName());
        toUpdate.setStatus(character.getStatus());
        toUpdate.setSpecies(character.getSpecies());
        toUpdate.setType(character.getType());
        toUpdate.setGender(character.getGender());
        toUpdate.setOriginName(character.getOriginName());
        toUpdate.setLocationName(character.getLocationName());
        toUpdate.setCreated(character.getCreated());
        persistToFile();
    }

    @Override
    public void deleteById(int id) {
        boolean removed = characters.removeIf(c -> c.getId() == id);
        if (!removed) {
            throw new RuntimeException("Персонаж с id " + id + " не найден для удаления");
        }
        persistToFile();
    }

    private void persistToFile() {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(filePath), StandardCharsets.UTF_8)) {
            bw.write("id,name,status,species,type,gender,origin/name,location/name,created\n");
            for (RmCharacter character : characters) {
                bw.write(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        character.getId(),
                        character.getName(),
                        character.getStatus(),
                        character.getSpecies(),
                        character.getType(),
                        character.getGender(),
                        character.getOriginName(),
                        character.getLocationName(),
                        character.getCreated()));
            }
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
            throw new RuntimeException("Не удалось записать файл " + filePath, e);
        }
    }
}
package ru.zanegin;

import ru.zanegin.model.RmCharacter;
import ru.zanegin.service.*;
import java.util.Vector;

public class Main {
    public static void main(String[] args) {
        // --- ОСНОВНОЕ ЗАДАНИЕ ---
        String inputFileName = "characters.csv";
        String outputFileName = "sorted_characters.csv";

        CsvCharacterReader reader = new CsvCharacterReader();
        Vector<RmCharacter> characters = reader.readFromResource(inputFileName);

        CharacterSorter sorter = new CharacterSorter();
        sorter.sortByStatus(characters);

        CsvCharacterWriter writer = new CsvCharacterWriter();
        writer.writeToCsv(outputFileName, characters);
        
        System.out.println(" Основное задание выполнено: данные отсортированы и записаны в " + outputFileName);

        // --- ДОПОЛНИТЕЛЬНОЕ ЗАДАНИЕ (CRUD) ---
        String testDbFile = "crud_test_characters.csv";
        
        // 1. Инициализируем тестовый файл данными из ресурса
        Vector<RmCharacter> initialData = reader.readFromResource(inputFileName);
        writer.writeToCsv(testDbFile, initialData);
        
        // 2. Создаем репозиторий
        CsvCharacterRepository repository = new CsvCharacterRepository(testDbFile);
        
        // CREATE
        RmCharacter newCharacter = new RmCharacter();
        newCharacter.setName("Vladimir Zanegin");
        newCharacter.setStatus("Alive");
        newCharacter.setSpecies("Human");
        newCharacter.setType("Student");
        newCharacter.setGender("Male");
        newCharacter.setOriginName("Earth");
        newCharacter.setLocationName("University");
        newCharacter.setCreated("2026-09-14T00:00:00.000Z");
        repository.save(newCharacter);
        System.out.println(" CREATE: Добавлен новый персонаж с ID: " + newCharacter.getId());

        // READ
        repository.findById(newCharacter.getId()).ifPresent(c -> 
            System.out.println(" READ: Найден персонаж: " + c.getName() + ", статус: " + c.getStatus())
        );

        // UPDATE
        newCharacter.setStatus("Dead");
        repository.update(newCharacter);
        System.out.println(" UPDATE: Статус персонажа обновлен на: " + newCharacter.getStatus());

        // DELETE
        repository.deleteById(newCharacter.getId());
        System.out.println(" DELETE: Персонаж с ID " + newCharacter.getId() + " удален.");
        
        System.out.println(" Дополнительное задание (CRUD) успешно продемонстрировано.");
    }
}
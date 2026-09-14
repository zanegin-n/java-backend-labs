package ru.zanegin.service;

import ru.zanegin.model.RmCharacter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;

public class CsvCharacterWriter {

    public void writeToCsv(String filePath, Vector<RmCharacter> characters) {
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
package ru.zanegin.service;

import ru.zanegin.model.RmCharacter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class CharacterSorter {

    public void sortByStatus(Vector<RmCharacter> characters) {
        Comparator<RmCharacter> statusComparator = Comparator.comparingInt(c -> getStatusPriority(c.getStatus()));
        Collections.sort(characters, statusComparator);
    }

    private int getStatusPriority(String status) {
        if (status == null) {
            return 4;
        }
        return switch (status) {
            case "Alive" -> 1;
            case "Dead" -> 2;
            case "unknown" -> 3;
            default -> 4;
        };
    }
}
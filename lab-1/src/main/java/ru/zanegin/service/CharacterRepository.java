package ru.zanegin.service;

import ru.zanegin.model.RmCharacter;
import java.util.List;
import java.util.Optional;

public interface CharacterRepository {
    List<RmCharacter> findAll();
    Optional<RmCharacter> findById(int id);
    void save(RmCharacter character);
    void update(RmCharacter character);
    void deleteById(int id);
}
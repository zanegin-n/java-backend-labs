package ru.zanegin.model;

import java.util.Objects;

public class RmCharacter {
    private int id;
    private String name;
    private String status;
    private String species;
    private String type;
    private String gender;
    private String originName;
    private String locationName;
    private String created;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getOriginName() { return originName; }
    public void setOriginName(String originName) { this.originName = originName; }
    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }
    public String getCreated() { return created; }
    public void setCreated(String created) { this.created = created; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RmCharacter that = (RmCharacter) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
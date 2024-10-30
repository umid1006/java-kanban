package user;

import java.util.Objects;

public class User {

    private int id;
    private String name;

    public User(String name) {
        this.name = name;
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() { // Add getter for name
        return name;
    }

    public void setName(String name) { // Add setter for name
        this.name = name;
    }

    @Override
    public boolean equals(Object o) { // Add toString method for easy printing/debugging
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() { // Add toString method for easy printing/debugging
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}


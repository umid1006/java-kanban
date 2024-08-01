package model;

public class Subtask extends Task {

    private final int epicId;


    public Subtask(String name, String description, Status status, int epicId) { // Corrected argument name (optional)
        super(name, description, status); // Assuming ID is set in Task constructor
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "model.Subtask{" +
                "id=" + getId() +  // Use getId() from model.Task
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + "NEW" + '\'' +
                ", epicId=" + epicId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + epicId;
        return result;
    }
}


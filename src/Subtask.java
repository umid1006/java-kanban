public class Subtask extends Task {

    protected int epicId;

    public Subtask(String name, String description, String status, int epicId) { // Corrected argument name (optional)
        super(name, description, status); // Assuming ID is set in Task constructor
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +  // Use getId() from Task
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", epicId=" + epicId +
                '}';
    }
}

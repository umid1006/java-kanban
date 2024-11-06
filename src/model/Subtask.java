package model;
import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    private final int epicId;
    private long duration;
    private LocalDateTime startTime;


    public Subtask(String name, String description, Status status, int epicId) { // Corrected argument name (optional)
        super(name, description, status); // Assuming ID is set in Task constructor
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration.toMinutes();
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null && duration != 0) {
            return startTime.plus(Duration.ofMinutes(duration));
        } else {
            return null;
            // Or handle the case where startTime or duration is null
        }
    }

    @Override
    public String toString() {
        return "model.Subtask{" +
                "id=" + getId() +  // Use getId() from model.Task
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + "NEW" + '\'' +
                ", duration=" + duration +
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

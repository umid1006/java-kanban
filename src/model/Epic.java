package model;

import java.util.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class Epic extends Task {

    private ArrayList<Integer> subtaskIds = new ArrayList<>();
    public Map<Integer, Subtask> subtasks = new HashMap<>();
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public void setSubtaskIds(List<Integer> newSubtaskIds) {
        this.subtaskIds = new ArrayList<>(newSubtaskIds);
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return new ArrayList<>(subtaskIds);
    }

    public Status recalculateEpicStatus() {
        if (subtasks.isEmpty()) {
            return Status.NEW;
        }

        for (Map.Entry<Integer, Subtask> entry : subtasks.entrySet()) {
            Subtask subtask = entry.getValue();
            if (!Objects.equals(subtask.getStatus(), Status.DONE)) {
                return Status.IN_PROGRESS;
            }
        }

        return Status.DONE;
    }

    public void updateFields() {
        List<Subtask> subtaskList = new ArrayList<>();
        for (Integer subtaskId : getSubtaskIds()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null) {
                subtaskList.add(subtask);
            }
        }

        if (subtaskList.isEmpty()) {
            this.duration = Duration.ZERO;
            this.startTime = null;
            this.endTime = null;
        } else {
            this.duration = Duration.ofMillis(subtaskList.stream()
                    .mapToLong(Task::getDuration)
                    .sum());

            this.startTime = subtaskList.stream()
                    .map(Task::getStartTime)
                    .filter(Objects::nonNull)
                    .min(LocalDateTime::compareTo)
                    .orElse(null);

            this.endTime = subtaskList.stream()
                    .map(Task::getEndTime)
                    .filter(Objects::nonNull)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);
        }
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        for (Integer subtaskId : subtaskIds) {
            hash = 31 * hash + subtaskId.hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;

        Epic other = (Epic) obj;
        if (subtaskIds.size() != other.subtaskIds.size()) return false;

        for (int i = 0; i < subtaskIds.size(); i++) {
            if (!Objects.equals(subtaskIds.get(i), other.subtaskIds.get(i))) {
                return false;
            }
    }
        return true;
}

    @Override
    public long getDuration() {
        return duration.toMinutes();
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

}
package model;

import java.util.*;

public class Epic extends Task {

    private ArrayList<Integer> subtaskIds = new ArrayList<>();
    public Map<Integer, Subtask> subtasks = new HashMap<>();

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
}
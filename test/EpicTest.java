import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {


    @Test
    void recalculateEpicStatus_newAndDone() {
        Epic epic = new Epic("Epic", "Description", Status.NEW);
        epic.subtasks.put(1, new Subtask("Subtask 1", "Desc", Status.NEW, 1));
        epic.subtasks.put(2, new Subtask("Subtask 2", "Desc", Status.DONE, 1));
        assertEquals(Status.IN_PROGRESS, epic.recalculateEpicStatus());
    }

    @Test
    void recalculateEpicStatus_inProgress() {
        Epic epic = new Epic("Epic", "Description", Status.NEW);
        epic.subtasks.put(1, new Subtask("Subtask 1", "Desc", Status.IN_PROGRESS, 1));
        assertEquals(Status.IN_PROGRESS, epic.recalculateEpicStatus());
    }
}
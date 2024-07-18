import java.lang.reflect.Array;
import java.util.ArrayList;

public class Epic extends Task {

    protected ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description, String status) {
        super(name, description, status);
    }

    @Override
    public boolean isEpic() {
        return true;
    }

    @Override
    public void setStatus(String status) {
        // Disallow manual status change for epics
        throw new UnsupportedOperationException("Changing epic status is not allowed");
    }
}
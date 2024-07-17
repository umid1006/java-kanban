public class Epic extends Task {

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
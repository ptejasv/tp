package seedu.duke.task;

import seedu.duke.exception.DukeException;
import seedu.duke.ui.Ui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskList {
    // TODO: Implement serialization/deserialization
    // TODO: Reorder methods

    private List<Task> taskList;

    public TaskList() {
        taskList = new ArrayList<>();
    }

    public TaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public void addTask(Task newTask) {
        taskList.add(newTask);
    }

    public void clearTaskList() {
        taskList.clear();
    }

    public boolean isEmpty() {
        return taskList.isEmpty();
    }

    public int getSize() {
        return taskList.size();
    }

    public int getNumberOfPendingTasks() {
        int count = 0;
        for (Task task : taskList) {
            if (!task.isDone()) {
                count++;
            }
        }
        return count;
    }

    public Task getTask(int index) {
        return taskList.get(index);
    }

    public void deleteTask(int index) {
        taskList.remove(index);
    }

    public void markTaskAsDone(int index) {
        taskList.get(index).setDone();
    }

    public TaskList filterTasksByKeyword(String keyword) {
        return new TaskList(taskList.stream()
                .filter(task -> task.getTitle().toLowerCase().contains(keyword))
                .collect(Collectors.toList()));
    }

    public TaskList filterTasksByPeriod(String period) {
        return new TaskList(taskList.stream()
                .filter(task -> task.getDayOfTheWeek().toLowerCase().contains(period))
                .collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            s.append(Ui.PADDING).append(i + 1).append(". ").append(task).append(System.lineSeparator());
        }
        return s.toString();
    }

    /**
     * Serializes the task list into its file data storage format.
     *
     * @return the serialized task list
     */
    public String serialize() {
        StringBuilder data = new StringBuilder();
        for (Task task : taskList) {
            data.append(task.serialize()).append(System.lineSeparator());
        }
        return data.toString();
    }

    /**
     * Filters out strings representing task data from a list of strings and deserializes
     * them into a list of task objects.
     *
     * @param data the list of strings
     * @return the list of task objects
     * @throws DukeException when there is task data that is not deserializable
     */
    public static List<Task> deserialize(List<String> data) throws DukeException {
        List<Task> taskList = new ArrayList<>();
        for (String entry : data) {
            if (entry.charAt(0) == 'T') {
                taskList.add(Task.deserialize(entry));
            }
        }
        return taskList;
    }
}

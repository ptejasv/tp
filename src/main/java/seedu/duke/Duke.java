package seedu.duke;

import java.io.IOException;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import seedu.duke.logic.commands.Command;
import seedu.duke.model.lesson.LessonList;
import seedu.duke.logic.parser.Parser;
import seedu.duke.model.module.FullModuleList;
import seedu.duke.model.module.ModuleList;
import seedu.duke.storage.Storage;
import seedu.duke.model.task.TaskList;
import seedu.duke.commons.core.Messages;
import seedu.duke.ui.Ui;

public class Duke {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final Ui ui;
    private final Storage storage;
    private TaskList taskList;
    private LessonList lessonList;
    private ModuleList moduleList;
    private FullModuleList fullModuleList;

    /**
     * The constructor method. Initializes ui and storage objects.
     * Fetches task and lesson data from a saved file if it exists,
     * otherwise creates a new task and lesson objects.
     */
    public Duke() {
        initializeLogger();
        ui = new Ui();
        storage = new Storage();

        try {
            List<String> taskData = storage.loadData(Storage.PATH_TO_TASK_FILE);
            // taskList = new TaskList(TaskList.deserialize(ui, taskData));
            storage.saveData(taskList);
            ui.printMessage(Messages.SUCCESS_RETRIEVING_TASK_DATA);
            LOGGER.info(Messages.SUCCESS_RETRIEVING_TASK_DATA);
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
            ui.printMessage(e.getMessage());
            storage.createNewDataFile(ui, "TASK");
            taskList = new TaskList();
        }

        try {
            List<String> lessonData = storage.loadData(Storage.PATH_TO_LESSON_FILE);
            lessonList = new LessonList(LessonList.deserialize(ui, lessonData));
            storage.saveData(lessonList);
            ui.printMessage(Messages.SUCCESS_RETRIEVING_LESSON_DATA);
            LOGGER.info(Messages.SUCCESS_RETRIEVING_LESSON_DATA);
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
            ui.printMessage(e.getMessage());
            storage.createNewDataFile(ui, "LESSON");
            lessonList = new LessonList();
        }

        try {
            List<String> moduleData = storage.loadData(Storage.PATH_TO_MODULE_FILE);
            // moduleList = new ModuleList(ModuleList.deserialize(ui, moduleData));
            storage.saveData(moduleList);
            ui.printMessage(Messages.SUCCESS_RETRIEVING_MODULE_DATA);
            LOGGER.info(Messages.SUCCESS_RETRIEVING_MODULE_DATA);
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
            ui.printMessage(e.getMessage());
            storage.createNewDataFile(ui, "MODULE");
            moduleList = new ModuleList();
        }
    }

    /**
     * The main routine of the program. Asks for user input, parses
     * to the corresponding command and executes it.
     */
    public void startProgram() {
        LOGGER.info("Executing main routine.");
        boolean isExit = false;
        while (!isExit) {
            try {
                String userResponse = ui.readUserResponse();
                Command command = Parser.parse(userResponse);
                command.execute(ui, storage, taskList, lessonList, moduleList);
                isExit = command.isExit();
            } catch (DukeException | IOException e) {
                LOGGER.warning("Invalid command.");
                ui.printMessage(e.getMessage());
            }
        }
        LOGGER.info("Main routine has ended.");
    }

    public void run() {
        ui.printGreeting();
        this.startProgram();
        ui.printExit();
    }

    /** The main method. Creates an instance of Duke and run it. */
    public static void main(String[] args) {
        Duke duke = new Duke();
        duke.run();
    }

    private void initializeLogger() {
        LogManager.getLogManager().reset();
        LOGGER.setLevel(Level.ALL);

        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.SEVERE);
        LOGGER.addHandler(ch);

        try {
            FileHandler fh = new FileHandler("duke.log");
            fh.setLevel(Level.FINE);
            LOGGER.addHandler(fh);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, Messages.ERROR_FILE_LOGGER, e);
        }
    }
}

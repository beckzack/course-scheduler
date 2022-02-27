/**
 * MIT License
 *
 * Copyright (c) 2022 Zackary Beck
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package cs2263_hw03;

import javafx.application.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.*;

import com.google.gson.*;

import java.util.*;
import java.io.*;

/**
 * A Java class that initializes and provides the functionality for a course editor GUI. Allows the user to enter information
 * about any course. The user is given the option to display the courses filtered by department or not filtered.
 */
public class CourseProcessor extends Application {

    private ObservableList<Course> coursesList = FXCollections.observableArrayList();
    private TableView<Course> tableView;

    /**
     * Start method for the application. Adds all buttons and nodes to the scene, which is then displayed on the stage
     *
     * @param stage  The window where all buttons and text fields will be shown
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("Course Processor");

        HBox addCourseBox = new HBox();
        addCourseControls(addCourseBox);
        addCourseBox.setAlignment(Pos.TOP_CENTER);

        VBox courseDisplay = new VBox();
        addCourseDisplay(courseDisplay);
        courseDisplay.setAlignment(Pos.CENTER);

        HBox saveAndLoadBox = new HBox();
        addSaveAndLoadButtons(saveAndLoadBox);
        saveAndLoadBox.setAlignment(Pos.BOTTOM_CENTER);

        VBox vBox = new VBox(addCourseBox, courseDisplay, saveAndLoadBox);

        Scene scene = new Scene(vBox, 800, 600);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * Adds the department combo box and course number, name, and hours text fields to the provided HBox. This method
     * also provides the functionality for an enter button. When pressed, it will ensure all fields are filled in and then
     * instantiate a course with the user-given parameters. Finally, it adds that course to the full list of courses.
     *
     * @param hBox  HBox which houses all the buttons and text fields
     */
    private void addCourseControls(HBox hBox) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPromptText("Department");
        comboBox.getItems().addAll(Course.getDepartments());
        HBox.setMargin(comboBox, new Insets(20, 10, 20, 10));

        TextField courseNumber = new TextField();
        courseNumber.setPromptText("Course Number");
        HBox.setMargin(courseNumber, new Insets(20, 10, 20, 10));

        TextField courseName = new TextField();
        courseName.setPromptText("Course Name");
        HBox.setMargin(courseName, new Insets(20, 10, 20, 10));

        TextField creditHours = new TextField();
        creditHours.setPromptText("Number of Credits");
        HBox.setMargin(creditHours, new Insets(20, 10, 20, 10));

        Button enterButton = new Button("Enter");
        HBox.setMargin(enterButton, new Insets(20, 10, 20 ,10));

        enterButton.setOnAction(e -> {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            boolean noErrors = true;

            if (comboBox.getSelectionModel().getSelectedItem() == null) {
                noErrors = false;
                errorAlert.setContentText("Please select a department");
                errorAlert.showAndWait();
            }
            if (courseNumber.getText().isEmpty()) {
                noErrors = false;
                errorAlert.setContentText("Please enter a course number");
                errorAlert.showAndWait();
            }
            if (courseName.getText().isEmpty()) {
                noErrors = false;
                errorAlert.setContentText("Please enter a course name");
                errorAlert.showAndWait();
            }
            if (creditHours.getText().isEmpty()) {
                noErrors = false;
                errorAlert.setContentText("Please enter the credit hours");
                errorAlert.showAndWait();
            }
            if (noErrors) {
                String department = comboBox.getSelectionModel().getSelectedItem();
                int number = Integer.parseInt(courseNumber.getText());
                String name = courseName.getText();
                int hours = Integer.parseInt(creditHours.getText());

                Course course = new Course(department, number, name, hours);
                coursesList.add(course);
                tableView.getItems().add(course);
            }
        });

        hBox.getChildren().addAll(comboBox, courseNumber, courseName, creditHours, enterButton);
    }

    /**
     * Adds the save, load, and exit buttons to the provided HBox. Save button serializes the current catalog of courses
     * for long term storage, while load deserializes objects from the object.json file. The exit button asks the user
     * to confirm if they want to exit, if yes, the application closes.
     *
     * @param hBox  HBox that houses the three buttons
     */
    private void addSaveAndLoadButtons(HBox hBox) {
        Button saveButton = new Button("Save Courses");
        HBox.setMargin(saveButton, new Insets(20, 10, 20, 10));

        saveButton.setOnAction(e -> {
            try {
                Gson gson = new Gson();
                File saveFile = new File("object.json");
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(saveFile, true));

                bufferedWriter.write(gson.toJson(coursesList));
                bufferedWriter.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        Button loadButton = new Button("Load Courses");
        HBox.setMargin(loadButton, new Insets(20, 10, 20, 10));

        loadButton.setOnAction(e -> {
            Gson gson = new Gson();
            File loadFile = new File("object.json");
            try {
                Scanner scanner = new Scanner(loadFile);
                Course[] loadedCourses = gson.fromJson(scanner.nextLine(), Course[].class);
                coursesList.addAll(loadedCourses);

                tableView.getItems().clear();
                tableView.getItems().addAll(coursesList);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });

        Button exitButton = new Button("Exit");
        HBox.setMargin(exitButton, new Insets(20, 10, 20, 10));

        exitButton.setOnAction(e -> {
            Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
            exitAlert.setContentText("Are you sure you want to exit?");

            Optional<ButtonType> buttonPressed = exitAlert.showAndWait();
            if (buttonPressed.get() == ButtonType.OK) {
                Stage stage = (Stage) exitButton.getScene().getWindow();
                stage.close();
            }
        });

        hBox.getChildren().addAll(saveButton, loadButton, exitButton);
    }

    /**
     * Adds the display by department combo box, display all, and the table to house the course information to the provided
     * VBox. Display by department changes the table to only contain courses in the specified department. Display all changes
     * the table to show all courses that the user entered or loaded in. The table consists of 4 columns that get the course
     * department code, number, name, and credit hours respectively.
     *
     * @param vBox  VBox that houses the two display nodes and the table of courses
     */
    private void addCourseDisplay(VBox vBox) {
        ComboBox<String> displayByDepartment = new ComboBox<>();
        displayByDepartment.setPromptText("Display (Dept)");
        displayByDepartment.getItems().addAll(Course.getDepartmentCodes());
        HBox.setMargin(displayByDepartment, new Insets(20, 10, 20, 10));

        Button displayAllCourses = new Button("Display All");
        HBox.setMargin(displayAllCourses, new Insets(20, 10, 20, 10));

        HBox displayButtons = new HBox(displayByDepartment, displayAllCourses);
        displayButtons.setAlignment(Pos.CENTER);

        tableView = new TableView<>();
        VBox.setMargin(tableView, new Insets(20, 20, 20, 20));

        TableColumn<Course, String> column1 = new TableColumn<>("Department Code");
        column1.setCellValueFactory(new PropertyValueFactory<>("departmentCode"));
        column1.setPrefWidth(115);
        tableView.getColumns().add(column1);

        TableColumn<Course, Integer> column2 = new TableColumn<>("Course Number");
        column2.setCellValueFactory(new PropertyValueFactory<>("courseNumber"));
        column2.setPrefWidth(105);
        tableView.getColumns().add(column2);

        TableColumn<Course, String> column3 = new TableColumn<>("Course Name");
        column3.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        column3.setPrefWidth(90);
        tableView.getColumns().add(column3);

        TableColumn<Course, Integer> column4 = new TableColumn<>("Credit Hours");
        column4.setCellValueFactory(new PropertyValueFactory<>("creditHours"));
        column4.setPrefWidth(100);
        tableView.getColumns().add(column4);

        displayAllCourses.setOnAction(e -> {
            tableView.getItems().clear();
            tableView.getItems().addAll(coursesList);
        });

        displayByDepartment.setOnAction(e -> {
            ObservableList<Course> deptList = FXCollections.observableArrayList();
            for (var course : coursesList) {
                if (course.getDepartmentCode().equals(displayByDepartment.getSelectionModel().getSelectedItem())) {
                    deptList.add(course);
                }
            }
            tableView.getItems().clear();
            tableView.getItems().addAll(deptList);
        });

        vBox.getChildren().addAll(displayButtons, tableView);
    }

    /**
     * Main method to launch the application
     *
     * @param args  User-given arguments/configurations
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}

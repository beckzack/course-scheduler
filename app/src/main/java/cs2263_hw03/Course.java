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

/**
 * A simple course data class that stores a course's dept./dept. code, course name and number, and credit hours
 */
public class Course {

    private static final String[] departmentCodes = {"CS", "MATH", "CHEM", "PHYS", "BIOL", "EE"};
    private static final String[] departments = {"Computer Science", "Mathematics", "Chemistry", "Physics", "Biology",
                                                "Electrical Engineering"};
    private String department;
    private int courseNumber;
    private String courseName;
    private int creditHours;
    private String departmentCode;

    /**
     * An empty default constructor for using GSON serialization
     */
    public Course() {

    }

    /**
     * Constructs a course object using the user-given data. Automatically calculates the department code based on the
     * given department
     *
     * @param department The course's department (e.g. Computer Science)
     * @param number The course's number (e.g. 2263)
     * @param name The course's name (e.g. Advanced Object-Oriented Programming)
     * @param hours The course's number of credit hours
     */
    public Course(String department, int number, String name, int hours) {
        this.department = department;
        this.courseNumber = number;
        this.courseName = name;
        this.creditHours = hours;

        for (int i = 0; i < departments.length; i++) {
            if (departments[i].equals(department)) {
                this.departmentCode = departmentCodes[i];
            }
        }
    }

    /**
     * Returns the list of department codes
     *
     * @return  The list of department codes on line 31
     */
    public static String[] getDepartmentCodes() { return departmentCodes; }

    /**
     * Returns the list of course departments
     *
     * @return  The list of departments on line 32
     */
    public static String[] getDepartments() { return departments; }

    /**
     * Returns the course's department (e.g. Computer Science)
     *
     * @return  The course's department
     */
    public String getDepartment() { return department; }

    /**
     * Returns the course's department code (e.g. CS)
     *
     * @return  The course's department code
     */
    public String getDepartmentCode() { return departmentCode; }

    /**
     * Returns the course's number (e.g. 2263)
     *
     * @return  The course's number
     */
    public int getCourseNumber() { return courseNumber; }

    /**
     * Return's the course's name (e.g. Advanced Object-Oriented Programming)
     *
     * @return  The course's name
     */
    public String getCourseName() { return courseName; }

    /**
     * Return's the course's credit hours
     *
     * @return  The course's credit hours
     */
    public int getCreditHours() { return creditHours; }
}

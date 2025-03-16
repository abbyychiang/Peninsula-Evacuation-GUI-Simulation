import java.awt.*;

public class Student {
    public int x, y;
    public int step = 0; // Step 0 = hallway, Step 1 = stairs, Step 2 = moving to field
    public boolean isFemale;
    public Color color;
    public String gradeLevel;

    public Student(int startX, int startY, boolean isFemale, String gradeLevel) {
        this.x = startX;
        this.y = startY;
        this.isFemale = isFemale;
        this.gradeLevel = gradeLevel;
        this.color = determineColor();
    }

   
    private Color determineColor() {
        if (isFemale) {
            return Color.PINK;
        } else {
            switch (gradeLevel) {
                case "Freshman": return Color.ORANGE;
                case "Sophomore": return Color.GREEN;
                case "Junior": return Color.MAGENTA;
                case "Senior": return Color.BLUE;
                default: return Color.GRAY;
            }
        }
    }

    // Move the student in the simulation
    public void move() {
        if (step == 0) {
            // Move through the hallway towards the stairs
            x += 1; // Increment x to move horizontally
            if (x >= 150) step = 1; // Transition to step 1 (stairs) when reaching the stairs
        } else if (step == 1) {
            // Move down the stairs
            y += 1; // Increment y to move vertically downwards
            if (y >= 250) step = 2; // Transition to step 2 (moving towards the field)
        } else if (step == 2) {
            // Move towards the field
            x += 1; // Move horizontally towards the field
            if (x >= 350) {
                y += 1; // Once at the x-coordinate of the field, start moving down towards the field
                if (y >= 350) step = 3; // Once at the field, mark as completed
            }
        }
    }

    // Check if the student has reached the field
    public boolean isAtField() {
        return step == 3; // If step is 3, the student has reached the field
    }

    // Draw the student on the screen
    public void draw(Graphics g) {
        g.setColor(color);
        if (isFemale) {
            // Draw a triangle for females (represents gender)
            int[] xPoints = {x, x + 10, x - 10};
            int[] yPoints = {y, y + 20, y + 20};
            g.fillPolygon(xPoints, yPoints, 3);
        } else {
            // Draw a square for males (represents gender)
            g.fillRect(x - 10, y - 10, 20, 20);
        }
    }
}

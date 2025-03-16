import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class evacuationPanel extends JPanel {
    private LinkedList<Student> students; // linkedList to store students
    private int[] congestionMap = new int[400]; // stair congestion
    private Timer timer;
    private long startTime;
    private JButton resetButton;

    public evacuationPanel() {
        setPreferredSize(new Dimension(500, 400));
        students = new LinkedList<>();
        startTime = System.currentTimeMillis();

        resetButton = new JButton("Reset");
        resetButton.setBounds(400, 350, 80, 40);
        resetButton.addActionListener(e -> resetSimulation());
        add(resetButton);

        // create students
        for (int i = 0; i < 60; i++) {
            // randomize spawn points for students 
            int spawnX = (int)(Math.random() * 50 + 50);
            int spawnY = (int)(Math.random() * 50 + 100);
            boolean isFemale = Math.random() < 0.5;
            String gradeLevel = getRandomGradeLevel();

            // spawn tennis court students 
            if (Math.random() < 0.3) {
                spawnX = (int)(Math.random() * 50 + 200);  // tennis court zone
            }

            students.add(new Student(spawnX, spawnY, isFemale, gradeLevel)); 
        }

        timer = new Timer(500, e -> {// ms
            for (Student s : students) {
                if (!s.isAtField()) {
                    s.move();

                    //  stair congestion
                    if (s.step == 1 && s.y >= 200 && s.y <= 260) {
                        congestionMap[s.y]++;
                    }
                }
            }
            repaint();
        });
        timer.start();
    }

    private String getRandomGradeLevel() {
        String[] grades = {"Freshman", "Sophomore", "Junior", "Senior"};
        return grades[(int)(Math.random() * grades.length)];
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // p building
        g.setColor(new Color(220, 220, 220));
        g.fillRect(0, 100, 150, 50);
        g.setColor(Color.BLACK);
        g.drawString("P Building", 20, 95);

        // tennis Court despawn
        g.setColor(new Color(180, 255, 180));
        g.fillRect(200, 100, 100, 50);
        g.setColor(Color.BLACK);
        g.drawString("Tennis Court", 210, 95);

        // stairs
        g.setColor(Color.GRAY);
        g.fillRect(150, 150, 50, 150);
        g.setColor(Color.BLACK);
        g.drawString("Stairs", 155, 145);

        // hallway to field
        g.setColor(new Color(100, 150, 255)); 
        g.fillRect(200, 250, 180, 50);
        g.drawString("To Field", 210, 245);

        g.setColor(Color.GREEN);
        g.fillRect(350, 0, 50, 400);
        g.setColor(Color.BLACK);
        g.drawString("Field", 360, 20);

      
        for (Student s : students) {
            if (!s.isAtField()) s.draw(g);
        }

        //red congestion
        g.setColor(Color.RED);
        for (int y = 150; y < 300; y++) {
            if (congestionMap[y] > 3) {
                g.drawLine(150, y, 200, y);
            }
        }

    
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        g.setColor(Color.BLACK);
        g.drawString("Time Elapsed: " + elapsedTime + " seconds", 10, 370);
    }

    private void resetSimulation() {
        students.clear(); 
        startTime = System.currentTimeMillis();
        for (int i = 0; i < 60; i++) {
            int spawnX = (int)(Math.random() * 50 + 50);
            int spawnY = (int)(Math.random() * 50 + 100);
            boolean isFemale = Math.random() < 0.5;
            String gradeLevel = getRandomGradeLevel();
            if (Math.random() < 0.3) {
                spawnX = (int)(Math.random() * 50 + 200);  
            }
            students.add(new Student(spawnX, spawnY, isFemale, gradeLevel)); 
        }
    }

    private static class Student {
        private int x, y;
        private boolean isFemale;
        private String gradeLevel;
        private boolean atField = false;
        private int step = 0; 
        private boolean isFromTennisCourt = false;

        public Student(int x, int y, boolean isFemale, String gradeLevel) {
            this.x = x;
            this.y = y;
            this.isFemale = isFemale;
            this.gradeLevel = gradeLevel;

           
            this.isFromTennisCourt = (x >= 200 && x <= 300);
        }

        public boolean isAtField() {
            return atField;
        }

        public void move() {
            if (isFromTennisCourt) {
                
                if (x < 350) x += 5; 
                if (x >= 350) atField = true;
            } else {
                
                if (step == 0) {
                    
                    if (x < 150) x += 5;
                    if (y < 200) y += 5;
                    if (x >= 150 && y >= 200) step = 1;
                } else if (step == 1) {
                    
                    if (y < 270) y += 5;
                    if (y >= 270) step = 2;
                } else if (step == 2) {
                    
                    if (x < 350) x += 5;
                    if (x >= 350) atField = true;
                }
            }
        }

        public void draw(Graphics g) {
            Color studentColor = getGradeColor();
            g.setColor(studentColor);

            // male as square and female as triangle
            if (isFemale) {
                // triangle
                int[] xPoints = {x, x + 10, x - 10};
                int[] yPoints = {y, y + 20, y + 20};
                g.fillPolygon(xPoints, yPoints, 3);
            } else {
                // square
                g.fillRect(x - 10, y - 10, 20, 20);
            }

           
            if (isFromTennisCourt && x < 350) {
                g.setColor(Color.BLUE); 
                g.drawLine(x, y, 350, y);
            }
        }

        private Color getGradeColor() {
            switch (gradeLevel) {
                case "Freshman":
                    return Color.ORANGE;
                case "Sophomore":
                    return Color.GREEN;
                case "Junior":
                    return Color.MAGENTA;
                case "Senior":
                    return Color.BLUE;
                default:
                    return Color.BLACK;
            }
        }
    }
}

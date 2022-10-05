import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {


    static String clasStartWith;
    static ArrayList<String> clasAndStudents;
    static ArrayList<Student> studs;

    static glassFinder gf;

    static Glass clas;

    static Gistogram gist;

    static FrameClass students;

    static FrameClass buttons;

    static HashMap<Integer, String> hm;


    public static void main(String[] args) throws IOException {
        firstGir();
    }

    private static void levelGir() {
        FrameClass setLevelNames = new FrameClass("Введите названия уровней", JFrame.HIDE_ON_CLOSE);
        int[] i = new int[]{0};
        hm = new HashMap<>();
        JTextField jtf = setLevelNames.createField(
                new Rectangle(0, 0, setLevelNames.frame.getWidth() / 2, 100)
        );
        JLabel label = setLevelNames.createLabel("Название уровня номер " + (i[0] + 1),
                new Rectangle(setLevelNames.frame.getWidth() / 2, 0, setLevelNames.frame.getWidth() / 2, 100),
                Color.BLACK
        );
        JButton button = setLevelNames.createButton("next",
                new Rectangle(0, 100, 100, 100),
                e -> {
                    hm.put(i[0], jtf.getText());
                    i[0]++;
                    if (i[0] == 7) {
                        setLevelNames.frame.setVisible(false);
                        try {
                            MapUpdater.upload(hm);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        firstGir();
                    }
                    label.setText("Название уровня номер " + (i[0] + 1));
                    jtf.setText("");
                }
        );
    }

    private static void firstGir() {
        try {
            hm = MapUpdater.download();
        } catch (IOException e) {
            System.out.println(-1);
        }
        if (hm != null) {
            FrameClass frame = new FrameClass("Выберите класс", JFrame.EXIT_ON_CLOSE);
            JTextField jtf = frame.createField(
                    new Rectangle(0, 0, frame.frame.getWidth(), 100));
            JButton goToClass = frame.createButton("Далее",
                    new Rectangle(0, 100, 100, 100),
                    e -> {
                        clasStartWith = jtf.getText();
                        gf = new glassFinder(clasStartWith);
                        try {
                            System.out.println(gf.findOrCreate());
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        clasAndStudents = gf.glassInfo;
                        studs = gf.students;
                        clas = new Glass(clasStartWith, studs);
                        for (int i = 0; i < clasAndStudents.size(); i++) {
                            System.out.println(i + " " + clasAndStudents.get(i));
                        }
                        frame.frame.setVisible(false);
                        if (clasAndStudents.size() != 1) {
                            if (gist == null) {
                                chooseGir(0);
                            } else chooseGir(3);
                        } else {
                            minFirstGir();
                        }
                    }
            );
        }else{
            levelGir();
        }
    }

    private static void zeroGir(){
        students=new FrameClass("Список учеников",JFrame.EXIT_ON_CLOSE);
        StringBuilder sb=new StringBuilder();
        sb.append("<html>");
        for (Student student:studs) {
            sb.append(student.surname).append(" ").append(student.name).append("<br>");
        }
        sb.append("</html>");
        JLabel label=students.createLabel(sb.toString(),
                new Rectangle(0,0,students.frame.getWidth(),students.frame.getHeight()-300),Color.BLACK
                );
        JButton addnew=students.createButton("новый",
                new Rectangle(0,label.getHeight(),students.frame.getWidth(),300),
                e-> chooseGir(-1)
                );
    }

    private static void minFirstGir(){
        FrameClass fillStud=new FrameClass("Введите список учеников",JFrame.EXIT_ON_CLOSE);
        JTextField surname=fillStud.createField(new Rectangle(0,0,500,100));
        JTextField name=fillStud.createField(new Rectangle(0,100,500,100));

        JLabel surnameLabel=fillStud.createLabel("Введите фамилию",
                new Rectangle(500,0,500,100), Color.BLACK);
        JLabel nameLabel=fillStud.createLabel("Введите имя",
                new Rectangle(500,100,500,100),Color.BLACK);

        JButton addStudent=fillStud.createButton("Добавить",
                new Rectangle(0,300,100,100),
                e->{
                    studs.add(new Student(surname.getText(),name.getText()));
                    surname.setText("");
                    name.setText("");
                }
                );

        JButton end=fillStud.createButton("Завершить",
                new Rectangle(500,300,100,100),
                e->{
                    if (!studs.contains(new Student(surname.getText(),name.getText()))){
                        studs.add(new Student(surname.getText(),name.getText()));
                        studs.get(studs.size()-1).setRating(3);
                    }
                    fillStud.frame.setVisible(false);
                    chooseGir(0);
                    try {
                        gf.upload(studs);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                );

        fillStud.frame.setSize(800,500);
    }

    private static void updateGist(){
        gist=new Gistogram(clas,"Гистограмма");
        gist.pack();
        gist.setExtendedState(Frame.MAXIMIZED_BOTH);
        gist.setVisible(true);
    }

    private static void buttonGir(){
        buttons=new FrameClass("Изменить рейтинг ученика",JFrame.HIDE_ON_CLOSE);
        ArrayList<JButton>jbs=new ArrayList<>();
        for (int i = 0; i <studs.size(); i++) {
            int finalI = i+1;
            jbs.add(buttons.createButton(String.valueOf(i),
                    new Rectangle((i%7)*100,((i-(i%7))*100)/7,100,100),
                    e-> studentsState(finalI,studs.get(finalI))
                    ));
        }
    }

    private static void studentsState(int num,Student student){
        FrameClass fr=new FrameClass(studs.get(num).surname+" "+studs.get(num).name,JFrame.HIDE_ON_CLOSE);
        JButton button1;
        JButton button = fr.createButton("+",
                new Rectangle(100, 0, 100, 100),
                e -> {
                    if (5 * student.gradeRating + student.rating < 30) {
                        student.setRating(student.rating + 1);
                        if (student.isUp()) {
                            JOptionPane.showMessageDialog(fr.frame, new String[]{student.surname + " " + student.name +
                                    " повысил(а) свой уровень. ",
                                    "Полученный уровень - " + hm.get(student.gradeRating)});
                            chooseGir(2);
                        }
                        try {
                            gf.upload(studs);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );
        button1=fr.createButton("-",
                new Rectangle(0,0,100,100),
                e-> {
                    if (student.rating != 0 || student.gradeRating != 0) {
                        student.setRating(student.rating - 1);
                        if (student.isDown()) {
                            JOptionPane.showMessageDialog(fr.frame, new String[]{student.surname + " " + student.name + " понизил(а) свой уровень. ",
                                    "Полученный уровень - " + hm.get(student.gradeRating)});
                            chooseGir(2);
                        }
                        try {
                            gf.upload(studs);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );
        fr.frame.setSize(200,150);
    }


    private static void chooseGir(int state){
            if(state==1){
                firstGir();
            }else if (state==-1){
                minFirstGir();
            }else if(state==0){
                if (students!=null) students.frame.setVisible(false);
                if (buttons != null)  buttons.frame.setVisible(false);
                if (gist != null) gist.setVisible(false);
                zeroGir();
                updateGist();
                buttonGir();
            }else if(state==2){
                gist.setVisible(false);
                updateGist();
            }else if (state==3){
                if (students!=null) students.frame.setVisible(false);
                zeroGir();
                gist.setVisible(false);
                updateGist();
                buttonGir();
            }
    }


}
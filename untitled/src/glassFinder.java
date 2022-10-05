import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.*;
import java.util.ArrayList;

public class glassFinder {
    String clasName;
//    JFrame frame;
    ArrayList<String> glassInfo=new ArrayList<>();
    ArrayList<Student> students=new ArrayList<>();
    Glass foundClass=null;

    public glassFinder(String clasName) {
        this.clasName = clasName;
    }

    public boolean findOrCreate() throws IOException {
        try {
            FileReader fr=new FileReader(clasName+".txt");
            BufferedReader reader=new BufferedReader(fr);
            String line= reader.readLine();
            int x=0;
            while(line!=null){
                if (x!=0){
                    String[]studentsInfo=line.split(" ");
                    students.add(new Student(studentsInfo[0],studentsInfo[1]));
                    students.get(students.size()-1).setRating(strtoint(studentsInfo[2]));
                    students.get(students.size()-1).setGradeRating(strtoint(studentsInfo[3]));
                }
                glassInfo.add(line);
                System.out.println(line);
                line= reader.readLine();
                x++;
            }
            foundClass=new Glass(clasName,students);

            return true;
        } catch (FileNotFoundException e) {
            File file=new File(clasName+".txt");
            file.createNewFile();
            FileWriter fw=new FileWriter(file);
            fillFile(fw);
            fw.flush();
            fw.close();
            glassInfo.add("filler");
            return false;
        }
    }

    public void upload(ArrayList<Student>students) throws IOException {
        this.students=students;
        File file=new File(clasName+".txt");
        if(file.delete()){
            file.createNewFile();
        }else System.out.println("-");

        FileWriter fw=new FileWriter(file);
        fw.append(clasName);
        fw.append("\n");
        for (int i = 0; i <students.size(); i++) {
            fw.append(students.get(i).surname+" "+students.get(i).name+" "+students.get(i).rating+" "+students.get(i).gradeRating);
            fw.append("\n");
        }
        fw.flush();
        fw.close();
    }

    private static int strtoint(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void fillFile(FileWriter fw) throws IOException {
        fw.append(clasName);
    }
}

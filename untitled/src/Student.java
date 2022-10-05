public class Student {
    String name;
    String surname;
    int rating = 3;
    int gradeRating=0;


    public void setRating(int rating) {
        this.rating = rating;
        
    }

    public boolean isUp(){
        if(rating==5){
            this.gradeRating++;
            rating=0;
            return true;
        }
        return false;
    }

    public void setGradeRating(int gradeRating) {
        this.gradeRating = gradeRating;
    }

    public boolean isDown(){
        if(rating==-5){
            this.gradeRating--;
            rating=0;
            return true;
        }
        return false;
    }

    public Student(String surname, String name) {
        this.name = name;
        this.surname = surname;
    }
}

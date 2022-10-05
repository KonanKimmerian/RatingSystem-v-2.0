import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

import javax.swing.*;
import java.util.ArrayList;

public class Gistogram extends ApplicationFrame {
    Glass glass;

    public Gistogram(Glass glass, String title) {
        super(title);
        this.glass = glass;
        CategoryDataset dataset = update();
        JFreeChart chart = createChart(dataset);
        ChartPanel panel = new ChartPanel(chart);

        JFrame frame = new JFrame();
        frame.setExtendedState(MAXIMIZED_BOTH);

        panel.setPreferredSize(frame.getSize());
        setContentPane(panel);
    }

    private JFreeChart createChart(CategoryDataset data) {
        return ChartFactory.createStackedBarChart(
                "Класс",
                "Ученики",
                "Рейтинг",
                data,
                PlotOrientation.VERTICAL,
                true, true, false
        );
    }

    private CategoryDataset update() {
        DefaultCategoryDataset result = new DefaultCategoryDataset();
        for(int i = 0; i < 6; i++){
            result.addValue(5,"lvl "+i,"PS");
        }

        ArrayList<Student> students = glass.students;
        for (int i = 0; i <students.size(); i++) {
            int a = 0;
            for (int j = 0; j <students.get(i).gradeRating; j++) {
                result.addValue(5,"lvl "+j,String.valueOf(i+1));
                a++;
            }
            result.addValue(students.get(i).rating,"lvl "+a,String.valueOf(i+1));
        }
        return result;
    }

}

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RenderingEngine extends JPanel {

    private Integer[] values;
    private String[] labels;
    private String title;
    private static Color[] colors = new Color[]{
            Color.red,
            Color.orange,
            Color.yellow,
            Color.green,
            Color.blue,
    };

    public static void render(Dataset dataset, ClustererType type) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame(type.toString());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 300);

        dataset.sortByCluster();
        ArrayList<Integer> clusters = new ArrayList<>();

        int currentCluster = dataset.get(0).getCluster();
        int currentClusterCount = 0;
        for (Sample sample : dataset) {
            int cluster = sample.getCluster();
            if (cluster == currentCluster) {
                currentClusterCount++;
            } else {
                currentCluster = cluster;
                clusters.add(currentClusterCount);
                currentClusterCount = 0;
            }
        }

        Integer[] values = clusters.toArray(new Integer[]{});
        String[] labels = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            labels[i] = Integer.toString(i);
        }

        frame.add(new RenderingEngine(values, labels, type));
        frame.setVisible(true);
    }

    public RenderingEngine(Integer[] values, String[] labels, ClustererType type) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame(type.toString());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 300);

        this.title = type.toString();
        this.labels = labels;
        this.values = values;
    }

    public void paintComponent(Graphics g) {
        try {
            super.paintComponent(g);
            if (values == null || values.length == 0) {
                return;
            }

            double minValue = 0;
            double maxValue = 0;
            for (double value : values) {
                if (minValue > value) {
                    minValue = value;
                }
                if (maxValue < value) {
                    maxValue = value;
                }
            }

            Dimension dim = getSize();
            int panelWidth = dim.width;
            int panelHeight = dim.height;
            int barWidth = panelWidth / values.length;

            Font titleFont = new Font("Book Antiqua", Font.BOLD, 15);
            FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);

            Font labelFont = new Font("Book Antiqua", Font.PLAIN, 14);
            FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);

            int titleWidth = titleFontMetrics.stringWidth(title);
            int stringHeight = titleFontMetrics.getAscent();
            int stringWidth = (panelWidth - titleWidth) / 2;
            g.setFont(titleFont);
            g.drawString(title, stringWidth, stringHeight);

            int top = titleFontMetrics.getHeight();
            int bottom = labelFontMetrics.getHeight();
            if (maxValue == minValue) {
                return;
            }
            double scale = (panelHeight - top - bottom) / (maxValue - minValue);
            stringHeight = panelHeight - labelFontMetrics.getDescent();
            g.setFont(labelFont);
            for (int j = 0; j < values.length; j++) {
                int valueP = j * barWidth + 1;
                int valueQ = top;
                int height = (int) (values[j] * scale);
                if (values[j] >= 0) {
                    valueQ += (int) ((maxValue - values[j]) * scale);
                } else {
                    valueQ += (int) (maxValue * scale);
                    height = -height;
                }

                g.setColor(colors[j]);
                g.fillRect(valueP, valueQ, barWidth - 2, height);
                g.setColor(Color.black);
                g.drawRect(valueP, valueQ, barWidth - 2, height);

                int labelWidth = labelFontMetrics.stringWidth(labels[j]);
                stringWidth = j * barWidth + (barWidth - labelWidth) / 2;
                g.drawString(labels[j], stringWidth, stringHeight);
            }
        } catch (Exception ex) {
            System.exit(-1);
        }
    }
}
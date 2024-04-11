package coding.toast.bread.color;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

public class GradientRgbCalculator {

    private static class GradientRgbViewer extends JFrame {
        public GradientRgbViewer(final List<Color> colorList) {
            setTitle("Gradient Rgb Viewer");
            setSize(600 + 25, 100);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            int colorListSize = colorList.size();
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    int squareSize = 600 / colorListSize;
                    int x = 0;
                    for (Color color : colorList) {
                        g2d.setColor(color);
                        g2d.fillRect(x, 0, squareSize, squareSize);
                        x += squareSize;
                    }
                }
            };

            add(panel);
            setVisible(true);
        }
    }

    public static Color getColorAtDistribution(LinearGradientPaint gradient, float dist) {
        float[] fractions = gradient.getFractions();
        System.out.println("fractions : " + Arrays.toString(fractions));

        Color[] colors = gradient.getColors();
        System.out.println("colors : " + Arrays.toString(colors));

        // 0.0~1.0 사이의 범위를 넘어선 dist 인 경우에는 최하 또는 최상의 color 를 return 한다.
        if (dist <= fractions[0]) {
            return colors[0];
        } else if (dist >= fractions[fractions.length - 1]) {
            return colors[colors.length - 1];
        }

        // 현재 dist 값이 fraction 의 몇번째 fraction[index], fraction[index+1]
        // 사이에 위치하는지 알아내기 위한 while 문이다.
        // while 문을 빠져나오면 dist 의 바로 이전 fraction 의 index 값이 나온다.
        int index = 0;
        while (index < fractions.length - 1 && dist > fractions[index]) {
            index++;
        }

        // factor 값을 구해서, color[index-1] 에 더해야 될 값의 정도(= %, 퍼센트)를 구한다.
        float factor = (dist - fractions[index-1]) / (fractions[index] - fractions[index - 1]);

        // rgb 값을 구해낸다.
        int red = (int) (colors[index - 1].getRed() + factor * (colors[index].getRed() - colors[index - 1].getRed()));
        int green = (int) (colors[index - 1].getGreen() + factor * (colors[index].getGreen() - colors[index - 1].getGreen()));
        int blue = (int) (colors[index - 1].getBlue() + factor * (colors[index].getBlue() - colors[index - 1].getBlue()));

        return new Color(red, green, blue);
    }

    public static void main(String[] args) {
        Point2D start = new Point2D.Float(0, 0);
        Point2D end = new Point2D.Float(100, 100);
        float[] dist = {0.0f, 0.5f, 1.0f};
        Color[] colors = {Color.BLUE, Color.GREEN, Color.RED};
        LinearGradientPaint linearGradientPaint = new LinearGradientPaint(start, end, dist, colors);

        List<Color> colorAtDistributionList = List.of(
                getColorAtDistribution(linearGradientPaint, 0.0f),
                getColorAtDistribution(linearGradientPaint, 0.1f),
                getColorAtDistribution(linearGradientPaint, 0.2f),
                getColorAtDistribution(linearGradientPaint, 0.3f),
                getColorAtDistribution(linearGradientPaint, 0.4f),
                getColorAtDistribution(linearGradientPaint, 0.5f),
                getColorAtDistribution(linearGradientPaint, 0.6f),
                getColorAtDistribution(linearGradientPaint, 0.7f),
                getColorAtDistribution(linearGradientPaint, 0.8f),
                getColorAtDistribution(linearGradientPaint, 0.9f),
                getColorAtDistribution(linearGradientPaint, 1.0f)
        );

        for (Color color : colorAtDistributionList) {
            System.out.println("color = " + color);
        }

        new GradientRgbViewer(colorAtDistributionList);
    }
}

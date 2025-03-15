package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.maths.Range;
import org.jzy3d.maths.Rectangle;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;


import java.awt.BasicStroke;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.BiFunction;
import java.util.function.Function;

public class GraphBuilder {

    private static final MathContext MC = new MathContext(10);

    public static void saveFunctionPlot(Function<BigDecimal, BigDecimal> function, String title,
                                        BigDecimal start, BigDecimal end, BigDecimal step,
                                        String outputFile, int width, int height, BigDecimal root) {
        XYSeries series = new XYSeries(title);
        for (BigDecimal x = start; x.compareTo(end) <= 0; x = x.add(step, MC)) {
            BigDecimal y = function.apply(x);
            series.add(x.doubleValue(), y.doubleValue());
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        System.out.println(root);

        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                "X",
                "Y",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();

        // Use java.awt.Color
        if (root != null) {
            ValueMarker marker = new ValueMarker(root.doubleValue());
            marker.setPaint(java.awt.Color.BLACK); 
            marker.setStroke(new BasicStroke(
                    2.0f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL,
                    0,
                    new float[]{5.0f, 5.0f},
                    0
            ));
            plot.addDomainMarker(marker);
        }

        plot.setDomainZeroBaselineVisible(true);
        plot.setRangeZeroBaselineVisible(true);

        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        domainAxis.setAutoRange(true);
        rangeAxis.setAutoRange(true);

        try {
            ChartUtils.saveChartAsPNG(new File(outputFile), chart, width, height);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении графика в файл " + outputFile, e);
        }
    }

    public static void save3DPlot(
            BiFunction<BigDecimal, BigDecimal, BigDecimal> f1,
            BiFunction<BigDecimal, BigDecimal, BigDecimal> f2,
            BigDecimal xStart, BigDecimal xEnd,
            BigDecimal yStart, BigDecimal yEnd,
            BigDecimal step,
            String outputFile,
            int width,
            int height
    ) {
        System.setProperty("jogl.glprofile", "GL4");
        Mapper mapper1 = new Mapper() {
            @Override
            public double f(double x, double y) {
                return f1.apply(BigDecimal.valueOf(x), BigDecimal.valueOf(y)).doubleValue();
            }
        };
        Mapper mapper2 = new Mapper() {
            @Override
            public double f(double x, double y) {
                return f2.apply(BigDecimal.valueOf(x), BigDecimal.valueOf(y)).doubleValue();
            }
        };

        Range rangeX = new Range(xStart.floatValue(), xEnd.floatValue());
        Range rangeY = new Range(yStart.floatValue(), yEnd.floatValue());
        int stepsX = (int) Math.ceil(xEnd.subtract(xStart, MC).doubleValue() / step.doubleValue());
        int stepsY = (int) Math.ceil(yEnd.subtract(yStart, MC).doubleValue() / step.doubleValue());

        Shape surface1 = Builder.buildOrthonormal(new OrthonormalGrid(rangeX, stepsX, rangeY, stepsY), mapper1);
        surface1.setColor(org.jzy3d.colors.Color.BLUE);
        surface1.setWireframeDisplayed(false);

        Shape surface2 = Builder.buildOrthonormal(new OrthonormalGrid(rangeX, stepsX, rangeY, stepsY), mapper2);
        surface2.setColor(org.jzy3d.colors.Color.RED);
        surface2.setWireframeDisplayed(false);

        AWTChart chart = new AWTChart(Quality.Advanced, "offscreen");
        chart.getScene().getGraph().add(surface1);
        chart.getScene().getGraph().add(surface2);

        try {
            chart.render();
            chart.screenshot(new File(outputFile));
            chart.dispose();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении 3D-графика: " + e.getMessage(), e);
        }
    }
}
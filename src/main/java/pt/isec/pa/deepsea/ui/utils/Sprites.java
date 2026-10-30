package pt.isec.pa.deepsea.ui.utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import pt.isec.pa.deepsea.model.data.CellType;
import pt.isec.pa.deepsea.ui.Theme;

/**
 * Minimal vector sprites drawn directly on a canvas, so the whole game shares
 * one flat visual language and scales cleanly with the window.
 */
public final class Sprites {
    private Sprites() {}

    public static void water(GraphicsContext gc, double x, double y, double s) {
        gc.setFill(Theme.BG);
        gc.fillRect(x, y, s, s);
        gc.setStroke(Theme.LINE);
        gc.setLineWidth(0.5);
        gc.strokeRect(x + 0.25, y + 0.25, s - 0.5, s - 0.5);
    }

    public static void fog(GraphicsContext gc, double x, double y, double s) {
        gc.setFill(Theme.BG_DEEP);
        gc.fillRect(x, y, s, s);
        gc.setFill(Theme.LINE);
        double d = Math.max(2, s * 0.06);
        gc.fillOval(x + s / 2 - d / 2, y + s / 2 - d / 2, d, d);
    }

    public static void cell(GraphicsContext gc, CellType type, double x, double y, double s) {
        water(gc, x, y, s);
        stroke(gc, s);
        switch (type) {
            case ROCK -> rock(gc, x, y, s);
            case ORE -> ore(gc, x, y, s);
            case ARTIFACT -> artifact(gc, x, y, s);
            case MONSTER -> monster(gc, x, y, s);
            case MARINE_ANIMAL -> animal(gc, x, y, s);
            case CURRENT -> current(gc, x, y, s);
            case WATER -> { }
        }
    }

    private static void stroke(GraphicsContext gc, double s) {
        gc.setLineWidth(Math.max(1.5, s * 0.045));
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineJoin(StrokeLineJoin.ROUND);
    }

    private static void poly(GraphicsContext gc, double x, double y, double s, boolean fill, double... p) {
        int n = p.length / 2;
        double[] xs = new double[n];
        double[] ys = new double[n];
        for (int i = 0; i < n; i++) {
            xs[i] = x + p[2 * i] * s;
            ys[i] = y + p[2 * i + 1] * s;
        }
        if (fill) gc.fillPolygon(xs, ys, n);
        gc.strokePolygon(xs, ys, n);
    }

    private static void circle(GraphicsContext gc, double cx, double cy, double r, boolean fill) {
        if (fill) gc.fillOval(cx - r, cy - r, 2 * r, 2 * r);
        else gc.strokeOval(cx - r, cy - r, 2 * r, 2 * r);
    }

    private static void rock(GraphicsContext gc, double x, double y, double s) {
        gc.setFill(Color.web("#4c5154"));
        gc.setStroke(Color.web("#6a7175"));
        poly(gc, x, y, s, true, .16, .58, .27, .26, .55, .14, .83, .34, .86, .72, .55, .86, .24, .8);
    }

    private static void ore(GraphicsContext gc, double x, double y, double s) {
        gc.setFill(Theme.ACCENT_DEEP);
        gc.setStroke(Theme.ACCENT_DARK);
        poly(gc, x, y, s, true, .5, .2, .78, .5, .5, .8, .22, .5);
    }

    private static void artifact(GraphicsContext gc, double x, double y, double s) {
        double cx = x + s / 2;
        double cy = y + s / 2;
        gc.setStroke(Theme.ACCENT);
        circle(gc, cx, cy, s * 0.3, false);
        gc.setFill(Theme.ACCENT);
        circle(gc, cx, cy, s * 0.1, true);
        gc.setStroke(Color.web("#b9dec2", 0.3));
        circle(gc, cx, cy, s * 0.41, false);
    }

    private static void monster(GraphicsContext gc, double x, double y, double s) {
        gc.setFill(Color.web("#cfd6d1"));
        gc.setStroke(Color.web("#e8ede9"));
        poly(gc, x, y, s, true, .2, .25, .8, .25, .5, .82);
        gc.setFill(Theme.BG_DEEP);
        circle(gc, x + s * 0.4, y + s * 0.4, s * 0.05, true);
        circle(gc, x + s * 0.6, y + s * 0.4, s * 0.05, true);
    }

    private static void animal(GraphicsContext gc, double x, double y, double s) {
        gc.setFill(Color.web("#8a948d"));
        gc.setStroke(Color.web("#a9b3ac"));
        gc.fillOval(x + s * 0.14, y + s * 0.3, s * 0.52, s * 0.4);
        gc.strokeOval(x + s * 0.14, y + s * 0.3, s * 0.52, s * 0.4);
        poly(gc, x, y, s, true, .62, .5, .88, .3, .88, .7);
        gc.setFill(Theme.BG_DEEP);
        circle(gc, x + s * 0.3, y + s * 0.46, s * 0.04, true);
    }

    private static void current(GraphicsContext gc, double x, double y, double s) {
        gc.setStroke(Theme.ACCENT_DARK);
        for (int row = 0; row < 3; row++) {
            double yy = y + s * (0.3 + row * 0.2);
            gc.beginPath();
            gc.moveTo(x + s * 0.15, yy);
            gc.bezierCurveTo(x + s * 0.3, yy - s * 0.1, x + s * 0.4, yy - s * 0.1, x + s * 0.5, yy);
            gc.bezierCurveTo(x + s * 0.6, yy + s * 0.1, x + s * 0.7, yy + s * 0.1, x + s * 0.85, yy);
            gc.stroke();
        }
    }

    public static void drone(GraphicsContext gc, double x, double y, double s) {
        double cx = x + s / 2;
        double cy = y + s / 2;
        stroke(gc, s);
        gc.setStroke(Theme.ACCENT);
        gc.setFill(Theme.ACCENT);
        double[][] rotors = {{.24, .24}, {.76, .24}, {.24, .76}, {.76, .76}};
        for (double[] r : rotors) {
            gc.strokeLine(cx, cy, x + r[0] * s, y + r[1] * s);
            circle(gc, x + r[0] * s, y + r[1] * s, s * 0.09, false);
        }
        circle(gc, cx, cy, s * 0.14, true);
    }

    public static void ship(GraphicsContext gc, double x, double y, double s) {
        stroke(gc, s);
        gc.setFill(Theme.ACCENT);
        gc.setStroke(Theme.ACCENT);
        poly(gc, x, y, s, true, .1, .55, .9, .55, .75, .82, .25, .82);
        gc.setFill(Theme.ACCENT_DARK);
        gc.setStroke(Theme.ACCENT_DARK);
        poly(gc, x, y, s, true, .36, .32, .64, .32, .64, .55, .36, .55);
        gc.setStroke(Theme.ACCENT);
        gc.strokeLine(x + s * 0.5, y + s * 0.32, x + s * 0.5, y + s * 0.14);
    }
}

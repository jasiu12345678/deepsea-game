package pt.isec.pa.deepsea.ui;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Colour palette and fonts shared by every view: dark greys with pale and
 * muted green accents. The same values are mirrored in {@code style.css}.
 */
public final class Theme {
    public static final Color BG_DEEP = Color.web("#141617");
    public static final Color BG = Color.web("#1b1d1f");
    public static final Color BG_RAISED = Color.web("#25282a");
    public static final Color LINE = Color.web("#33373a");
    public static final Color TEXT = Color.web("#dde4de");
    public static final Color MUTED = Color.web("#848d87");
    public static final Color ACCENT = Color.web("#b9dec2");
    public static final Color ACCENT_DARK = Color.web("#74a283");
    public static final Color ACCENT_DEEP = Color.web("#3f5a49");

    public static final String STYLESHEET = Theme.class.getResource("/style.css").toExternalForm();

    private Theme() {}

    public static Font font(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }
}

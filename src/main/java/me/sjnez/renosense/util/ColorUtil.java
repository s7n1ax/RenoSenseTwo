package me.sjnez.renosense.util;

import java.awt.Color;
import me.sjnez.renosense.features.modules.client.ClickGui;
import org.lwjgl.opengl.GL11;

public class ColorUtil {
    public static int toARGB(int r, int g, int b, int a) {
        return new Color(r, g, b, a).getRGB();
    }

    public static int toRGBA(int r, int g, int b) {
        return ColorUtil.toRGBA(r, g, b, 255);
    }

    public static int toRGBA(int r, int g, int b, int a) {
        return (r << 16) + (g << 8) + b + (a << 24);
    }

    public static int toRGBA(float r, float g, float b, float a) {
        return ColorUtil.toRGBA((int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f), (int)(a * 255.0f));
    }

    public static Color rainbow(int delay) {
        double rainbowState = Math.ceil((double)(System.currentTimeMillis() + (long)delay) / 20.0);
        return Color.getHSBColor((float)((rainbowState %= 360.0) / 360.0), ClickGui.getInstance().rainbowSaturation.getValue().floatValue() / 255.0f, ClickGui.getInstance().rainbowBrightness.getValue().floatValue() / 255.0f);
    }

    public static float[] GenerateHSB(Color color) {
        float n7;
        float n5;
        float[] rgbColorComponents = color.getRGBColorComponents(null);
        float n = rgbColorComponents[0];
        float n2 = rgbColorComponents[1];
        float n3 = rgbColorComponents[2];
        float min = Math.min(n, Math.min(n2, n3));
        float max = Math.max(n, Math.max(n2, n3));
        float n4 = 0.0f;
        if (max == min) {
            n4 = 0.0f;
            n5 = max;
        } else if (max == n) {
            n4 = (60.0f * (n2 - n3) / (max - min) + 360.0f) % 360.0f;
            n5 = max;
        } else if (max == n2) {
            n4 = 60.0f * (n3 - n) / (max - min) + 120.0f;
            n5 = max;
        } else {
            if (max == n3) {
                n4 = 60.0f * (n - n2) / (max - min) + 240.0f;
            }
            n5 = max;
        }
        float n6 = (n5 + min) / 2.0f;
        if (max == min) {
            n7 = 0.0f;
        } else {
            float n8 = Math.min(n6, 0.5f);
            float n9 = max;
            n7 = n8 <= 0.0f ? (n9 - min) / (max + min) : (n9 - min) / (2.0f - max - min);
        }
        return new float[]{n4, n7 * 100.0f, n6 * 100.0f};
    }

    public static int toRGBA(float[] colors) {
        if (colors.length != 4) {
            throw new IllegalArgumentException("colors[] must have a length of 4!");
        }
        return ColorUtil.toRGBA(colors[0], colors[1], colors[2], colors[3]);
    }

    public static int toRGBA(double[] colors) {
        if (colors.length != 4) {
            throw new IllegalArgumentException("colors[] must have a length of 4!");
        }
        return ColorUtil.toRGBA((float)colors[0], (float)colors[1], (float)colors[2], (float)colors[3]);
    }

    public static int toRGBA(Color color) {
        return ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static Color newAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static void glColor(Color color) {
        GL11.glColor4f((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
    }

    public static Color interpolate(float value, Color start, Color end) {
        float sr = (float)start.getRed() / 255.0f;
        float sg = (float)start.getGreen() / 255.0f;
        float sb = (float)start.getBlue() / 255.0f;
        float sa = (float)start.getAlpha() / 255.0f;
        float er = (float)end.getRed() / 255.0f;
        float eg = (float)end.getGreen() / 255.0f;
        float eb = (float)end.getBlue() / 255.0f;
        float ea = (float)end.getAlpha() / 255.0f;
        float r = sr * value + er * (1.0f - value);
        float g = sg * value + eg * (1.0f - value);
        float b = sb * value + eb * (1.0f - value);
        float a = sa * value + ea * (1.0f - value);
        return new Color(r, g, b, a);
    }
}

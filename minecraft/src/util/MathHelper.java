package util;

public final class MathHelper {
    private static float[] SIN_TABLE = new float[65536];

    public static final float sin(float var0) {
        return SIN_TABLE[(int)(var0 * 10430.378F) & '\uffff'];
    }

    public static final float cos(float var0) {
        return SIN_TABLE[(int)(var0 * 10430.378F + 16384.0F) & '\uffff'];
    }

    public static final float sqrt_float(float var0) {
        return (float)Math.sqrt((double)var0);
    }

    public static int floor_float(float var0) {
        int var1 = (int)var0;
        return var0 < (float)var1 ? var1 - 1 : var1;
    }

    public static int floor_double(double var0) {
        int var2 = (int)var0;
        return var0 < (double)var2 ? var2 - 1 : var2;
    }

    public static float abs(float var0) {
        return var0 >= 0.0F ? var0 : -var0;
    }

    static {
        for(int var0 = 0; var0 < 65536; ++var0) {
            SIN_TABLE[var0] = (float)Math.sin((double)var0 * Math.PI * 2.0D / 65536.0D);
        }
    }

    public static int clamp_int(int value, int min, int max) {
        // Ensures that the value is between min and max, inclusive.
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        } else {
            return value;
        }
    }

    public static float sqrt(float f) {
        return (float) Math.sqrt(f);
    }

    /**
     * Clips the value to be within the specified min and max range.
     * 
     * @param value The value to be clipped.
     * @param min The minimum value to clip to.
     * @param max The maximum value to clip to.
     * @return The clipped value.
     */
    public static float clip(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }

    /**
     * Clips the value to be within the specified min and max range.
     * 
     * @param value The value to be clipped.
     * @param min The minimum value to clip to.
     * @param max The maximum value to clip to.
     * @return The clipped value.
     */
    public static int clip(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }
}

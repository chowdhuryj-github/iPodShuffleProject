package iPodShuffleJava;

/**
 * a class that contains the format and default value
 */
public class StructField {

    public final String format;
    public final Object defaultValue;

    public StructField(String format, Object defaulValue) {
        this.format = format;
        this.defaultValue = defaulValue;
    }
    
}

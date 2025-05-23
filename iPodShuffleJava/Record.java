package iPodShuffleJava;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * the record class
 */
public class Record {


    protected Shuffler shuffler;
    protected Map<String, StructField> struct;
    protected Map<String, Object> fields;
    protected boolean trackVoiceOver;
    protected boolean playlistVoiceOver;
    protected boolean rename;
    protected int trackGain;


    /**
     * constructor for record
     * @param shuffler the shuffler object being passed in
     */
    public Record(Shuffler shuffler) {

        this.shuffler = shuffler;
        this.struct = new LinkedHashMap<>();
        this.fields = new LinkedHashMap<>();
        this.trackVoiceOver = shuffler.trackVoiceOver;
        this.playlistVoiceOver = shuffler.playlistVoiceOver;
        this.rename = shuffler.rename;
        this.trackGain = shuffler.trackGain;

    }


    /**
     * method for retrieving an item
     * @param item name of field in a record object
     * @return object
     */
    public Object getItem(String item) {
        
        if(!struct.containsKey(item)) {
            throw new IllegalArgumentException("Key Not Found:" + item);
        }

        return fields.getOrDefault(item, struct.get(item).defaultValue);

    }


    /**
     * method for setting an item
     * @param item the name of field in a record object
     * @param value the value that goes with the item
     */
    public void setItem(String item, Object value) {
        fields.put(item, value);
    }


    public byte[] construct() {

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        Iterator<String> iterator = struct.keySet().iterator();
        while(iterator.hasNext()) {
            String fieldName = iterator.next(); // extracting the key
            StructField fieldSpec = struct.get(fieldName); // looks for the format
            String format = fieldSpec.format; // retrieves the format string
            Object value = fields.getOrDefault(fieldName, fieldSpec.defaultValue); // get user defined value

            try {
                switch(format) {

                    /**
                     * writes a little endian 32-bit integer to a binary stream when the format specifier is "I"
                     */
                    case "I": { // 4 byte
                        ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
                        buffer.putInt(((Number) value).intValue());
                        output.write(buffer.array());
                        break;
                    }

                    case "H": { // 2 byte
                        ByteBuffer buffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
                        buffer.putShort(((Number) value).shortValue());
                        output.write(buffer.array());
                        break;
                    }

                    case "B": { // 1 byte
                        output.write(((Number) value).intValue() & 0xFF);
                        break;
                    }

                    case "Q": {
                        ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
                        buffer.putLong(((Number) value).longValue());
                        output.write(buffer.array());
                        break;
                    }

                    case "4s": {
                        byte[] bytes = ((String) value).getBytes("ASCII");
                        byte[] padded = new byte[4];
                        System.arraycopy(bytes, 0, padded, 0, Math.min(4, bytes.length));
                        output.write(padded);
                        break;
                    }

                    case "8s": {
                        byte[] bytes = (byte[]) value;
                        byte[] padded = new byte[8];
                        System.arraycopy(bytes, 0, padded, 0, Math.min(8, bytes.length));
                        output.write(padded);
                        break;
                    }

                    case "20s": {
                        byte[] bytes = (byte[]) value;
                        byte[] padded = new byte[20];
                        System.arraycopy(bytes, 0, padded, 0, Math.min(20, bytes.length));
                        output.write(padded);
                        break;
                    }

                    case "256s" : {
                        byte[] bytes = (byte[]) value;
                        byte[] padded = new byte[256];
                        System.arraycopy(bytes, 0, padded, 0, Math.min(256, bytes.length));
                        output.write(padded);
                        break;
                    }

                    default:
                        throw new IllegalArgumentException("Unsupported Format: " + format);

                }
            } catch (Exception exception) {
                throw new RuntimeException("Failed to Pack Field: " + fieldName);
            }
        }

        return output.toByteArray();

    }


    /**
     * retrieving the path to the iPod 
     * @param shuffler the shuffler base path
     * @param fileName the file path
     * @return the relative path
     */
    public String pathToiPod(Shuffler shuffler, String fileName) {

        // check if filename is within the shuffler file path
        boolean doesContain = iPodShuffle.isPathPrefix(fileName, shuffler.path);
        if(!doesContain) {
            throw new IllegalArgumentException("Can't Get IPod Filename");
        }

        Path filePath = Paths.get(fileName).toAbsolutePath().normalize();
        Path shufflerPath = Paths.get(shuffler.path).toAbsolutePath().normalize();
        Path relativePath = shufflerPath.relativize(filePath);

        String relativeStringPath = relativePath.toString().replace(File.separatorChar, '/');
        return relativeStringPath;

    }


    /**
     * method that converts iPod path to computer path
     * @param shuffler the shuffler object
     * @param iPodName the string path for the ipod
     * @return a path object
     */
    public Path iPodToPath(Shuffler shuffler, String iPodName) {

        // .normalize() cleans up the resulting path
        Path shufflerPath = Paths.get(shuffler.path).toAbsolutePath().normalize();

        // .replace("/", File.separator) changes to system slashes
        Path fullPath = shufflerPath.resolve(iPodName.replace("/", File.separator)).normalize();

        return fullPath;

    }


    /**
     * method that returns the shuffler
     * @return the shuffler
     */
    public Shuffler shuffleDB() {
        return this.shuffler;
    }


    /**
     * method that returns the root directory
     * @return a path object
     */
    public Path basePath() {

        Path shufflerPath = Paths.get(this.shuffler.path).toAbsolutePath().normalize();
        return shufflerPath;

    }


    /**
     * method to return the list of tracks
     * @return a list of tracks
     */
    public List<String> returnTracks() {

        List<String> trackList = this.shuffler.getTracks();
        return trackList;

    }


    /**
     * method to return a list of albums
     * @return a list of albums
     */
    public List<String> returnAlbums() {

        List<String> albumList = this.shuffler.getAlbums();
        return albumList;

    }


    /**
     * method to return a list of lists
     * @return a list of lists
     */
    public List<String> returnLists() {

        List<String> list = this.shuffler.getLists();
        return list;
    }

    
}

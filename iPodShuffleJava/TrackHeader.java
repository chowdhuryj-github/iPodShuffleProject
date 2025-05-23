package iPodShuffleJava;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * class for TrackHeader
 */
public class TrackHeader extends Record {

    int baseOffset = 0;

    /**
     * constructor for the TrackHeader
     * @param shuffler the shuffler object
     */
    public TrackHeader(Shuffler shuffler) {

        super(shuffler);
        this.baseOffset = 0;

        this.struct = new LinkedHashMap<>();
        this.struct.put("header_id", new StructField("4s", "hths"));
        this.struct.put("total_length", new StructField("I", 0));
        this.struct.put("number_of_tracks", new StructField("I", 0));
        this.struct.put("unknown1", new StructField("Q", 0L));

    }

    /**
     * method for constructing the track header
     */
    public byte[] construct() {
        
            try {

                List<String> tracks = this.shuffler.getTracks();
                int numberOfTracks = tracks.size();
                int totalLength = 20 + (numberOfTracks * 4);

                this.setItem("number_of_tracks", numberOfTracks);
                this.setItem("total_length", 20 +totalLength);

                byte[] headerBytes = super.construct();

                ByteArrayOutputStream pointerTable = new ByteArrayOutputStream();
                ByteArrayOutputStream trackData = new ByteArrayOutputStream();

                for(String trackPath : tracks) {

                    Track track = new Track(this.shuffler);
                    System.out.println("[*] Adding Track: " + trackPath);
                    track.populate(this.shuffler, trackPath);

                    int pointer = this.baseOffset + totalLength + trackData.size();

                    ByteBuffer pointerBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
                    pointerBuffer.putInt(pointer);
                    pointerTable.write(pointerBuffer.array());

                    byte[] trackBytes = track.construct();
                    trackData.write(trackBytes);

                }

                ByteArrayOutputStream finalOutput = new ByteArrayOutputStream();
                finalOutput.write(headerBytes);
                finalOutput.write(pointerTable.toByteArray());
                finalOutput.write(trackData.toByteArray());
                return finalOutput.toByteArray();

        } catch(IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }
    
}

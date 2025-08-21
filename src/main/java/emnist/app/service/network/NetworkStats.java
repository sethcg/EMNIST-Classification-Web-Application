package emnist.app.service.network;

import java.io.Serializable;
import java.util.HashMap;

public class NetworkStats implements Serializable {

    public Integer imageNum = 0;

    public Double accuracy = 0.0;
    public Double loss = 0.0;

    public NetworkStats() { }

    public NetworkStats(Integer imageNum, Double accuracy, Double loss) {
        this.imageNum = imageNum;
        this.accuracy = accuracy;
        this.loss = loss;
    }

    public HashMap<String, String> getMappedObject(boolean hasNetwork) {
        return new HashMap<String, String>() {
            {
                put("hasNetwork", Boolean.toString(hasNetwork));
                put("imageNum", Integer.toString(imageNum));
                put("accuracy", Double.toString(accuracy));
                put("loss", Double.toString(loss));
            }
        };
    }

}

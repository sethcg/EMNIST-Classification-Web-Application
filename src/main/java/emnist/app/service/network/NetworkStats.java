package emnist.app.service.network;

import java.io.Serializable;
import java.util.HashMap;

public class NetworkStats implements Serializable{

    public boolean hasNetwork = false;

    public int imageNum = 0;

    public double accuracy, loss = 0.0f;

    public HashMap<String, String> getMappedObject() {
        return new HashMap<String, String>(){{
            put("hasNetwork", Boolean.toString(hasNetwork));
            put("imageNum", Integer.toString(imageNum));
            put("accuracy", Double.toString(accuracy));
            put("loss", Double.toString(loss));
        }};
    }

}

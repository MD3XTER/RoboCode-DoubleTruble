package nl.saxion.dhi1vsq3;

import java.io.Serializable;

public class TakeLeadMessage implements Serializable {
    boolean takeLead;

    public TakeLeadMessage(boolean takeLead) {
        this.takeLead = takeLead;
    }

    public boolean isTakeLead() {
        return takeLead;
    }
}

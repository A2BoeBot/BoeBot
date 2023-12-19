package hardware.other;

public class NeoPixel {
    private int address;
    private int rood;
    private int groen;
    private int blauw;

    public NeoPixel(int address) {
        this.address = address;
    }

    public int getAddress() {
        return address;
    }

    public int getRood() {
        return rood;
    }

    public int getGroen() {
        return groen;
    }

    public int getBlauw() {
        return blauw;
    }

    public void setRood(int rood) {
        this.rood = rood;
    }

    public void setGroen(int groen) {
        this.groen = groen;
    }

    public void setBlauw(int blauw) {
        this.blauw = blauw;
    }

    @Override
    public String toString() {
        return "NeoPixel{" +
                "address=" + address +
                ", rood=" + rood +
                ", groen=" + groen +
                ", blauw=" + blauw +
                '}';
    }
}

package hardware.sensoren;

public interface UltrasoonCallback {
    void gevaarAfstand(Ultrasoon ultrasoon);
    void clearAfstand(Ultrasoon ultrasoon);
    void stopAfstand(Ultrasoon ultrasoon);

}

package sensoren;

import TI.SerialConnection;
import robot.Updatable;

public class Bluetooth implements Updatable {

    private SerialConnection serial;
    private BluetoothCallback callback;

    public Bluetooth(int baud, BluetoothCallback callback) {
        this.serial = new SerialConnection(baud);
        this.callback = callback;
    }public Bluetooth(BluetoothCallback callback) {
        this.serial = new SerialConnection();
        this.callback = callback;
    }

    @Override
    public void update() {
        if (this.serial.available() != 0) {
            byte[] bytes = new byte[this.serial.available()];
            for (int i = 0; i < bytes.length; i++) {
                if (this.serial.available() != 0)
                    bytes[i] = ((byte) this.serial.readByte());
                else
                    bytes[i] = 0;
            }
            String str = new String(bytes);
            this.callback.tekstOntvangen(str);
        }
    }
}

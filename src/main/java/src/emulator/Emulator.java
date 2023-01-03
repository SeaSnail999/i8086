package src.emulator;

import src.emulator.cpu.Cpu;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


public class Emulator {
    
    private Memory mem;
    private Cpu cpu;
    
    private void init() throws SecurityException, IOException {
        mem = new Memory(0x100000);

        loadBIOS();
        
        cpu = new Cpu(mem);
    }

    void loadBIOS() {
        try {
            String biosFileName = "noname.bin_";
            InputStream fin = Emulator.class.getClassLoader().getResourceAsStream(biosFileName);
            //InputStream fin = new FileInputStream(new File(biosFileName));
            byte[] buf = new byte[1024];
            // k - всего прочитано байт
            int k = 0;
            while (k < buf.length) {
                // t - количество прочитанных байт, обычно 65536
                int t = fin.read(buf, k, buf.length - k);
                // ничего не прочитано
                if (t < 0)
                    break;
                // увеличивается по мере чтения биоса
                k += t;
            }
            System.out.println(Arrays.toString(buf));
            fin.close();
            byte[] xbuf = new byte[k];
            System.arraycopy(buf, 0, xbuf, 0, k);
            // Ведущие нули
            mem.loadData(0x100000 - k, xbuf);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: logger here;
        }
    }
    
    private void run() throws Exception {
        while (!cpu.halt) {
            cpu.step();
        }
    }

    public static void main(String[] args) throws Exception {
        Emulator emulator = new Emulator();
        emulator.init();
        System.out.println(emulator.cpu.state);
        emulator.run();
        System.out.println("success");
        //System.out.println(emulator.mem.toString());
    }
}

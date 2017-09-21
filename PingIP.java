import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PingIP {
    public static void main(String[] args) {
        String ip = "127.0.0.1";
        pingIP(ip);

        ip = "192.168.1.1";
        pingIP(ip);
    }

    public static void pingIP(String ip) {
        String pingResult = "";

        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        String pingCmd = "ping " + (isWindows? "-n" : "-c") + " 1 " + ip;

        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(pingCmd);

            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                pingResult += inputLine + "\n";
            }
            in.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

package configZAP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import JSandXML.JSONObject;
import JSandXML.XML;
import br.com.softplan.security.zap.zaproxy.clientapi.core.ApiResponse;
import br.com.softplan.security.zap.zaproxy.clientapi.core.ApiResponseElement;
import br.com.softplan.security.zap.zaproxy.clientapi.core.ClientApi;
import br.com.softplan.security.zap.zaproxy.clientapi.core.ClientApiException;

public class Zap {
    // test commit
    private String URL = "localhost";
    private int Port = 8500;
    private ClientApi api;
    private String target = "https://www.troyhunt.com";

    public Zap() {
        // Scanner line = new Scanner(System.in);
        // System.out.println("URL: ");
        // this.URL = line.nextLine();
        // System.out.println("Port: ");
        // this.Port = Integer.parseInt(line.nextLine());
        // System.out.println("Target: ");
        // this.target = line.nextLine();
        this.api = new ClientApi(this.URL, this.Port);
    }

    // Lance ZAPproxy
    public void startZAP() throws IOException {
        String line;
        String[] command = { "CMD", "/C", "C:/Program Files (x86)/OWASP/Zed Attack Proxy/zap.bat" };
        ProcessBuilder buildProc = new ProcessBuilder(command);
        buildProc.directory(new File("C:/Program Files (x86)/OWASP/Zed Attack Proxy"));
        Process p2 = buildProc.start();
        BufferedReader ipBuf = new BufferedReader(new InputStreamReader(p2.getInputStream()));
        OutputStreamWriter oStream = new OutputStreamWriter(p2.getOutputStream());
        oStream.write("buildProcess where name='ZAP.exe'");
        oStream.flush();
        oStream.close();/*
                         * while ((line = ipBuf.readLine()) != null) { if (line.contains("INFO") &&
                         * line.contains("org.parosproxy.paros.control.Control") && line.contains(
                         * "New Session")) { ipBuf.close(); break; } }
                         */
    }

    public void startSession() throws ClientApiException {

        // Date date = new Date();
        api.core.newSession("1214", "", "");
        api.core.saveSession("1214", "Test_ZAP_V1_00", "");
    }

    public void StartScan() throws ClientApiException, InterruptedException {

        ApiResponse resp = api.spider.scan("unc5f254dl5dnsetutqcaadttk", target, "100", "", "");
        api.pscan.enableAllScanners("1234");
        String scanid;
        int progress;
        scanid = ((ApiResponseElement) resp).getValue();

        // Poll the status until it completes
        while (true) {
            Thread.sleep(1000);
            progress = Integer
                    .parseInt(((ApiResponseElement) api.spider.status(scanid)).getValue());
            System.out.println("Spider progress : " + progress + "%");
            if (progress >= 100)
                break;
        }
        resp = api.ascan.scan("unc5f254dl5dnsetutqcaadttk", target, "true", "true", "", "", "");
        scanid = ((ApiResponseElement) resp).getValue();
        while (true) {
            Thread.sleep(10000);
            progress = Integer.parseInt(((ApiResponseElement) api.ascan.status(scanid)).getValue());
            System.out.println("ActiveScan progress : " + progress + "%");
            if (progress >= 100)
                break;
        }

    }

    public static String extractResponse(ApiResponse response) {
        return ((ApiResponseElement) response).getValue();
    }

    public void GeneratingReport()
            throws ClientApiException, FileNotFoundException, InterruptedException {

        // api.context.setContextInScope("1234", target, "false");
        String responseValue = extractResponse(api.pscan.recordsToScan());
        while (Integer.parseInt(responseValue) > 0) {
            Thread.sleep(1000);
            responseValue = extractResponse(api.pscan.recordsToScan());
        }
        String str = new String(api.core.xmlreport("1214"));
        JSONObject xmlJSONObj = XML.toJSONObject(str);
        System.out.println(xmlJSONObj.toString());
        Date date = new Date();
        String dt = new SimpleDateFormat("dd-kk-mm").format(date);
        PrintWriter out = new PrintWriter(
                "C:/Users/ssinquin/Desktop/testZap/ReportForSpiderWithActiveScan-" + dt + ".html");
        out.println(str);
        out.close();
        System.out.println("Rapport ReportForSpiderWithActiveScan-" + dt + " genere avec succes");
        api.core.shutdown("");

    }
}

package stupuid.maga.legacy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.StringTokenizer;
import org.apache.commons.net.SocketClient;

public class SimpleFTP {
    private static boolean DEBUG = false;
    private BufferedReader reader = null;
    private Socket socket = null;
    private BufferedWriter writer = null;

    public synchronized void connect(String host, String user, String pass) throws IOException {
        if (this.socket != null) {
            throw new IOException("SimpleFTP is already connected. Disconnect first.");
        }
        this.socket = new Socket(host, 21);
        this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        String response = readLine();
        if (response.startsWith("220 ")) {
            sendLine("USER " + user);
            response = readLine();
            if (response.startsWith("331 ")) {
                sendLine("PASS " + pass);
                response = readLine();
                if (!response.startsWith("230 ")) {
                    throw new IOException("230SimpleFTP was unable to log in with the supplied password: " + response);
                }
            } else {
                throw new IOException("331SimpleFTP received an unknown response after sending the user: " + response);
            }
        }
        throw new IOException("220SimpleFTP received an unknown response when connecting to the FTP server: " + response);
    }

    public synchronized void disconnect() throws IOException {
        try {
            sendLine("QUIT");
            this.socket = null;
        } catch (Throwable th) {
            this.socket = null;
        }
    }

    public synchronized String pwd() throws IOException {
        String dir;
        sendLine("PWD");
        dir = null;
        String response = readLine();
        if (response.startsWith("257 ")) {
            int firstQuote = response.indexOf(34);
            int secondQuote = response.indexOf(34, firstQuote + 1);
            if (secondQuote > 0) {
                dir = response.substring(firstQuote + 1, secondQuote);
            }
        }
        return dir;
    }

    public synchronized boolean cwd(String dir) throws IOException {
        sendLine("CWD " + dir);
        return readLine().startsWith("250 ");
    }

    public synchronized boolean stor(File file) throws IOException {
        if (file.isDirectory()) {
            throw new IOException("SimpleFTP cannot upload a directory.");
        }
        return stor(new FileInputStream(file), file.getName());
    }

    public synchronized boolean stor(InputStream inputStream, String filename) throws IOException {
        BufferedInputStream input = new BufferedInputStream(inputStream);
        sendLine("PASV");
        String response = readLine();
        if (response.startsWith("227 ")) {
            String ip = null;
            int port = -1;
            int opening = response.indexOf(40);
            int closing = response.indexOf(41, opening + 1);
            if (closing > 0) {
                StringTokenizer tokenizer = new StringTokenizer(response.substring(opening + 1, closing), ",");
                try {
                    ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "." + tokenizer.nextToken() + "." + tokenizer.nextToken();
                    port = (Integer.parseInt(tokenizer.nextToken()) * 256) + Integer.parseInt(tokenizer.nextToken());
                } catch (Exception e) {
                    throw new IOException("SimpleFTP received bad data link information: " + response);
                }
            }
            sendLine("STOR " + filename);
            Socket dataSocket = new Socket(ip, port);
            response = readLine();
            if (response.startsWith("150 ")) {
                BufferedOutputStream output = new BufferedOutputStream(dataSocket.getOutputStream());
                byte[] buffer = new byte[4096];
                while (true) {
                    int bytesRead = input.read(buffer);
                    if (bytesRead != -1) {
                        output.write(buffer, 0, bytesRead);
                    } else {
                        output.flush();
                        output.close();
                        input.close();
                    }
                }
            } else {
                throw new IOException("SimpleFTP was not allowed to send the file: " + response);
            }
        }
        throw new IOException("SimpleFTP could not request passive mode: " + response);
        return readLine().startsWith("226 ");
    }

    public synchronized boolean bin() throws IOException {
        sendLine("TYPE I");
        return readLine().startsWith("200 ");
    }

    public synchronized boolean ascii() throws IOException {
        sendLine("TYPE A");
        return readLine().startsWith("200 ");
    }

    private void sendLine(String line) throws IOException {
        if (this.socket == null) {
            throw new IOException("SimpleFTP is not connected.");
        }
        try {
            this.writer.write(line + SocketClient.NETASCII_EOL);
            this.writer.flush();
            if (DEBUG) {
                System.out.println("> " + line);
            }
        } catch (IOException e) {
            this.socket = null;
            throw e;
        }
    }

    private String readLine() throws IOException {
        String line = this.reader.readLine();
        if (DEBUG) {
            System.out.println("< " + line);
        }
        return line;
    }
}

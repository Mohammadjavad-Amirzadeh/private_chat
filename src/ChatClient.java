import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static BufferedReader in;
    private static PrintWriter out;

    public static void main(String[] args) throws Exception {
        // اتصال به سرور
        Socket socket = new Socket("localhost", 1234); // پورت سرور را بر روی ۱۲۳۴ تنظیم می‌کنیم
        System.out.println("Connected to the chat server");

        // ایجاد یک thread جدید برای خواندن پیام‌های سرور
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // خواندن نام کاربری از کاربر
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        // ارسال نام کاربری به سرور
        out.println(username);

        // خواندن پیام های سرور و نمایش آنها به کاربر
        Thread readThread = new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        readThread.start();

        // خواندن و ارسال پیام ها
        String inputLine;
        while ((inputLine = scanner.nextLine()) != null) {
            if (inputLine.startsWith("@")) { // اگر پیام خصوصی باشد
                int spaceIndex = inputLine.indexOf(" ");
                if (spaceIndex != -1) {
                    String recipient = inputLine.substring(1, spaceIndex); // نام کاربری دریافت کننده پیام
                    String message = inputLine.substring(spaceIndex + 1); // متن پیام خصوصی
                    out.println("@" + recipient + " " + message); // ارسال پیام خصوصی به دریافت کننده
                }
            } else { // اگر پیام عمومی باشد
                out.println(inputLine); // ارسال پیام عمومی به سرور
            }
        }
    }
}
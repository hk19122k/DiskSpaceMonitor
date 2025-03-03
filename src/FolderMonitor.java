import java.io.File;
import java.text.DecimalFormat;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class FolderMonitor {
    private static final String FOLDER_PATH = "C:\\Users\\HP\\Downloads\\Telegram Desktop"; // Change this to your folder path
    private static final long MAX_FOLDER_SIZE = 10L * 1024 * 1024 * 1024; // 10 GB limit (Change as needed)
    private static final double THRESHOLD_PERCENTAGE = 70.0; // Alert when usage exceeds 70%

    public static void main(String[] args) {
        long folderSize = getFolderSize(new File(FOLDER_PATH));
        double usagePercentage = (folderSize * 100.0) / MAX_FOLDER_SIZE;

        System.out.println("Folder Size: " + formatSize(folderSize));
        System.out.println("Usage: " + new DecimalFormat("#.##").format(usagePercentage) + "%");

        if (usagePercentage >= THRESHOLD_PERCENTAGE) {
            sendEmailAlert(folderSize, usagePercentage);
        }
    }

    // Function to get folder size
    private static long getFolderSize(File folder) {
        if (!folder.exists()) return 0;
        long size = 0;
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                size += file.isFile() ? file.length() : getFolderSize(file);
            }
        }
        return size;
    }

    // Function to format size in GB
    private static String formatSize(long bytes) {
        double sizeInGB = bytes / (1024.0 * 1024 * 1024);
        return new DecimalFormat("#.##").format(sizeInGB) + " GB";
    }

    // Function to send email
    private static void sendEmailAlert(long folderSize, double usagePercentage) {
        final String senderEmail = "lathaharis8@gmail.com"; // Change to your email
        final String password = "rbif ppwb ttwn jise"; // Use an App Password if using Gmail
        final String recipientEmail = "lathaharis8@gmail.com"; // Change to the recipient's email

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Alert: Folder Size Exceeded 70% Limit!");
            String emailBody = "⚠️ **WARNING: Storage Limit Exceeded!** ⚠️\n\n"
                    + "📂 **Folder Path:** " + FOLDER_PATH + "\n"
                    + "📊 **Usage:** " + new DecimalFormat("#.##").format(usagePercentage) + "% of allocated space\n"
                    + "💾 **Current Folder Size:** " + formatSize(folderSize) + "\n\n"
                    + "🔴 **Action Required:**\n"
                    + "  - Delete unnecessary files\n"
                    + "  - Move old data to another location\n"
                    + "  - Increase storage capacity if needed\n\n"
                    + "This is an automated alert. Please take immediate action to avoid storage issues.\n\n"
                    + "🔔 **System Alert Bot**";

   message.setText(emailBody);


            Transport.send(message);
            System.out.println("Alert email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

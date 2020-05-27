package Application.Database;

import Application.Models.User;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class UserUtils {
    DatabaseConnection db;
    PreparedStatement pSt;

    public UserUtils(DatabaseConnection db) {
        this.db = db;
    }

    public boolean userExists(String username) {
        // if user exists:
        if (findUser(username) == null) {
            return false;
        }
        // else:
        return true;
    }

    public int deleteUser(String username, char[] pw) {
        int error = 0;
        if (validateAccount(username, pw) != null) {
            try {
                String sql = "delete from users where username = ?";
                pSt = db.conn.prepareStatement(sql);
                pSt.setString(1, username);
                pSt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e);
                // database error
                error = 1;
            }
        } else {
            // username or password is not correct.
            error = 2;
        }
        return error;
    }

    public boolean updateEmail(String username, String email) {
        String sql = "update users set email = ? where username = ?";

        try {
            pSt = db.conn.prepareStatement(sql);
            pSt.setString(1, email);

            pSt.setString(2, username);

            pSt.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    // find a specific user, if not exists, return null:
    private User findUser(String username) {
        String query = "select * from users where username=?";
        User user = null;
        try {
            pSt = db.conn.prepareStatement(query);
            pSt.setString(1, username);
            ResultSet rs = pSt.executeQuery();

            if (rs.isBeforeFirst()) { // if there is a record
                user = new User();
                while (rs.next()) {
                    user.setHash(rs.getString(2));
                    user.setEmail(rs.getString(4));
                    user.setSalt(rs.getString(3));
                    user.setType(rs.getInt(5));
                    user.setUsername(rs.getString(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    // check account's validation when LOGGING IN, if valid, return User.
    public User validateAccount(String username, char[] password) {
        // first we find the user:
        User user = findUser(username);

        // then get the hash and salt
        if (user != null) {
            String userHash = user.getHash();
            String userSalt = user.getSalt();

            // generate new hash based on the in-put password:
            String new_hash = HashingUtils.generate_Hash_Using_PBKDF2(userSalt, password);

            // compare hashes:
            if (!new_hash.equals(userHash)) {
                user = null;
            }
        }
        return user;
    }

    public boolean createNewUser(String username, String email, int type) {
        String pw = HashingUtils.generateRandomPassword(6);
        try {
            String employee_code = generateEmployeeCode();
            insertUser(username, pw.toCharArray(), type, email, employee_code);
            System.out.println("Create user: " + username);
            System.out.println("Sending user info to " + email + " ...");
            String job = "employee";
            if (type == 1) {
                job = "admin";
            }
            String m = "Welcome to KRAKEN FORCE PRISON, new " + job + "!\nBelow are your account details:\n\nYOUR EMPLOYEE CODE: " + employee_code +
                    "\nYOUR USERNAME: " + username + "\nYOUR PASSWORD: " + pw + " (You should change your password after login)" +
                    "\n\nThis prison is full of dangerous criminals," +
                    " I hope you survive your first month, uwahahaha!\n\nGood luck,\nPrison Manager.";
            sendMessageToEmail("Welcome to Kraken Prison!",m, email, System.getenv("MAIL_USER"), System.getenv("MAIL_PW"));
            return true;

        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;

    }

    // add an user to database
    private void insertUser(String username, char[] pw, int type, String email, String employee_code) throws SQLException {
        if (findUser(username) != null) {
            // user already exists
            throw new SQLException("User exists");
        }

        String salt = HashingUtils.generateSalt();
        String hash = HashingUtils.generate_Hash_Using_PBKDF2(salt, pw);


        String sql = "insert into users(username, hash_password, salt, email, type, employee_code) values(?,?,?,?,?,?)";
        pSt = db.conn.prepareStatement(sql, pSt.RETURN_GENERATED_KEYS);
        pSt.setString(1, username);
        pSt.setString(2, hash);
        pSt.setString(3, salt);
        pSt.setString(4, email);
        pSt.setInt(5, type);
        pSt.setString(6, employee_code);
        int rs = pSt.executeUpdate();
        if (rs != 1) {
            throw new SQLException("Some thing went wrong. affected row is not 1");
        }
    }


    // send message to email using Google SMTP SERVER. We need a google account to send the message.
    // set the user and password of that account to the SYSTEM VARIABLE "MAIL_USER" and "MAIL_PW"
    public void sendMessageToEmail(String title, String message, String to_email, String from_email, String from_email_password) {
        String host = "smtp.gmail.com";

        Properties p = System.getProperties();
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.host", host);
        p.put("mail.smtp.user", from_email);
        p.put("mail.smtp.password", from_email_password);
        p.put("mail.smtp.port", "587");
        p.put("mail.smtp.auth", "true");

        Session s = Session.getDefaultInstance(p);

        try {
            // set up the message object:
            MimeMessage m = new MimeMessage(s);
            m.setFrom(new InternetAddress(from_email));
            m.addRecipient(Message.RecipientType.TO, new InternetAddress(to_email));

            m.setSubject(title);
            m.setText(message + "\n");

            //send email:
            Transport transport = s.getTransport("smtp");
            transport.connect(host, from_email, from_email_password);
            transport.sendMessage(m, m.getAllRecipients());

            System.out.println("Email sent successfully");
            transport.close();
        } catch (MessagingException me) {
            me.printStackTrace();
        }
    }

    public int changePassword(String username, char[] oldPw, char[] newPw) {
        int error = 0;
        if (validateAccount(username, oldPw) != null) {
            if (setNewPassword(username, newPw)) {
                System.out.println("Changed password successfully");
            } else {
                System.out.println("Failed changing password.");
                error = 1;
            }
        } else {
            System.out.println("Current password is not correct");
            error = 2;
        }
        // 0: success, 1: database error, 2: old password is not correct
        return error;
    }

    private boolean setNewPassword(String username, char[] newPW) {
        HashAndSalt hs = HashingUtils.getHashAndSaltFromPassword(newPW);
        String salt = hs.getSalt();
        String hash = hs.getHash();
        boolean rs = true;
        String sql = "update users set hash_password = ?, salt = ?  where username = ?";

        try {
            db.conn.setAutoCommit(false);
            pSt = db.conn.prepareStatement(sql);
            pSt.setString(1, hash);
            pSt.setString(2, salt);
            pSt.setString(3, username);
            int row = pSt.executeUpdate();
            if (row != 1) {
                throw new SQLException("Error updating");
            }
            db.conn.commit();
        } catch (SQLException e) {
            rs = false;
            try {
                db.conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                db.conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rs;
    }

    private boolean checkEmail(String username, String email) {
        email = email.trim();
        User u = findUser(username);
        if (u == null) {
            return false;
        }
        if (email.equals(u.getEmail())) {
            return true;
        }
        return false;
    }

    // reset user's password
    public int resetPassword(String username, String email) {
        int error = 0;
        if (checkEmail(username, email)) {
            String new_password = HashingUtils.generateRandomPassword(6);

            if (setNewPassword(username, new_password.toCharArray())) {
                // send them the password
                String m = "Your new password is : " + new_password;

                sendMessageToEmail("Your New Password" , m, email, System.getenv("MAIL_USER"), System.getenv("MAIL_PW"));

            } else {
                // database error resetting password
                error = 2;
            }
        } else {
            // username and email dont match!
            error = 1;
        }
        return error;
    }

    public String generateEmployeeCode() throws SQLException {
        String id = HashingUtils.generateRandomEmployeeCode();
        String sql = "select * from users where employee_code = ?";
        pSt = db.conn.prepareStatement(sql);
        pSt.setString(1, id);
        ResultSet rs = pSt.executeQuery();

        // if the id has not been taken (result set is empty):
        if (!rs.isBeforeFirst()) {
            return id;
            // else: keep doing the same thing using recursion.
        } else {
            return generateEmployeeCode();
        }
    }

}

package PRODIGY_03;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ContactManagement {
    private String name;
    private String phoneNumber;
    private String email;

    public ContactManagement(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\n" + "Phone number: " + phoneNumber + "\n" + "Email: " + email;
    }

    public static ContactManagement fromString(String contactString) {
        String[] lines = contactString.split("\n");
        String name = lines[0].split(": ")[1];
        String phoneNumber = lines[1].split(": ")[1];
        String email = lines[2].split(": ")[1];
        return new ContactManagement(name, phoneNumber, email);
    }
    public static void main(String[] args) {
        new MyFrame();
    }
}
class MyFrame extends JFrame implements ActionListener {
    private JButton button;
    private JPanel panel;
    private ArrayList<ContactManagement> contacts;
    private static final String FILE_NAME = "contacts.txt";

    public MyFrame() {
        contacts = new ArrayList<>();
        loadContacts();

        button = new JButton("Open Contact Management System");
        button.addActionListener(this);

        panel = new JPanel();
        panel.add(button);

        this.add(panel);
        this.setTitle("Contact Management System");
        this.setSize(400, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            String[] options = {"Add new contact", "View Contact List", "Edit Existing Contact(s)", "Delete Contact(s)"};
            String input = (String) JOptionPane.showInputDialog(this, "Choose an option", "Contact Management System",
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            switch (input) {
                case "Add new contact":
                    addNewContact();
                    break;
                case "View Contact List":
                    viewContactList();
                    break;
                case "Edit Existing Contact(s)":
                    editContact();
                    break;
                case "Delete Contact(s)":
                    deleteContact();
                    break;
            }
        }
    }

    private void addNewContact() {
        String name = JOptionPane.showInputDialog(this, "Enter Full Name:");
        String phone = JOptionPane.showInputDialog(this, "Enter Phone Number:");
        String email = JOptionPane.showInputDialog(this, "Enter Email Address:");
        contacts.add(new ContactManagement(name, phone, email));
        JOptionPane.showMessageDialog(null,"Contact added successfully");
        saveContacts();
    }

    private void viewContactList() {
        StringBuilder contactList = new StringBuilder();
        for (ContactManagement contact : contacts) {
            contactList.append(contact.toString()).append("\n\n");
        }
        JOptionPane.showMessageDialog(this, contactList.length() > 0 ? contactList.toString() : "No contacts found.");
    }

    private void editContact() {
        String name = JOptionPane.showInputDialog(this, "Enter the name of the contact to edit:");
        for (ContactManagement contact : contacts) {
            if (contact.getName().equalsIgnoreCase(name)) {
                String newName = JOptionPane.showInputDialog(this, "Enter new Full Name:", contact.getName());
                String newPhone = JOptionPane.showInputDialog(this, "Enter new Phone Number:", contact.getPhoneNumber());
                String newEmail = JOptionPane.showInputDialog(this, "Enter new Email Address:", contact.getEmail());
                contact.setName(newName);
                contact.setPhoneNumber(newPhone);
                contact.setEmail(newEmail);
                JOptionPane.showMessageDialog(null,"Contact edited successfully");
                saveContacts();
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Contact not found.");
    }

    private void deleteContact() {
        String name = JOptionPane.showInputDialog(this, "Enter the name of the contact to delete:");
        for (ContactManagement contact : contacts) {
            if (contact.getName().equalsIgnoreCase(name)) {
                contacts.remove(contact);
                JOptionPane.showMessageDialog(null,"Contact deleted successfully");
                saveContacts();
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Contact not found.");
    }

    private void saveContacts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (ContactManagement contact : contacts) {
                writer.write(contact.toString());
                writer.newLine();
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving contacts.");
        }
    }

    private void loadContacts() {
        contacts.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            StringBuilder contactString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    if (contactString.length() > 0) {
                        contacts.add(ContactManagement.fromString(contactString.toString()));
                        contactString.setLength(0);
                    }
                } else {
                    contactString.append(line).append("\n");
                }
            }
            if (contactString.length() > 0) {
                contacts.add(ContactManagement.fromString(contactString.toString()));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error!. Unable to retrieve the contact list.");
        }
    }
}

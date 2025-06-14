package view;

import DAO.AlunoDAO;
import DAO.TutorDAO;
import model.Aluno;
import model.Tutor;
import utils.SecurityUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder; // Importar LineBorder
import java.awt.*;

public class CadastroPanel extends JPanel {
    private CapacitaGUI parentFrame;

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeComboBox;
    private JTextField areaField;
    private JLabel areaLabel; 

    private AlunoDAO alunoDAO = new AlunoDAO();
    private TutorDAO tutorDAO = new TutorDAO();

    public CadastroPanel(CapacitaGUI parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 248, 255));

      
        JPanel registerFormPanel = new JPanel(new GridBagLayout());
        registerFormPanel.setBackground(Color.WHITE); 
        registerFormPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(150, 150, 150), 1), 
                new EmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL; 


        JLabel titleLabel = new JLabel("Novo Cadastro");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(65, 105, 225)); 
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER; 
        registerFormPanel.add(titleLabel, gbc);

       
        gbc.gridy++;
        gbc.insets = new Insets(25, 0, 15, 0); 
        registerFormPanel.add(Box.createRigidArea(new Dimension(0, 0)), gbc);

       
        Dimension fieldSize = new Dimension(280, 35);
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        
        
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel nameLabel = new JLabel("Nome:");
        nameLabel.setFont(labelFont);
        registerFormPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        nameField = new JTextField(20);
        nameField.setPreferredSize(fieldSize);
        nameField.setMaximumSize(fieldSize);
        nameField.setFont(fieldFont);
        registerFormPanel.add(nameField, gbc);

       
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        registerFormPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        emailField = new JTextField(20);
        emailField.setPreferredSize(fieldSize);
        emailField.setMaximumSize(fieldSize);
        emailField.setFont(fieldFont);
        registerFormPanel.add(emailField, gbc);

       
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel passwordLabel = new JLabel("Senha:");
        passwordLabel.setFont(labelFont);
        registerFormPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(fieldSize);
        passwordField.setMaximumSize(fieldSize);
        passwordField.setFont(fieldFont);
        registerFormPanel.add(passwordField, gbc);

       
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel userTypeLabel = new JLabel("Tipo de Usuário:");
        userTypeLabel.setFont(labelFont);
        registerFormPanel.add(userTypeLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        userTypeComboBox = new JComboBox<>(new String[]{"Aluno", "Tutor"});
        userTypeComboBox.setBackground(Color.WHITE);
        userTypeComboBox.setFont(fieldFont); 
        userTypeComboBox.setPreferredSize(fieldSize); 
        userTypeComboBox.setMaximumSize(fieldSize);
        registerFormPanel.add(userTypeComboBox, gbc);

     
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        areaLabel = new JLabel("Área (Tutor):"); 
        areaLabel.setFont(labelFont);
        areaLabel.setVisible(false);
        registerFormPanel.add(areaLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        areaField = new JTextField(20);
        areaField.setPreferredSize(fieldSize);
        areaField.setMaximumSize(fieldSize);
        areaField.setFont(fieldFont);
        areaField.setVisible(false);
        registerFormPanel.add(areaField, gbc);

        userTypeComboBox.addActionListener(e -> {
            boolean isTutor = "Tutor".equals(userTypeComboBox.getSelectedItem());
            areaLabel.setVisible(isTutor);
            areaField.setVisible(isTutor);
            registerFormPanel.revalidate(); 
            registerFormPanel.repaint();
           
            Window ancestorWindow = SwingUtilities.getWindowAncestor(this);
            if (ancestorWindow != null) {
                ancestorWindow.revalidate();
                ancestorWindow.repaint();
            }
        });

      
        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 10, 0);
        registerFormPanel.add(Box.createRigidArea(new Dimension(0, 0)), gbc);

       
        JButton registerButton = new JButton("Cadastrar");
        registerButton.setBackground(new Color(60, 179, 113)); 
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setPreferredSize(new Dimension(150, 45));
        registerButton.setMaximumSize(new Dimension(150, 45));
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        registerButton.addActionListener(e -> registerUser());
        registerFormPanel.add(registerButton, gbc);


        gbc.gridy++;
        gbc.insets = new Insets(15, 0, 5, 0);
        registerFormPanel.add(Box.createRigidArea(new Dimension(0, 0)), gbc);

       
        JButton backButton = new JButton("Voltar ao Login");
        backButton.setBackground(new Color(100, 149, 237)); 
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setPreferredSize(new Dimension(150, 40));
        backButton.setMaximumSize(new Dimension(150, 40));
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        backButton.addActionListener(e -> parentFrame.showLoginPanel());
        registerFormPanel.add(backButton, gbc);

       
        GridBagConstraints outerGbc = new GridBagConstraints();
        outerGbc.gridx = 0;
        outerGbc.gridy = 0;
        outerGbc.weightx = 1.0;
        outerGbc.weighty = 1.0;
        outerGbc.fill = GridBagConstraints.NONE;
        add(registerFormPanel, outerGbc);
    }

    private void registerUser() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String userType = (String) userTypeComboBox.getSelectedItem();
        String area = areaField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome, Email e Senha são obrigatórios.", "Erro de Cadastro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String hashedPassword = SecurityUtils.hashPassword(password);

            if ("Aluno".equals(userType)) {
                Aluno aluno = new Aluno();
                aluno.setNome(name);
                aluno.setEmail(email);
                aluno.setSenha(hashedPassword);
                alunoDAO.criar(aluno);
                JOptionPane.showMessageDialog(this, "Aluno cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                parentFrame.setLoggedInUser(aluno);
                parentFrame.showMainAppPanel();

            } else if ("Tutor".equals(userType)) {
                if (area.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Área de especialização é obrigatória para Tutores.", "Erro de Cadastro", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Tutor tutor = new Tutor();
                tutor.setNome(name);
                tutor.setEmail(email);
                tutor.setSenha(hashedPassword);
                tutor.setArea(area);
                tutorDAO.criar(tutor);
                JOptionPane.showMessageDialog(this, "Tutor cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                parentFrame.setLoggedInUser(tutor);
                parentFrame.showMainAppPanel();

            }
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        areaField.setText("");
        userTypeComboBox.setSelectedIndex(0);
    }
}
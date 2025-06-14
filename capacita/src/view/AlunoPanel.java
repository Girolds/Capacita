package view;

import DAO.AlunoDAO;
import DAO.CursoDAO;
import DAO.ModuloDAO;
import DAO.PostagemDAO;
import DAO.VideoAulaDAO;
import model.Aluno;
import model.Curso;
import model.Modulo;
import model.Postagem;
import model.VideoAula;
import model.Usuario; // Importar Usuario para setLoggedInUser
import utils.SecurityUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

import config.Conectante;
import main.CapacitaGUI;

public class AlunoPanel extends JPanel {
    private Aluno loggedInAluno;
    private CapacitaGUI parentFrame; 

    private JTextField nameProfileField;
    private JTextField emailProfileField;
    private JPasswordField passwordProfileField;

    private AlunoDAO alunoDAO = new AlunoDAO(); 
    private CursoDAO cursoDAO = new CursoDAO();
    private ModuloDAO moduloDAO = new ModuloDAO();
    private PostagemDAO postagemDAO = new PostagemDAO();
    private VideoAulaDAO videoAulaDAO = new VideoAulaDAO();

    private JList<Curso> availableCoursesList;
    private DefaultListModel<Curso> availableCoursesListModel;
    private JList<Curso> enrolledCoursesList;
    private DefaultListModel<Curso> enrolledCoursesListModel;

    private JPanel cardPanel; 
    private CardLayout cardLayout; 

    private CursoDetalhesPanelAluno cursoDetalhesPanelAluno; 

    private class CursoCellRenderer extends JPanel implements ListCellRenderer<Curso> {
        private JLabel titleLabel;
        private JLabel tutorLabel; 
        private JTextArea descriptionArea;
        private JLabel areaLabel;
        public JButton enrollButtonCell;
        public JButton detailsButtonCell;
        public JButton unenrollButtonCell;
        private JPanel buttonsPanel;

        private final Dimension BUTTON_SIZE_ENROLL = new Dimension(100, 28); 
        private final Dimension BUTTON_SIZE_INSCRITO = new Dimension(80, 28); 
        private final Dimension BUTTON_SIZE_DETAILS = new Dimension(110, 28); 
        private final Dimension BUTTON_SIZE_UNENROLL = new Dimension(140, 28); 

        public CursoCellRenderer() {
            setLayout(new BorderLayout(5, 5)); 
            setBorder(new EmptyBorder(5, 10, 5, 10)); 
            setBackground(Color.WHITE);
            
            setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(200, 200, 200), 1), 
                    new EmptyBorder(5, 10, 5, 10) 
            ));

            titleLabel = new JLabel();
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
            titleLabel.setForeground(new Color(50, 50, 150)); 

            tutorLabel = new JLabel(); 
            tutorLabel.setFont(new Font("Arial", Font.PLAIN, 11)); 
            tutorLabel.setForeground(new Color(150, 50, 50)); 

            descriptionArea = new JTextArea();
            descriptionArea.setFont(new Font("Arial", Font.PLAIN, 12)); 
            descriptionArea.setLineWrap(true); 
            descriptionArea.setWrapStyleWord(true); 
            descriptionArea.setEditable(false);
            descriptionArea.setBackground(getBackground()); 
            descriptionArea.setForeground(Color.BLACK); 
            descriptionArea.setRows(4); 
            descriptionArea.setMinimumSize(new Dimension(10, 60)); 
            descriptionArea.setPreferredSize(new Dimension(250, 60)); 

            areaLabel = new JLabel();
            areaLabel.setFont(new Font("Arial", Font.ITALIC, 11)); 
            areaLabel.setForeground(new Color(100, 100, 100)); 

            enrollButtonCell = new JButton(); 
            enrollButtonCell.setFont(new Font("Arial", Font.BOLD, 12));
            enrollButtonCell.setFocusPainted(false); 
            enrollButtonCell.setPreferredSize(BUTTON_SIZE_ENROLL); 
            enrollButtonCell.setMaximumSize(BUTTON_SIZE_ENROLL); 

            detailsButtonCell = new JButton("Ver Detalhes"); 
            detailsButtonCell.setFont(new Font("Arial", Font.BOLD, 12));
            detailsButtonCell.setFocusPainted(false); 
            detailsButtonCell.setPreferredSize(BUTTON_SIZE_DETAILS); 
            detailsButtonCell.setMaximumSize(BUTTON_SIZE_DETAILS); 
            detailsButtonCell.setBackground(new Color(70, 130, 180)); 
            detailsButtonCell.setForeground(Color.WHITE);

            unenrollButtonCell = new JButton("Cancelar Inscrição"); 
            unenrollButtonCell.setFont(new Font("Arial", Font.BOLD, 12));
            unenrollButtonCell.setFocusPainted(false);
            unenrollButtonCell.setPreferredSize(BUTTON_SIZE_UNENROLL);
            unenrollButtonCell.setMaximumSize(BUTTON_SIZE_UNENROLL);
            unenrollButtonCell.setBackground(new Color(220, 20, 60)); 
            unenrollButtonCell.setForeground(Color.WHITE);


            JPanel textContentAndInfoPanel = new JPanel();
            textContentAndInfoPanel.setLayout(new BoxLayout(textContentAndInfoPanel, BoxLayout.Y_AXIS)); 
            textContentAndInfoPanel.setBackground(getBackground());
            textContentAndInfoPanel.add(titleLabel);
            textContentAndInfoPanel.add(tutorLabel);
            textContentAndInfoPanel.add(descriptionArea);
            textContentAndInfoPanel.add(areaLabel);
            textContentAndInfoPanel.setBorder(new EmptyBorder(0, 0, 5, 0)); 

            buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)); 
            buttonsPanel.setBackground(getBackground());
            
            add(textContentAndInfoPanel, BorderLayout.CENTER);
            add(buttonsPanel, BorderLayout.SOUTH); 

            setPreferredSize(new Dimension(450, 180)); 
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Curso> list, Curso curso, int index, boolean isSelected, boolean cellHasFocus) {
            titleLabel.setText(curso.getTitulo());
            if (curso.getTutor() != null && curso.getTutor().getNome() != null) {
                tutorLabel.setText("Ministrado por: " + curso.getTutor().getNome());
            } else {
                tutorLabel.setText("Ministrado por: (Tutor não especificado)");
            }
            descriptionArea.setText(curso.getDescricao());
            areaLabel.setText("Área: " + curso.getArea());

            buttonsPanel.removeAll(); 
            
            boolean inscrito = (loggedInAluno != null && alunoDAO.isAlunoInscrito(loggedInAluno.getId(), curso.getId()));

            if (list == availableCoursesList) { 
                if (inscrito) {
                    enrollButtonCell.setText("Inscrito");
                    enrollButtonCell.setEnabled(false);
                    enrollButtonCell.setBackground(new Color(173, 216, 230)); 
                    enrollButtonCell.setForeground(Color.DARK_GRAY);
                    enrollButtonCell.setPreferredSize(BUTTON_SIZE_INSCRITO); 
                    enrollButtonCell.setMaximumSize(BUTTON_SIZE_INSCRITO);
                    detailsButtonCell.setVisible(true); 
                    detailsButtonCell.setEnabled(true);
                } else {
                    enrollButtonCell.setText("Inscrever-se");
                    enrollButtonCell.setEnabled(true);
                    enrollButtonCell.setBackground(new Color(34, 139, 34)); 
                    enrollButtonCell.setForeground(Color.WHITE);
                    enrollButtonCell.setPreferredSize(BUTTON_SIZE_ENROLL); 
                    enrollButtonCell.setMaximumSize(BUTTON_SIZE_ENROLL);
                    detailsButtonCell.setVisible(false); 
                    detailsButtonCell.setEnabled(false);
                }
                buttonsPanel.add(enrollButtonCell);
                if (detailsButtonCell.isVisible()) {
                    buttonsPanel.add(detailsButtonCell);
                }

            } else if (list == enrolledCoursesList) { 
                enrollButtonCell.setText("Inscrito"); 
                enrollButtonCell.setEnabled(false);
                enrollButtonCell.setBackground(new Color(173, 216, 230)); 
                enrollButtonCell.setForeground(Color.DARK_GRAY);
                enrollButtonCell.setPreferredSize(BUTTON_SIZE_INSCRITO); 
                enrollButtonCell.setMaximumSize(BUTTON_SIZE_INSCRITO);

                detailsButtonCell.setVisible(true); 
                detailsButtonCell.setEnabled(true);
                unenrollButtonCell.setVisible(true); 
                unenrollButtonCell.setEnabled(true);

                buttonsPanel.add(unenrollButtonCell); 
                buttonsPanel.add(detailsButtonCell); 
            }

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                titleLabel.setForeground(list.getSelectionForeground());
                tutorLabel.setForeground(list.getSelectionForeground()); 
                descriptionArea.setForeground(list.getSelectionForeground());
                areaLabel.setForeground(list.getSelectionForeground());
                descriptionArea.setBackground(list.getSelectionBackground());
                buttonsPanel.setBackground(list.getSelectionBackground()); 
            } else {
                setBackground(list.getBackground());
                titleLabel.setForeground(new Color(50, 50, 150));
                tutorLabel.setForeground(new Color(150, 150, 150)); 
                descriptionArea.setForeground(Color.BLACK);
                areaLabel.setForeground(new Color(100, 100, 100));
                descriptionArea.setBackground(list.getBackground());
                buttonsPanel.setBackground(list.getBackground()); 
            }
            return this;
        }
    }


    public AlunoPanel(CapacitaGUI parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout(10, 10)); 
        setBackground(new Color(240, 248, 255));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Bem-vindo(a), Aluno(a)!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS)); 
        sidebarPanel.setBackground(new Color(220, 230, 240)); 
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(sidebarPanel, BorderLayout.WEST); 

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(new Color(240, 248, 255));
        add(cardPanel, BorderLayout.CENTER); 

        JPanel availableCoursesContent = new JPanel(new BorderLayout(5, 5));
        availableCoursesContent.setBackground(new Color(240, 248, 255));
        JLabel availableCoursesLabel = new JLabel("Todos os Cursos Disponíveis:");
        availableCoursesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        availableCoursesContent.add(availableCoursesLabel, BorderLayout.NORTH);

        availableCoursesListModel = new DefaultListModel<>();
        availableCoursesList = new JList<>(availableCoursesListModel);
        availableCoursesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        availableCoursesList.setCellRenderer(new CursoCellRenderer()); 
        availableCoursesList.setFixedCellHeight(180); 
        availableCoursesList.setFixedCellWidth(450); 
        availableCoursesList.setLayoutOrientation(JList.VERTICAL); 

        availableCoursesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = availableCoursesList.locationToIndex(e.getPoint());
                if (index != -1) {
                    Curso clickedCourse = availableCoursesListModel.getElementAt(index);
                    
                    Rectangle cellBounds = availableCoursesList.getCellBounds(index, index);
                    
                    CursoCellRenderer tempRenderer = new CursoCellRenderer();

                    int clickX = e.getX();
                    int clickY = e.getY();

                    int buttonsPanelY = cellBounds.y + cellBounds.height - tempRenderer.BUTTON_SIZE_ENROLL.height - 5 - 5; 
                    int currentButtonX = cellBounds.x + 10; 

                    boolean inscrito = (loggedInAluno != null && alunoDAO.isAlunoInscrito(loggedInAluno.getId(), clickedCourse.getId()));
                    Dimension enrollButtonSize = inscrito ? tempRenderer.BUTTON_SIZE_INSCRITO : tempRenderer.BUTTON_SIZE_ENROLL;
                    
                    Rectangle enrollButtonArea = new Rectangle(currentButtonX, buttonsPanelY, enrollButtonSize.width, enrollButtonSize.height);

                    currentButtonX += enrollButtonSize.width + 5; 

                    Rectangle detailsButtonArea = null;
                    if (inscrito) { 
                        detailsButtonArea = new Rectangle(currentButtonX, buttonsPanelY, tempRenderer.BUTTON_SIZE_DETAILS.width, tempRenderer.BUTTON_SIZE_DETAILS.height);
                    }

                    if (enrollButtonArea.contains(clickX, clickY) && !inscrito) { 
                        performEnrollment(clickedCourse);
                    } else if (detailsButtonArea != null && detailsButtonArea.contains(clickX, clickY) && inscrito) { 
                        showCourseDetailsPanel(clickedCourse);
                    }
                }
            }
        });

        JScrollPane availableCoursesScrollPane = new JScrollPane(availableCoursesList);
        availableCoursesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
        availableCoursesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        availableCoursesContent.add(availableCoursesScrollPane, BorderLayout.CENTER);

        cardPanel.add(availableCoursesContent, "CursosDisponiveis"); 

        JPanel myCoursesContent = new JPanel(new BorderLayout(5, 5)); 
        myCoursesContent.setBackground(new Color(240, 248, 255));
        myCoursesContent.setBorder(new EmptyBorder(10, 10, 10, 10)); 

        JLabel myCoursesLabel = new JLabel("Meus Cursos Inscritos:");
        myCoursesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        myCoursesContent.add(myCoursesLabel, BorderLayout.NORTH);

        enrolledCoursesListModel = new DefaultListModel<>();
        enrolledCoursesList = new JList<>(enrolledCoursesListModel);
        enrolledCoursesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        enrolledCoursesList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        enrolledCoursesList.setCellRenderer(new CursoCellRenderer());
        enrolledCoursesList.setFixedCellHeight(180); 
        enrolledCoursesList.setFixedCellWidth(450); 

      
        enrolledCoursesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = enrolledCoursesList.locationToIndex(e.getPoint());
                if (index != -1) {
                    Curso clickedCourse = enrolledCoursesListModel.getElementAt(index);
                    
                    Rectangle cellBounds = enrolledCoursesList.getCellBounds(index, index);
                    CursoCellRenderer tempRenderer = new CursoCellRenderer();

                    int clickX = e.getX();
                    int clickY = e.getY();


                    int buttonsPanelY = cellBounds.y + cellBounds.height - tempRenderer.BUTTON_SIZE_UNENROLL.height - 5 - 5; 
                    int currentButtonX = cellBounds.x + 10; 

                    Rectangle unenrollButtonArea = new Rectangle(currentButtonX, buttonsPanelY, tempRenderer.BUTTON_SIZE_UNENROLL.width, tempRenderer.BUTTON_SIZE_UNENROLL.height);
                    currentButtonX += tempRenderer.BUTTON_SIZE_UNENROLL.width + 5;
                    Rectangle detailsButtonArea = new Rectangle(currentButtonX, buttonsPanelY, tempRenderer.BUTTON_SIZE_DETAILS.width, tempRenderer.BUTTON_SIZE_DETAILS.height);

                    if (unenrollButtonArea.contains(clickX, clickY)) {
                        performUnenrollment(clickedCourse);
                    } else if (detailsButtonArea.contains(clickX, clickY)) {
                        showCourseDetailsPanel(clickedCourse);
                    }
                }
            }
        });


        JScrollPane enrolledCoursesScrollPane = new JScrollPane(enrolledCoursesList);
        enrolledCoursesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
        enrolledCoursesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        myCoursesContent.add(enrolledCoursesScrollPane, BorderLayout.CENTER); 

        cardPanel.add(myCoursesContent, "MeusCursos"); 


        JPanel profileOuterPanel = new JPanel(new GridBagLayout());
        profileOuterPanel.setBackground(new Color(240, 248, 255));

        GridBagConstraints outerGbc = new GridBagConstraints();
        outerGbc.gridx = 0;
        outerGbc.gridy = 0;
        outerGbc.weightx = 1.0;
        outerGbc.weighty = 1.0;
        outerGbc.fill = GridBagConstraints.NONE;

        JPanel profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBackground(new Color(255, 255, 255));
        profilePanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(150, 150, 150), 1),
                new EmptyBorder(25, 25, 25, 25)
        ));

        GridBagConstraints profileGbc = new GridBagConstraints();
        profileGbc.insets = new Insets(10, 10, 10, 10);
        profileGbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel profileTitle = new JLabel("Editar Perfil");
        profileTitle.setFont(new Font("Arial", Font.BOLD, 28));
        profileTitle.setForeground(new Color(50, 50, 150));
        profileGbc.gridx = 0;
        profileGbc.gridy = 0;
        profileGbc.gridwidth = 2;
        profileGbc.anchor = GridBagConstraints.CENTER;
        profilePanel.add(profileTitle, profileGbc);

        profileGbc.gridy++;
        profileGbc.insets = new Insets(20, 0, 10, 0);
        profilePanel.add(Box.createRigidArea(new Dimension(0, 0)), profileGbc);

        Dimension fieldSize = new Dimension(250, 30);

        profileGbc.gridwidth = 1;
        profileGbc.gridx = 0;
        profileGbc.gridy++;
        profileGbc.anchor = GridBagConstraints.EAST;
        profilePanel.add(new JLabel("Nome:"), profileGbc);
        profileGbc.gridx = 1;
        profileGbc.anchor = GridBagConstraints.WEST;
        nameProfileField = new JTextField(20);
        nameProfileField.setPreferredSize(fieldSize);
        nameProfileField.setMaximumSize(fieldSize);
        profilePanel.add(nameProfileField, profileGbc);

        profileGbc.gridx = 0;
        profileGbc.gridy++;
        profileGbc.anchor = GridBagConstraints.EAST;
        profilePanel.add(new JLabel("Email:"), profileGbc);
        profileGbc.gridx = 1;
        profileGbc.anchor = GridBagConstraints.WEST;
        emailProfileField = new JTextField(20);
        emailProfileField.setPreferredSize(fieldSize);
        emailProfileField.setMaximumSize(fieldSize);
        profilePanel.add(emailProfileField, profileGbc);

        profileGbc.gridx = 0;
        profileGbc.gridy++;
        profileGbc.anchor = GridBagConstraints.EAST;
        profilePanel.add(new JLabel("Nova Senha:"), profileGbc);
        profileGbc.gridx = 1;
        profileGbc.anchor = GridBagConstraints.WEST;
        passwordProfileField = new JPasswordField(20);
        passwordProfileField.setPreferredSize(fieldSize);
        passwordProfileField.setMaximumSize(fieldSize);
        profilePanel.add(passwordProfileField, profileGbc);
      
        profileGbc.gridy++;
        profileGbc.insets = new Insets(20, 0, 10, 0);
        profilePanel.add(Box.createRigidArea(new Dimension(0, 0)), profileGbc);

        JButton updateProfileButton = new JButton("Atualizar Perfil");
        updateProfileButton.setBackground(new Color(60, 179, 113));
        updateProfileButton.setForeground(Color.WHITE);
        updateProfileButton.setFont(new Font("Arial", Font.BOLD, 16));
        updateProfileButton.setPreferredSize(new Dimension(150, 40));
        updateProfileButton.setMaximumSize(new Dimension(150, 40));
        profileGbc.gridx = 0;
        profileGbc.gridy++;
        profileGbc.gridwidth = 2;
        profileGbc.anchor = GridBagConstraints.CENTER;
        updateProfileButton.addActionListener(e -> {
            String newName = nameProfileField.getText();
            String newEmail = emailProfileField.getText();
            String newPassword = new String(passwordProfileField.getPassword());
            updateAlunoProfile(newName, newEmail, newPassword);

            setLoggedInAluno(loggedInAluno); 
        });
        profilePanel.add(updateProfileButton, profileGbc);
        profileOuterPanel.add(profilePanel, outerGbc);
        cardPanel.add(profileOuterPanel, "EditarPerfil"); 

      
        cursoDetalhesPanelAluno = new CursoDetalhesPanelAluno(); 
        cardPanel.add(cursoDetalhesPanelAluno, "CourseDetails"); 

        JButton btnAvailableCourses = createSidebarButton("Cursos Disponíveis");
        btnAvailableCourses.addActionListener(e -> {
            cardLayout.show(cardPanel, "CursosDisponiveis");
            loadAvailableCourses(); 
        });
        sidebarPanel.add(btnAvailableCourses);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5))); 

        JButton btnMyCourses = createSidebarButton("Meus Cursos");
        btnMyCourses.addActionListener(e -> {
            cardLayout.show(cardPanel, "MeusCursos");
            loadEnrolledCourses(); 
        });
        sidebarPanel.add(btnMyCourses);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5))); 

        JButton btnEditProfile = createSidebarButton("Editar Perfil");
        btnEditProfile.addActionListener(e -> {
            cardLayout.show(cardPanel, "EditarPerfil");
            nameProfileField.setText(loggedInAluno.getNome());
            emailProfileField.setText(loggedInAluno.getEmail());
            passwordProfileField.setText(""); 
        });
        sidebarPanel.add(btnEditProfile);
        sidebarPanel.add(Box.createVerticalGlue()); 

        cardLayout.show(cardPanel, "CursosDisponiveis");

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(220, 20, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.addActionListener(e -> parentFrame.logout());
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.setBackground(new Color(240, 248, 255));
        southPanel.add(logoutButton);
        add(southPanel, BorderLayout.SOUTH);
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(100, 149, 237)); 
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT); 
        button.setMaximumSize(new Dimension(180, 40)); 
        button.setPreferredSize(new Dimension(180, 40)); 
        return button;
    }

    public void showCourseDetailsPanel(Curso curso) {
 
        if (loggedInAluno != null) { 
            cursoDetalhesPanelAluno.setLoggedInUser(loggedInAluno);
        }
        cursoDetalhesPanelAluno.setCourse(curso); 
        cardLayout.show(cardPanel, "CourseDetails"); 
    }

    public void showAvailableCoursesPanel() {
        cardLayout.show(cardPanel, "CursosDisponiveis");
        loadAvailableCourses(); 
    }

    public void setLoggedInAluno(Aluno aluno) {
        this.loggedInAluno = aluno;
        if (loggedInAluno != null) {
            ((JLabel) getComponent(0)).setText("Bem-vindo(a), Aluno(a) " + loggedInAluno.getNome() + "!");
            loadAvailableCourses();
            loadEnrolledCourses();
     
            if (cursoDetalhesPanelAluno != null) {
                cursoDetalhesPanelAluno.setLoggedInUser(loggedInAluno);
            }

            nameProfileField.setText(loggedInAluno.getNome());
            emailProfileField.setText(loggedInAluno.getEmail());
            passwordProfileField.setText("");
        }
    }

    private void loadAvailableCourses() {
        availableCoursesListModel.clear();
        List<Curso> cursos = cursoDAO.listar(); 
        for (Curso curso : cursos) {
            availableCoursesListModel.addElement(curso);
        }
    }

    private void loadEnrolledCourses() {
        enrolledCoursesListModel.clear();
        try (Connection con = Conectante.createConnectionToMySQL()) {
            String sql = "SELECT c.id, c.titulo, c.descricao, c.area, c.id_tutor, u.nome AS nome_tutor " +
                         "FROM Curso c JOIN UsuarioCurso uc ON c.id = uc.id_curso " +
                         "LEFT JOIN Usuario u ON c.id_tutor = u.id " + 
                         "WHERE uc.id_usuario = ?";
            try (PreparedStatement pstm = con.prepareStatement(sql)) {
                pstm.setInt(1, loggedInAluno.getId());
                try (ResultSet rset = pstm.executeQuery()) {
                    while (rset.next()) {
                        Curso curso = new Curso();
                        curso.setId(rset.getInt("id"));
                        curso.setTitulo(rset.getString("titulo"));
                        curso.setDescricao(rset.getString("descricao"));
                        curso.setArea(rset.getString("area"));

                        int idTutor = rset.getInt("id_tutor");
                        if (!rset.wasNull()) {
                            model.Tutor tutor = new model.Tutor(); 
                            tutor.setId(idTutor);
                            tutor.setNome(rset.getString("nome_tutor"));
                            curso.setTutor(tutor);
                        }
                        
                        enrolledCoursesListModel.addElement(curso);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar cursos inscritos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void performEnrollment(Curso curso) {
        if (loggedInAluno == null) {
            JOptionPane.showMessageDialog(this, "Nenhum aluno logado para se inscrever.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try (Connection con = Conectante.createConnectionToMySQL()) {
            String checkSql = "SELECT COUNT(*) FROM UsuarioCurso WHERE id_usuario = ? AND id_curso = ?";
            try (PreparedStatement checkPstm = con.prepareStatement(checkSql)) {
                checkPstm.setInt(1, loggedInAluno.getId());
                checkPstm.setInt(2, curso.getId());
                try (ResultSet rs = checkPstm.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        JOptionPane.showMessageDialog(this, "Você já está inscrito(a) neste curso.", "Inscrição", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
            }

            String sql = "INSERT INTO UsuarioCurso (data_inscricao, estado, id_usuario, id_curso) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstm = con.prepareStatement(sql)) {
                pstm.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
                pstm.setString(2, "ativo");
                pstm.setInt(3, loggedInAluno.getId());
                pstm.setInt(4, curso.getId());
                pstm.execute();
                JOptionPane.showMessageDialog(this, "Inscrição no curso realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                loadAvailableCourses(); 
                loadEnrolledCourses(); 
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao se inscrever no curso: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void performUnenrollment(Curso curso) {
        if (loggedInAluno == null) {
            JOptionPane.showMessageDialog(this, "Nenhum aluno logado para cancelar a inscrição.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja cancelar a inscrição no curso '" + curso.getTitulo() + "'?",
                "Confirmar Cancelamento", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                alunoDAO.desinscreverCurso(loggedInAluno.getId(), curso.getId());
                JOptionPane.showMessageDialog(this, "Inscrição cancelada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                loadEnrolledCourses(); 
                loadAvailableCourses(); 
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao cancelar inscrição: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void updateAlunoProfile(String newName, String newEmail, String newPassword) {
        if (loggedInAluno == null) {
            JOptionPane.showMessageDialog(this, "Nenhum aluno logado para atualizar.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean updated = false;
        if (!newName.trim().isEmpty() && !newName.equals(loggedInAluno.getNome())) {
            loggedInAluno.setNome(newName);
            updated = true;
        }
        if (!newEmail.trim().isEmpty() && !newEmail.equals(loggedInAluno.getEmail())) {
            loggedInAluno.setEmail(newEmail);
            updated = true;
        }
        if (!newPassword.trim().isEmpty()) {
            loggedInAluno.setSenha(SecurityUtils.hashPassword(newPassword));
            updated = true;
        }

        if (updated) {
            try {
                alunoDAO.atualizar(loggedInAluno);
                JOptionPane.showMessageDialog(this, "Perfil atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar perfil: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nenhuma alteração foi feita no perfil.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
package view;

import DAO.CursoDAO;
import DAO.ModuloDAO;
import DAO.PostagemDAO;
import DAO.TutorDAO;
import DAO.VideoAulaDAO;
import main.CapacitaGUI;
import model.Curso;
import model.Modulo;
import model.Postagem;
import model.Tutor;
import model.VideoAula;
import utils.SecurityUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.sql.SQLException;

public class TutorPanel extends JPanel {
    private Tutor loggedInTutor;
    private CapacitaGUI parentFrame;

    private CursoDAO cursoDAO = new CursoDAO();
    private ModuloDAO moduloDAO = new ModuloDAO();
    private PostagemDAO postagemDAO = new PostagemDAO();
    private TutorDAO tutorDAO = new TutorDAO();
    private VideoAulaDAO videoAulaDAO = new VideoAulaDAO();

    private JList<Curso> myCoursesList;
    private DefaultListModel<Curso> myCoursesListModel;
    private CursoDetalhesPanelTutor cursoDetalhesPanelTutor;

    private JTextField createCourseTitleField;
    private JTextArea createCourseDescriptionArea;
    private JTextField createCourseAreaField;
    private JButton createCourseButton;

    private JComboBox<Curso> editDeleteCourseComboBox;
    private DefaultComboBoxModel<Curso> editDeleteCourseComboBoxModel;
    private JTextField editCourseTitleField;
    private JTextArea editCourseDescriptionArea;
    private JTextField editCourseAreaField;
    private JButton updateCourseButton;
    private JButton deleteCourseButton;

    private JComboBox<Curso> createModuloCourseComboBox;
    private DefaultComboBoxModel<Curso> createModuloCourseComboBoxModel;
    private JTextField createModuloTitleField;
    private JTextArea createModuloDescriptionArea;
    private JButton createModuloButton;

    private JComboBox<Curso> moduleCourseFilterComboBox;
    private DefaultComboBoxModel<Curso> moduleCourseFilterComboBoxModel;
    private JComboBox<Modulo> editDeleteModuloComboBox;
    private DefaultComboBoxModel<Modulo> editDeleteModuloComboBoxModel;
    private JTextField editModuloTitleField;
    private JTextArea editModuloDescriptionArea;
    private JButton updateModuloButton;
    private JButton deleteModuloButton;

    private JComboBox<Modulo> addVideoModuloComboBox;
    private DefaultComboBoxModel<Modulo> addVideoModuloComboBoxModel;
    private JTextField addVideoTitleField;
    private JTextField addVideoUrlField;
    private JButton addVideoButton;

    private JComboBox<Modulo> videoModuloFilterComboBox;
    private DefaultComboBoxModel<Modulo> videoModuloFilterComboBoxModel;
    private JComboBox<VideoAula> editDeleteVideoComboBox;
    private DefaultComboBoxModel<VideoAula> editDeleteVideoComboBoxModel;
    private JTextField editVideoTitleField;
    private JTextField editVideoUrlField;
    private JButton updateVideoButton;
    private JButton deleteVideoButton;

    private JTextField createPostagemTitleField;
    private JTextArea createPostagemContentArea;
    private JRadioButton postagemForCourseRadio;
    private JRadioButton postagemForModuloRadio;
    private ButtonGroup postagemTypeGroup;
    private JComboBox<Curso> postagemCourseComboBox;
    private DefaultComboBoxModel<Curso> postagemCourseComboBoxModel;
    private JComboBox<Modulo> postagemModuloComboBox;
    private DefaultComboBoxModel<Modulo> postagemModuloComboBoxModel;
    private JButton createPostagemButton;

    private JRadioButton editPostagemForCourseRadio;
    private JRadioButton editPostagemForModuloRadio;
    private ButtonGroup editPostagemTypeGroup;
    private JComboBox<Curso> editPostagemCourseFilterComboBox;
    private DefaultComboBoxModel<Curso> editPostagemCourseFilterComboBoxModel;
    private JComboBox<Modulo> editPostagemModuloFilterComboBox;
    private DefaultComboBoxModel<Modulo> editPostagemModuloFilterComboBoxModel;
    private JComboBox<Postagem> editDeletePostagemComboBox;
    private DefaultComboBoxModel<Postagem> editDeletePostagemComboBoxModel;
    private JTextField editPostagemTitleField;
    private JTextArea editPostagemContentArea;
    private JButton updatePostagemButton;
    private JButton deletePostagemButton;

    private JTextField nameProfileField;
    private JTextField emailProfileField;
    private JPasswordField passwordProfileField;
    private JTextField areaProfileField;

    private JPanel cardPanel;
    private CardLayout cardLayout;

    private final Dimension fieldSize = new Dimension(250, 30);
    private final Dimension textAreaSize = new Dimension(250, 80);
    private final Font labelFont = new Font("Arial", Font.PLAIN, 12);
    private final Font fieldFont = new Font("Arial", Font.PLAIN, 12);
    private final Font buttonFont = new Font("Arial", Font.BOLD, 14);
    private final Dimension buttonDim = new Dimension(150, 35);


    private class CursoCellRenderer extends JPanel implements ListCellRenderer<Curso> {
        private JLabel titleLabel;
        private JLabel tutorLabel;
        private JTextArea descriptionArea;
        private JLabel areaLabel;
        public JButton detailsButtonCell;
        public JButton editCourseButtonCell;
        private JPanel buttonsPanel;

        private final Dimension BUTTON_SIZE_DETAILS = new Dimension(110, 28);
        private final Dimension BUTTON_SIZE_EDIT = new Dimension(110, 28);

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

            detailsButtonCell = new JButton("Ver Detalhes");
            detailsButtonCell.setFont(new Font("Arial", Font.BOLD, 12));
            detailsButtonCell.setFocusPainted(false);
            detailsButtonCell.setPreferredSize(BUTTON_SIZE_DETAILS);
            detailsButtonCell.setMaximumSize(BUTTON_SIZE_DETAILS);
            detailsButtonCell.setBackground(new Color(70, 130, 180));
            detailsButtonCell.setForeground(Color.WHITE);

            editCourseButtonCell = new JButton("Editar Curso");
            editCourseButtonCell.setFont(new Font("Arial", Font.BOLD, 12));
            editCourseButtonCell.setFocusPainted(false);
            editCourseButtonCell.setPreferredSize(BUTTON_SIZE_EDIT);
            editCourseButtonCell.setMaximumSize(BUTTON_SIZE_EDIT);
            editCourseButtonCell.setBackground(new Color(255, 165, 0));
            editCourseButtonCell.setForeground(Color.WHITE);


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
        public Component getListCellRendererComponent(JList<? extends Curso> list, Curso value, int index, boolean isSelected, boolean cellHasFocus) {

            Curso curso = value;
            titleLabel.setText(curso.getTitulo());
            if (curso.getTutor() != null && curso.getTutor().getNome() != null) {
                tutorLabel.setText("Ministrado por: " + curso.getTutor().getNome());
            } else {
                tutorLabel.setText("Ministrado por: (Tutor não especificado)");
            }
            descriptionArea.setText(curso.getDescricao());
            areaLabel.setText("Área: " + curso.getArea());

            buttonsPanel.removeAll();

            buttonsPanel.add(detailsButtonCell);
            buttonsPanel.add(editCourseButtonCell);

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


    public TutorPanel(CapacitaGUI parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 248, 255));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Bem-vindo(a), Tutor(a)!");
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


        JPanel myTutorCoursesContent = new JPanel(new BorderLayout(5, 5));
        myTutorCoursesContent.setBackground(new Color(240, 248, 255));
        JLabel myCoursesLabel = new JLabel("Meus Cursos Criados:");
        myCoursesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        myTutorCoursesContent.add(myCoursesLabel, BorderLayout.NORTH);

        myCoursesListModel = new DefaultListModel<>();
        myCoursesList = new JList<>(myCoursesListModel);
        myCoursesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        myCoursesList.setCellRenderer(new CursoCellRenderer());
        myCoursesList.setFixedCellHeight(180);
        myCoursesList.setFixedCellWidth(450);
        myCoursesList.setLayoutOrientation(JList.VERTICAL);

        myCoursesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = myCoursesList.locationToIndex(e.getPoint());
                if (index != -1) {
                    Curso clickedCourse = myCoursesListModel.getElementAt(index);

                    Rectangle cellBounds = myCoursesList.getCellBounds(index, index);
                    CursoCellRenderer tempRenderer = new CursoCellRenderer();

                    int clickX = e.getX();
                    int clickY = e.getY();

                   
                    int buttonsPanelHeight = tempRenderer.BUTTON_SIZE_DETAILS.height + 5; 
                    int buttonsPanelY = cellBounds.y + cellBounds.height - buttonsPanelHeight;
                    int currentButtonX = cellBounds.x + 10; 

                    Rectangle detailsButtonArea = new Rectangle(currentButtonX, buttonsPanelY, tempRenderer.BUTTON_SIZE_DETAILS.width, tempRenderer.BUTTON_SIZE_DETAILS.height);

                    currentButtonX += tempRenderer.BUTTON_SIZE_DETAILS.width + 5; 

                    Rectangle editButtonArea = new Rectangle(currentButtonX, buttonsPanelY, tempRenderer.BUTTON_SIZE_EDIT.width, tempRenderer.BUTTON_SIZE_EDIT.height);

                    if (detailsButtonArea.contains(clickX, clickY)) {
                        showCourseDetailsPanel(clickedCourse);
                    } else if (editButtonArea.contains(clickX, clickY)) {
                        navigateToManageCoursesAndSelect(clickedCourse);
                    }
                }
            }
        });


        JScrollPane myTutorCoursesScrollPane = new JScrollPane(myCoursesList);
        myTutorCoursesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        myTutorCoursesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        myTutorCoursesContent.add(myTutorCoursesScrollPane, BorderLayout.CENTER);
        cardPanel.add(myTutorCoursesContent, "MeusCursosTutor");


       
        JPanel manageCoursesPanel = new JPanel();
        manageCoursesPanel.setLayout(new GridBagLayout()); 
        manageCoursesPanel.setBackground(new Color(240, 248, 255));
        manageCoursesPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.insets = new Insets(10, 0, 10, 0); 
        mainGbc.fill = GridBagConstraints.HORIZONTAL;
        mainGbc.gridx = 0;
        mainGbc.weightx = 1.0;

      
        JPanel createCourseSubPanel = new JPanel(new GridBagLayout());
        createCourseSubPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(65, 105, 225)), "Criar Novo Curso", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), new Color(65, 105, 225)));
        createCourseSubPanel.setBackground(Color.WHITE);
        GridBagConstraints cccGbc = new GridBagConstraints();
        cccGbc.insets = new Insets(8, 8, 8, 8);
        cccGbc.fill = GridBagConstraints.HORIZONTAL;

        cccGbc.gridx = 0; cccGbc.gridy = 0; cccGbc.anchor = GridBagConstraints.EAST; createCourseSubPanel.add(new JLabel("Título:", JLabel.RIGHT), cccGbc);
        cccGbc.gridx = 1; cccGbc.anchor = GridBagConstraints.WEST; cccGbc.weightx = 1.0; createCourseTitleField = new JTextField(20); createCourseTitleField.setPreferredSize(fieldSize); createCourseTitleField.setFont(fieldFont); createCourseSubPanel.add(createCourseTitleField, cccGbc);

        cccGbc.gridx = 0; cccGbc.gridy = 1; cccGbc.anchor = GridBagConstraints.EAST; cccGbc.weightx = 0;
        createCourseSubPanel.add(new JLabel("Descrição:", JLabel.RIGHT), cccGbc);
        cccGbc.gridx = 1; cccGbc.anchor = GridBagConstraints.WEST; cccGbc.weightx = 1.0; cccGbc.weighty = 1.0;
        createCourseDescriptionArea = new JTextArea(3, 20); createCourseDescriptionArea.setLineWrap(true); createCourseDescriptionArea.setWrapStyleWord(true);
        createCourseDescriptionArea.setFont(fieldFont);
        JScrollPane createDescScrollPane = new JScrollPane(createCourseDescriptionArea);
        createDescScrollPane.setPreferredSize(textAreaSize);
        createCourseSubPanel.add(createDescScrollPane, cccGbc);

        cccGbc.gridx = 0; cccGbc.gridy = 2; cccGbc.anchor = GridBagConstraints.EAST; cccGbc.weighty = 0;
        createCourseSubPanel.add(new JLabel("Área:", JLabel.RIGHT), cccGbc);
        cccGbc.gridx = 1; cccGbc.anchor = GridBagConstraints.WEST; cccGbc.weightx = 1.0; createCourseAreaField = new JTextField(20); createCourseAreaField.setPreferredSize(fieldSize); createCourseAreaField.setFont(fieldFont); createCourseSubPanel.add(createCourseAreaField, cccGbc);

        cccGbc.gridx = 0; cccGbc.gridy = 3; cccGbc.gridwidth = 2; cccGbc.anchor = GridBagConstraints.CENTER; cccGbc.weightx = 0;
        createCourseButton = new JButton("Criar Curso");
        createCourseButton.setBackground(new Color(60, 179, 113));
        createCourseButton.setForeground(Color.WHITE);
        createCourseButton.setFont(buttonFont);
        createCourseButton.setPreferredSize(buttonDim);
        createCourseButton.addActionListener(e -> createCourse());
        createCourseSubPanel.add(createCourseButton, cccGbc);

        mainGbc.gridy = 0;
        manageCoursesPanel.add(createCourseSubPanel, mainGbc);

        
        JPanel editDeleteCourseSubPanel = new JPanel(new GridBagLayout());
        editDeleteCourseSubPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(65, 105, 225)), "Editar / Excluir Curso", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), new Color(65, 105, 225)));
        editDeleteCourseSubPanel.setBackground(Color.WHITE);
        GridBagConstraints edcCbc = new GridBagConstraints();
        edcCbc.insets = new Insets(8, 8, 8, 8);
        edcCbc.fill = GridBagConstraints.HORIZONTAL;

        edcCbc.gridx = 0; edcCbc.gridy = 0; edcCbc.anchor = GridBagConstraints.EAST; editDeleteCourseSubPanel.add(new JLabel("Selecionar Curso:", JLabel.RIGHT), edcCbc);
        edcCbc.gridx = 1; edcCbc.anchor = GridBagConstraints.WEST; edcCbc.weightx = 1.0; editDeleteCourseComboBoxModel = new DefaultComboBoxModel<>();
        editDeleteCourseComboBox = new JComboBox<>(editDeleteCourseComboBoxModel);
        editDeleteCourseComboBox.setPreferredSize(fieldSize); editDeleteCourseComboBox.setFont(fieldFont);
        editDeleteCourseComboBox.addActionListener(e -> loadCourseDetailsForEdit());
        editDeleteCourseSubPanel.add(editDeleteCourseComboBox, edcCbc);

        edcCbc.gridx = 0; edcCbc.gridy = 1; edcCbc.anchor = GridBagConstraints.EAST; edcCbc.weightx = 0;
        editDeleteCourseSubPanel.add(new JLabel("Título:", JLabel.RIGHT), edcCbc);
        edcCbc.gridx = 1; edcCbc.anchor = GridBagConstraints.WEST; edcCbc.weightx = 1.0; editCourseTitleField = new JTextField(20); editCourseTitleField.setPreferredSize(fieldSize); editCourseTitleField.setFont(fieldFont); editDeleteCourseSubPanel.add(editCourseTitleField, edcCbc);

        edcCbc.gridx = 0; edcCbc.gridy = 2; edcCbc.anchor = GridBagConstraints.EAST; edcCbc.weightx = 0;
        editDeleteCourseSubPanel.add(new JLabel("Descrição:", JLabel.RIGHT), edcCbc);
        edcCbc.gridx = 1; edcCbc.anchor = GridBagConstraints.WEST; edcCbc.weightx = 1.0; edcCbc.weighty = 1.0;
        editCourseDescriptionArea = new JTextArea(3, 20); editCourseDescriptionArea.setLineWrap(true); editCourseDescriptionArea.setWrapStyleWord(true);
        editCourseDescriptionArea.setFont(fieldFont);
        JScrollPane editDescScrollPane = new JScrollPane(editCourseDescriptionArea);
        editDescScrollPane.setPreferredSize(textAreaSize);
        editDeleteCourseSubPanel.add(editDescScrollPane, edcCbc);

        edcCbc.gridx = 0; edcCbc.gridy = 3; edcCbc.anchor = GridBagConstraints.EAST; edcCbc.weighty = 0;
        editDeleteCourseSubPanel.add(new JLabel("Área:", JLabel.RIGHT), edcCbc);
        edcCbc.gridx = 1; edcCbc.anchor = GridBagConstraints.WEST; edcCbc.weightx = 1.0; editCourseAreaField = new JTextField(20); editCourseAreaField.setPreferredSize(fieldSize); editCourseAreaField.setFont(fieldFont); editDeleteCourseSubPanel.add(editCourseAreaField, edcCbc);

        edcCbc.gridx = 0; edcCbc.gridy = 4; edcCbc.gridwidth = 1; edcCbc.anchor = GridBagConstraints.CENTER; edcCbc.weightx = 0;
        updateCourseButton = new JButton("Atualizar");
        updateCourseButton.setBackground(new Color(70, 130, 180));
        updateCourseButton.setForeground(Color.WHITE);
        updateCourseButton.setFont(buttonFont);
        updateCourseButton.setPreferredSize(buttonDim);
        updateCourseButton.addActionListener(e -> updateCourse());
        editDeleteCourseSubPanel.add(updateCourseButton, edcCbc);

        edcCbc.gridx = 1; edcCbc.gridy = 4; edcCbc.gridwidth = 1; edcCbc.anchor = GridBagConstraints.CENTER; edcCbc.weightx = 0;
        deleteCourseButton = new JButton("Excluir");
        deleteCourseButton.setBackground(new Color(220, 20, 60));
        deleteCourseButton.setForeground(Color.WHITE);
        deleteCourseButton.setFont(buttonFont);
        deleteCourseButton.setPreferredSize(buttonDim);
        deleteCourseButton.addActionListener(e -> {
            try {
                deleteCourse();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir curso: " + e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });
        editDeleteCourseSubPanel.add(deleteCourseButton, edcCbc);

        mainGbc.gridy = 1;
        manageCoursesPanel.add(editDeleteCourseSubPanel, mainGbc);
        cardPanel.add(manageCoursesPanel, "GerenciarCursos");


        
        JPanel modulesPanel = new JPanel();
        modulesPanel.setLayout(new GridBagLayout());
        modulesPanel.setBackground(new Color(240, 248, 255));
        modulesPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        mainGbc.gridy = 0; 
        JPanel createModuloSubPanel = new JPanel(new GridBagLayout());
        createModuloSubPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(65, 105, 225)), "Criar Novo Módulo", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), new Color(50, 50, 150)));
        createModuloSubPanel.setBackground(Color.WHITE);
        GridBagConstraints cmGbc = new GridBagConstraints();
        cmGbc.insets = new Insets(8, 8, 8, 8);
        cmGbc.fill = GridBagConstraints.HORIZONTAL;

        cmGbc.gridx = 0; cmGbc.gridy = 0; cmGbc.anchor = GridBagConstraints.EAST; createModuloSubPanel.add(new JLabel("Curso:", JLabel.RIGHT), cmGbc);
        cmGbc.gridx = 1; cmGbc.anchor = GridBagConstraints.WEST; cmGbc.weightx = 1.0; createModuloCourseComboBoxModel = new DefaultComboBoxModel<>();
        createModuloCourseComboBox = new JComboBox<>(createModuloCourseComboBoxModel);
        createModuloCourseComboBox.setPreferredSize(fieldSize); createModuloCourseComboBox.setFont(fieldFont);
        createModuloSubPanel.add(createModuloCourseComboBox, cmGbc);

        cmGbc.gridx = 0; cmGbc.gridy = 1; cmGbc.anchor = GridBagConstraints.EAST; cmGbc.weightx = 0;
        createModuloSubPanel.add(new JLabel("Título:", JLabel.RIGHT), cmGbc);
        cmGbc.gridx = 1; cmGbc.anchor = GridBagConstraints.WEST; cmGbc.weightx = 1.0; createModuloTitleField = new JTextField(20); createModuloTitleField.setPreferredSize(fieldSize); createModuloTitleField.setFont(fieldFont); createModuloSubPanel.add(createModuloTitleField, cmGbc);

        cmGbc.gridx = 0; cmGbc.gridy = 2; cmGbc.anchor = GridBagConstraints.EAST; cmGbc.weightx = 0;
        createModuloSubPanel.add(new JLabel("Descrição:", JLabel.RIGHT), cmGbc);
        cmGbc.gridx = 1; cmGbc.anchor = GridBagConstraints.WEST; cmGbc.weightx = 1.0; cmGbc.weighty = 1.0;
        createModuloDescriptionArea = new JTextArea(3, 20); createModuloDescriptionArea.setLineWrap(true); createModuloDescriptionArea.setWrapStyleWord(true); createModuloDescriptionArea.setPreferredSize(textAreaSize); createModuloDescriptionArea.setFont(fieldFont); createModuloSubPanel.add(new JScrollPane(createModuloDescriptionArea), cmGbc);

        cmGbc.gridx = 0; cmGbc.gridy = 3; cmGbc.gridwidth = 2; cmGbc.anchor = GridBagConstraints.CENTER; cmGbc.weighty = 0; cmGbc.weightx = 0;
        createModuloButton = new JButton("Criar Módulo");
        createModuloButton.setBackground(new Color(60, 179, 113));
        createModuloButton.setForeground(Color.WHITE);
        createModuloButton.setFont(buttonFont);
        createModuloButton.setPreferredSize(buttonDim);
        createModuloButton.addActionListener(e -> createModulo());
        createModuloSubPanel.add(createModuloButton, cmGbc);

        mainGbc.gridy = 0;
        modulesPanel.add(createModuloSubPanel, mainGbc);

        JPanel editDeleteModuloSubPanel = new JPanel(new GridBagLayout());
        editDeleteModuloSubPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(65, 105, 225)), "Editar / Excluir Módulo", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), new Color(50, 50, 150)));
        editDeleteModuloSubPanel.setBackground(Color.WHITE);
        GridBagConstraints edmGbc = new GridBagConstraints();
        edmGbc.insets = new Insets(8, 8, 8, 8);
        edmGbc.fill = GridBagConstraints.HORIZONTAL;

        edmGbc.gridx = 0; edmGbc.gridy = 0; edmGbc.anchor = GridBagConstraints.EAST; editDeleteModuloSubPanel.add(new JLabel("Filtrar por Curso:", JLabel.RIGHT), edmGbc);
        edmGbc.gridx = 1; edmGbc.anchor = GridBagConstraints.WEST; edmGbc.weightx = 1.0; moduleCourseFilterComboBoxModel = new DefaultComboBoxModel<>();
        moduleCourseFilterComboBox = new JComboBox<>(moduleCourseFilterComboBoxModel);
        moduleCourseFilterComboBox.setPreferredSize(fieldSize); moduleCourseFilterComboBox.setFont(fieldFont);
        moduleCourseFilterComboBox.addActionListener(e -> {
            try {
                loadModulesForEditDelete();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar módulos: " + e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });
        editDeleteModuloSubPanel.add(moduleCourseFilterComboBox, edmGbc);

        edmGbc.gridx = 0; edmGbc.gridy = 1; edmGbc.anchor = GridBagConstraints.EAST; edmGbc.weightx = 0;
        editDeleteModuloSubPanel.add(new JLabel("Selecionar Módulo:", JLabel.RIGHT), edmGbc);
        edmGbc.gridx = 1; edmGbc.anchor = GridBagConstraints.WEST; edmGbc.weightx = 1.0; editDeleteModuloComboBoxModel = new DefaultComboBoxModel<>();
        editDeleteModuloComboBox = new JComboBox<>(editDeleteModuloComboBoxModel);
        editDeleteModuloComboBox.setPreferredSize(fieldSize); editDeleteModuloComboBox.setFont(fieldFont);
        editDeleteModuloComboBox.addActionListener(e -> loadModuloDetailsForEdit());
        editDeleteModuloSubPanel.add(editDeleteModuloComboBox, edmGbc);

        edmGbc.gridx = 0; edmGbc.gridy = 2; edmGbc.anchor = GridBagConstraints.EAST; edmGbc.weightx = 0;
        editDeleteModuloSubPanel.add(new JLabel("Título:", JLabel.RIGHT), edmGbc);
        edmGbc.gridx = 1; edmGbc.anchor = GridBagConstraints.WEST; edmGbc.weightx = 1.0; editModuloTitleField = new JTextField(20); editModuloTitleField.setPreferredSize(fieldSize); editModuloTitleField.setFont(fieldFont); editDeleteModuloSubPanel.add(editModuloTitleField, edmGbc);

        edmGbc.gridx = 0; edmGbc.gridy = 3; edmGbc.anchor = GridBagConstraints.EAST; edmGbc.weightx = 0;
        editDeleteModuloSubPanel.add(new JLabel("Descrição:", JLabel.RIGHT), edmGbc);
        edmGbc.gridx = 1; edmGbc.anchor = GridBagConstraints.WEST; edmGbc.weightx = 1.0; edmGbc.weighty = 1.0;
        editModuloDescriptionArea = new JTextArea(3, 20); editModuloDescriptionArea.setLineWrap(true); editModuloDescriptionArea.setWrapStyleWord(true); editModuloDescriptionArea.setPreferredSize(textAreaSize); editModuloDescriptionArea.setFont(fieldFont); editDeleteModuloSubPanel.add(new JScrollPane(editModuloDescriptionArea), edmGbc);

        edmGbc.gridx = 0; edmGbc.gridy = 4; edmGbc.gridwidth = 1; edmGbc.anchor = GridBagConstraints.CENTER; edmGbc.weighty = 0; edmGbc.weightx = 0;
        updateModuloButton = new JButton("Atualizar");
        updateModuloButton.setBackground(new Color(70, 130, 180));
        updateModuloButton.setForeground(Color.WHITE);
        updateModuloButton.setFont(buttonFont);
        updateModuloButton.setPreferredSize(buttonDim);
        updateModuloButton.addActionListener(e -> {
            try {
                updateModulo();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar módulo: " + e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });
        editDeleteModuloSubPanel.add(updateModuloButton, edmGbc);

        edmGbc.gridx = 1; edmGbc.gridy = 4; edmGbc.gridwidth = 1; edmGbc.anchor = GridBagConstraints.CENTER; edmGbc.weightx = 0;
        deleteModuloButton = new JButton("Excluir");
        deleteModuloButton.setBackground(new Color(220, 20, 60));
        deleteModuloButton.setForeground(Color.WHITE);
        deleteModuloButton.setFont(buttonFont);
        deleteModuloButton.setPreferredSize(buttonDim);
        deleteModuloButton.addActionListener(e -> {
            try {
                deleteModulo();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir módulo: " + e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });
        editDeleteModuloSubPanel.add(deleteModuloButton, edmGbc);
        mainGbc.gridy = 1;
        modulesPanel.add(editDeleteModuloSubPanel, mainGbc);
        cardPanel.add(modulesPanel, "Modulos");

       
        JPanel videoPanel = new JPanel();
        videoPanel.setLayout(new GridBagLayout()); 
        videoPanel.setBackground(new Color(240, 248, 255));
        videoPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        mainGbc.gridy = 0; 
        JPanel addVideoSubPanel = new JPanel(new GridBagLayout());
        addVideoSubPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(65, 105, 225)), "Adicionar Nova Videoaula", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), new Color(50, 50, 150)));
        addVideoSubPanel.setBackground(Color.WHITE);
        GridBagConstraints avGbc = new GridBagConstraints();
        avGbc.insets = new Insets(8, 8, 8, 8);
        avGbc.fill = GridBagConstraints.HORIZONTAL;

        avGbc.gridx = 0; avGbc.gridy = 0; avGbc.anchor = GridBagConstraints.EAST; addVideoSubPanel.add(new JLabel("Módulo:", JLabel.RIGHT), avGbc);
        avGbc.gridx = 1; avGbc.anchor = GridBagConstraints.WEST; avGbc.weightx = 1.0; addVideoModuloComboBoxModel = new DefaultComboBoxModel<>();
        addVideoModuloComboBox = new JComboBox<>(addVideoModuloComboBoxModel);
        addVideoModuloComboBox.setPreferredSize(fieldSize); addVideoModuloComboBox.setFont(fieldFont);
        addVideoSubPanel.add(addVideoModuloComboBox, avGbc);

        avGbc.gridx = 0; avGbc.gridy = 1; avGbc.anchor = GridBagConstraints.EAST; avGbc.weightx = 0;
        addVideoSubPanel.add(new JLabel("Título:", JLabel.RIGHT), avGbc);
        avGbc.gridx = 1; avGbc.anchor = GridBagConstraints.WEST; avGbc.weightx = 1.0; addVideoTitleField = new JTextField(20); addVideoTitleField.setPreferredSize(fieldSize); addVideoTitleField.setFont(fieldFont); addVideoSubPanel.add(addVideoTitleField, avGbc);

        avGbc.gridx = 0; avGbc.gridy = 2; avGbc.anchor = GridBagConstraints.EAST; avGbc.weightx = 0;
        addVideoSubPanel.add(new JLabel("URL:", JLabel.RIGHT), avGbc);
        avGbc.gridx = 1; avGbc.anchor = GridBagConstraints.WEST; avGbc.weightx = 1.0; addVideoUrlField = new JTextField(20); addVideoUrlField.setPreferredSize(fieldSize); addVideoUrlField.setFont(fieldFont); addVideoSubPanel.add(addVideoUrlField, avGbc);

        avGbc.gridx = 0; avGbc.gridy = 3; avGbc.gridwidth = 2; avGbc.anchor = GridBagConstraints.CENTER; avGbc.weightx = 0;
        addVideoButton = new JButton("Adicionar Videoaula");
        addVideoButton.setBackground(new Color(60, 179, 113));
        addVideoButton.setForeground(Color.WHITE);
        addVideoButton.setFont(buttonFont);
        addVideoButton.setPreferredSize(buttonDim);
        addVideoButton.addActionListener(e -> {
            try {
                addVideoaula();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar videoaula: " + e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });
        addVideoSubPanel.add(addVideoButton, avGbc);
        mainGbc.gridy = 0;
        videoPanel.add(addVideoSubPanel, mainGbc);

        JPanel editDeleteVideoSubPanel = new JPanel(new GridBagLayout());
        editDeleteVideoSubPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(65, 105, 225)), "Editar / Excluir Videoaula", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), new Color(50, 50, 150)));
        editDeleteVideoSubPanel.setBackground(Color.WHITE);
        GridBagConstraints edvGbc = new GridBagConstraints();
        edvGbc.insets = new Insets(8, 8, 8, 8);
        edvGbc.fill = GridBagConstraints.HORIZONTAL;

        edvGbc.gridx = 0; edvGbc.gridy = 0; edvGbc.anchor = GridBagConstraints.EAST; editDeleteVideoSubPanel.add(new JLabel("Filtrar por Módulo:", JLabel.RIGHT), edvGbc);
        edvGbc.gridx = 1; edvGbc.anchor = GridBagConstraints.WEST; edvGbc.weightx = 1.0; videoModuloFilterComboBoxModel = new DefaultComboBoxModel<>();
        videoModuloFilterComboBox = new JComboBox<>(videoModuloFilterComboBoxModel);
        videoModuloFilterComboBox.setPreferredSize(fieldSize); videoModuloFilterComboBox.setFont(fieldFont);
        videoModuloFilterComboBox.addActionListener(e -> {
            try {
                loadVideosForEditDelete();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar vídeos: " + e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });
        editDeleteVideoSubPanel.add(videoModuloFilterComboBox, edvGbc);

        edvGbc.gridx = 0; edvGbc.gridy = 1; edvGbc.anchor = GridBagConstraints.EAST; edvGbc.weightx = 0;
        editDeleteVideoSubPanel.add(new JLabel("Selecionar Videoaula:", JLabel.RIGHT), edvGbc);
        edvGbc.gridx = 1; edvGbc.anchor = GridBagConstraints.WEST; edvGbc.weightx = 1.0; editDeleteVideoComboBoxModel = new DefaultComboBoxModel<>();
        editDeleteVideoComboBox = new JComboBox<>(editDeleteVideoComboBoxModel);
        editDeleteVideoComboBox.setPreferredSize(fieldSize); editDeleteVideoComboBox.setFont(fieldFont);
        editDeleteVideoComboBox.addActionListener(e -> loadVideoDetailsForEdit());
        editDeleteVideoSubPanel.add(editDeleteVideoComboBox, edvGbc);

        edvGbc.gridx = 0; edvGbc.gridy = 2; edvGbc.anchor = GridBagConstraints.EAST; edvGbc.weightx = 0;
        editDeleteVideoSubPanel.add(new JLabel("Título:", JLabel.RIGHT), edvGbc);
        edvGbc.gridx = 1; edvGbc.anchor = GridBagConstraints.WEST; edvGbc.weightx = 1.0; editVideoTitleField = new JTextField(20); editVideoTitleField.setPreferredSize(fieldSize); editVideoTitleField.setFont(fieldFont); editDeleteVideoSubPanel.add(editVideoTitleField, edvGbc);

        edvGbc.gridx = 0; edvGbc.gridy = 3; edvGbc.anchor = GridBagConstraints.EAST; edvGbc.weightx = 0;
        editDeleteVideoSubPanel.add(new JLabel("URL:", JLabel.RIGHT), edvGbc);
        edvGbc.gridx = 1; edvGbc.anchor = GridBagConstraints.WEST; edvGbc.weightx = 1.0; editVideoUrlField = new JTextField(20); editVideoUrlField.setPreferredSize(fieldSize); editVideoUrlField.setFont(fieldFont); editDeleteVideoSubPanel.add(editVideoUrlField, edvGbc);

        edvGbc.gridx = 0; edvGbc.gridy = 4; edvGbc.gridwidth = 1; edvGbc.anchor = GridBagConstraints.CENTER; edvGbc.weightx = 0;
        updateVideoButton = new JButton("Atualizar");
        updateVideoButton.setBackground(new Color(70, 130, 180));
        updateVideoButton.setForeground(Color.WHITE);
        updateVideoButton.setFont(buttonFont);
        updateVideoButton.setPreferredSize(buttonDim);
        updateVideoButton.addActionListener(e -> {
            try {
                updateVideoaula();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar videoaula: " + e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });
        editDeleteVideoSubPanel.add(updateVideoButton, edvGbc);

        edvGbc.gridx = 1; edvGbc.gridy = 4; edvGbc.gridwidth = 1; edvGbc.anchor = GridBagConstraints.CENTER; edvGbc.weightx = 0;
        deleteVideoButton = new JButton("Excluir");
        deleteVideoButton.setBackground(new Color(220, 20, 60));
        deleteVideoButton.setForeground(Color.WHITE);
        deleteVideoButton.setFont(buttonFont);
        deleteVideoButton.setPreferredSize(buttonDim);
        deleteVideoButton.addActionListener(e -> {
            try {
                deleteVideoaula();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir videoaula: " + e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });
        editDeleteVideoSubPanel.add(deleteVideoButton, edvGbc);
        mainGbc.gridy = 1;
        videoPanel.add(editDeleteVideoSubPanel, mainGbc);
        cardPanel.add(videoPanel, "Videoaulas");

        
        JPanel postagemPanel = new JPanel();
        postagemPanel.setLayout(new GridBagLayout());
        postagemPanel.setBackground(new Color(240, 248, 255));
        postagemPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        mainGbc.gridy = 0; 
        JPanel createPostagemSubPanel = new JPanel(new GridBagLayout());
        createPostagemSubPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(65, 105, 225)), "Postar Novo Material", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), new Color(50, 50, 150)));
        createPostagemSubPanel.setBackground(Color.WHITE);
        GridBagConstraints cpGbc = new GridBagConstraints();
        cpGbc.insets = new Insets(8, 8, 8, 8);
        cpGbc.fill = GridBagConstraints.HORIZONTAL;

        cpGbc.gridx = 0; cpGbc.gridy = 0; cpGbc.anchor = GridBagConstraints.EAST; createPostagemSubPanel.add(new JLabel("Título:", JLabel.RIGHT), cpGbc);
        cpGbc.gridx = 1; cpGbc.anchor = GridBagConstraints.WEST; cpGbc.weightx = 1.0; createPostagemTitleField = new JTextField(20); createPostagemTitleField.setPreferredSize(fieldSize); createPostagemTitleField.setFont(fieldFont); createPostagemSubPanel.add(createPostagemTitleField, cpGbc);

        cpGbc.gridx = 0; cpGbc.gridy = 1; cpGbc.anchor = GridBagConstraints.EAST; cpGbc.weightx = 0;
        createPostagemSubPanel.add(new JLabel("Conteúdo:", JLabel.RIGHT), cpGbc);
        cpGbc.gridx = 1; cpGbc.anchor = GridBagConstraints.WEST; cpGbc.weightx = 1.0; cpGbc.weighty = 1.0;
        createPostagemContentArea = new JTextArea(5, 20); createPostagemContentArea.setLineWrap(true); createPostagemContentArea.setWrapStyleWord(true); createPostagemContentArea.setPreferredSize(textAreaSize); createPostagemContentArea.setFont(fieldFont); createPostagemSubPanel.add(new JScrollPane(createPostagemContentArea), cpGbc);

        postagemTypeGroup = new ButtonGroup();
        postagemForCourseRadio = new JRadioButton("Para Curso");
        postagemForModuloRadio = new JRadioButton("Para Módulo");
        postagemForCourseRadio.setBackground(createPostagemSubPanel.getBackground()); 
        postagemForModuloRadio.setBackground(createPostagemSubPanel.getBackground()); 
        postagemTypeGroup.add(postagemForCourseRadio);
        postagemTypeGroup.add(postagemForModuloRadio);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radioPanel.setBackground(createPostagemSubPanel.getBackground());
        radioPanel.add(postagemForCourseRadio);
        radioPanel.add(postagemForModuloRadio);
        cpGbc.gridx = 0; cpGbc.gridy = 2; cpGbc.gridwidth = 2; cpGbc.anchor = GridBagConstraints.WEST; cpGbc.weightx = 0; cpGbc.weighty = 0;
        createPostagemSubPanel.add(radioPanel, cpGbc);


        postagemForCourseRadio.addActionListener(e -> {
            postagemCourseComboBox.setEnabled(true);
            postagemModuloComboBox.setEnabled(false);
            postagemModuloComboBoxModel.removeAllElements(); 
        });
        postagemForModuloRadio.addActionListener(e -> {
            postagemCourseComboBox.setEnabled(true); 
            postagemModuloComboBox.setEnabled(true);
            loadPostagemModulesForCourse();
        });


        cpGbc.gridwidth = 1;
        cpGbc.gridx = 0; cpGbc.gridy = 3; cpGbc.anchor = GridBagConstraints.EAST; createPostagemSubPanel.add(new JLabel("Curso:", JLabel.RIGHT), cpGbc);
        cpGbc.gridx = 1; cpGbc.anchor = GridBagConstraints.WEST; cpGbc.weightx = 1.0; postagemCourseComboBoxModel = new DefaultComboBoxModel<>();
        postagemCourseComboBox = new JComboBox<>(postagemCourseComboBoxModel);
        postagemCourseComboBox.setPreferredSize(fieldSize); postagemCourseComboBox.setFont(fieldFont);
        postagemCourseComboBox.addActionListener(e -> loadPostagemModulesForCourse());
        createPostagemSubPanel.add(postagemCourseComboBox, cpGbc);

        cpGbc.gridx = 0; cpGbc.gridy = 4; cpGbc.anchor = GridBagConstraints.EAST; cpGbc.weightx = 0;
        createPostagemSubPanel.add(new JLabel("Módulo:", JLabel.RIGHT), cpGbc);
        cpGbc.gridx = 1; cpGbc.anchor = GridBagConstraints.WEST; cpGbc.weightx = 1.0; postagemModuloComboBoxModel = new DefaultComboBoxModel<>();
        postagemModuloComboBox = new JComboBox<>(postagemModuloComboBoxModel);
        postagemModuloComboBox.setPreferredSize(fieldSize); postagemModuloComboBox.setFont(fieldFont);
       
        createPostagemSubPanel.add(postagemModuloComboBox, cpGbc);

        postagemForCourseRadio.setSelected(true); 
        postagemModuloComboBox.setEnabled(false); 

        cpGbc.gridx = 0; cpGbc.gridy = 5; cpGbc.gridwidth = 2; cpGbc.anchor = GridBagConstraints.CENTER; cpGbc.weightx = 0;
        createPostagemButton = new JButton("Postar Material");
        createPostagemButton.setBackground(new Color(60, 179, 113));
        createPostagemButton.setForeground(Color.WHITE);
        createPostagemButton.setFont(buttonFont);
        createPostagemButton.setPreferredSize(buttonDim);
        createPostagemButton.addActionListener(e -> {
            try {
                createPostagem();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Erro ao criar postagem: " + e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });
        createPostagemSubPanel.add(createPostagemButton, cpGbc);
        mainGbc.gridy = 0;
        postagemPanel.add(createPostagemSubPanel, mainGbc);

        JPanel editDeletePostagemSubPanel = new JPanel(new GridBagLayout());
        editDeletePostagemSubPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(65, 105, 225)), "Editar / Excluir Material", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), new Color(50, 50, 150)));
        editDeletePostagemSubPanel.setBackground(Color.WHITE);
        GridBagConstraints edpGbc = new GridBagConstraints();
        edpGbc.insets = new Insets(8, 8, 8, 8);
        edpGbc.fill = GridBagConstraints.HORIZONTAL;

        editPostagemForCourseRadio = new JRadioButton("Filtrar por Curso");
        editPostagemForModuloRadio = new JRadioButton("Filtrar por Módulo");
        editPostagemForCourseRadio.setBackground(editDeletePostagemSubPanel.getBackground());
        editPostagemForModuloRadio.setBackground(editDeletePostagemSubPanel.getBackground());
        editPostagemTypeGroup = new ButtonGroup();
        editPostagemTypeGroup.add(editPostagemForCourseRadio);
        editPostagemTypeGroup.add(editPostagemForModuloRadio);

        JPanel editRadioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        editRadioPanel.setBackground(editDeletePostagemSubPanel.getBackground());
        editRadioPanel.add(editPostagemForCourseRadio);
        editRadioPanel.add(editPostagemForModuloRadio);
        edpGbc.gridx = 0; edpGbc.gridy = 0; edpGbc.gridwidth = 2; edpGbc.anchor = GridBagConstraints.WEST; edpGbc.weightx = 0; edpGbc.weighty = 0;
        editDeletePostagemSubPanel.add(editRadioPanel, edpGbc);

       
        editPostagemForCourseRadio.addActionListener(e -> {
            editPostagemCourseFilterComboBox.setEnabled(true);
            editPostagemModuloFilterComboBox.setEnabled(false);
            editPostagemModuloFilterComboBoxModel.removeAllElements(); 
            try {
                loadPostagensForEditDelete();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar postagens: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        editPostagemForModuloRadio.addActionListener(e -> {
            editPostagemCourseFilterComboBox.setEnabled(true); 
            editPostagemModuloFilterComboBox.setEnabled(true);
            try {
                loadEditPostagemModulesForCourse(); 
                loadPostagensForEditDelete();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar módulos/postagens: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        edpGbc.gridwidth = 1;
        edpGbc.gridx = 0; edpGbc.gridy = 1; edpGbc.anchor = GridBagConstraints.EAST; editDeletePostagemSubPanel.add(new JLabel("Filtrar por Curso:", JLabel.RIGHT), edpGbc);
        edpGbc.gridx = 1; edpGbc.anchor = GridBagConstraints.WEST; edpGbc.weightx = 1.0; editPostagemCourseFilterComboBoxModel = new DefaultComboBoxModel<>();
        editPostagemCourseFilterComboBox = new JComboBox<>(editPostagemCourseFilterComboBoxModel);
        editPostagemCourseFilterComboBox.setPreferredSize(fieldSize); editPostagemCourseFilterComboBox.setFont(fieldFont);
        editPostagemCourseFilterComboBox.addActionListener(e -> {
            try {
                if (editPostagemForCourseRadio.isSelected()) {
                    loadPostagensForEditDelete();
                } else if (editPostagemForModuloRadio.isSelected()) {
                    loadEditPostagemModulesForCourse();
                }
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar postagens/módulos: " + e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });
        editDeletePostagemSubPanel.add(editPostagemCourseFilterComboBox, edpGbc);

        edpGbc.gridx = 0; edpGbc.gridy = 2; edpGbc.anchor = GridBagConstraints.EAST; edpGbc.weightx = 0;
        editDeletePostagemSubPanel.add(new JLabel("Módulo:", JLabel.RIGHT), edpGbc);
        edpGbc.gridx = 1; edpGbc.anchor = GridBagConstraints.WEST; edpGbc.weightx = 1.0; editPostagemModuloFilterComboBoxModel = new DefaultComboBoxModel<>();
        editPostagemModuloFilterComboBox = new JComboBox<>(editPostagemModuloFilterComboBoxModel);
        editPostagemModuloFilterComboBox.setPreferredSize(fieldSize); editPostagemModuloFilterComboBox.setFont(fieldFont);
        editPostagemModuloFilterComboBox.addActionListener(e -> {
            try {
                loadPostagensForEditDelete();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar postagens: " + e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });
        editDeletePostagemSubPanel.add(editPostagemModuloFilterComboBox, edpGbc);

        
        editPostagemForCourseRadio.setSelected(true);
        editPostagemModuloFilterComboBox.setEnabled(false);

        edpGbc.gridx = 0; edpGbc.gridy = 3; edpGbc.anchor = GridBagConstraints.EAST; editDeletePostagemSubPanel.add(new JLabel("Selecionar Postagem:", JLabel.RIGHT), edpGbc);
        edpGbc.gridx = 1; edpGbc.anchor = GridBagConstraints.WEST; edpGbc.weightx = 1.0; editDeletePostagemComboBoxModel = new DefaultComboBoxModel<>();
        editDeletePostagemComboBox = new JComboBox<>(editDeletePostagemComboBoxModel);
        editDeletePostagemComboBox.setPreferredSize(fieldSize); editDeletePostagemComboBox.setFont(fieldFont);
        editDeletePostagemComboBox.addActionListener(e -> loadPostagemDetailsForEdit());
        editDeletePostagemSubPanel.add(editDeletePostagemComboBox, edpGbc);

        edpGbc.gridx = 0; edpGbc.gridy = 4; edpGbc.anchor = GridBagConstraints.EAST; edpGbc.weightx = 0;
        editDeletePostagemSubPanel.add(new JLabel("Título:", JLabel.RIGHT), edpGbc);
        edpGbc.gridx = 1; edpGbc.anchor = GridBagConstraints.WEST; edpGbc.weightx = 1.0; editPostagemTitleField = new JTextField(20); editPostagemTitleField.setPreferredSize(fieldSize); editPostagemTitleField.setFont(fieldFont); editDeletePostagemSubPanel.add(editPostagemTitleField, edpGbc);

        edpGbc.gridx = 0; edpGbc.gridy = 5; edpGbc.anchor = GridBagConstraints.EAST; edpGbc.weightx = 0;
        editDeletePostagemSubPanel.add(new JLabel("Conteúdo:", JLabel.RIGHT), edpGbc);
        edpGbc.gridx = 1; edpGbc.anchor = GridBagConstraints.WEST; edpGbc.weightx = 1.0; edpGbc.weighty = 1.0;
        editPostagemContentArea = new JTextArea(5, 20); editPostagemContentArea.setLineWrap(true); editPostagemContentArea.setWrapStyleWord(true); editPostagemContentArea.setPreferredSize(textAreaSize); editPostagemContentArea.setFont(fieldFont); editDeletePostagemSubPanel.add(new JScrollPane(editPostagemContentArea), edpGbc);

        edpGbc.gridx = 0; edpGbc.gridy = 6; edpGbc.gridwidth = 1; edpGbc.anchor = GridBagConstraints.CENTER; edpGbc.weighty = 0; edpGbc.weightx = 0;
        updatePostagemButton = new JButton("Atualizar");
        updatePostagemButton.setBackground(new Color(70, 130, 180));
        updatePostagemButton.setForeground(Color.WHITE);
        updatePostagemButton.setFont(buttonFont);
        updatePostagemButton.setPreferredSize(buttonDim);
        updatePostagemButton.addActionListener(e -> {
            try {
                updatePostagem();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar postagem: " + e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });
        editDeletePostagemSubPanel.add(updatePostagemButton, edpGbc);

        edpGbc.gridx = 1; edpGbc.gridy = 6; edpGbc.gridwidth = 1; edpGbc.anchor = GridBagConstraints.CENTER; edpGbc.weightx = 0;
        deletePostagemButton = new JButton("Excluir");
        deletePostagemButton.setBackground(new Color(220, 20, 60));
        deletePostagemButton.setForeground(Color.WHITE);
        deletePostagemButton.setFont(buttonFont);
        deletePostagemButton.setPreferredSize(buttonDim);
        deletePostagemButton.addActionListener(e -> {
            try {
                deletePostagem();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir postagem: " + e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });
        editDeletePostagemSubPanel.add(deletePostagemButton, edpGbc);
        mainGbc.gridy = 1;
        postagemPanel.add(editDeletePostagemSubPanel, mainGbc);
        cardPanel.add(postagemPanel, "Postagens");

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

        JLabel profileTitle = new JLabel("Editar Perfil do Tutor");
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

        profileGbc.gridx = 0;
        profileGbc.gridy++;
        profileGbc.anchor = GridBagConstraints.EAST;
        profilePanel.add(new JLabel("Área de Especialização:"), profileGbc);
        profileGbc.gridx = 1;
        profileGbc.anchor = GridBagConstraints.WEST;
        areaProfileField = new JTextField(20);
        areaProfileField.setPreferredSize(fieldSize);
        areaProfileField.setMaximumSize(fieldSize);
        profilePanel.add(areaProfileField, profileGbc);

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
        updateProfileButton.addActionListener(e -> updateTutorProfile());
        profilePanel.add(updateProfileButton, profileGbc);
        profileOuterPanel.add(profilePanel, outerGbc);
        cardPanel.add(profileOuterPanel, "EditarPerfil");


        cursoDetalhesPanelTutor = new CursoDetalhesPanelTutor(this);
        cardPanel.add(cursoDetalhesPanelTutor, "CourseDetails");

        JButton btnMyCourses = createSidebarButton("Meus Cursos");
        btnMyCourses.addActionListener(e -> {
            cardLayout.show(cardPanel, "MeusCursosTutor");
            try {
                loadMyTutorCourses();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar meus cursos: " + e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });
        sidebarPanel.add(btnMyCourses);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JButton btnManageCourses = createSidebarButton("Gerenciar Cursos");
        btnManageCourses.addActionListener(e -> {
            cardLayout.show(cardPanel, "GerenciarCursos");
            try {
                loadCoursesIntoComboBoxes();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar cursos para gerenciamento: " + e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });
        sidebarPanel.add(btnManageCourses);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JButton btnModules = createSidebarButton("Módulos");
        btnModules.addActionListener(e -> {
            cardLayout.show(cardPanel, "Modulos");
            try {
                loadCoursesIntoComboBoxes();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar cursos: " + e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
            try {
                loadAllModulesForComboBoxes(); 
                loadModulesForEditDelete(); 
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar todos os módulos: " + e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });
        sidebarPanel.add(btnModules);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JButton btnVideos = createSidebarButton("Videoaulas");
        btnVideos.addActionListener(e -> {
            cardLayout.show(cardPanel, "Videoaulas");
            try {
                loadCoursesIntoComboBoxes(); 
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar cursos: " + e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
            try {
                loadAllModulesForComboBoxes();
                loadVideosForEditDelete(); 
            }
             catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar vídeos: " + e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });
        sidebarPanel.add(btnVideos);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JButton btnPostagens = createSidebarButton("Postagens");
        btnPostagens.addActionListener(e -> {
            cardLayout.show(cardPanel, "Postagens");
            try {
                loadCoursesIntoComboBoxes(); 
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar cursos: " + e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
           
            postagemForCourseRadio.setSelected(true);
            postagemCourseComboBox.setEnabled(true);
            postagemModuloComboBox.setEnabled(false);
            postagemModuloComboBoxModel.removeAllElements(); 

            editPostagemForCourseRadio.setSelected(true);
            editPostagemCourseFilterComboBox.setEnabled(true);
            editPostagemModuloFilterComboBox.setEnabled(false);
            editPostagemModuloFilterComboBoxModel.removeAllElements();

            try {
                loadPostagensForEditDelete(); 
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar postagens: " + e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });
        sidebarPanel.add(btnPostagens);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JButton btnEditProfile = createSidebarButton("Editar Perfil");
        btnEditProfile.addActionListener(e -> {
            cardLayout.show(cardPanel, "EditarPerfil");
            if (loggedInTutor != null) {
                nameProfileField.setText(loggedInTutor.getNome());
                emailProfileField.setText(loggedInTutor.getEmail());
                areaProfileField.setText(loggedInTutor.getArea());
            }
            passwordProfileField.setText("");
        });
        sidebarPanel.add(btnEditProfile);
        sidebarPanel.add(Box.createVerticalGlue());

        cardLayout.show(cardPanel, "MeusCursosTutor");

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(220, 20, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.addActionListener(e -> parentFrame.logout());
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.setBackground(new Color(240, 248, 255));
        southPanel.add(logoutButton);
        add(southPanel, BorderLayout.SOUTH);

        try {
            loadMyTutorCourses();
            loadCoursesIntoComboBoxes(); 
            loadAllModulesForComboBoxes(); 
            setupComboBoxRenderers();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao inicializar painel do tutor: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
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

    public void setLoggedInTutor(Tutor tutor) {
        this.loggedInTutor = tutor;
        if (loggedInTutor != null) {
            ((JLabel) getComponent(0)).setText("Bem-vindo(a), Tutor(a) " + loggedInTutor.getNome() + "!");
            try {
                loadMyTutorCourses();
                loadCoursesIntoComboBoxes(); 
                loadAllModulesForComboBoxes(); 

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao definir tutor logado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
            if (cursoDetalhesPanelTutor != null) {
                cursoDetalhesPanelTutor.setLoggedInUser(loggedInTutor);
            }

            nameProfileField.setText(loggedInTutor.getNome());
            emailProfileField.setText(loggedInTutor.getEmail());
            areaProfileField.setText(loggedInTutor.getArea());
            passwordProfileField.setText("");
        }
    }

    public void showCourseDetailsPanel(Curso curso) {
        if (loggedInTutor != null) {
            cursoDetalhesPanelTutor.setLoggedInUser(loggedInTutor);
        }
        cursoDetalhesPanelTutor.setCourse(curso);
        cardLayout.show(cardPanel, "CourseDetails");
    }

    public void showMyTutorCoursesPanel() {
        cardLayout.show(cardPanel, "MeusCursosTutor");
        try {
            loadMyTutorCourses();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar meus cursos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void navigateToManageCoursesAndSelect(Curso curso) {
        cardLayout.show(cardPanel, "GerenciarCursos");
        Curso courseToSelect = null;
        for (int i = 0; i < editDeleteCourseComboBoxModel.getSize(); i++) {
            Curso item = editDeleteCourseComboBoxModel.getElementAt(i);
            if (item.getId() == curso.getId()) {
                courseToSelect = item;
                break;
            }
        }

        if (courseToSelect != null) {
            editDeleteCourseComboBox.setSelectedItem(courseToSelect);
            loadCourseDetailsForEdit();
        } else {
            try {
                loadCoursesIntoComboBoxes();
                for (int i = 0; i < editDeleteCourseComboBoxModel.getSize(); i++) {
                    Curso item = editDeleteCourseComboBoxModel.getElementAt(i);
                    if (item.getId() == curso.getId()) {
                        courseToSelect = item;
                        break;
                    }
                }
                if (courseToSelect != null) {
                    editDeleteCourseComboBox.setSelectedItem(courseToSelect);
                    loadCourseDetailsForEdit();
                } else {
                    JOptionPane.showMessageDialog(this, "Curso não encontrado para edição após recarregar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao navegar para edição do curso: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    public void navigateToManageModulesAndSelect(Modulo modulo) {
        cardLayout.show(cardPanel, "Modulos");
        if (modulo.getCurso() != null) {
            Curso courseToSelect = null;
            for (int i = 0; i < moduleCourseFilterComboBoxModel.getSize(); i++) {
                Curso item = moduleCourseFilterComboBoxModel.getElementAt(i);
                if (item.getId() == modulo.getCurso().getId()) {
                    courseToSelect = item;
                    break;
                }
            }

            if (courseToSelect != null) {
                moduleCourseFilterComboBox.setSelectedItem(courseToSelect);
            } else {
                try {
                    loadCoursesIntoComboBoxes();
                    for (int i = 0; i < moduleCourseFilterComboBoxModel.getSize(); i++) {
                        Curso item = moduleCourseFilterComboBoxModel.getElementAt(i);
                        if (item.getId() == modulo.getCurso().getId()) {
                            courseToSelect = item;
                            break;
                        }
                    }
                    if (courseToSelect != null) {
                        moduleCourseFilterComboBox.setSelectedItem(courseToSelect);
                    } else {
                        JOptionPane.showMessageDialog(this, "Curso do módulo não encontrado no filtro.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Erro ao carregar curso do módulo para filtro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        }
        Modulo moduloToSelect = null;
        for (int i = 0; i < editDeleteModuloComboBoxModel.getSize(); i++) {
            Modulo item = editDeleteModuloComboBoxModel.getElementAt(i);
            if (item.getId() == modulo.getId()) {
                moduloToSelect = item;
                break;
            }
        }

        if (moduloToSelect != null) {
            editDeleteModuloComboBox.setSelectedItem(moduloToSelect);
            loadModuloDetailsForEdit();
        } else {
             JOptionPane.showMessageDialog(this, "Módulo não encontrado para edição.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void loadMyTutorCourses() throws Exception {
        myCoursesListModel.clear();
        if (loggedInTutor != null) {
            List<Curso> cursos = cursoDAO.listarPorTutor(loggedInTutor.getId());
            for (Curso curso : cursos) {
                myCoursesListModel.addElement(curso);
            }
        }
    }

    private void setupComboBoxRenderers() {
        editDeleteCourseComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Curso) {
                    setText(((Curso) value).getTitulo());
                }
                return this;
            }
        });

        createModuloCourseComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Curso) {
                    setText(((Curso) value).getTitulo());
                }
                return this;
            }
        });

        moduleCourseFilterComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Curso) {
                    setText(((Curso) value).getTitulo());
                }
                return this;
            }
        });

        editDeleteModuloComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Modulo) {
                    setText(((Modulo) value).getTitulo());
                }
                return this;
            }
        });

        addVideoModuloComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Modulo) {
                    setText(((Modulo) value).getTitulo());
                }
                return this;
            }
        });

        videoModuloFilterComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Modulo) {
                    setText(((Modulo) value).getTitulo());
                }
                return this;
            }
        });

        editDeleteVideoComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof VideoAula) {
                    setText(((VideoAula) value).getTitulo());
                }
                return this;
            }
        });

        postagemCourseComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Curso) {
                    setText(((Curso) value).getTitulo());
                }
                return this;
            }
        });

        postagemModuloComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Modulo) {
                    setText(((Modulo) value).getTitulo());
                }
                return this;
            }
        });

        editPostagemCourseFilterComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Curso) {
                    setText(((Curso) value).getTitulo());
                }
                return this;
            }
        });

        editPostagemModuloFilterComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Modulo) {
                    setText(((Modulo) value).getTitulo());
                }
                return this;
            }
        });

        editDeletePostagemComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Postagem) {
                    setText(((Postagem) value).getTitulo());
                }
                return this;
            }
        });
    }

    private void loadCoursesIntoComboBoxes() throws Exception {
        if (loggedInTutor == null) {
            return;
        }

        List<Curso> cursos = cursoDAO.listarPorTutor(loggedInTutor.getId());

       
        editDeleteCourseComboBoxModel.removeAllElements();
        createModuloCourseComboBoxModel.removeAllElements();
        postagemCourseComboBoxModel.removeAllElements();
        editPostagemCourseFilterComboBoxModel.removeAllElements();
        moduleCourseFilterComboBoxModel.removeAllElements();

        for (Curso curso : cursos) {
            editDeleteCourseComboBoxModel.addElement(curso);
            createModuloCourseComboBoxModel.addElement(curso);
            postagemCourseComboBoxModel.addElement(curso);
            editPostagemCourseFilterComboBoxModel.addElement(curso);
            moduleCourseFilterComboBoxModel.addElement(curso);
        }

        
        setComboBoxDefaultSelection(editDeleteCourseComboBox);
        setComboBoxDefaultSelection(createModuloCourseComboBox);
        setComboBoxDefaultSelection(postagemCourseComboBox);
        setComboBoxDefaultSelection(editPostagemCourseFilterComboBox);
        setComboBoxDefaultSelection(moduleCourseFilterComboBox);

     
        loadCourseDetailsForEdit(); 
        loadModulesForEditDelete(); 
        loadPostagemModulesForCourse(); 
        loadEditPostagemModulesForCourse(); 
        loadAllModulesForComboBoxes(); 
    }


    private void setComboBoxDefaultSelection(JComboBox<?> comboBox) {
        if (comboBox.getModel().getSize() > 0) {
            comboBox.setSelectedIndex(0);
        } else {
            comboBox.setSelectedIndex(-1);
        }
    }


    private void loadCourseDetailsForEdit() {
        Curso selectedCourse = (Curso) editDeleteCourseComboBox.getSelectedItem();
        if (selectedCourse != null) {
            editCourseTitleField.setText(selectedCourse.getTitulo());
            editCourseDescriptionArea.setText(selectedCourse.getDescricao());
            editCourseAreaField.setText(selectedCourse.getArea());
        } else {
            editCourseTitleField.setText("");
            editCourseDescriptionArea.setText("");
            editCourseAreaField.setText("");
        }
    }

    private void createCourse() {
        if (loggedInTutor == null) {
            JOptionPane.showMessageDialog(this, "Erro: Não há tutor logado para criar o curso.", "Erro de Autorização", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String titulo = createCourseTitleField.getText();
        String descricao = createCourseDescriptionArea.getText();
        String area = createCourseAreaField.getText();

        if (titulo.isEmpty() || descricao.isEmpty() || area.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos de curso são obrigatórios.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Curso novoCurso = new Curso();
        novoCurso.setTitulo(titulo);
        novoCurso.setDescricao(descricao);
        novoCurso.setArea(area);
        novoCurso.setTutor(loggedInTutor);

        try {
            cursoDAO.criar(novoCurso);
            JOptionPane.showMessageDialog(this, "Curso criado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            clearCreateCourseFields();
            loadMyTutorCourses();
            loadCoursesIntoComboBoxes();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao criar curso: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateCourse() {
        Curso selectedCourse = (Curso) editDeleteCourseComboBox.getSelectedItem();
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Selecione um curso para atualizar.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (loggedInTutor == null || selectedCourse.getTutor() == null || selectedCourse.getTutor().getId() != loggedInTutor.getId()) {
            JOptionPane.showMessageDialog(this, "Você só pode modificar cursos que você criou.", "Acesso Negado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String titulo = editCourseTitleField.getText();
        String descricao = editCourseDescriptionArea.getText();
        String area = editCourseAreaField.getText();

        if (titulo.isEmpty() || descricao.isEmpty() || area.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos de curso são obrigatórios.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        selectedCourse.setTitulo(titulo);
        selectedCourse.setDescricao(descricao);
        selectedCourse.setArea(area);

        try {
            cursoDAO.atualizar(selectedCourse);
            JOptionPane.showMessageDialog(this, "Curso atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            loadMyTutorCourses();
            loadCoursesIntoComboBoxes();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar curso: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deleteCourse() {
        Curso selectedCourse = (Curso) editDeleteCourseComboBox.getSelectedItem();
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Selecione um curso para excluir.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (loggedInTutor == null || selectedCourse.getTutor() == null || selectedCourse.getTutor().getId() != loggedInTutor.getId()) {
            JOptionPane.showMessageDialog(this, "Você só pode excluir cursos que você criou.", "Acesso Negado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o curso '" + selectedCourse.getTitulo() + "' e TODO o seu conteúdo (módulos, videoaulas, postagens)?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                cursoDAO.deletar(selectedCourse.getId());
                JOptionPane.showMessageDialog(this, "Curso excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                loadMyTutorCourses();
                loadCoursesIntoComboBoxes();
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir curso: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void clearCreateCourseFields() {
        createCourseTitleField.setText("");
        createCourseDescriptionArea.setText("");
        createCourseAreaField.setText("");
    }

    private void loadAllModulesForComboBoxes() throws Exception {
        addVideoModuloComboBoxModel.removeAllElements();

        if (loggedInTutor == null) return;

        List<Curso> allCourses = cursoDAO.listarPorTutor(loggedInTutor.getId());
        for (Curso curso : allCourses) {
            List<Modulo> modules = moduloDAO.listarPorCurso(curso.getId());
            for (Modulo mod : modules) {
                addVideoModuloComboBoxModel.addElement(mod);
            }
        }

        setComboBoxDefaultSelection(addVideoModuloComboBox);
    }


    private void loadModulesForEditDelete() throws Exception {
        editDeleteModuloComboBoxModel.removeAllElements();

        if (loggedInTutor == null) return;

        Curso selectedCourse = (Curso) moduleCourseFilterComboBox.getSelectedItem();
        if (selectedCourse != null && selectedCourse.getTutor() != null && selectedCourse.getTutor().getId() == loggedInTutor.getId()) {
            List<Modulo> modulos = moduloDAO.listarPorCurso(selectedCourse.getId());
            for (Modulo modulo : modulos) {
                editDeleteModuloComboBoxModel.addElement(modulo);
            }
            setComboBoxDefaultSelection(editDeleteModuloComboBox);
        } else {
            editDeleteModuloComboBox.setSelectedIndex(-1);
        }
        loadModuloDetailsForEdit();
        try {
            loadVideosForEditDelete(); 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar vídeos para edição/exclusão: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void loadModuloDetailsForEdit() {
        Modulo selectedModulo = (Modulo) editDeleteModuloComboBox.getSelectedItem();
        if (selectedModulo != null) {
            editModuloTitleField.setText(selectedModulo.getTitulo());
            editModuloDescriptionArea.setText(selectedModulo.getDescricao());
        } else {
            editModuloTitleField.setText("");
            editModuloDescriptionArea.setText("");
        }
    }

    private void createModulo() {
        Curso selectedCourse = (Curso) createModuloCourseComboBox.getSelectedItem();
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Selecione um curso para o módulo.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (loggedInTutor == null || selectedCourse.getTutor() == null || selectedCourse.getTutor().getId() != loggedInTutor.getId()) {
            JOptionPane.showMessageDialog(this, "Você só pode criar módulos para cursos que você criou.", "Acesso Negado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String titulo = createModuloTitleField.getText();
        String descricao = createModuloDescriptionArea.getText();

        if (titulo.isEmpty() || descricao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Título e descrição do módulo são obrigatórios.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Modulo novoModulo = new Modulo();
        novoModulo.setTitulo(titulo);
        novoModulo.setDescricao(descricao);

        try {
            moduloDAO.criar(novoModulo, selectedCourse.getId());
            JOptionPane.showMessageDialog(this, "Módulo criado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            clearCreateModuloFields();
            loadCoursesIntoComboBoxes();
            loadAllModulesForComboBoxes();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao criar módulo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateModulo() {
        Modulo selectedModulo = (Modulo) editDeleteModuloComboBox.getSelectedItem();
        if (selectedModulo == null) {
            JOptionPane.showMessageDialog(this, "Selecione um módulo para atualizar.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Curso cursoDoModulo = null;
        try {
            if (selectedModulo.getId() != 0) {
                cursoDoModulo = cursoDAO.buscarCursoPorModulo(selectedModulo.getId());
            } else {
                JOptionPane.showMessageDialog(this, "Módulo selecionado não tem ID válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao verificar autoria do módulo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro inesperado ao verificar autoria do módulo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        if (loggedInTutor == null || cursoDoModulo == null || cursoDoModulo.getTutor() == null || cursoDoModulo.getTutor().getId() != loggedInTutor.getId()) {
            JOptionPane.showMessageDialog(this, "Você só pode modificar módulos de cursos que você criou.", "Acesso Negado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String titulo = editModuloTitleField.getText();
        String descricao = editModuloDescriptionArea.getText();

        if (titulo.isEmpty() || descricao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Título e descrição do módulo são obrigatórios.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        selectedModulo.setTitulo(titulo);
        selectedModulo.setDescricao(descricao);

        try {
            moduloDAO.atualizar(selectedModulo);
            JOptionPane.showMessageDialog(this, "Módulo atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            loadCoursesIntoComboBoxes(); 
            loadAllModulesForComboBoxes();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar módulo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deleteModulo() {
        Modulo selectedModulo = (Modulo) editDeleteModuloComboBox.getSelectedItem();
        if (selectedModulo == null) {
            JOptionPane.showMessageDialog(this, "Selecione um módulo para excluir.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Curso cursoDoModulo = null;
        try {
            if (selectedModulo.getId() != 0) {
                cursoDoModulo = cursoDAO.buscarCursoPorModulo(selectedModulo.getId());
            } else {
                JOptionPane.showMessageDialog(this, "Módulo selecionado não tem ID válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao verificar autoria do módulo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro inesperado ao verificar autoria do módulo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        if (loggedInTutor == null || cursoDoModulo == null || cursoDoModulo.getTutor() == null || cursoDoModulo.getTutor().getId() != loggedInTutor.getId()) {
            JOptionPane.showMessageDialog(this, "Você só pode excluir módulos de cursos que você criou.", "Acesso Negado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o módulo '" + selectedModulo.getTitulo() + "' e todo o seu conteúdo (videoaulas e postagens)?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                moduloDAO.deletar(selectedModulo.getId());
                JOptionPane.showMessageDialog(this, "Módulo excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                loadCoursesIntoComboBoxes(); 
                loadAllModulesForComboBoxes();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir módulo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void clearCreateModuloFields() {
        createModuloTitleField.setText("");
        createModuloDescriptionArea.setText("");
    }


    private void loadVideosForEditDelete() throws Exception {
        editDeleteVideoComboBoxModel.removeAllElements();
        videoModuloFilterComboBoxModel.removeAllElements();

        if (loggedInTutor == null) return;

       
        List<Curso> allCourses = cursoDAO.listarPorTutor(loggedInTutor.getId());
        for (Curso curso : allCourses) {
            List<Modulo> modules = moduloDAO.listarPorCurso(curso.getId());
            for (Modulo mod : modules) {
                videoModuloFilterComboBoxModel.addElement(mod);
            }
        }

        setComboBoxDefaultSelection(videoModuloFilterComboBox);

        Modulo selectedModulo = (Modulo) videoModuloFilterComboBox.getSelectedItem();
        if (selectedModulo != null) {
            List<VideoAula> videos = videoAulaDAO.listarPorModulo(selectedModulo.getId());
            for (VideoAula video : videos) {
                editDeleteVideoComboBoxModel.addElement(video);
            }
            setComboBoxDefaultSelection(editDeleteVideoComboBox);
        } else {
            editDeleteVideoComboBox.setSelectedIndex(-1);
        }
        loadVideoDetailsForEdit();
    }

    private void loadVideoDetailsForEdit() {
        VideoAula selectedVideo = (VideoAula) editDeleteVideoComboBox.getSelectedItem();
        if (selectedVideo != null) {
            editVideoTitleField.setText(selectedVideo.getTitulo());
            editVideoUrlField.setText(selectedVideo.getUrl());
        } else {
            editVideoTitleField.setText("");
            editVideoUrlField.setText("");
        }
    }

    private void addVideoaula() {
        Modulo selectedModulo = (Modulo) addVideoModuloComboBox.getSelectedItem();
        if (selectedModulo == null) {
            JOptionPane.showMessageDialog(this, "Selecione um módulo para a videoaula.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Curso cursoDoModulo = null;
        try {
            if (selectedModulo.getId() != 0) {
                cursoDoModulo = cursoDAO.buscarCursoPorModulo(selectedModulo.getId());
            } else {
                JOptionPane.showMessageDialog(this, "Módulo selecionado não tem ID válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao verificar autoria do módulo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro inesperado ao verificar autoria do módulo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        if (loggedInTutor == null || cursoDoModulo == null || cursoDoModulo.getTutor() == null || cursoDoModulo.getTutor().getId() != loggedInTutor.getId()) {
            JOptionPane.showMessageDialog(this, "Você só pode adicionar videoaulas a módulos de cursos que você criou.", "Acesso Negado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String titulo = addVideoTitleField.getText();
        String url = addVideoUrlField.getText();

        if (titulo.isEmpty() || url.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Título e URL da videoaula são obrigatórios.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        VideoAula novaVideoAula = new VideoAula();
        novaVideoAula.setTitulo(titulo);
        novaVideoAula.setUrl(url);

        try {
            videoAulaDAO.postar(novaVideoAula, selectedModulo.getId());
            JOptionPane.showMessageDialog(this, "Videoaula adicionada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            clearAddVideoaulaFields();
            loadVideosForEditDelete(); 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar videoaula: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateVideoaula() {
        VideoAula selectedVideo = (VideoAula) editDeleteVideoComboBox.getSelectedItem();
        if (selectedVideo == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma videoaula para atualizar.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Modulo moduloDaVideoaula = null;
        Curso cursoDoModuloDaVideoaula = null;
        try {
            VideoAula fullVideo = videoAulaDAO.buscar(selectedVideo.getId());
            if (fullVideo != null && fullVideo.getIdModulo() != 0) {
                int idModuloFromVideo = fullVideo.getIdModulo();
                moduloDaVideoaula = moduloDAO.buscar(idModuloFromVideo);
                if (moduloDaVideoaula != null) {
                    cursoDoModuloDaVideoaula = cursoDAO.buscarCursoPorModulo(moduloDaVideoaula.getId());
                } else {
                     JOptionPane.showMessageDialog(this, "Módulo da videoaula não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                     return;
                }
            } else {
                 JOptionPane.showMessageDialog(this, "Videoaula ou ID do módulo não encontrados.", "Erro", JOptionPane.ERROR_MESSAGE);
                 return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro SQL ao verificar autoria da videoaula: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro inesperado ao verificar autoria da videoaula: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        if (loggedInTutor == null || cursoDoModuloDaVideoaula == null || cursoDoModuloDaVideoaula.getTutor() == null || cursoDoModuloDaVideoaula.getTutor().getId() != loggedInTutor.getId()) {
            JOptionPane.showMessageDialog(this, "Você só pode modificar videoaulas de cursos que você criou.", "Acesso Negado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String titulo = editVideoTitleField.getText();
        String url = editVideoUrlField.getText();

        if (titulo.isEmpty() || url.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Título e URL da videoaula são obrigatórios.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        selectedVideo.setTitulo(titulo);
        selectedVideo.setUrl(url);

        try {
            videoAulaDAO.atualizar(selectedVideo);
            JOptionPane.showMessageDialog(this, "Videoaula atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            loadVideosForEditDelete();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar videoaula: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deleteVideoaula() {
        VideoAula selectedVideo = (VideoAula) editDeleteVideoComboBox.getSelectedItem();
        if (selectedVideo == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma videoaula para excluir.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Modulo moduloDaVideoaula = null;
        Curso cursoDoModuloDaVideoaula = null;
        try {
            VideoAula fullVideo = videoAulaDAO.buscar(selectedVideo.getId());
            if (fullVideo != null && fullVideo.getIdModulo() != 0) {
                int idModuloFromVideo = fullVideo.getIdModulo();
                moduloDaVideoaula = moduloDAO.buscar(idModuloFromVideo);
                if (moduloDaVideoaula != null) {
                    cursoDoModuloDaVideoaula = cursoDAO.buscarCursoPorModulo(moduloDaVideoaula.getId());
                } else {
                     JOptionPane.showMessageDialog(this, "Módulo da videoaula não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                     return;
                }
            } else {
                 JOptionPane.showMessageDialog(this, "Videoaula ou ID do módulo não encontrados.", "Erro", JOptionPane.ERROR_MESSAGE);
                 return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro SQL ao verificar autoria da videoaula: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro inesperado ao verificar autoria da videoaula: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        if (loggedInTutor == null || cursoDoModuloDaVideoaula == null || cursoDoModuloDaVideoaula.getTutor() == null || cursoDoModuloDaVideoaula.getTutor().getId() != loggedInTutor.getId()) {
            JOptionPane.showMessageDialog(this, "Você só pode excluir videoaulas de cursos que você criou.", "Acesso Negado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir a videoaula '" + selectedVideo.getTitulo() + "'?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                videoAulaDAO.deletar(selectedVideo.getId());
                JOptionPane.showMessageDialog(this, "Videoaula excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                loadVideosForEditDelete(); 
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir videoaula: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void clearAddVideoaulaFields() {
        addVideoTitleField.setText("");
        addVideoUrlField.setText("");
    }

    private void loadPostagemModulesForCourse() {
        postagemModuloComboBoxModel.removeAllElements();
        postagemModuloComboBox.setEnabled(postagemForModuloRadio.isSelected());

        if (postagemForModuloRadio.isSelected()) {
            Curso selectedCourse = (Curso) postagemCourseComboBox.getSelectedItem();
            if (selectedCourse != null) {
                if (loggedInTutor == null || selectedCourse.getTutor() == null || selectedCourse.getTutor().getId() != loggedInTutor.getId()) {
                    JOptionPane.showMessageDialog(this, "Selecione um curso que você criou para postar no módulo.", "Acesso Negado", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                List<Modulo> modules = moduloDAO.listarPorCurso(selectedCourse.getId());
                for (Modulo mod : modules) {
                    postagemModuloComboBoxModel.addElement(mod);
                }
            }
        }
        setComboBoxDefaultSelection(postagemModuloComboBox);
    }


    private void loadEditPostagemModulesForCourse() {
        editPostagemModuloFilterComboBoxModel.removeAllElements();
        editPostagemModuloFilterComboBox.setEnabled(editPostagemForModuloRadio.isSelected()); 

        if (editPostagemForModuloRadio.isSelected()) {
            Curso selectedCourse = (Curso) editPostagemCourseFilterComboBox.getSelectedItem();
            if (selectedCourse != null) {
                if (loggedInTutor == null || selectedCourse.getTutor() == null || selectedCourse.getTutor().getId() != loggedInTutor.getId()) {
                    JOptionPane.showMessageDialog(this, "Selecione um curso que você criou para filtrar por módulo.", "Acesso Negado", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                List<Modulo> modules = moduloDAO.listarPorCurso(selectedCourse.getId());
                for (Modulo mod : modules) {
                    editPostagemModuloFilterComboBoxModel.addElement(mod);
                }
            }
        }
        setComboBoxDefaultSelection(editPostagemModuloFilterComboBox);
    }


    private void loadPostagensForEditDelete() throws Exception {
        editDeletePostagemComboBoxModel.removeAllElements();
        if (loggedInTutor == null) return;

        List<Postagem> postagens = null;

   
        editPostagemModuloFilterComboBoxModel.removeAllElements();
        editPostagemModuloFilterComboBox.setEnabled(false);

        if (editPostagemForCourseRadio.isSelected()) {
            Curso selectedCourse = (Curso) editPostagemCourseFilterComboBox.getSelectedItem();
            if (selectedCourse != null) {
                if (selectedCourse.getTutor() == null || selectedCourse.getTutor().getId() != loggedInTutor.getId()) {
                    JOptionPane.showMessageDialog(this, "Você só pode ver postagens de cursos que você criou.", "Acesso Negado", JOptionPane.WARNING_MESSAGE);
                    editPostagemTitleField.setText("");
                    editPostagemContentArea.setText("");
                    return;
                }
                postagens = postagemDAO.listarPorCursoSemModulo(selectedCourse.getId());
            
                postagens.removeIf(p -> p.getModulo() != null && p.getModulo().getId() != 0);
            }
        } else if (editPostagemForModuloRadio.isSelected()) {
            editPostagemModuloFilterComboBox.setEnabled(true); 

       
            Curso selectedCourse = (Curso) editPostagemCourseFilterComboBox.getSelectedItem();
            if (selectedCourse != null) {
                if (loggedInTutor == null || selectedCourse.getTutor() == null || selectedCourse.getTutor().getId() != loggedInTutor.getId()) {
                    JOptionPane.showMessageDialog(this, "Selecione um curso que você criou para filtrar por módulo.", "Acesso Negado", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                List<Modulo> modulesForFilter = moduloDAO.listarPorCurso(selectedCourse.getId());
                for (Modulo mod : modulesForFilter) {
                    editPostagemModuloFilterComboBoxModel.addElement(mod);
                }
                setComboBoxDefaultSelection(editPostagemModuloFilterComboBox);
            }

            Modulo selectedModulo = (Modulo) editPostagemModuloFilterComboBox.getSelectedItem();
            if (selectedModulo != null) {
                Curso cursoDoModulo = null;
                try {
                    cursoDoModulo = cursoDAO.buscarCursoPorModulo(selectedModulo.getId());
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Erro ao verificar autoria do módulo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    return;
                }
                if (cursoDoModulo == null || cursoDoModulo.getTutor() == null || cursoDoModulo.getTutor().getId() != loggedInTutor.getId()) {
                    JOptionPane.showMessageDialog(this, "Você só pode ver postagens de módulos de cursos que você criou.", "Acesso Negado", JOptionPane.WARNING_MESSAGE);
                    editPostagemTitleField.setText("");
                    editPostagemContentArea.setText("");
                    return;
                }
                postagens = postagemDAO.listarPorModulo(selectedModulo.getId());
            }
        }

        if (postagens != null) {
            for (Postagem postagem : postagens) {
                editDeletePostagemComboBoxModel.addElement(postagem);
            }
        }

        setComboBoxDefaultSelection(editDeletePostagemComboBox);
        loadPostagemDetailsForEdit();
    }

    private void loadPostagemDetailsForEdit() {
        Postagem selectedPostagem = (Postagem) editDeletePostagemComboBox.getSelectedItem();
        if (selectedPostagem != null) {
            editPostagemTitleField.setText(selectedPostagem.getTitulo());
            editPostagemContentArea.setText(selectedPostagem.getConteudo());
        } else {
            editPostagemTitleField.setText("");
            editPostagemContentArea.setText("");
        }
    }

    private void createPostagem() throws Exception {
        String titulo = createPostagemTitleField.getText();
        String conteudo = createPostagemContentArea.getText();

        if (titulo.isEmpty() || conteudo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Título e conteúdo da postagem são obrigatórios.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

       
        Postagem novaPostagem = new Postagem();
        novaPostagem.setTitulo(titulo);
        novaPostagem.setConteudo(conteudo);

        if (postagemForCourseRadio.isSelected()) {
            Curso selectedCourse = (Curso) postagemCourseComboBox.getSelectedItem();
            if (selectedCourse == null) {
                JOptionPane.showMessageDialog(this, "Selecione um curso para a postagem.", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (loggedInTutor == null || selectedCourse.getTutor() == null || selectedCourse.getTutor().getId() != loggedInTutor.getId()) {
                JOptionPane.showMessageDialog(this, "Você só pode postar em cursos que você criou.", "Acesso Negado", JOptionPane.ERROR_MESSAGE);
                return;
            }
            novaPostagem.setCurso(selectedCourse);
            novaPostagem.setModulo(null);
        } else if (postagemForModuloRadio.isSelected()) {
            Modulo selectedModulo = (Modulo) postagemModuloComboBox.getSelectedItem();
            if (selectedModulo == null) {
                JOptionPane.showMessageDialog(this, "Selecione um módulo para a postagem.", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Curso cursoDoModulo = null;
            try {
                cursoDoModulo = cursoDAO.buscarCursoPorModulo(selectedModulo.getId());
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao verificar autoria do módulo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }
            if (loggedInTutor == null || cursoDoModulo == null || cursoDoModulo.getTutor() == null || cursoDoModulo.getTutor().getId() != loggedInTutor.getId()) {
                JOptionPane.showMessageDialog(this, "Você só pode postar em módulos de cursos que você criou.", "Acesso Negado", JOptionPane.ERROR_MESSAGE);
                return;
            }
            novaPostagem.setModulo(selectedModulo);
            novaPostagem.setCurso(null);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione se a postagem é para um Curso ou Módulo.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            postagemDAO.postar(novaPostagem);
            JOptionPane.showMessageDialog(this, "Postagem criada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            clearCreatePostagemFields();
            loadPostagensForEditDelete(); 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao criar postagem: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updatePostagem() {
        Postagem selectedPostagem = (Postagem) editDeletePostagemComboBox.getSelectedItem();
        if (selectedPostagem == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma postagem para atualizar.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Curso cursoAssociado = null;
        if (selectedPostagem.getCurso() != null) {
            cursoAssociado = selectedPostagem.getCurso();
        } else if (selectedPostagem.getModulo() != null) {
            try {
                cursoAssociado = cursoDAO.buscarCursoPorModulo(selectedPostagem.getModulo().getId());
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao verificar autoria da postagem: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro inesperado ao verificar autoria da postagem: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }
        }
        if (loggedInTutor == null || cursoAssociado == null || cursoAssociado.getTutor() == null || cursoAssociado.getTutor().getId() != loggedInTutor.getId()) {
            JOptionPane.showMessageDialog(this, "Você só pode modificar postagens de cursos/módulos que você criou.", "Acesso Negado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String titulo = editPostagemTitleField.getText();
        String conteudo = editPostagemContentArea.getText();

        if (titulo.isEmpty() || conteudo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Título e conteúdo da postagem são obrigatórios.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        selectedPostagem.setTitulo(titulo);
        selectedPostagem.setConteudo(conteudo);

        try {
            postagemDAO.atualizar(selectedPostagem);
            JOptionPane.showMessageDialog(this, "Postagem atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            loadPostagensForEditDelete(); 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar postagem: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deletePostagem() {
        Postagem selectedPostagem = (Postagem) editDeletePostagemComboBox.getSelectedItem();
        if (selectedPostagem == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma postagem para excluir.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Curso cursoAssociado = null;
        if (selectedPostagem.getCurso() != null) {
            cursoAssociado = selectedPostagem.getCurso();
        } else if (selectedPostagem.getModulo() != null) {
            try {
                cursoAssociado = cursoDAO.buscarCursoPorModulo(selectedPostagem.getModulo().getId());
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao verificar autoria da postagem: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro inesperado ao verificar autoria da postagem: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }
        }
        if (loggedInTutor == null || cursoAssociado == null || cursoAssociado.getTutor() == null || cursoAssociado.getTutor().getId() != loggedInTutor.getId()) {
            JOptionPane.showMessageDialog(this, "Você só pode excluir postagens de cursos/módulos que você criou.", "Acesso Negado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir a postagem '" + selectedPostagem.getTitulo() + "'?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                postagemDAO.deletar(selectedPostagem.getId());
                JOptionPane.showMessageDialog(this, "Postagem excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                loadPostagensForEditDelete();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir postagem: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void clearCreatePostagemFields() {
        createPostagemTitleField.setText("");
        createPostagemContentArea.setText("");
        postagemForCourseRadio.setSelected(true);
        postagemCourseComboBox.setEnabled(true);
        postagemModuloComboBox.setEnabled(false);
        postagemModuloComboBoxModel.removeAllElements();
    }

    private void updateTutorProfile() {
        if (loggedInTutor == null) {
            JOptionPane.showMessageDialog(this, "Nenhum tutor logado para atualizar.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean updated = false;
        String newName = nameProfileField.getText().trim();
        String newEmail = emailProfileField.getText().trim();
        String newPassword = new String(passwordProfileField.getPassword()).trim();
        String newArea = areaProfileField.getText().trim();

        if (!newName.isEmpty() && !newName.equals(loggedInTutor.getNome())) {
            loggedInTutor.setNome(newName);
            updated = true;
        }
        if (!newEmail.isEmpty() && !newEmail.equals(loggedInTutor.getEmail())) {
            loggedInTutor.setEmail(newEmail);
            updated = true;
        }
        if (!newPassword.isEmpty()) {
            loggedInTutor.setSenha(SecurityUtils.hashPassword(newPassword));
            updated = true;
        }
        if (!newArea.isEmpty() && !newArea.equals(loggedInTutor.getArea())) {
            loggedInTutor.setArea(newArea);
            updated = true;
        }

        if (updated) {
            try {
                tutorDAO.atualizar(loggedInTutor);
                JOptionPane.showMessageDialog(this, "Perfil atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                if (getComponent(0) instanceof JLabel) {
                    ((JLabel) getComponent(0)).setText("Bem-vindo(a), Tutor(a) " + loggedInTutor.getNome() + "!");
                }
                passwordProfileField.setText("");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar perfil: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nenhuma alteração foi feita no perfil.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
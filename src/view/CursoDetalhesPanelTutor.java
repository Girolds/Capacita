package view;

import DAO.CursoDAO; 
import DAO.ModuloDAO;
import DAO.PostagemDAO;
import DAO.VideoAulaDAO;
import model.Curso;
import model.Modulo;
import model.Postagem;
import model.Tutor; 
import model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder; 
import java.awt.*;
import java.sql.SQLException; 
import java.util.List; 

public class CursoDetalhesPanelTutor extends CursoDetalhesPanelAluno {

    public TutorPanel tutorPanelCallback;

    private JButton editCourseDetailsButton;
    private JButton deleteCourseDetailsButton;
    private JPanel courseManagementButtonsPanel;

    public CursoDetalhesPanelTutor(TutorPanel tutorPanel) {
        super(); 
        this.tutorPanelCallback = tutorPanel;
        
        courseManagementButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        courseManagementButtonsPanel.setBackground(new Color(240, 248, 255));
        courseManagementButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        editCourseDetailsButton = new JButton("Editar Curso");
        editCourseDetailsButton.setBackground(new Color(255, 165, 0));
        editCourseDetailsButton.setForeground(Color.WHITE);
        editCourseDetailsButton.setFont(new Font("Arial", Font.BOLD, 14));
        editCourseDetailsButton.setPreferredSize(new Dimension(150, 40));
        editCourseDetailsButton.addActionListener(e -> navigateToEditCourse());
        courseManagementButtonsPanel.add(editCourseDetailsButton);

        deleteCourseDetailsButton = new JButton("Excluir Curso");
        deleteCourseDetailsButton.setBackground(new Color(220, 20, 60));
        deleteCourseDetailsButton.setForeground(Color.WHITE);
        deleteCourseDetailsButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteCourseDetailsButton.setPreferredSize(new Dimension(150, 40));
        deleteCourseDetailsButton.addActionListener(e -> deleteCurrentCourse());
        courseManagementButtonsPanel.add(deleteCourseDetailsButton);

        this.add(courseManagementButtonsPanel, BorderLayout.SOUTH);
        courseManagementButtonsPanel.setVisible(false); // Inicia oculto
    }

    @Override
    public void setLoggedInUser(Usuario user) {
        super.setLoggedInUser(user); 
        updateVisibilityBasedOnUserAndCourse(); 
      
    }

    @Override
    public void setCourse(Curso curso) {
        if (curso != null && curso.getTutor() == null && curso.getId() != 0) {
            try {
                Curso tempCurso = cursoDAO.buscar(curso.getId()); 
                if (tempCurso != null && tempCurso.getTutor() != null) {
                    curso.setTutor(tempCurso.getTutor());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.setCourse(curso);
        updateVisibilityBasedOnUserAndCourse(); 
    }

    private void updateVisibilityBasedOnUserAndCourse() {
        boolean isTutorOwner = false;
        if (loggedInUser instanceof Tutor && curso != null && curso.getTutor() != null) {
            isTutorOwner = (curso.getTutor().getId() == ((Tutor) loggedInUser).getId()); 
        }
        courseManagementButtonsPanel.setVisible(isTutorOwner);

        revalidate();
        repaint();
    }

    private void navigateToEditCourse() {
        if (tutorPanelCallback != null && curso != null) {
            tutorPanelCallback.navigateToManageCoursesAndSelect(curso);
        } else {
            JOptionPane.showMessageDialog(this, "Erro: Não é possível editar o curso.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCurrentCourse() {
        if (tutorPanelCallback != null && curso != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir o curso '" + curso.getTitulo() + "' e TODO o seu conteúdo (módulos, videoaulas, postagens)?",
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    cursoDAO.deletar(curso.getId()); 
                    JOptionPane.showMessageDialog(this, "Curso excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    tutorPanelCallback.showMyTutorCoursesPanel();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir curso: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Erro: Não é possível excluir o curso.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void moduleActionCompleted() {
        super.moduleActionCompleted(); 
        revalidate();
        repaint();
    }
}
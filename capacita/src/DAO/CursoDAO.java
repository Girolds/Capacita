package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.Conectante;
import model.Curso;
import model.Tutor;
import model.Modulo; 

public class CursoDAO {

    private TutorDAO tutorDAO = new TutorDAO();

    public void criar(Curso curso) {    
        String sql = "INSERT INTO Curso (titulo, descricao, area, id_tutor) VALUES (?, ?, ?, ?)";    

        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstm.setString(1, curso.getTitulo());
            pstm.setString(2, curso.getDescricao());
            pstm.setString(3, curso.getArea());
            // NOVO: Define o ID do tutor
            if (curso.getTutor() != null && curso.getTutor().getId() != 0) {
                pstm.setInt(4, curso.getTutor().getId());
            } else {
                pstm.setNull(4, java.sql.Types.INTEGER);
            }

            pstm.execute();

            try (ResultSet rset = pstm.getGeneratedKeys()) {
                if (rset.next()) {
                    curso.setId(rset.getInt(1));
                }
            }

            System.out.println("Curso criado com sucesso!");
            
        } catch (SQLException e) {
            System.err.println("Erro SQL ao criar curso: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao criar curso: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Curso> listar() {

        String sql = "SELECT c.id, c.titulo, c.descricao, c.area, c.id_tutor, u.nome AS nome_tutor, u.email AS email_tutor, u.area AS area_tutor " + // Adicionado campos do tutor
                     "FROM Curso c LEFT JOIN Usuario u ON c.id_tutor = u.id";

        List<Curso> cursos = new ArrayList<>();

        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql);
             ResultSet rset = pstm.executeQuery()) {

            while (rset.next()) {
                Curso curso = new Curso();
                curso.setId(rset.getInt("id"));
                curso.setTitulo(rset.getString("titulo"));
                curso.setDescricao(rset.getString("descricao"));
                curso.setArea(rset.getString("area"));

                int idTutor = rset.getInt("id_tutor");
                if (!rset.wasNull()) {    
                    Tutor tutor = new Tutor();
                    tutor.setId(idTutor);
                    tutor.setNome(rset.getString("nome_tutor"));
                    tutor.setEmail(rset.getString("email_tutor")); 
                    tutor.setArea(rset.getString("area_tutor"));   
                    curso.setTutor(tutor);
                }

                cursos.add(curso);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar cursos: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao listar cursos: " + e.getMessage());
            e.printStackTrace();
        }
        return cursos;
    }

    public void atualizar(Curso curso) {

        String sql = "UPDATE Curso SET titulo = ?, descricao = ?, area = ?, id_tutor = ? WHERE id = ?";

        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql)) {

            pstm.setString(1, curso.getTitulo());
            pstm.setString(2, curso.getDescricao());
            pstm.setString(3, curso.getArea());
            
            if (curso.getTutor() != null && curso.getTutor().getId() != 0) {
                pstm.setInt(4, curso.getTutor().getId());
            } else {
                pstm.setNull(4, java.sql.Types.INTEGER);
            }
            pstm.setInt(5, curso.getId());

            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Curso atualizado com sucesso!");
            } else {
                System.out.println("Nenhum curso encontrado com o ID especificado.");
            }
            
        } catch (SQLException e) {
            System.err.println("Erro SQL ao atualizar curso: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao atualizar curso: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void deletar(int id) {
        Connection con = null;    
        try {
            con = Conectante.createConnectionToMySQL();
            con.setAutoCommit(false);    

          
            String selectModulosSql = "SELECT id FROM Modulo WHERE id_curso = ?";
            try (PreparedStatement pstmSelectModulos = con.prepareStatement(selectModulosSql)) {
                pstmSelectModulos.setInt(1, id);
                try (ResultSet rsetModulos = pstmSelectModulos.executeQuery()) {
                    while (rsetModulos.next()) {
                        int idModulo = rsetModulos.getInt("id");
                        
                        String deleteVideoaulasSql = "DELETE FROM Videoaula WHERE id_modulo = ?";
                        try (PreparedStatement pstmDeleteVideoaulas = con.prepareStatement(deleteVideoaulasSql)) {
                            pstmDeleteVideoaulas.setInt(1, idModulo);
                            pstmDeleteVideoaulas.execute();
                        }
                        
                        String deletePostagensModuloSql = "DELETE FROM Postagem WHERE id_modulo = ?";
                        try (PreparedStatement pstmDeletePostagensModulo = con.prepareStatement(deletePostagensModuloSql)) {
                            pstmDeletePostagensModulo.setInt(1, idModulo);
                            pstmDeletePostagensModulo.execute();
                        }
                    }
                }
            }
            
          
            String deletePostagensCursoSql = "DELETE FROM Postagem WHERE id_curso = ? AND id_modulo IS NULL";
            try (PreparedStatement pstmDeletePostagensCurso = con.prepareStatement(deletePostagensCursoSql)) {
                pstmDeletePostagensCurso.setInt(1, id);
                pstmDeletePostagensCurso.execute();
            }


            String deleteModulosSql = "DELETE FROM Modulo WHERE id_curso = ?";
            try (PreparedStatement pstmDeleteModulos = con.prepareStatement(deleteModulosSql)) {
                pstmDeleteModulos.setInt(1, id);
                pstmDeleteModulos.execute();
            }

          
            String deleteUsuarioCursoSql = "DELETE FROM UsuarioCurso WHERE id_curso = ?";
            try (PreparedStatement pstmUsuarioCurso = con.prepareStatement(deleteUsuarioCursoSql)) {
                pstmUsuarioCurso.setInt(1, id);
                pstmUsuarioCurso.execute();
            }

         
            String deleteCursoSql = "DELETE FROM Curso WHERE id = ?";
            try (PreparedStatement pstmDeleteCurso = con.prepareStatement(deleteCursoSql)) {
                pstmDeleteCurso.setInt(1, id);
                pstmDeleteCurso.execute();
            }

            con.commit();
            System.out.println("Curso deletado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro SQL ao deletar curso: " + e.getMessage());
            if (con != null) {
                try {
                    System.err.println("Rollback da transação ao deletar curso.");
                    con.rollback();    
                } catch (SQLException ex) {
                    System.err.println("Erro ao realizar rollback: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao deletar curso: " + e.getMessage());
            if (con != null) {
                try {
                    System.err.println("Rollback da transação ao deletar curso.");
                    con.rollback();    
                } catch (SQLException ex) {
                    System.err.println("Erro ao realizar rollback: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar a conexão no finally: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public Curso buscar(int id) {
        
        String sql = "SELECT c.id, c.titulo, c.descricao, c.area, c.id_tutor, u.nome AS nome_tutor, u.email AS email_tutor, u.area AS area_tutor " +
                     "FROM Curso c LEFT JOIN Usuario u ON c.id_tutor = u.id " +
                     "WHERE c.id = ?";
        Curso curso = new Curso();    

        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setInt(1, id);
            try (ResultSet rset = pstm.executeQuery()) {
                if (rset.next()) {
                    curso.setId(rset.getInt("id"));
                    curso.setTitulo(rset.getString("titulo"));
                    curso.setDescricao(rset.getString("descricao"));
                    curso.setArea(rset.getString("area"));

                    int idTutor = rset.getInt("id_tutor");
                    if (!rset.wasNull()) {
                        Tutor tutor = new Tutor();
                        tutor.setId(idTutor);
                        tutor.setNome(rset.getString("nome_tutor"));
                        tutor.setEmail(rset.getString("email_tutor"));
                        tutor.setArea(rset.getString("area_tutor"));  
                        curso.setTutor(tutor);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar curso: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar curso: " + e.getMessage());
            e.printStackTrace();
        }
        return curso;
    }

   
    public List<Curso> listarPorTutor(int idTutor) throws Exception {
        List<Curso> cursos = new ArrayList<>();
   
        String sql = "SELECT c.id, c.titulo, c.descricao, c.area, c.id_tutor, u.nome AS nome_tutor, u.email AS email_tutor, u.area AS area_tutor " +
                     "FROM Curso c JOIN Usuario u ON c.id_tutor = u.id WHERE c.id_tutor = ?";

        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql)) {

            pstm.setInt(1, idTutor);
            try (ResultSet rset = pstm.executeQuery()) {
                while (rset.next()) {
                    Curso curso = new Curso();
                    curso.setId(rset.getInt("id"));
                    curso.setTitulo(rset.getString("titulo"));
                    curso.setDescricao(rset.getString("descricao"));
                    curso.setArea(rset.getString("area"));

                    Tutor tutor = new Tutor();
                    tutor.setId(rset.getInt("id_tutor"));
                    tutor.setNome(rset.getString("nome_tutor"));
                    tutor.setEmail(rset.getString("email_tutor"));
                    tutor.setArea(rset.getString("area_tutor"));
                    curso.setTutor(tutor);

                    cursos.add(curso);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cursos;
    }

  
    public Curso buscarCursoPorModulo(int idModulo) throws Exception {
    
        String sql = "SELECT c.id, c.titulo, c.descricao, c.area, c.id_tutor, u.nome AS nome_tutor, u.email AS email_tutor, u.area AS area_tutor " +
                     "FROM Curso c JOIN Modulo m ON c.id = m.id_curso " +
                     "JOIN Usuario u ON c.id_tutor = u.id WHERE m.id = ?";
        Curso curso = null;

        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql)) {

            pstm.setInt(1, idModulo);
            try (ResultSet rset = pstm.executeQuery()) {
                if (rset.next()) {
                    curso = new Curso();
                    curso.setId(rset.getInt("id"));
                    curso.setTitulo(rset.getString("titulo"));
                    curso.setDescricao(rset.getString("descricao"));
                    curso.setArea(rset.getString("area"));

                    Tutor tutor = new Tutor();
                    tutor.setId(rset.getInt("id_tutor"));
                    tutor.setNome(rset.getString("nome_tutor"));
                    tutor.setEmail(rset.getString("email_tutor"));
                    tutor.setArea(rset.getString("area_tutor"));
                    curso.setTutor(tutor);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return curso;
    }
}
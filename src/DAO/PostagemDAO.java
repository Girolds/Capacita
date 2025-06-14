package DAO;

import config.Conectante;
import model.Postagem;
import model.Curso;
import model.Modulo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PostagemDAO {

    private CursoDAO cursoDAO = new CursoDAO();
    private ModuloDAO moduloDAO = new ModuloDAO();

    public void postar(Postagem postagem) throws SQLException {
        String sql;
        int generatedId;

        try (Connection con = Conectante.createConnectionToMySQL()) {
            if (postagem.getCurso() != null) {
             
                sql = "INSERT INTO Postagem (titulo, conteudo, id_curso, id_modulo) VALUES (?, ?, ?, NULL)";
                try (PreparedStatement pstm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    pstm.setString(1, postagem.getTitulo());
                    pstm.setString(2, postagem.getConteudo());
                    pstm.setInt(3, postagem.getCurso().getId());
                    pstm.executeUpdate();
                    try (ResultSet rset = pstm.getGeneratedKeys()) {
                        if (rset.next()) {
                            generatedId = rset.getInt(1);
                            postagem.setId(generatedId);
                        }
                    }
                }
            } else if (postagem.getModulo() != null) {
                
                sql = "INSERT INTO Postagem (titulo, conteudo, id_curso, id_modulo) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    pstm.setString(1, postagem.getTitulo());
                    pstm.setString(2, postagem.getConteudo());
                 
                    Curso cursoDoModulo = moduloDAO.buscar(postagem.getModulo().getId()).getCurso();
                    pstm.setInt(3, cursoDoModulo.getId());
                    pstm.setInt(4, postagem.getModulo().getId());
                    pstm.executeUpdate();
                    try (ResultSet rset = pstm.getGeneratedKeys()) {
                        if (rset.next()) {
                            generatedId = rset.getInt(1);
                            postagem.setId(generatedId);
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("Postagem deve estar associada a um Curso ou a um Módulo.");
            }
            System.out.println("Postagem criada com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro SQL ao criar postagem: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao criar postagem: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Erro inesperado ao criar postagem", e);
        }
    }

    public List<Postagem> listarPorModulo(int id_modulo) {
        String sql = "SELECT id, titulo, conteudo, id_curso, id_modulo FROM Postagem WHERE id_modulo = ?";
        List<Postagem> postagens = new ArrayList<>();

        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setInt(1, id_modulo);
            try (ResultSet rset = pstm.executeQuery()) {
                while (rset.next()) {
                    Postagem postagem = new Postagem();
                    postagem.setId(rset.getInt("id"));
                    postagem.setTitulo(rset.getString("titulo"));
                    postagem.setConteudo(rset.getString("conteudo"));
                    
                    int idCurso = rset.getInt("id_curso");
                    if (!rset.wasNull()) {
                        postagem.setCurso(cursoDAO.buscar(idCurso));
                    }
                    int idModulo = rset.getInt("id_modulo");
                    if (!rset.wasNull()) {
                        postagem.setModulo(moduloDAO.buscar(idModulo));
                    }
                    postagens.add(postagem);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar postagens por módulo: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao listar postagens por módulo: " + e.getMessage());
            e.printStackTrace();
        }
        return postagens;
    }

    
    public List<Postagem> listarPorCursoSemModulo(int id_curso) {
        String sql = "SELECT id, titulo, conteudo, id_curso, id_modulo FROM Postagem WHERE id_curso = ? AND id_modulo IS NULL";
        List<Postagem> postagens = new ArrayList<>();

        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setInt(1, id_curso);
            try (ResultSet rset = pstm.executeQuery()) {
                while (rset.next()) {
                    Postagem postagem = new Postagem();
                    postagem.setId(rset.getInt("id"));
                    postagem.setTitulo(rset.getString("titulo"));
                    postagem.setConteudo(rset.getString("conteudo"));

                    int idCurso = rset.getInt("id_curso");
                    if (!rset.wasNull()) {
                        postagem.setCurso(cursoDAO.buscar(idCurso));
                    }
      
                    postagens.add(postagem);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar postagens por curso: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao listar postagens por curso: " + e.getMessage());
            e.printStackTrace();
        }
        return postagens;
    }

  
    public List<Postagem> listarTodasPorCurso(int id_curso) {
        String sql = "SELECT id, titulo, conteudo, id_curso, id_modulo FROM Postagem WHERE id_curso = ?";
        List<Postagem> postagens = new ArrayList<>();

        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setInt(1, id_curso);
            try (ResultSet rset = pstm.executeQuery()) {
                while (rset.next()) {
                    Postagem postagem = new Postagem();
                    postagem.setId(rset.getInt("id"));
                    postagem.setTitulo(rset.getString("titulo"));
                    postagem.setConteudo(rset.getString("conteudo"));

                    int idCurso = rset.getInt("id_curso");
                    if (!rset.wasNull()) {
                        postagem.setCurso(cursoDAO.buscar(idCurso));
                    }
                    int idModulo = rset.getInt("id_modulo");
                    if (!rset.wasNull()) {
                        postagem.setModulo(moduloDAO.buscar(idModulo));
                    }
                    postagens.add(postagem);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar todas as postagens por curso: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao listar todas as postagens por curso: " + e.getMessage());
            e.printStackTrace();
        }
        return postagens;
    }
    
    public void atualizar(Postagem postagem) throws SQLException {
        String sql;
        
        try (Connection con = Conectante.createConnectionToMySQL()) {
            if (postagem.getCurso() != null && postagem.getModulo() == null) {
  
                sql = "UPDATE Postagem SET titulo = ?, conteudo = ?, id_curso = ?, id_modulo = NULL WHERE id = ?";
                try (PreparedStatement pstm = con.prepareStatement(sql)) {
                    pstm.setString(1, postagem.getTitulo());
                    pstm.setString(2, postagem.getConteudo());
                    pstm.setInt(3, postagem.getCurso().getId());
                    pstm.setInt(4, postagem.getId());
                    pstm.executeUpdate();
                }
            } else if (postagem.getModulo() != null) {
    
                Curso cursoDoModulo = moduloDAO.buscar(postagem.getModulo().getId()).getCurso();
                sql = "UPDATE Postagem SET titulo = ?, conteudo = ?, id_curso = ?, id_modulo = ? WHERE id = ?";
                try (PreparedStatement pstm = con.prepareStatement(sql)) {
                    pstm.setString(1, postagem.getTitulo());
                    pstm.setString(2, postagem.getConteudo());
                    pstm.setInt(3, cursoDoModulo.getId());
                    pstm.setInt(4, postagem.getModulo().getId());
                    pstm.setInt(5, postagem.getId());
                    pstm.executeUpdate();
                }
            } else {
                throw new IllegalArgumentException("Postagem para atualização deve estar associada a um Curso ou a um Módulo.");
            }
            System.out.println("Postagem atualizada com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro SQL ao atualizar postagem: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao atualizar postagem: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Erro inesperado ao atualizar postagem", e);
        }
    }


    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM Postagem WHERE id = ?";

        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setInt(1, id);
            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Postagem deletada com sucesso!");
            } else {
                System.out.println("Nenhuma postagem encontrada com o ID especificado.");
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao deletar postagem: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao deletar postagem: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Erro inesperado ao deletar postagem", e);
        }
    }

    public Postagem buscar(int id) throws SQLException {
        String sql = "SELECT id, titulo, conteudo, id_curso, id_modulo FROM Postagem WHERE id = ?";
        Postagem postagem = null;

        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setInt(1, id);
            try (ResultSet rset = pstm.executeQuery()) {
                if (rset.next()) {
                    postagem = new Postagem();
                    postagem.setId(rset.getInt("id"));
                    postagem.setTitulo(rset.getString("titulo"));
                    postagem.setConteudo(rset.getString("conteudo"));

                    int idCurso = rset.getInt("id_curso");
                    if (!rset.wasNull()) {
                        postagem.setCurso(cursoDAO.buscar(idCurso));
                    }
                    int idModulo = rset.getInt("id_modulo");
                    if (!rset.wasNull()) {
                        postagem.setModulo(moduloDAO.buscar(idModulo));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar postagem: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar postagem: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Erro inesperado ao buscar postagem", e);
        }
        return postagem;
    }
}
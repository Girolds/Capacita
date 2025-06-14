package DAO;

import config.Conectante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List; // IMPORTE CORRETO: java.util.List
import model.Modulo;
import model.Curso;

public class ModuloDAO {

    private CursoDAO cursoDAO = new CursoDAO();

    public void criar(Modulo modulo, int id_curso) throws SQLException {
        
        String sql = "INSERT INTO Modulo (titulo, conteudo, id_curso) VALUES (?, ?, ?)";
        
        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstm.setString(1, modulo.getTitulo());
            pstm.setString(2, modulo.getDescricao());
            pstm.setInt(3, id_curso);

            pstm.execute();
            
            try (ResultSet rset = pstm.getGeneratedKeys()) {
                if (rset.next()) {
                    modulo.setId(rset.getInt(1));
                }
            }
            Curso curso = cursoDAO.buscar(id_curso);
            modulo.setCurso(curso);

            System.out.println("Módulo criado com sucesso!");
            
        } catch (SQLException e) {
            System.err.println("Erro SQL ao criar módulo: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao criar módulo: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Erro inesperado ao criar módulo", e);
        }
    }

    
    public List<Modulo> listarPorCurso(int id_curso) {
        
        String sql = "SELECT m.id, m.titulo, m.conteudo AS descricao, m.id_curso, c.titulo AS curso_titulo FROM Modulo m JOIN Curso c ON m.id_curso = c.id WHERE m.id_curso = ?";
        List<Modulo> modulos = new ArrayList<>();

        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setInt(1, id_curso);
            try (ResultSet rset = pstm.executeQuery()) {
                while (rset.next()) {
                    Modulo modulo = new Modulo();
                    
                    modulo.setId(rset.getInt("id"));
                    modulo.setTitulo(rset.getString("titulo"));

                    modulo.setDescricao(rset.getString("descricao")); 
                    
                    int cursoId = rset.getInt("id_curso");
                    if (!rset.wasNull()) {
                        Curso curso = new Curso(); 
                        curso.setId(cursoId);
                        curso.setTitulo(rset.getString("curso_titulo"));
                        modulo.setCurso(curso);
                    }
                    modulos.add(modulo);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar módulos por curso: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao listar módulos por curso: " + e.getMessage());
            e.printStackTrace();
        }
        return modulos;
    }

    
    public void atualizar(Modulo modulo) throws SQLException {
        

        String sql = "UPDATE Modulo SET titulo = ?, conteudo = ? WHERE id = ?";

        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql)) {

            pstm.setString(1, modulo.getTitulo());
            pstm.setString(2, modulo.getDescricao());
            pstm.setInt(3, modulo.getId());

            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Módulo atualizado com sucesso!");
            } else {
                System.out.println("Nenhum módulo encontrado com o ID especificado.");
            }
            
        } catch (SQLException e) {
            System.err.println("Erro SQL ao atualizar módulo: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao atualizar módulo: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Erro inesperado ao atualizar módulo", e);
        }
    }

    
    public void deletar(int id) throws SQLException {
        Connection con = null;    
        try {
            con = Conectante.createConnectionToMySQL();
            con.setAutoCommit(false);    
            
            String deleteVideoaulasSql = "DELETE FROM Videoaula WHERE id_modulo = ?";
            try (PreparedStatement pstmVideoaulas = con.prepareStatement(deleteVideoaulasSql)) {
                pstmVideoaulas.setInt(1, id);
                pstmVideoaulas.execute();
            }

            String deletePostagensSql = "DELETE FROM Postagem WHERE id_modulo = ?";
            try (PreparedStatement pstmPostagens = con.prepareStatement(deletePostagensSql)) {
                pstmPostagens.setInt(1, id);
                pstmPostagens.execute();
            }

            String deleteModuloSql = "DELETE FROM Modulo WHERE id = ?";
            try (PreparedStatement pstmModulo = con.prepareStatement(deleteModuloSql)) {
                pstmModulo.setInt(1, id);
                pstmModulo.execute();
            }

            con.commit();    
            System.out.println("Módulo deletado com sucesso!");
            
        } catch (SQLException e) {
            System.err.println("Erro SQL ao deletar módulo: " + e.getMessage());
            if (con != null) {
                try {
                    System.err.println("Rollback da transação ao deletar módulo.");
                    con.rollback();    
                } catch (SQLException ex) {
                    System.err.println("Erro ao realizar rollback: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao deletar módulo: " + e.getMessage());
            if (con != null) {
                try {
                    System.err.println("Rollback da transação ao deletar módulo.");
                    con.rollback();    
                } catch (SQLException ex) {
                    System.err.println("Erro ao realizar rollback: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            throw new SQLException("Erro inesperado ao deletar módulo", e);
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
    
    
    public Modulo buscar(int id) throws SQLException {
        

        String sql = "SELECT m.id, m.titulo, m.conteudo AS descricao, m.id_curso, c.titulo AS curso_titulo, c.id_tutor FROM Modulo m JOIN Curso c ON m.id_curso = c.id WHERE m.id = ?";
        Modulo modulo = null;
        
        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setInt(1, id);
            try (ResultSet rset = pstm.executeQuery()) {
                if (rset.next()) {
                    modulo = new Modulo();
                    modulo.setId(rset.getInt("id"));
                    modulo.setTitulo(rset.getString("titulo"));

                    modulo.setDescricao(rset.getString("descricao"));
                    
                    int idCurso = rset.getInt("id_curso");
                    if (!rset.wasNull()) {
                        Curso curso = cursoDAO.buscar(idCurso);
                        modulo.setCurso(curso);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar módulo: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar módulo: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Erro inesperado ao buscar módulo", e);
        }
        return modulo;
    }
}
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.Conectante;
import model.Tutor;


public class TutorDAO {

    public void criar(Tutor tutor) {
        
        String sql = "INSERT INTO Usuario (nome, email, senha, tipo_usuario, area) VALUES (?, ?, ?, 'tutor', ?)";
        
        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstm.setString(1, tutor.getNome());
            pstm.setString(2, tutor.getEmail());
            pstm.setString(3, tutor.getSenha()); 
            pstm.setString(4, tutor.getArea()); 

            pstm.execute();
            
            try (ResultSet rset = pstm.getGeneratedKeys()) {
                if (rset.next()) {
                    tutor.setId(rset.getInt(1));
                }
            }

            String tutorSql = "INSERT INTO Tutor (id_usuario, area_especializacao) VALUES (?, ?)";
            try (PreparedStatement tutorPstm = con.prepareStatement(tutorSql)) {
                tutorPstm.setInt(1, tutor.getId());
                tutorPstm.setString(2, tutor.getArea()); 
                tutorPstm.execute();
            }

            System.out.println("Tutor criado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro SQL ao criar tutor: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao criar tutor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Tutor> listarTutores() {
        
        String sql = "SELECT u.id, u.nome, u.email, u.senha, u.area FROM Usuario u JOIN Tutor t ON u.id = t.id_usuario WHERE u.tipo_usuario = 'tutor'";
        List<Tutor> tutores = new ArrayList<>();

        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql);
             ResultSet rset = pstm.executeQuery()) {

            while (rset.next()) {
                Tutor tutor = new Tutor();
                
                tutor.setId(rset.getInt("id"));
                tutor.setNome(rset.getString("nome"));
                tutor.setEmail(rset.getString("email"));
                tutor.setSenha(rset.getString("senha"));
                tutor.setArea(rset.getString("area")); 

                tutores.add(tutor);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar tutores: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao listar tutores: " + e.getMessage());
            e.printStackTrace();
        }
        return tutores;
    }

    public void atualizar(Tutor tutor) {
        
        String sql = "UPDATE Usuario SET nome = ?, email = ?, senha = ?, area = ? WHERE id = ? AND tipo_usuario = 'tutor'";

        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql)) {

            pstm.setString(1, tutor.getNome());
            pstm.setString(2, tutor.getEmail());
            pstm.setString(3, tutor.getSenha()); 
            pstm.setString(4, tutor.getArea());
            pstm.setInt(5, tutor.getId());

            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Tutor atualizado com sucesso!");
          
                String updateTutorSql = "UPDATE Tutor SET area_especializacao = ? WHERE id_usuario = ?";
                try (PreparedStatement updateTutorPstm = con.prepareStatement(updateTutorSql)) {
                    updateTutorPstm.setString(1, tutor.getArea());
                    updateTutorPstm.setInt(2, tutor.getId());
                    updateTutorPstm.executeUpdate();
                }
            } else {
                System.out.println("Nenhum tutor encontrado com o ID especificado ou não é um tutor.");
            }
            
        } catch (SQLException e) {
            System.err.println("Erro SQL ao atualizar tutor: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao atualizar tutor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deletar(int id) {
        
        String deleteTutorSql = "DELETE FROM Tutor WHERE id_usuario = ?";
        
        String deleteUsuarioSql = "DELETE FROM Usuario WHERE id = ? AND tipo_usuario = 'tutor'";

        Connection con = null;
        try {
            con = Conectante.createConnectionToMySQL();
            con.setAutoCommit(false);

            try (PreparedStatement pstmTutor = con.prepareStatement(deleteTutorSql)) {
                pstmTutor.setInt(1, id);
                pstmTutor.execute();
            }

            try (PreparedStatement pstmUsuario = con.prepareStatement(deleteUsuarioSql)) {
                pstmUsuario.setInt(1, id);
                pstmUsuario.execute();
            }

            con.commit();
            System.out.println("Tutor deletado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro SQL ao deletar tutor: " + e.getMessage());
            if (con != null) {
                try {
                    System.err.println("Rollback da transação ao deletar tutor.");
                    con.rollback(); 
                } catch (SQLException ex) {
                    System.err.println("Erro ao realizar rollback: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao deletar tutor: " + e.getMessage());
            if (con != null) {
                try {
                    System.err.println("Rollback da transação ao deletar tutor.");
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

    public Tutor buscar(int id) {
        String sql = "SELECT u.id, u.nome, u.email, u.senha, u.tipo_usuario, u.area FROM Usuario u WHERE u.id = ? AND u.tipo_usuario = 'tutor'";
        Tutor tutor = new Tutor(); 

        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setInt(1, id);
            try (ResultSet rset = pstm.executeQuery()) {
                if (rset.next()) {
                    tutor.setId(rset.getInt("id"));
                    tutor.setNome(rset.getString("nome"));
                    tutor.setEmail(rset.getString("email"));
                    tutor.setSenha(rset.getString("senha"));
                    tutor.setArea(rset.getString("area"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar tutor: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar tutor: " + e.getMessage());
            e.printStackTrace();
        }
        return tutor;
    }
}
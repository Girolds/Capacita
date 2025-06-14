package DAO;

import config.Conectante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.VideoAula;

public class VideoAulaDAO {

    public void postar(VideoAula videoAula, int id_modulo) throws SQLException {
        String sql = "INSERT INTO Videoaula (titulo, url, id_modulo) VALUES (?, ?, ?)";
        
        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstm.setString(1, videoAula.getTitulo());
            pstm.setString(2, videoAula.getUrl());
            pstm.setInt(3, id_modulo);

            pstm.execute();
            
            try (ResultSet rset = pstm.getGeneratedKeys()) {
                if (rset.next()) {
                    videoAula.setId(rset.getInt(1));
                }
            }
            videoAula.setIdModulo(id_modulo); 
            
            System.out.println("Videoaula criada com sucesso!");
            
        } catch (SQLException e) {
            System.err.println("Erro SQL ao criar videoaula: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao criar videoaula: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Erro inesperado ao criar videoaula", e);
        }
    }


    public List<VideoAula> listarPorModulo(int id_modulo) {
        
        String sql = "SELECT id, titulo, url, id_modulo FROM Videoaula WHERE id_modulo = ?";

        List<VideoAula> videoAulas = new ArrayList<>();

        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setInt(1, id_modulo);
            try (ResultSet rset = pstm.executeQuery()) {
                while (rset.next()) {
                    VideoAula videoAula = new VideoAula();
                    
                    videoAula.setId(rset.getInt("id"));
                    videoAula.setTitulo(rset.getString("titulo"));
                    videoAula.setUrl(rset.getString("url"));
                    videoAula.setIdModulo(rset.getInt("id_modulo"));
                    
                    videoAulas.add(videoAula);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar videoaulas por módulo: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao listar videoaulas por módulo: " + e.getMessage());
            e.printStackTrace();
        }
        return videoAulas;
    }

    
    public void atualizar(VideoAula videoAula) throws SQLException {
        
        String sql = "UPDATE Videoaula SET titulo = ?, url = ? WHERE id = ?";

        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql)) {

            pstm.setString(1, videoAula.getTitulo());
            pstm.setString(2, videoAula.getUrl());
            pstm.setInt(3, videoAula.getId());

            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Videoaula atualizada com sucesso!");
            } else {
                System.out.println("Nenhuma videoaula encontrada com o ID especificado.");
            }
            
        } catch (SQLException e) {
            System.err.println("Erro SQL ao atualizar videoaula: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao atualizar videoaula: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Erro inesperado ao atualizar videoaula", e);
        }
    }

    
    public void deletar(int id) throws SQLException {
        
        String sql = "DELETE FROM Videoaula WHERE id = ?";

        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setInt(1, id);
            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Videoaula deletada com sucesso!");
            } else {
                System.out.println("Nenhuma videoaula encontrada com o ID especificado.");
            }
            
        } catch (SQLException e) {
            System.err.println("Erro SQL ao deletar videoaula: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao deletar videoaula: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Erro inesperado ao deletar videoaula", e);
        }
    }

    
    public VideoAula buscar(int id) throws SQLException {
        String sql = "SELECT id, titulo, url, id_modulo FROM Videoaula WHERE id = ?";
        VideoAula videoAula = null;

        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setInt(1, id);
            try (ResultSet rset = pstm.executeQuery()) {
                if (rset.next()) {
                    videoAula = new VideoAula();
                    videoAula.setId(rset.getInt("id"));
                    videoAula.setTitulo(rset.getString("titulo"));
                    videoAula.setUrl(rset.getString("url"));
                    videoAula.setIdModulo(rset.getInt("id_modulo")); 
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar videoaula: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar videoaula: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Erro inesperado ao buscar videoaula", e);
        }
        return videoAula;
    }
}
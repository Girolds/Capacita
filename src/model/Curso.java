package model;

import java.util.List;

public class Curso {
    private int id; 
    private String titulo;
    private String descricao;
    private String area; 
    private Tutor tutor;
    
    private List<Modulo> modulos;
    
    private List<Postagem> postagens;

    public List<Postagem> getPostagens() {
        return postagens;
    }
    public void setPostagens(List<Postagem> postagens) {
        this.postagens = postagens;
    }
    public List<Modulo> getModulos() {
        return modulos;
    }
    public void setModulos(List<Modulo> modulos) {
        this.modulos = modulos;
    }
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }
}
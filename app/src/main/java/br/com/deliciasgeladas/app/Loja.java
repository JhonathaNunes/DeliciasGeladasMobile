package br.com.deliciasgeladas.app;

/**
 * Created by 16254855 on 11/12/2017.
 */

public class Loja {
    int id;
    String nome;
    String logradouro;
    String telefone;
    String imagem;
    String horario;
    Double lon;
    Double lat;

    public static Loja criar(int id, String nome, String logradouro,
                     String telefone, String imagem, String horario, Double lat, Double lon){
        Loja l = new Loja();
        l.setId(id);
        l.setNome(nome);
        l.setLogradouro(logradouro);
        l.setTelefone(telefone);
        l.setImagem(imagem);
        l.setHorario(horario);
        l.setLat(lat);
        l.setLon(lon);

        return l;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }
}

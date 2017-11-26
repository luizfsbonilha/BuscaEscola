package br.bonilha.buscaescola;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Luiz Fernando on 21/11/2017.
 */

public class Escola implements Serializable{
    private String nome;
    private String rede;
    private String municipio;
    private String uf;
    private String situacaoFuncionamento;
    private String categoriaEscolaPrivada;
    private String esferaAdministrativa;
    private double latitude;
    private double longitude;

    public Escola(){

    }

    public Escola(String nome, String rede, Double latitude, Double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRede() {
        return rede;
    }

    public void setRede(String rede) {
        this.rede = rede;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getSituacaoFuncionamento() {
        return situacaoFuncionamento;
    }

    public void setSituacaoFuncionamento(String situacaoFuncionamento) {
        this.situacaoFuncionamento = situacaoFuncionamento;
    }

    public String getCategoriaEscolaPrivada() {
        return categoriaEscolaPrivada;
    }

    public void setCategoriaEscolaPrivada(String categoriaEscolaPrivada) {
        this.categoriaEscolaPrivada = categoriaEscolaPrivada;
    }

    public String getEsferaAdministrativa() {
        return esferaAdministrativa;
    }

    public void setEsferaAdministrativa(String esferaAdministrativa) {
        this.esferaAdministrativa = esferaAdministrativa;
    }


}

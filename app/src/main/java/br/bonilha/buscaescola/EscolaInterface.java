package br.bonilha.buscaescola;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EscolaInterface {
    @GET("rest/escolas")
    Call<List<Escola>> listarEscolas();
}

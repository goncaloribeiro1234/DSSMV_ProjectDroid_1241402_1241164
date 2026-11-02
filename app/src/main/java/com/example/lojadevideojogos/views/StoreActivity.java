package com.example.lojadevideojogos.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.lojadevideojogos.controllers.StoreController;
import com.example.lojadevideojogos.databinding.ActivityStoreBinding;
import com.example.lojadevideojogos.util.DataState;
import com.example.lojadevideojogos.util.SessionManager;

import java.util.ArrayList;

public class StoreActivity extends AppCompatActivity {

    private ActivityStoreBinding binding;
    private GameCardAdapter adapter;
    private StoreController storeController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurar RecyclerView para 2 colunas
        binding.rvGames.setLayoutManager(new GridLayoutManager(this, 2));

        // 1. Inicializar o Adapter e abrir o ecrã de detalhes ao clicar num jogo
        adapter = new GameCardAdapter(new ArrayList<>(), game -> {
            Intent intent = new Intent(StoreActivity.this, GameDetailActivity.class);
            intent.putExtra("EXTRA_GAME", game); // Passa o objeto Game inteiro para o detalhe
            startActivity(intent);
        });
        binding.rvGames.setAdapter(adapter);

        // 2. Instanciar o controlador e ficar à escuta da internet
        storeController = new StoreController();
        storeController.getGames().observe(this, state -> {
            if (state.status == DataState.Status.SUCCESS) {
                adapter.setGames(state.data);
            } else if (state.status == DataState.Status.ERROR) {
                Toast.makeText(this, "Erro a carregar loja: " + state.errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // 3. Disparar o pedido GET para ir buscar os jogos ao RestDB
        storeController.loadGames();

        // Configurar Navegação
        binding.btnNavSearch.setOnClickListener(v -> {
            Intent intent = new Intent(StoreActivity.this, com.example.lojadevideojogos.MainActivity.class);
            startActivity(intent);
        });

        binding.btnNavExit.setOnClickListener(v -> {
            SessionManager.getInstance().setProfile(SessionManager.Profile.CLIENTE); // Reset
            Intent intent = new Intent(StoreActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
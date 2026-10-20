package com.example.lojadevideojogos;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.lojadevideojogos.controllers.RawgController;
import com.example.lojadevideojogos.databinding.ActivityMainBinding;
import com.example.lojadevideojogos.model.Game;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private RawgController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        controller = new RawgController();

        // 1. Observar os resultados da API
        controller.getSearchResults().observe(this, state -> {
            switch (state.status) {
                case LOADING:
                    binding.tvResultados.setText("A pesquisar...\nPor favor aguarde.");
                    binding.ivCover.setImageDrawable(null); // Limpa a imagem anterior
                    break;
                case SUCCESS:
                    if (!state.data.isEmpty()) {
                        // Pega no PRIMEIRO jogo para mostrar a capa
                        Game primeiroJogo = state.data.get(0);

                        // Usar o Glide para carregar a imagem da Internet
                        Glide.with(this)
                                .load(primeiroJogo.getImageUrl())
                                .into(binding.ivCover);

                        // Construir o texto com os restantes jogos
                        StringBuilder sb = new StringBuilder("Top Resultados:\n\n");
                        for (int i = 0; i < Math.min(5, state.data.size()); i++) {
                            Game g = state.data.get(i);
                            sb.append(i + 1).append("º ").append(g.getTitle()).append("\n");
                        }
                        binding.tvResultados.setText(sb.toString());
                    }
                    break;
                case EMPTY:
                    binding.tvResultados.setText("Nenhum jogo encontrado.");
                    binding.ivCover.setImageDrawable(null);
                    break;
                case ERROR:
                    binding.tvResultados.setText("ERRO: " + state.errorMessage);
                    binding.ivCover.setImageDrawable(null);
                    break;
            }
        });

        // 2. Configurar o clique do Botão de Pesquisa
        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = binding.etSearch.getText().toString();
                if (!query.isEmpty()) {
                    controller.searchGames(query); // Faz a pesquisa com o que o utilizador escreveu
                }
            }
        });
    }
}
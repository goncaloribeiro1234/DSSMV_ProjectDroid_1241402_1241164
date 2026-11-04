package com.example.lojadevideojogos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.lojadevideojogos.controllers.RawgController;
import com.example.lojadevideojogos.databinding.ActivityMainBinding;
import com.example.lojadevideojogos.model.Game;
import com.example.lojadevideojogos.views.ScannerActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private RawgController controller;

    // Lançador para receber o resultado do Scanner de Capas
    private final ActivityResultLauncher<Intent> scannerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String scannedGameName = result.getData().getStringExtra("SCANNED_GAME_NAME");
                    if (scannedGameName != null && !scannedGameName.isEmpty()) {
                        // Coloca automaticamente o nome na barra de pesquisa
                        binding.etSearch.setText(scannedGameName);
                        // Dispara logo a pesquisa na RAWG API
                        controller.searchGames(scannedGameName);
                    }
                }
            }
    );

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
                    binding.ivCover.setImageDrawable(null);
                    break;
                case SUCCESS:
                    if (!state.data.isEmpty()) {
                        Game primeiroJogo = state.data.get(0);

                        Glide.with(this)
                                .load(primeiroJogo.getImageUrl())
                                .into(binding.ivCover);

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

        // 2. Configurar o clique do Botão de Pesquisa manual
        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = binding.etSearch.getText().toString();
                if (!query.isEmpty()) {
                    controller.searchGames(query);
                }
            }
        });

        // 3. Configurar o botão para abrir o Scanner de Capas (Certifica-te que tens um botão com id 'btnOpenScanner' no activity_main.xml, ou ajusta o ID)
        // Se ainda não tens o botão no XML, podes adicioná-lo no activity_main.xml

        binding.btnOpenScanner.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
            scannerLauncher.launch(intent);
        });

    }
}
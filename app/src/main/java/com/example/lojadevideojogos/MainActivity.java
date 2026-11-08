package com.example.lojadevideojogos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.lojadevideojogos.controllers.RawgController;
import com.example.lojadevideojogos.databinding.ActivityMainBinding;
import com.example.lojadevideojogos.model.Game;
import com.example.lojadevideojogos.views.GameDetailActivity;
import com.example.lojadevideojogos.views.ScannerActivity;

import android.graphics.drawable.Drawable;

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
                        // Dispara logo a pesquisa
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

        // 1. Observar os resultados da API / Mocks
        controller.getSearchResults().observe(this, state -> {
            switch (state.status) {
                case LOADING:
                    binding.tvResultados.setText("A pesquisar...\nPor favor aguarde.");
                    binding.ivCover.setImageDrawable(null);
                    break;
                case SUCCESS:
                    if (!state.data.isEmpty()) {
                        Game primeiroJogo = state.data.get(0);

                        Log.d("DEBUG_URL", "A tentar carregar URL: " + primeiroJogo.getImageUrl());

                        // Glide com listener para detetar erros exatos no Logcat
                        Glide.with(this)
                                .load(primeiroJogo.getImageUrl())
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        Log.e("GLIDE_FAIL", "ERRO AO CARREGAR IMAGEM: " + (e != null ? e.getMessage() : "desconhecido"), e);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        Log.d("GLIDE_SUCCESS", "Imagem carregada com sucesso!");
                                        return false;
                                    }
                                })
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
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

        // 3. Configurar o botão para abrir o Scanner de Capas
        binding.btnOpenScanner.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
            scannerLauncher.launch(intent);
        });

        // 4. Configurar clique na capa para passar o objeto Game completo para o ecrã de detalhes
        binding.ivCover.setOnClickListener(v -> {
            if (controller.getSearchResults().getValue() != null &&
                    controller.getSearchResults().getValue().data != null &&
                    !controller.getSearchResults().getValue().data.isEmpty()) {

                Game jogoSelecionado = controller.getSearchResults().getValue().data.get(0);

                Intent intent = new Intent(MainActivity.this, GameDetailActivity.class);
                intent.putExtra("EXTRA_GAME", jogoSelecionado);
                startActivity(intent);
            }
        });
    }
}
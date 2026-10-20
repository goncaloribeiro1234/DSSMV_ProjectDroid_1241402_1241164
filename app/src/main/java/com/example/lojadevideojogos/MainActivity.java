package com.example.lojadevideojogos;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lojadevideojogos.controllers.RawgController;
import com.example.lojadevideojogos.model.Game;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- TESTE FASE 3 ---
        RawgController controller = new RawgController();

        // Observar as mudanças de estado da chamada à API
        controller.getSearchResults().observe(this, state -> {
            switch (state.status) {
                case LOADING:
                    Log.d("RAWG", "A pesquisar jogos...");
                    break;
                case SUCCESS:
                    Log.d("RAWG", "SUCESSO! Encontrados: " + state.data.size());
                    for (int i = 0; i < Math.min(5, state.data.size()); i++) {
                        Game g = state.data.get(i);
                        Log.d("RAWG", (i+1) + "º Jogo: " + g.getTitle() + " (ID RAWG: " + g.getRawgId() + ")");
                    }
                    break;
                case EMPTY:
                    Log.d("RAWG", "Nenhum jogo encontrado.");
                    break;
                case ERROR:
                    Log.e("RAWG", "ERRO: " + state.errorMessage);
                    break;
            }
        });

        // Disparar a pesquisa (o resultado vai aparecer no Logcat)
        controller.searchGames("zelda");
    }
}
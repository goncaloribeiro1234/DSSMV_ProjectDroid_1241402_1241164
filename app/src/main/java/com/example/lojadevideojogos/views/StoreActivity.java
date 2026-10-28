package com.example.lojadevideojogos.views;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.lojadevideojogos.databinding.ActivityStoreBinding;
import com.example.lojadevideojogos.mock.MockData;
import com.example.lojadevideojogos.util.SessionManager;

public class StoreActivity extends AppCompatActivity {

    private ActivityStoreBinding binding;
    private GameCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurar RecyclerView para 2 colunas
        binding.rvGames.setLayoutManager(new GridLayoutManager(this, 2));

        // Carregar dados Mock
        adapter = new GameCardAdapter(MockData.getMockGames(), game -> {
            // Abrir detalhe passando o Título por agora (na fase CRUD passamos o ID)
            Intent intent = new Intent(StoreActivity.this, GameDetailActivity.class);
            intent.putExtra("GAME_TITLE", game.getTitle());
            startActivity(intent);
        });
        binding.rvGames.setAdapter(adapter);

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
package com.example.lojadevideojogos.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lojadevideojogos.R;
import com.example.lojadevideojogos.util.SessionManager;

public class GameDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Cria um layout simples activity_game_detail.xml com uma TextView (tvDetailTitle) e botões
        setContentView(R.layout.activity_game_detail);

        String title = getIntent().getStringExtra("GAME_TITLE");
        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        if (tvTitle != null && title != null) tvTitle.setText(title);

        Button btnBuy = findViewById(R.id.btnBuy);
        Button btnEdit = findViewById(R.id.btnEdit);

        // Lógica de Perfil
        if (SessionManager.getInstance().isManager()) {
            if(btnBuy != null) btnBuy.setVisibility(View.GONE);
            if(btnEdit != null) btnEdit.setVisibility(View.VISIBLE);
        } else {
            if(btnBuy != null) btnBuy.setVisibility(View.VISIBLE);
            if(btnEdit != null) btnEdit.setVisibility(View.GONE);
        }
    }
}
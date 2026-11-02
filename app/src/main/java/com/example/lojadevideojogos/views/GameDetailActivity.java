package com.example.lojadevideojogos.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.lojadevideojogos.R;
import com.example.lojadevideojogos.controllers.StoreController;
import com.example.lojadevideojogos.model.Game;
import com.example.lojadevideojogos.util.DataState;
import com.example.lojadevideojogos.util.SessionManager;

public class GameDetailActivity extends AppCompatActivity {

    private StoreController storeController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        // Instanciar o StoreController para lidar com as operações na BD
        storeController = new StoreController();

        // Referências aos componentes do XML
        ImageView imageCapa = findViewById(R.id.imageDetailCapa);
        TextView tvTitle = findViewById(R.id.textDetailTitulo);
        TextView textPreco = findViewById(R.id.textDetailPreco);
        TextView textStock = findViewById(R.id.textDetailStock);

        Button btnBuy = findViewById(R.id.btnComprarDetalhe);
        Button btnEdit = findViewById(R.id.btnEdit);

        // Receber o objeto Game enviado pelo Adapter
        Game game = (Game) getIntent().getSerializableExtra("EXTRA_GAME");

        if (game != null) {
            if (tvTitle != null) tvTitle.setText(game.getTitle());
            if (textPreco != null) textPreco.setText(game.getPrice() + " €");
            if (textStock != null) textStock.setText("Stock: " + game.getStock() + " unidades");

            if (imageCapa != null) {
                Glide.with(this)
                        .load(game.getImageUrl())
                        .into(imageCapa);
            }
        }

        // Observar o estado das ações do StoreController (ex: update)
        storeController.getActionState().observe(this, state -> {
            if (state.status == DataState.Status.SUCCESS) {
                // Mensagem genérica ou específica dependendo da ação
                Toast.makeText(this, "Operação realizada com sucesso!", Toast.LENGTH_SHORT).show();
            } else if (state.status == DataState.Status.ERROR) {
                Toast.makeText(this, "Erro: " + state.errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // Lógica de Compra (Cliente)
        if (btnBuy != null) {
            btnBuy.setOnClickListener(v -> {
                if (game != null) {
                    if (game.getStock() > 0) {
                        int novoStock = game.getStock() - 1;
                        game.setStock(novoStock);

                        // Atualizar visualmente de imediato
                        if (textStock != null) {
                            textStock.setText("Stock: " + novoStock + " unidades");
                        }

                        // Chamar o StoreController para atualizar no RestDB
                        storeController.updateGame(game);

                    } else {
                        Toast.makeText(this, "Produto esgotado!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        // Lógica de Edição (Gestor)
        if (btnEdit != null) {
            btnEdit.setOnClickListener(v -> {
                if (game != null) {
                    Intent intent = new Intent(GameDetailActivity.this, EditGameActivity.class);
                    intent.putExtra("EXTRA_GAME", game);
                    startActivity(intent);
                }
            });
        }

        // Lógica de Perfil (Gestor vs Cliente)
        if (SessionManager.getInstance().isManager()) {
            if(btnBuy != null) btnBuy.setVisibility(View.GONE);
            if(btnEdit != null) btnEdit.setVisibility(View.VISIBLE);
        } else {
            if(btnBuy != null) btnBuy.setVisibility(View.VISIBLE);
            if(btnEdit != null) btnEdit.setVisibility(View.GONE);
        }
    }
}
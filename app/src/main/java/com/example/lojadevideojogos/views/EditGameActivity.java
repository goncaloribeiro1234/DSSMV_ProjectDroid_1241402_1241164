package com.example.lojadevideojogos.views;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lojadevideojogos.R;
import com.example.lojadevideojogos.controllers.StoreController;
import com.example.lojadevideojogos.model.Game;
import com.example.lojadevideojogos.util.DataState;

public class EditGameActivity extends AppCompatActivity {

    private EditText editTitulo, editPreco, editStock, editImageUrl;
    private Button btnSalvar;
    private StoreController storeController;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_game);

        storeController = new StoreController();

        editTitulo = findViewById(R.id.editTitulo);
        editPreco = findViewById(R.id.editPreco);
        editStock = findViewById(R.id.editStock);
        editImageUrl = findViewById(R.id.editImageUrl);
        btnSalvar = findViewById(R.id.btnSalvarEdicao);

        // Receber o jogo enviado (seja para editar ou um novo jogo vazio)
        game = (Game) getIntent().getSerializableExtra("EXTRA_GAME");

        if (game != null) {
            editTitulo.setText(game.getTitle());
            if (game.getPrice() > 0) editPreco.setText(String.valueOf(game.getPrice()));
            if (game.getStock() > 0) editStock.setText(String.valueOf(game.getStock()));
            editImageUrl.setText(game.getImageUrl());
        }

        // Observar o resultado da operação na BD
        storeController.getActionState().observe(this, state -> {
            if (state.status == DataState.Status.SUCCESS) {
                Toast.makeText(this, "Operação realizada com sucesso!", Toast.LENGTH_SHORT).show();
                finish(); // Fecha o ecrã e regressa
            } else if (state.status == DataState.Status.ERROR) {
                Toast.makeText(this, "Erro: " + state.errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // Botão para guardar as alterações ou criar um novo jogo
        btnSalvar.setOnClickListener(v -> {
            if (game != null) {
                try {
                    game.setTitle(editTitulo.getText().toString().trim());
                    game.setPrice(Double.parseDouble(editPreco.getText().toString().trim()));
                    game.setStock(Integer.parseInt(editStock.getText().toString().trim()));
                    game.setImageUrl(editImageUrl.getText().toString().trim());

                    // Se não tiver ID válido ou for um mock, fazemos addGame (POST) em vez de update (PUT)
                    if (game.getId() == null || game.getId().isEmpty() || game.getId().startsWith("mock_")) {
                        storeController.addGame(game);
                    } else {
                        storeController.updateGame(game);
                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Verifica se os valores numéricos estão corretos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
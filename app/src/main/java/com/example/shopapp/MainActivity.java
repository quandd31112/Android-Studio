package com.example.shopapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Item> itemList;
    private ShoppingItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ShoppingItemAdapter(itemList, new ShoppingItemAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                showAddEditDialog(position);
            }

            @Override
            public void onDeleteClick(int position) {
                itemList.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });
        recyclerView.setAdapter(adapter);

        Button btnAddItem = findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(v -> showAddEditDialog(-1)); // -1 để phân biệt là thêm mới
    }

    private void showAddEditDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_item, null);
        builder.setView(dialogView);
        EditText editTextItemName = dialogView.findViewById(R.id.editTextItemName);
        EditText editTextQuantity = dialogView.findViewById(R.id.editTextQuantity);
        Button btnAdd = dialogView.findViewById(R.id.btnAdd);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        if (position != -1) {
            editTextItemName.setText(itemList.get(position).getName());
            editTextQuantity.setText(String.valueOf(itemList.get(position).getQuantity()));
            btnAdd.setText("Save");
        } else {
            btnAdd.setText("Add");
        }

        AlertDialog dialog = builder.create();

        btnAdd.setOnClickListener(v -> {
            String name = editTextItemName.getText().toString().trim();
            String quantityStr = editTextQuantity.getText().toString().trim();

            if (!name.isEmpty() && !quantityStr.isEmpty()) {
                int quantity = Integer.parseInt(quantityStr);

                if (position == -1) {
                    // Thêm
                    itemList.add(new Item(name, quantity));
                    adapter.notifyItemInserted(itemList.size() - 1);
                } else {
                    // Sửa
                    itemList.get(position).setName(name);
                    itemList.get(position).setQuantity(quantity);
                    adapter.notifyItemChanged(position);
                }
                dialog.dismiss();
            } else {
                if (name.isEmpty()) editTextItemName.setError("Nhập tên mặt hàng");
                if (quantityStr.isEmpty()) editTextQuantity.setError("Nhập số lượng");
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}

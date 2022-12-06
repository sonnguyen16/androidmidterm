package com.example.phpmysql;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

public class FurnitureAdapter extends ArrayAdapter<Furniture> {

    MainActivity context;
    int resource;

    public FurnitureAdapter(MainActivity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(resource,null);

        ImageView imageView = view.findViewById(R.id.img);
        TextView name = view.findViewById(R.id.name);
        TextView detail = view.findViewById(R.id.detail);
        TextView price = view.findViewById(R.id.price);
        ConstraintLayout layoutFront = view.findViewById(R.id.cardfront);
        ConstraintLayout layoutBehind = view.findViewById(R.id.cardbehind);
        ImageView close = view.findViewById(R.id.close);
        ImageView edit = view.findViewById(R.id.edit);
        ImageView delete = view.findViewById(R.id.delete);


        Furniture f = getItem(position);

        if(!f.getImage().equals("")){
            Picasso.get().load(f.getImage()).into(imageView);
        }
        name.setText(f.getName());
        detail.setText(f.getDetail());
        price.setText("$"+f.getPrice());

        view.setOnLongClickListener(v->{
            layoutFront.animate().translationX(-44);
            layoutBehind.animate().translationX(44);
            return false;
        });

        close.setOnClickListener(v->{
            layoutFront.animate().translationX(0);
            layoutBehind.animate().translationX(0);
        });

        edit.setOnClickListener(v->{
            layoutFront.animate().translationX(0);
            layoutBehind.animate().translationX(0);
            context.OpenDiaLog(false, f);
        });

        delete.setOnClickListener(v->{
            context.OpenDialogDelete(f.getId());
        });

        return view;
    }
}

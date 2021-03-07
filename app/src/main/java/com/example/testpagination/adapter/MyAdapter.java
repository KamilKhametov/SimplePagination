package com.example.testpagination.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testpagination.R;
import com.example.testpagination.model.ModelProduct;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    List<ModelProduct> modelList;
    Context context;

    public MyAdapter( List<ModelProduct> modelList, Context context ) {
        this.modelList=modelList;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        View view =LayoutInflater.from ( parent.getContext () ).inflate ( R.layout.item_layout, parent, false );
        ViewHolder vh = new ViewHolder ( view );
        return vh;
    }

    @Override
    public void onBindViewHolder( @NonNull ViewHolder holder, int position ) {
        ModelProduct modelProduct = modelList.get ( position );
        String image_url = modelProduct.getImage ();

        Picasso.with ( context )
                .load ( image_url )
                .into ( holder.imageProd );
        holder.titleProd.setText ( modelProduct.getName () );
    }

    @Override
    public int getItemCount() {
        return modelList.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageProd;
        TextView titleProd;

        public ViewHolder( @NonNull View itemView ) {
            super ( itemView );
            imageProd = itemView.findViewById ( R.id.imageProduct );
            titleProd= itemView.findViewById ( R.id.titleProduct );
        }
    }

    public void add(ModelProduct modelProduct){
        modelList.add ( modelProduct );
        notifyItemInserted ( modelList.size () - 1 );
    }

    public void addAll(List<ModelProduct> modelList){
        for (ModelProduct m : modelList){
            add ( m );
        }
    }

    public void addBottomItem(){
        add ( new ModelProduct () );
    }

    public void removedLastEmptyItem(){
        int position = modelList.size () - 1;
        ModelProduct item = getItem(position);

        if (item != null){
            modelList.remove ( position );
            notifyItemRemoved ( position );
        }
    }

    private ModelProduct getItem( int position ) {
        return modelList.get ( position );
    }

}

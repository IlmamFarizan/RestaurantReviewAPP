// Generated by view binder compiler. Do not edit!
package com.example.hotelreviewapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.hotelreviewapp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemRecyclerBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final ConstraintLayout baseLayout;

  @NonNull
  public final ImageView imageView;

  @NonNull
  public final TextView location;

  @NonNull
  public final TextView name;

  @NonNull
  public final ImageView popupImage;

  @NonNull
  public final RatingBar ratingBar;

  private ItemRecyclerBinding(@NonNull CardView rootView, @NonNull ConstraintLayout baseLayout,
      @NonNull ImageView imageView, @NonNull TextView location, @NonNull TextView name,
      @NonNull ImageView popupImage, @NonNull RatingBar ratingBar) {
    this.rootView = rootView;
    this.baseLayout = baseLayout;
    this.imageView = imageView;
    this.location = location;
    this.name = name;
    this.popupImage = popupImage;
    this.ratingBar = ratingBar;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemRecyclerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemRecyclerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_recycler, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemRecyclerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.base_layout;
      ConstraintLayout baseLayout = ViewBindings.findChildViewById(rootView, id);
      if (baseLayout == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageView imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.location;
      TextView location = ViewBindings.findChildViewById(rootView, id);
      if (location == null) {
        break missingId;
      }

      id = R.id.name;
      TextView name = ViewBindings.findChildViewById(rootView, id);
      if (name == null) {
        break missingId;
      }

      id = R.id.popup_image;
      ImageView popupImage = ViewBindings.findChildViewById(rootView, id);
      if (popupImage == null) {
        break missingId;
      }

      id = R.id.rating_bar;
      RatingBar ratingBar = ViewBindings.findChildViewById(rootView, id);
      if (ratingBar == null) {
        break missingId;
      }

      return new ItemRecyclerBinding((CardView) rootView, baseLayout, imageView, location, name,
          popupImage, ratingBar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}

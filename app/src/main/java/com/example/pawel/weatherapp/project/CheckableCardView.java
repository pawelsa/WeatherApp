package com.example.pawel.weatherapp.project;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;

import com.example.pawel.weatherapp.R;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class CheckableCardView
		extends CardView
		implements Checkable {
	
	private static final int[] CHECKED_STATE_SET = {
			android.R.attr.state_checked,
			};
	
	private boolean isChecked;
	
	public CheckableCardView(Context context) {
		super(context);
		init(null);
	}
	
	private void init(AttributeSet attrs) {
		
		setClickable(true);
		setChecked(false);
		
		setCardBackgroundColor(ContextCompat.getColorStateList(getContext(), R.color.selector_card_view_background));
	}
	
	public CheckableCardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}
	
	public CheckableCardView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs);
	}
	
	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
		if ( isChecked() ) {
			mergeDrawableStates(drawableState, CHECKED_STATE_SET);
		}
		return drawableState;
	}
	
	@Override
	public boolean isChecked() {
		return isChecked;
	}
	
	@Override
	public void setChecked(boolean checked) {
		this.isChecked = checked;
	}
	
	@Override
	public boolean performClick() {
		if ( ! isChecked() ) {
			toggle();
		}
		return super.performClick();
	}
	
	@Override
	public void toggle() {
		setChecked(! this.isChecked);
	}
}
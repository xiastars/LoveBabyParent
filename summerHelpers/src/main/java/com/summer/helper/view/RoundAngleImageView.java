package com.summer.helper.view;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;

import com.summer.helper.R;
import com.summer.helper.utils.SUtils;

public class RoundAngleImageView extends AppCompatImageView {
	public static final String TAG = "RoundedImageView";
	public static final float DEFAULT_RADIUS = 0f;
	public static final float DEFAULT_BORDER_WIDTH = 0f;
	private static final ScaleType[] SCALE_TYPES = { ScaleType.MATRIX,
			ScaleType.FIT_XY, ScaleType.FIT_START, ScaleType.FIT_CENTER,
			ScaleType.FIT_END, ScaleType.CENTER, ScaleType.CENTER_CROP,
			ScaleType.CENTER_INSIDE };

	private Context context;
	private float cornerRadius = DEFAULT_RADIUS;
	private float borderWidth = DEFAULT_BORDER_WIDTH;
	private ColorStateList borderColor = ColorStateList
			.valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
	private boolean isOval = false;
	private boolean mutateBackground = false;

	private int mResource;
	private Drawable mDrawable;
	private Drawable mBackgroundDrawable;
	
	private float outerCircleRadiusProgress = 0f;
    private float innerCircleRadiusProgress = 0f;

    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
	private ScaleType mScaleType = ScaleType.CENTER_CROP;
    private static final int START_COLOR = 0xFFFF5722;
    private static final int END_COLOR = 0xFFFFC107;

	/* 触摸时显示动画 */
	boolean showTouchAnim = true;

	public RoundAngleImageView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public RoundAngleImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		this.context = context;
		init();
	}

	public RoundAngleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		this.context = context;
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.RoundedImageView, defStyle, 0);

		setScaleType(ScaleType.CENTER_CROP);

		cornerRadius = a.getDimensionPixelSize(
				R.styleable.RoundedImageView_corner_radius, -1);
		borderWidth = a.getDimensionPixelSize(
				R.styleable.RoundedImageView_border_width, -1);

		// don't allow negative values for radius and border
		if (cornerRadius < 0) {
			cornerRadius = DEFAULT_RADIUS;
		}
		if (borderWidth < 0) {
			borderWidth = DEFAULT_BORDER_WIDTH;
		}

		borderColor = a
				.getColorStateList(R.styleable.RoundedImageView_border_color);
		if (borderColor == null) {
			borderColor = ColorStateList
					.valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
		}
		
		mutateBackground = a.getBoolean(
				R.styleable.RoundedImageView_mutate_background, false);
		isOval = a.getBoolean(R.styleable.RoundedImageView_oval, false);
		
		Drawable backDrawable = a.getDrawable(R.styleable.RoundedImageView_default_background);
		if(backDrawable != null){
			backDrawable = RoundedDrawable.fromDrawable(backDrawable,context);
		}else{
			backDrawable = RoundedDrawable.fromDrawable(context.getResources().getDrawable(R.drawable.trans),context);
		}
		((RoundedDrawable) backDrawable).setScaleType(mScaleType)
			.setCornerRadius(cornerRadius).setBorderWidth(borderWidth)
			.setBorderColor(borderColor).setOval(isOval);
		setBackgroundDrawable(backDrawable);
		
		updateDrawableAttrs();
		updateBackgroundDrawableAttrs(true);

		a.recycle();
		init();
	}

	private void init() {
		SUtils.clickTransColor(this);
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		invalidate();
	}

	/**
	 * Return the current scale type in use by this ImageView.
	 * 
	 * @attr ref android.R.styleable#ImageView_scaleType
	 * @see android.widget.ImageView.ScaleType
	 */
	@Override
	public ScaleType getScaleType() {
		return mScaleType;
	}

	/**
	 * Controls how the image should be resized or moved to match the size of
	 * this ImageView.
	 * 
	 * @param scaleType
	 *            The desired scaling mode.
	 * @attr ref android.R.styleable#ImageView_scaleType
	 */
	@Override
	public void setScaleType(ScaleType scaleType) {
		assert scaleType != null;

		if (mScaleType != scaleType) {
			mScaleType = scaleType;

			switch (scaleType) {
			case CENTER:
			case CENTER_CROP:
				super.setScaleType(ScaleType.CENTER_CROP);
				break;
			case CENTER_INSIDE:
			case FIT_CENTER:
			case FIT_START:
			case FIT_END:
			case FIT_XY:
				super.setScaleType(ScaleType.FIT_XY);
				break;
			default:
				super.setScaleType(ScaleType.CENTER_CROP);
				break;
			}

			updateDrawableAttrs();
			updateBackgroundDrawableAttrs(false);
			invalidate();
		}
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		mResource = 0;
		mDrawable = RoundedDrawable.fromDrawable(drawable,context);
		updateDrawableAttrs();
		super.setImageDrawable(mDrawable);
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		mResource = 0;
		mDrawable = RoundedDrawable.fromBitmap(bm,getWidth(),getHeight());
		updateDrawableAttrs();
		super.setImageDrawable(mDrawable);
	}

	@Override
	public void setImageResource(int resId) {
		if (mResource != resId) {
			mResource = resId;
			mDrawable = resolveResource();
			updateDrawableAttrs();
			super.setImageDrawable(mDrawable);
		}
	}

	@Override
	public void setImageURI(Uri uri) {
		super.setImageURI(uri);
		setImageDrawable(getDrawable());
	}

	private Drawable resolveResource() {
		Resources rsrc = getResources();
		if (rsrc == null) {
			return null;
		}

		Drawable d = null;

		if (mResource != 0) {
			try {
				d = rsrc.getDrawable(mResource);
			} catch (Exception e) {
				Log.w(TAG, "Unable to find resource: " + mResource, e);
				// Don't try again.
				mResource = 0;
			}
		}
		return RoundedDrawable.fromDrawable(d,context);
	}

	void updateDrawableAttrs() {
		updateAttrs(mDrawable);
	}

	void updateBackgroundDrawableAttrs(boolean convert) {
		if (mutateBackground) {
			if (convert) {
				mBackgroundDrawable = RoundedDrawable
						.fromDrawable(mBackgroundDrawable,context);
			}
			updateAttrs(mBackgroundDrawable);
		}
	}

	private void updateAttrs(Drawable drawable) {
		if (drawable == null) {
			return;
		}
		if (drawable instanceof RoundedDrawable) {
			((RoundedDrawable) drawable).setScaleType(mScaleType)
					.setCornerRadius(cornerRadius).setBorderWidth(borderWidth)
					.setBorderColor(borderColor).setOval(isOval);
		} else if (drawable instanceof LayerDrawable) {
			// loop through layers to and set drawable attrs
			LayerDrawable ld = ((LayerDrawable) drawable);
			for (int i = 0, layers = ld.getNumberOfLayers(); i < layers; i++) {
				updateAttrs(ld.getDrawable(i));
			}
		}
	}

	@Override
	@Deprecated
	public void setBackgroundDrawable(Drawable background) {
		mBackgroundDrawable = background;
		updateBackgroundDrawableAttrs(true);
		super.setBackgroundDrawable(mBackgroundDrawable);
	}

	public float getCornerRadius() {
		return cornerRadius;
	}

	public void setCornerRadius(int resId) {
		this.cornerRadius = resId;
	}

	public void setCornerRadius(float radius) {
		if (cornerRadius == radius) {
			return;
		}

		cornerRadius = radius;
	}

	public float getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(int resId) {
		this.borderWidth = resId;
	}
	
    public void setInnerCircleRadiusProgress(float innerCircleRadiusProgress) {
        this.innerCircleRadiusProgress = innerCircleRadiusProgress;
        postInvalidate();
    }

    public float getInnerCircleRadiusProgress() {
        return innerCircleRadiusProgress;
    }

    public void setOuterCircleRadiusProgress(float outerCircleRadiusProgress) {
        this.outerCircleRadiusProgress = outerCircleRadiusProgress;
        postInvalidate();
    }
    
	public void setBorderWidth(float width) {
		if (borderWidth == width) {
			return;
		}

		borderWidth = width;
		updateDrawableAttrs();
		updateBackgroundDrawableAttrs(false);
		invalidate();
	}

	public boolean isShowTouchAnim() {
		return showTouchAnim;
	}

	public void setShowTouchAnim(boolean showTouchAnim) {
		this.showTouchAnim = showTouchAnim;
		if(!showTouchAnim){
			this.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return false;
				}
			});
		}
	}

	public int getBorderColor() {
		return borderColor.getDefaultColor();
	}

	public void setBorderColor(int color) {
		setBorderColor(ColorStateList.valueOf(color));
	}

	public ColorStateList getBorderColors() {
		return borderColor;
	}

	public void setBorderColor(ColorStateList colors) {
		if (borderColor.equals(colors)) {
			return;
		}

		borderColor = (colors != null) ? colors : ColorStateList
				.valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
		updateDrawableAttrs();
		updateBackgroundDrawableAttrs(false);
		if (borderWidth > 0) {
			invalidate();
		}
	}

	public boolean isOval() {
		return isOval;
	}

	public void setOval(boolean oval) {
		isOval = oval;
		updateDrawableAttrs();
		updateBackgroundDrawableAttrs(false);
		invalidate();
	}

	public boolean isMutateBackground() {
		return mutateBackground;
	}

	public void setMutateBackground(boolean mutate) {
		if (mutateBackground == mutate) {
			return;
		}

		mutateBackground = mutate;
		updateBackgroundDrawableAttrs(true);
		invalidate();
	}
	
    public float getOuterCircleRadiusProgress() {
        return outerCircleRadiusProgress;
    }
	
    @SuppressLint("NewApi")
	public static final Property<RoundAngleImageView, Float> INNER_CIRCLE_RADIUS_PROGRESS =
            new Property<RoundAngleImageView, Float>(Float.class, "innerCircleRadiusProgress") {
                @Override
                public Float get(RoundAngleImageView object) {
                    return object.getInnerCircleRadiusProgress();
                }

                @Override
                public void set(RoundAngleImageView object, Float value) {
                    object.setInnerCircleRadiusProgress(value);
                }
            };

    @SuppressLint("NewApi")
	public static final Property<RoundAngleImageView, Float> OUTER_CIRCLE_RADIUS_PROGRESS =
            new Property<RoundAngleImageView, Float>(Float.class, "outerCircleRadiusProgress") {
                @Override
                public Float get(RoundAngleImageView object) {
                    return object.getOuterCircleRadiusProgress();
                }

                @Override
                public void set(RoundAngleImageView object, Float value) {
                    object.setOuterCircleRadiusProgress(value);
                }
            };
}

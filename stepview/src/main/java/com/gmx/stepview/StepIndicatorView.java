package com.gmx.stepview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

/**
 * 流程指示器
 * (步骤)
 * 1, 固定View的宽度和高度 (这里为了方便, 还需对warp_content进行处理)
 * 2, 画线
 * 3, 画icon
 * 4, 画文本
 */
public class StepIndicatorView extends View {
    private static final String TAG = "StepIndicatorView";

    private TextPaint mCompletedTextPaint;   // 定义文本 paint (流程已完成)
    private TextPaint mUnCompleteTextPaint;  // 定义文本 paint (流程未完成)
    private Paint mCompletedPaint;     // 定义paint
    private Paint mUnCompletePaint;    // 定义paint
    private List<String> mTextList;  // 流程文本列表
    private Drawable mCompletedIcon = ContextCompat.getDrawable(getContext(), R.drawable.complted);    // 流程已完成图标
    private Drawable mCompletingIcon = ContextCompat.getDrawable(getContext(), R.drawable.attention);   // 流程正在进行中图标
    private Drawable mUnCompleteIcon = ContextCompat.getDrawable(getContext(), R.drawable.default_icon);   // 流程未完成图标
    private int mCompletedPostion;     // 当前流程的位置
    private int mStepNum;               // 流程总数
    @ColorInt
    private int mStepCompletedLineColor = ContextCompat.getColor(getContext(), android.R.color.white);   // 线颜色 (流程已完成)
    @ColorInt
    private int mStepUnCompleteLineColor = ContextCompat.getColor(getContext(), R.color.uncompleted_color);  // 线颜色 (流程未完成)
    @ColorInt
    private int mStepCompeletedTextColor = ContextCompat.getColor(getContext(), android.R.color.white);   // 文本颜色 (流程已完成)
    @ColorInt
    private int mStepUnCompleteTextColor = ContextCompat.getColor(getContext(), R.color.uncompleted_text_color); // 文本颜色 (流程未完成)

    @ColorInt
    private int mStepCompletedTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());   // 文本颜色 (流程已完成)
    @ColorInt
    private int mStepUnCompleteTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()); // 文本颜色 (流程未完成)


    private int mCompletedLineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());  // 定义10dip线高度(流程已完成)
    private int mUnCompletedLineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());  // 定义8dip线高度(流程未完成)


    private int mLineLength = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());  // 定义10dip线高度(流程已完成)
    private int mLinePadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mCompletedIcon.getMinimumWidth(), getResources().getDisplayMetrics());  // 线与线之间距离40dp

    private int mTextMarginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());  // 文本向上外边距


    private Path mPath = new Path();
    private DashPathEffect mEffect = new DashPathEffect(new float[]{6, 6, 6, 6}, 1);


    private Point mLeftCenterPoint;  // 左边中心点坐标
    private int mBackgroundColor = Color.parseColor("#00BEAF");
    private boolean mWidthWrapContent = false;
    private boolean mHeightWrapContent = false;
    private int mOrientation = LinearLayoutCompat.HORIZONTAL; // 默认水平方向

    public StepIndicatorView(Context context) {
        this(context, null);
    }

    public StepIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepIndicatorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        loadAttributes(attrs, defStyle);
    }

    private void loadAttributes(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.stepIndicatorView, defStyle, 0);

        // 设置文本(大小, 颜色)
        setTextConifg(a);

        // 设置线(长度, 颜色, 高度)
        setLineConfig(a);

        // 设置ICON
        setIconConfig(a);

        //设置数据来源
        setDataConfig(a);

        a.recycle();

        init();
    }

    /**
     * 设置数据来源
     *
     * @param a
     */
    private void setDataConfig(TypedArray a) {

        if (a.hasValue(R.styleable.stepIndicatorView_stepIndicatorViewTextList)) {
            CharSequence[] textArray = a.getTextArray(R.styleable.stepIndicatorView_stepIndicatorViewTextList);
            String[] mEntriesString = new String[textArray.length];
            int i = 0;
            for (CharSequence ch : textArray) {
                mEntriesString[i++] = ch.toString();
            }
            mTextList = Arrays.asList(mEntriesString);
            mStepNum = mTextList.size();
        }

        if (a.hasValue(R.styleable.stepIndicatorView_stepIndicatorViewCompletedPosition)) {
            mCompletedPostion = a.getInt(R.styleable.stepIndicatorView_stepIndicatorViewCompletedPosition, 0);
        }


        if (a.hasValue(R.styleable.stepIndicatorView_stepIndicatorViewBackgroundColor)) {
            mBackgroundColor = a.getColor(R.styleable.stepIndicatorView_stepIndicatorViewBackgroundColor, 0);
        }

        if(a.hasValue(R.styleable.stepIndicatorView_stepIndicatorViewOrientation)) {
            mOrientation = a.getInt(R.styleable.stepIndicatorView_stepIndicatorViewOrientation, LinearLayoutCompat.HORIZONTAL);
        }

    }

    /**
     * 设置Icon()
     *
     * @param a
     */
    private void setIconConfig(TypedArray a) {

        if (a.hasValue(R.styleable.stepIndicatorView_stepIndicatorViewCompletedIcon)) {
            mCompletedIcon = a.getDrawable(R.styleable.stepIndicatorView_stepIndicatorViewCompletedIcon);
        }

        if (a.hasValue(R.styleable.stepIndicatorView_stepIndicatorViewUnCompleteIcon)) {
            mCompletingIcon = a.getDrawable(R.styleable.stepIndicatorView_stepIndicatorViewUnCompleteIcon);
        }

        if (a.hasValue(R.styleable.stepIndicatorView_stepIndicatorViewDefaultIcon)) {
            mUnCompleteIcon = a.getDrawable(R.styleable.stepIndicatorView_stepIndicatorViewDefaultIcon);
        }

    }

    /**
     * 设置线(长度, 颜色, 高度)
     *
     * @param a
     */
    private void setLineConfig(TypedArray a) {

        // 线颜色
        if (a.hasValue(R.styleable.stepIndicatorView_stepIndicatorViewLineColor)) {
            int color = a.getColor(R.styleable.stepIndicatorView_stepIndicatorViewLineColor, 0);
            mStepCompletedLineColor = color;
            mStepUnCompleteLineColor = color;
        } else {
            if (a.hasValue(R.styleable.stepIndicatorView_stepIndicatorViewCompletedLineColor)) {
                mStepCompletedLineColor = a.getColor(R.styleable.stepIndicatorView_stepIndicatorViewCompletedLineColor, 0);
            }

            if (a.hasValue(R.styleable.stepIndicatorView_stepIndicatorViewUnCompleteLineColor)) {
                mStepUnCompleteLineColor = a.getColor(R.styleable.stepIndicatorView_stepIndicatorViewUnCompleteLineColor, 0);
            }
        }


        // 线高度
        if (a.hasValue(R.styleable.stepIndicatorView_stepIndicatorViewLineHeight)) {
            int height = a.getInt(R.styleable.stepIndicatorView_stepIndicatorViewLineHeight, 0);
            mCompletedLineHeight = height;
            mUnCompletedLineHeight = height;
        } else {
            if (a.hasValue(R.styleable.stepIndicatorView_stepIndicatorViewCompletedLineHeight)) {
                mCompletedPaint.setStrokeWidth(a.getInt(R.styleable.stepIndicatorView_stepIndicatorViewCompletedLineHeight, 0));
            }

            if (a.hasValue(R.styleable.stepIndicatorView_stepIndicatorViewUnCompleteLineHeight)) {
                mUnCompletePaint.setStrokeWidth(a.getInt(R.styleable.stepIndicatorView_stepIndicatorViewUnCompleteLineHeight, 0));
            }

        }

        // 线长度
        if (a.hasValue(R.styleable.stepIndicatorView_stepIndicatorViewLineLength)) {
            mLineLength = a.getInt(R.styleable.stepIndicatorView_stepIndicatorViewLineLength, 0);
        }

    }

    /**
     * 设置文本(大小, 颜色, 上边距)
     *
     * @param a
     */
    private void setTextConifg(TypedArray a) {

        // 文本大小
        if (a.hasValue(R.styleable.stepIndicatorView_stepIndicatorViewTextSize)) {
            int textSize = a.getInt(R.styleable.stepIndicatorView_stepIndicatorViewTextSize, 0);
            mStepCompletedTextSize = textSize;
            mStepUnCompleteTextSize = textSize;
        } else {
            if (a.hasValue(R.styleable.stepIndicatorView_stepIndicatorViewCompletedTextSize)) {
                mStepCompletedTextSize = a.getInt(R.styleable.stepIndicatorView_stepIndicatorViewCompletedTextSize, 0);
            }

            if (a.hasValue(R.styleable.stepIndicatorView_stepIndicatorViewUnCompleteTextSize)) {
                mStepUnCompleteTextSize = a.getInt(R.styleable.stepIndicatorView_stepIndicatorViewUnCompleteTextSize, 0);
            }
        }


        // 文本颜色
        if (a.hasValue(R.styleable.stepIndicatorView_stepIndicatorViewTextColor)) {
            int color = a.getColor(R.styleable.stepIndicatorView_stepIndicatorViewTextColor, 0);
            mStepCompeletedTextColor = color;
            mStepUnCompleteTextColor = color;
        } else {

            if (a.hasValue(R.styleable.stepIndicatorView_stepIndicatorViewCompletedTextColor)) {
                mStepCompeletedTextColor = a.getColor(R.styleable.stepIndicatorView_stepIndicatorViewCompletedTextColor, 0);
            }

            if (a.hasValue(R.styleable.stepIndicatorView_stepIndicatorViewUnCompleteTextColor)) {
                mStepUnCompleteTextColor = a.getColor(R.styleable.stepIndicatorView_stepIndicatorViewUnCompleteTextColor, 0);
            }
        }


        // 文本上边距
        if (a.hasValue(R.styleable.stepIndicatorView_stepIndicatorViewTextMaiginTop)) {
            mTextMarginTop = a.getInt(R.styleable.stepIndicatorView_stepIndicatorViewTextMaiginTop, 0);
        }
    }


    private void init() {
        // 初始化画线所需工具
        mCompletedPaint = new Paint();
        mCompletedPaint.setAntiAlias(true);
        mCompletedPaint.setColor(mStepCompletedLineColor);
        mCompletedPaint.setStyle(Paint.Style.STROKE);
        mCompletedPaint.setStrokeWidth(mCompletedLineHeight);


        mUnCompletePaint = new Paint();
        mUnCompletePaint.setAntiAlias(true);
        mUnCompletePaint.setStyle(Paint.Style.STROKE);
        mUnCompletePaint.setColor(mStepUnCompleteLineColor);
        mUnCompletePaint.setStrokeWidth(mUnCompletedLineHeight);
        mUnCompletePaint.setPathEffect(mEffect);


        mCompletedTextPaint = new TextPaint();
        mCompletedTextPaint.setColor(mStepCompeletedTextColor);
        mCompletedTextPaint.setTextSize(mStepCompletedTextSize);
        mCompletedTextPaint.setTypeface(Typeface.DEFAULT_BOLD);


        mUnCompleteTextPaint = new TextPaint();
        mUnCompleteTextPaint.setColor(mStepUnCompleteTextColor);
        mUnCompleteTextPaint.setTextSize(mStepUnCompleteTextSize);


        setBackgroundColor(mBackgroundColor);
    }


    private int getUseSpaceWidth() {
        return mLinePadding * mStepNum + mLineLength * (mStepNum - 1);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // TODO: 16/6/29 wrap_content 处理
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        // 水平方向
        if(LinearLayoutCompat.HORIZONTAL == mOrientation) {
            if (MeasureSpec.AT_MOST == MeasureSpec.getMode(widthMeasureSpec)) {
                mWidthWrapContent = true;
                width = getUseSpaceWidth() + getPaddingLeft() + getPaddingRight() + getTextPreAndEndLength();
            }

            if (MeasureSpec.AT_MOST == MeasureSpec.getMode(heightMeasureSpec)) {
                mHeightWrapContent = true;
                height = getPaddingTop() + getPaddingBottom() + mLinePadding + mTextMarginTop + getTextHeight(mTextList.get(0)) + 5;
            }
        }
        // 垂直方向
        else {


        }

        setMeasuredDimension(width, height);
    }

    /**
     * 获取文本前后暂用空间
     *
     * @return
     */
    private int getTextPreAndEndLength() {
        int width = 0;

        // 只有一个流程
        if (mStepNum == 1) {
            if (1 <= mCompletedPostion) {
                int textWidth = (int) mCompletedTextPaint.measureText(mTextList.get(0));
                if (textWidth > mLinePadding) {
                    // 将多余宽度加上
                    width += textWidth - mLinePadding;
                }
            } else {
                int textWidth = (int) mUnCompleteTextPaint.measureText(mTextList.get(0));
                if (textWidth > mLinePadding) {
                    // 将多余宽度加上
                    width += textWidth - mLinePadding;
                }
            }
        }

        // 两个流程以上
        if (mStepNum > 1) {

            if (1 <= mCompletedPostion) {
                int textWidth = (int) mCompletedTextPaint.measureText(mTextList.get(0));
                if (textWidth > mLinePadding) {
                    // 将多余宽度加上
                    width += textWidth / 2 - mLinePadding / 2;
                }
            } else {
                int textWidth = (int) mUnCompleteTextPaint.measureText(mTextList.get(0));
                if (textWidth > mLinePadding) {
                    // 将多余宽度加上
                    width += textWidth / 2 - mLinePadding / 2;
                }
            }


            if (mStepNum - 1 <= mCompletedPostion) {
                int textWidth = (int) mCompletedTextPaint.measureText(mTextList.get(mStepNum - 1));
                if (textWidth > mLinePadding) {
                    // 将多余宽度加上
                    width += textWidth / 2 - mLinePadding / 2;
                }
            } else {
                int textWidth = (int) mUnCompleteTextPaint.measureText(mTextList.get(mStepNum - 1));
                if (textWidth > mLinePadding) {
                    // 将多余宽度加上
                    width += textWidth / 2 - mLinePadding / 2;
                }
            }
        }

        Log.e(TAG, "getTextPreAndEndLength: " + width);
        return width;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        checkInitData();
        obtainLeftStartPoint();
    }


    private void checkInitData() {
        if (mTextList == null) {
            Toast.makeText(getContext(), "请调用 setStepIndicatorViewTextList() 进行初始化!!", Toast.LENGTH_LONG).show();
            throw new RuntimeException("请调用 setStepIndicatorViewTextList() 进行初始化!!");
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw line
        drawLine(canvas);

        // draw icon
        drawIcon(canvas);

        // draw text
        drawText(canvas);
    }


    /**
     * 画线
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {

        int preX = 0;
        int lastX = mLeftCenterPoint.x + getOneTextOneHalfWidth();

        int lineY = getPaddingTop() + mLinePadding / 2;

        // 这里-1, 列表 3个圆只画2条线
        for (int i = 0; i < mStepNum - 1; i++) {

            preX = lastX + mLinePadding;
            lastX = preX + mLineLength;

//            if (i == 0) {
//                // 获取第一个文本多余的长度
//                int width = getOneTextOneHalfWidth();
//                preX += width;
//                lastX = preX + mLineLength;
//            }


            // 已经完成的 画实线
            if (i < mCompletedPostion) {
                canvas.drawLine(preX, lineY, lastX, lineY, mCompletedPaint);
            }
            // 未完成的
            else {
                mPath.moveTo(preX, lineY);
                mPath.lineTo(lastX, lineY);
                canvas.drawPath(mPath, mUnCompletePaint);
            }
        }

    }

    /**
     * 获取第一个文本多余的长度
     */
    private int getOneTextOneHalfWidth() {
        int width = 0;
        String text = mTextList.get(0);
        if (mCompletedPostion >= 1) {
            int textWidth = (int) mCompletedTextPaint.measureText(text);
            if (textWidth > mLinePadding) {
                // 将多余宽度加上
                width = textWidth / 2 - mLinePadding / 2;
            }
        } else {
            int textWidth = (int) mUnCompleteTextPaint.measureText(text);
            if (textWidth > mLinePadding) {
                // 将多余宽度加上
                width = textWidth / 2 - mLinePadding / 2;
            }
        }
        Log.e(TAG, "第一个文本 - 第一个icon =  " + width + "px");
        return width;
    }


    /**
     * 画icon
     *
     * @param canvas
     */
    private void drawIcon(Canvas canvas) {
        Rect rect = null;

        int top_y = mLeftCenterPoint.y;
        int bottom_y = mLeftCenterPoint.y + mLinePadding;

        int top_x = mLeftCenterPoint.x + getOneTextOneHalfWidth();
        int bottom_x = mLeftCenterPoint.x + mLinePadding + getOneTextOneHalfWidth();

        for (int i = 0; i < mStepNum; i++) {

            if (i != 0) {
                top_x = top_x + mLinePadding + mLineLength;
                bottom_x = bottom_x + mLinePadding + mLineLength;
            }

            // 已经完成的
            if (i < mCompletedPostion) {
                rect = new Rect(top_x, top_y, bottom_x, bottom_y);
                mCompletedIcon.setBounds(rect);
                mCompletedIcon.draw(canvas);
            }
            // 正在进行
            else if (i == mCompletedPostion) {
                rect = new Rect(top_x, top_y, bottom_x, bottom_y);
                mCompletingIcon.setBounds(rect);
                mCompletingIcon.draw(canvas);
            }
            // 未完成的(默认)
            else {
                rect = new Rect(top_x, top_y, bottom_x, bottom_y);
                mUnCompleteIcon.setBounds(rect);
                mUnCompleteIcon.draw(canvas);
            }

        }

    }

    /**
     * 画文本
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {

        int textY = 0;
        int textX = 0;
        int centerPositionX = mLeftCenterPoint.x + mLinePadding / 2 + getOneTextOneHalfWidth();

        for (int i = 0; i < mStepNum; i++) {

            if (i != 0) {
                centerPositionX += mLinePadding + mLineLength;
            }

            String text = mTextList.get(i);
            int textWidth = getTextWidth(text);
            int textHeight = getTextHeight(text);

            textY = textHeight + mTextMarginTop + mLinePadding + getPaddingTop();
            textX = centerPositionX - textWidth / 2;

            // 已经完成的
            if (i <= mCompletedPostion) {
                canvas.drawText(text, textX, textY, mCompletedTextPaint);
            }
            // 未完成的(默认)
            else {
                canvas.drawText(text, textX, textY, mUnCompleteTextPaint);
            }

        }
    }

    private int getTextHeight(String text) {
        Rect rect = new Rect();
        mCompletedTextPaint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    private int getTextWidth(String text) {
        Rect rect = new Rect();
        mUnCompleteTextPaint.getTextBounds(text, 0, text.length(), rect);
        return rect.width();
    }


    /**
     * 获取左边中心点坐标
     */
    private void obtainLeftStartPoint() {
        int useSpaceSize = getUseSpaceWidth();
        int x = getPaddingLeft() + ((getWidth() - getPaddingLeft() - getPaddingRight() - useSpaceSize) / 2);
        int y = getPaddingTop();

        if(mWidthWrapContent) {
            x = 0;
        }

        mLeftCenterPoint = new Point(x, y);
        Log.e(TAG, "left start point.x = " + x + ", point.y = " + y);
    }


//    =======================================文本设置================================================

    /**
     * 设置流程文本字体大小(默认: 14sp)
     *
     * @param textSize
     * @return
     */
    public StepIndicatorView setStepIndicatorViewTextSize(int textSize) {
        mStepCompletedTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, getResources().getDisplayMetrics());
        mStepUnCompleteTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, getResources().getDisplayMetrics());

        mCompletedTextPaint.setTextSize(mStepCompletedTextSize);
        mUnCompleteTextPaint.setTextSize(mStepUnCompleteTextSize);
        return this;
    }

    /**
     * 设置流程完成文本字体大小(默认: 14sp)
     *
     * @param textSize
     * @return
     */
    public StepIndicatorView setStepIndicatorViewCompletedTextSize(int textSize) {
        mStepCompletedTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, getResources().getDisplayMetrics());
        mCompletedTextPaint.setTextSize(mStepCompletedTextSize);
        return this;
    }

    /**
     * 设置流程未完成文本字体大小(默认: 14sp)
     *
     * @param textSize
     * @return
     */
    public StepIndicatorView setStepIndicatorViewUnCompleteTextSize(int textSize) {
        mStepUnCompleteTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, getResources().getDisplayMetrics());
        mUnCompleteTextPaint.setTextSize(mStepUnCompleteTextSize);
        return this;
    }

    /**
     * 设置流程文本颜色(默认: 白色)
     *
     * @param color
     * @return
     */
    public StepIndicatorView setStepIndicatorViewTextColor(@ColorInt int color) {
        mStepCompeletedTextColor = color;
        mStepUnCompleteTextColor = color;

        mCompletedTextPaint.setColor(mStepCompeletedTextColor);
        mUnCompleteTextPaint.setColor(mStepUnCompleteTextColor);
        return this;
    }

    /**
     * 设置流程完成文本颜色(默认: 白色)
     *
     * @param color
     * @return
     */
    public StepIndicatorView setStepIndicatorViewCompletedTextColor(@ColorInt int color) {
        mStepCompeletedTextColor = color;

        mCompletedTextPaint.setColor(mStepCompeletedTextColor);
        return this;
    }

    /**
     * 设置流程未完成文本颜色(默认: 白色)
     *
     * @param color
     * @return
     */
    public StepIndicatorView setStepIndicatorViewUnCompleteTextColor(@ColorInt int color) {
        mStepUnCompleteTextColor = color;

        mUnCompleteTextPaint.setColor(mStepUnCompleteTextColor);
        return this;
    }

    /**
     * 设置文本外边距(默认: 5dp)
     *
     * @param textMarginTop
     * @return
     */
    public StepIndicatorView setStepIndicatorViewTextMaiginTop(int textMarginTop) {
        mTextMarginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, textMarginTop, getResources().getDisplayMetrics());
        return this;
    }


//    =======================================线设置=================================================

    /**
     * 设置流程完成线的颜色(默认: 白色)
     *
     * @param color
     * @return
     */
    public StepIndicatorView setStepIndicatorViewLineColor(@ColorInt int color) {
        mStepCompletedLineColor = color;
        mStepUnCompleteLineColor = color;

        mCompletedPaint.setColor(mStepCompletedLineColor);
        mUnCompletePaint.setColor(mStepUnCompleteLineColor);
        return this;
    }

    /**
     * 设置流程完成线的颜色(默认: 白色)
     *
     * @param color
     * @return
     */
    public StepIndicatorView setStepIndicatorViewCompletedLineColor(@ColorInt int color) {
        mStepCompletedLineColor = color;

        mCompletedPaint.setColor(mStepCompletedLineColor);
        return this;
    }

    /**
     * 设置流程未完成线的颜色(默认: #F8F8F8)
     *
     * @param color
     * @return
     */
    public StepIndicatorView setStepIndicatorViewUnCompleteLineColor(@ColorInt int color) {
        mStepUnCompleteLineColor = color;

        mUnCompletePaint.setColor(mStepUnCompleteLineColor);
        return this;
    }

    /**
     * 设置流程完成线高度(默认: 2dp)
     *
     * @param lenght
     * @return
     */
    public StepIndicatorView setStepIndicatorViewLineHeight(int lenght) {
        mCompletedLineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, lenght, getResources().getDisplayMetrics());
        mUnCompletedLineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, lenght, getResources().getDisplayMetrics());

        mCompletedPaint.setStrokeWidth(mCompletedLineHeight);
        mUnCompletePaint.setStrokeWidth(mUnCompletedLineHeight);
        return this;
    }


    /**
     * 设置流程完成线高度(默认: 2dp)
     *
     * @param lenght
     * @return
     */
    public StepIndicatorView setStepIndicatorViewCompletedLineHeight(int lenght) {
        mCompletedLineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, lenght, getResources().getDisplayMetrics());

        mCompletedPaint.setStrokeWidth(mCompletedLineHeight);
        return this;
    }

    /**
     * 设置未完成线的高度(默认: 1dp)
     *
     * @param lenght
     * @return
     */
    public StepIndicatorView setStepIndicatorViewUnCompleteLineHeight(int lenght) {
        mUnCompletedLineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, lenght, getResources().getDisplayMetrics());

        mUnCompletePaint.setStrokeWidth(mUnCompletedLineHeight);
        return this;
    }

    /**
     * 设置流程线的长度
     *
     * @param lenght
     * @return
     */
    public StepIndicatorView setStepIndicatorViewLineLength(int lenght) {
        mLineLength = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, lenght, getResources().getDisplayMetrics());
        return this;
    }


//    =======================================ICON设置===============================================

    /**
     * 设置已经完成ICON
     *
     * @param drawable
     * @return
     */
    public StepIndicatorView setStepIndicatorViewCompletedIcon(Drawable drawable) {
        mCompletedIcon = drawable;
        return this;
    }

    /**
     * 设置未完成ICON
     *
     * @param drawable
     * @return
     */
    public StepIndicatorView setStepIndicatorViewUnCompleteIcon(Drawable drawable) {
        mCompletingIcon = drawable;
        return this;
    }

    /**
     * 设置默认ICON
     *
     * @param drawable
     * @return
     */
    public StepIndicatorView setStepIndicatorViewDefaultIcon(Drawable drawable) {
        mUnCompleteIcon = drawable;
        return this;
    }


//    =======================================需要初始化==============================================

    /**
     * 设置流程文本
     *
     * @param textList
     */
    public StepIndicatorView initStepIndicatorViewTextList(@NonNull List<String> textList) {
        mTextList = textList;
        mStepNum = mTextList.size();
        return this;
    }

    /**
     * 设置已经完成的流程position
     *
     * @param completedStepNum
     * @return
     */
    public StepIndicatorView initStepIndicatorViewCompletedPosition(int completedStepNum) {
        mCompletedPostion = completedStepNum;
        return this;
    }


//    =======================================具体操作==============================================

    /**
     * 设置流程文本
     *
     * @param textList
     */
    public void setStepIndicatorViewTextList(List<String> textList) {
        initStepIndicatorViewTextList(textList);
        invalidate();
    }


    /**
     * 设置正在进行的流程position
     *
     * @param completedStepNum
     * @return
     */
    public void setStepIndicatorViewCompletedPosition(int completedStepNum) {
        initStepIndicatorViewCompletedPosition(completedStepNum);
        invalidate();
    }

    /**
     * 设置背景颜色
     *
     * @param color
     */
    public void setStepIndicatorViewBackgroundColor(@ColorInt int color) {
        mBackgroundColor = color;
    }

}

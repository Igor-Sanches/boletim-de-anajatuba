package com.igordutrasanches.anajatubaboletim.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ImageUtils {
    public static final int AVATAR_HEIGHT = 128;
    public static final int AVATAR_WIDTH = 128;

    public static Bitmap roundedImage(Context context, Bitmap bitmap) {
        RoundedBitmapDrawable create = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        create.setCornerRadius(((float) Math.max(bitmap.getWidth(), bitmap.getHeight())) / 2.0f);
        return create.getBitmap();
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int i) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        float max = ((float) Math.max(bitmap.getWidth(), bitmap.getHeight())) / 2.0f;
        paint.setAntiAlias(true);
        canvas.drawRoundRect(rectF, max, max, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return createBitmap;
    }

    public static Bitmap cropToSquare(Bitmap bitmap) {
        if (bitmap.getWidth() >= bitmap.getHeight()) {
            return Bitmap.createBitmap(bitmap, (bitmap.getWidth() / 2) - (bitmap.getHeight() / 2), 0, bitmap.getHeight(), bitmap.getHeight());
        }
        return Bitmap.createBitmap(bitmap, 0, (bitmap.getHeight() / 2) - (bitmap.getWidth() / 2), bitmap.getWidth(), bitmap.getWidth());
    }

    public static String encodeBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
    }

    public static String encodeBase64(Context context, int i) {
        Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), i);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        decodeResource.compress(CompressFormat.PNG, 100, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
    }

    public static Bitmap makeImageLite(InputStream inputStream, int i, int i2, int i3, int i4) {
        int i5 = 1;
        if (i2 > i4 || i > i3) {
            int i6 = i2 / 2;
            int i7 = i / 2;
            while (i6 / i5 >= i4 && i7 / i5 >= i3) {
                i5 *= 2;
            }
        }
        Options options = new Options();
        options.inSampleSize = i5;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(inputStream, null, options);
    }

    public static InputStream convertBitmapToInputStream(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 0, byteArrayOutputStream);
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    public static Bitmap getBitmap(Context context, int i) {
        Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, i);
        if (VERSION.SDK_INT < 21) {
            drawable = DrawableCompat.wrap(drawable).mutate();
        }
        Bitmap createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return createBitmap;
    }

    public static void displayRoundImageFromUrl(final Context context, String str, final ImageView imageView) {
        if (context != null) {
            Glide.with(context).asBitmap().apply(new RequestOptions().centerCrop().dontAnimate()).load(str).into(new BitmapImageViewTarget(imageView) {
                /* access modifiers changed from: protected */
                public void setResource(Bitmap bitmap) {
                    RoundedBitmapDrawable create = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
                    create.setCircular(true);
                    imageView.setImageDrawable(create);
                }
            });
        }
    }

    public static void displayImageFromUrl(Context context, String str, ImageView imageView, Drawable drawable) {
        displayImageFromUrl(context, str, imageView, drawable, null);
    }

    public static void displayImageFromUrl(Context context, String str, ImageView imageView, Drawable drawable, RequestListener requestListener) {
        RequestOptions placeholder = new RequestOptions().dontAnimate().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(drawable);
        if (requestListener != null) {
            Glide.with(context).load(str).apply(placeholder).listener(requestListener).into(imageView);
        } else {
            Glide.with(context).load(str).apply(placeholder).listener(requestListener).into(imageView);
        }
    }

    public static void displayRoundImageFromUrlWithoutCache(Context context, String str, ImageView imageView) {
        displayRoundImageFromUrlWithoutCache(context, str, imageView, null);
    }

    public static void displayRoundImageFromUrlWithoutCache(final Context context, String str, final ImageView imageView, RequestListener requestListener) {
        RequestOptions skipMemoryCache = new RequestOptions().centerCrop().dontAnimate().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true);
        if (requestListener != null) {
            Glide.with(context).asBitmap().load(str).apply(skipMemoryCache).listener(requestListener).into(new BitmapImageViewTarget(imageView) {
                /* access modifiers changed from: protected */
                public void setResource(Bitmap bitmap) {
                    RoundedBitmapDrawable create = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
                    create.setCircular(true);
                    imageView.setImageDrawable(create);
                }
            });
        } else {
            Glide.with(context).asBitmap().load(str).apply(skipMemoryCache).into(new BitmapImageViewTarget(imageView) {
                /* access modifiers changed from: protected */
                public void setResource(Bitmap bitmap) {
                    RoundedBitmapDrawable create = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
                    create.setCircular(true);
                    imageView.setImageDrawable(create);
                }
            });
        }
    }

    public static void displayImageFromUrlWithPlaceHolder(Context context, String str, ImageView imageView, int i) {
        Glide.with(context).load(str).apply(new RequestOptions().dontAnimate().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(i)).into(imageView);
    }

    public static void displayGifImageFromUrl(Context context, String str, ImageView imageView, Drawable drawable, RequestListener requestListener) {
        RequestOptions placeholder = new RequestOptions().dontAnimate().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).dontAnimate().placeholder(drawable);
        if (requestListener != null) {
            Glide.with(context).asGif().load(str).apply(placeholder).listener(requestListener).into(imageView);
        } else {
            Glide.with(context).asGif().load(str).apply(placeholder).into(imageView);
        }
    }

    public static void displayGifImageFromUrl(Context context, String str, ImageView imageView, String str2, Drawable drawable) {
        RequestOptions placeholder = new RequestOptions().dontAnimate().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).dontAnimate().placeholder(drawable);
        if (str2 != null) {
            Glide.with(context).asGif().load(str).apply(placeholder).thumbnail(Glide.with(context).asGif().load(str2)).into(imageView);
        } else {
            Glide.with(context).asGif().load(str).apply(placeholder).into(imageView);
        }
    }

    public static String resizeAndCompressImageBeforeSend(Context context, String str, String str2) {
        int length;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        options.inSampleSize = calculateInSampleSize(options, 800, 800);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Config.ARGB_8888;
        Bitmap decodeFile = BitmapFactory.decodeFile(str, options);
        int i = 100;
        do {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            StringBuilder sb = new StringBuilder();
            sb.append("Quality: ");
            sb.append(i);
            Log.d("compressBitmap", sb.toString());
            decodeFile.compress(CompressFormat.JPEG, i, byteArrayOutputStream);
            length = byteArrayOutputStream.toByteArray().length;
            i -= 5;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Size: ");
            sb2.append(length / 1024);
            sb2.append(" kb");
            Log.d("compressBitmap", sb2.toString());
        } while (length >= 716800);
        String str3 = "compressBitmap";
        try {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("cacheDir: ");
            sb3.append(context.getCacheDir());
            Log.d(str3, sb3.toString());
            StringBuilder sb4 = new StringBuilder();
            sb4.append(context.getCacheDir());
            sb4.append(str2);
            FileOutputStream fileOutputStream = new FileOutputStream(sb4.toString());
            decodeFile.compress(CompressFormat.JPEG, i, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception unused) {
            Log.e("compressBitmap", "Error on saving file");
        }
        StringBuilder sb5 = new StringBuilder();
        sb5.append(context.getCacheDir());
        sb5.append(str2);
        return sb5.toString();
    }

    public static int calculateInSampleSize(Options options, int i, int i2) {
        String str = "MemoryInformation";
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        StringBuilder sb = new StringBuilder();
        sb.append("image height: ");
        sb.append(i3);
        sb.append("---image width: ");
        sb.append(i4);
        Log.d(str, sb.toString());
        int i5 = 1;
        if (i3 > i2 || i4 > i) {
            int i6 = i3 / 2;
            int i7 = i4 / 2;
            while (i6 / i5 > i2 && i7 / i5 > i) {
                i5 *= 2;
            }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("inSampleSize: ");
        sb2.append(i5);
        Log.d(str, sb2.toString());
        return i5;
    }
}

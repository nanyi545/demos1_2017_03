package com.webcon.wp.utils;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

import com.webcon.sus.demo.R;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class ExperssionUtil {
	/**
	 * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
	 * 
	 * @param context
	 * @param spannableString
	 * @param patten
	 * @param start
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws NumberFormatException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void dealExpression(Context context,
			SpannableString spannableString, Pattern patten, int start)
			throws SecurityException, NoSuchFieldException,
			NumberFormatException, IllegalArgumentException,
			IllegalAccessException {
		//获得匹配器开始匹配字符串spannableString
		Matcher matcher = patten.matcher(spannableString);
		while (matcher.find()) {//尝试在目标字符串里查找下一个匹配字符串
			String key = matcher.group();//返回当前查找而获得的与组匹配的所有子串内容
			if (matcher.start() < start) {
				continue;
			}
			Field field = R.drawable.class.getDeclaredField(key);
			// 通过上面匹配得到的字符串来生成图片资源id
			int resId = Integer.parseInt(field.get(null).toString()); 
			if (resId != 0) {
				AnimationDrawable mFace = new AnimationDrawable();
				// byte mFrame = 0;
				final GifOpenHelper gHelper = new GifOpenHelper();
				gHelper.read(context.getResources().openRawResource(resId));
				BitmapDrawable bd = new BitmapDrawable(gHelper.getImage());
				mFace.addFrame(bd, gHelper.getDelay(0));
				for (int i = 1; i < gHelper.getFrameCount(); i++) {
					mFace.addFrame(new BitmapDrawable(gHelper.nextBitmap()),
							gHelper.getDelay(i));
				}
				mFace.setBounds(0, 0, bd.getIntrinsicWidth() * 2,
						bd.getIntrinsicHeight() * 2);
				//mFace.setOneShot(false);
				mFace.selectDrawable(1);// 设置停在第1帧
				ImageSpan span = new ImageSpan(mFace, ImageSpan.ALIGN_BASELINE);
				// 计算该图片名字的长度，也就是要替换的字符串的长度
				int end = matcher.start() + key.length(); 
				// 用该图片替换字符串中规定的位置
				spannableString.setSpan(span, matcher.start(), end,
						Spannable.SPAN_INCLUSIVE_EXCLUSIVE); 
				if (end < spannableString.length()) { // 如果整个字符串还未验证完，则继续。。
					dealExpression(context, spannableString, patten, end);
				}
				break;
			}
		}
	}

	/**
	 * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
	 * 
	 * @param context
	 * @param str
	 * @return
	 */
	public static SpannableString getExpressionString(Context context,
			String str, String zhengze) {
		SpannableString spannableString = new SpannableString(str);
		Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE); // 通过传入的正则表达式来生成一个pattern
		try {
			dealExpression(context, spannableString, sinaPatten, 0);
		} catch (Exception e) {
			Log.e("dealExpression", e.getMessage());
		}
		return spannableString;
	}

}

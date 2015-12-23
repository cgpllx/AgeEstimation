package com.konka.ageestimation.ui.presenter;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.konka.ageestimation.KonkaApplication;
import com.konka.ageestimation.R;
import com.konka.ageestimation.ui.activity.MainActivity;
import com.konka.ageestimation.ui.pojo.Face;
import com.konka.ageestimation.ui.util.AppUtil;
import com.konka.ageestimation.ui.util.BitmapUtil;
import com.konka.ageestimation.ui.util.FileUtil;
import com.konka.ageestimation.ui.util.Files;
import com.konka.ageestimation.ui.view.IPhotoView;
import com.konka.project.KonkaSo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AnalysePresenterCompl implements IAnalysePresenter {
	private static final String TAG = "AnalysePresenterCompl";
	public static final int TYPE_PICK_CAMERA = 0;
	public static final int TYPE_PICK_GALLERY = 1;
	private static final String OUTPUT_IMAGE_JPG = "output_image.jpg";
	private static final String OUTPUT_IMAGE_SMALL_JPG = "output_image_small.jpg";
	IPhotoView iPhotoView;
	File appBaseDir;
	private Uri imageUri;

	public AnalysePresenterCompl(IPhotoView iPhotoView) {
		this.iPhotoView = iPhotoView;
		File dir = new File(FileUtil.getSdpath() + File.separator + "Moe Studio");
		dir.mkdir();
		appBaseDir = new File(dir.getAbsolutePath() + File.separator + "How Old Robot");
		appBaseDir.mkdir();
	}

	@Override
	public void doAnalyse(Context context, Bitmap bitmap, String imgPath) {
		// int mode = ParaSetting.mode.value;
		// switch (mode) {
		// case ModeConstant.OFF_LINE:
		// postRequest(context, bitmap);// 离线识别
		// break;
		// case ModeConstant.ON_LINE:
		postRequest(context, imgPath, bitmap);// 网络识别
		// break;
		// }
		// postRequest(imgPath);
		// postRequest(context, bitmap);

	}

	@Override
	public void pickPhoto(Activity activity, int type) {
		File outputImage;
		switch (type) {
		case TYPE_PICK_CAMERA:
			Intent takePicture = new Intent("android.media.action.IMAGE_CAPTURE");
			outputImage = new File(appBaseDir.getAbsolutePath() + File.separator + OUTPUT_IMAGE_JPG);
			if (!outputImage.exists())
				try {
					outputImage.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			imageUri = Uri.fromFile(outputImage);
			// takePicture.putExtra("return-data", false);
			takePicture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			try {
				activity.startActivityForResult(takePicture, MainActivity.ACTIVITY_REQUEST_CAMERA);
			} catch (Exception e) {
				e.printStackTrace();
				iPhotoView.toast(activity.getResources().getString(R.string.main_pick_camera_fail));
			}
			break;
		case TYPE_PICK_GALLERY:
			Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			pickPhoto.putExtra("crop", "true");// 允许裁剪
			outputImage = new File(appBaseDir.getAbsolutePath() + File.separator + OUTPUT_IMAGE_JPG);
			if (!outputImage.exists())
				try {
					outputImage.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			imageUri = Uri.fromFile(outputImage);
			// takePicture.putExtra("return-data", false);
			pickPhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			try {
				activity.startActivityForResult(pickPhoto, MainActivity.ACTIVITY_REQUEST_GALLERY);
			} catch (Exception e) {
				e.printStackTrace();
				pickPhoto.putExtra("crop", false);// 不允许裁剪
				try {
					activity.startActivityForResult(pickPhoto, MainActivity.ACTIVITY_REQUEST_GALLERY);
				} catch (Throwable t) {
					t.printStackTrace();
					iPhotoView.toast(activity.getResources().getString(R.string.main_pick_gallery_fail));
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void getImage(Context context, Intent intent) {
		try {
			Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageUri));
			// 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
			int widthBitmap = bitmap.getWidth();
			int heightBitmap = bitmap.getHeight();
			int widthMax = AppUtil.getScreenWitdh(context) - (context.getResources().getDimensionPixelSize(R.dimen.margin_main_left) + context.getResources().getDimensionPixelSize(R.dimen.border_main_photo) + context.getResources().getDimensionPixelSize(R.dimen.offset_main_photo)) * 2;
			int heightMax = iPhotoView.getPhotoContainer().getHeight() - (context.getResources().getDimensionPixelSize(R.dimen.border_main_photo) + context.getResources().getDimensionPixelSize(R.dimen.offset_main_photo)) * 2;
			if (widthBitmap > widthMax && heightBitmap > heightMax) {
				float rateWidth = (float) widthBitmap / (float) widthMax;
				float rateHeight = (float) heightBitmap / (float) heightMax;
				if (rateWidth >= rateHeight)
					bitmap = BitmapUtil.zoomBitmapToWidth(bitmap, widthMax);
				else
					bitmap = BitmapUtil.zoomBitmapToHeight(bitmap, heightMax);
			} else if (widthBitmap > widthMax) {
				bitmap = BitmapUtil.zoomBitmapToWidth(bitmap, widthMax);
			} else if (heightBitmap > heightMax) {
				bitmap = BitmapUtil.zoomBitmapToHeight(bitmap, heightMax);
			}
			String imgPath = appBaseDir.getAbsolutePath() + File.separator + OUTPUT_IMAGE_SMALL_JPG;
			if (BitmapUtil.saveBitmapToSd(bitmap, 80, imgPath))
				iPhotoView.onGetImage(bitmap, imgPath);
		} catch (Exception e) {
			iPhotoView.toast(context.getResources().getString(R.string.main_get_img_fail));
			e.printStackTrace();
		}
	}

	private void postRequest(Context context, Bitmap bitmap) {
		KonkaApplication app = (KonkaApplication) context.getApplicationContext();
		String modelpath = app.getModelPath();
		check(modelpath, bitmap);

	}

	private void check(final String modelpath, final Bitmap bitmap) {
		Observable<List<Face>> myObservable = Observable.create(new Observable.OnSubscribe<List<Face>>() {
			@Override
			public void call(Subscriber<? super List<Face>> sub) {

				int[] results = KonkaSo.FaceDetect(Files.bitmap2intArray(bitmap), modelpath, bitmap.getWidth(), bitmap.getHeight());
				float[] results_age = KonkaSo.AgeGenderEstimate(Files.bitmap2intArray(bitmap), modelpath, bitmap.getWidth(), bitmap.getHeight());
				List<Face> faceList = new ArrayList<>();
				int array_resultslength = results_age.length;
				int ageAndSexCount = 0;

				for (int i = 0; i < array_resultslength; i++) {
					System.out.println("results_age[" + i + "]=" + results_age[i]);
				}
				if (results_age != null && array_resultslength > 1) {
					int facecount = (int) results_age[0];
					System.out.println("facecount" + facecount);
					System.out.println("results_age[0]" + results_age[0]);
					System.out.println("array_resultslength" + array_resultslength);
					System.out.println("facecount << 1" + (facecount << 1));
					System.out.println("facecount << 1" + ((facecount << 1) + 1));
					System.out.println("facecount << 1" + (facecount << 1 + 1));
					if (array_resultslength >= ((facecount << 1) + 1)) {// 数据格式正确
						ageAndSexCount = facecount;
					}
				}

				if (results != null && results.length % 4 == 0) {
					int count = results.length / 4;
					// int agecount = results_age.length / 2;
					System.out.println(count);
					System.out.println("count====" + count);
					System.out.println("agecount===" + ageAndSexCount);
					// if (agecount < count) {// 保证两个数据一致
					// sub.onError(new Exception("返回数据格式错误"));
					// return;
					// }
					for (int i = 0; i < count; i++) {
						// x坐标，y坐标，人脸宽，人脸高
						int index = i * 4;
						int x = results[index];
						int y = results[index + 1];
						int w = results[index + 2];
						int h = results[index + 3];

						int ageIndex = i * 2 % ageAndSexCount;
						float age = results_age[ageIndex + 1];
						float sex = results_age[ageIndex + 2];
						Face face = new Face();
						face.faceRectangle.height = h;
						face.faceRectangle.width = w;
						face.faceRectangle.left = x;
						face.faceRectangle.top = y;

						face.attributes.age1 = (int) age;
						face.attributes.gender = (int) sex;
						faceList.add(face);

					}
					if (!sub.isUnsubscribed()) {
						sub.onNext(faceList);
						sub.onCompleted();
					}
				} else {
					if (!sub.isUnsubscribed()) {
						sub.onError(new Exception("返回数据格式错误"));
					}
				}

			}
		});
		myObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<Face>>() {
			@Override
			public void onStart() {
				super.onStart();
				iPhotoView.showProgressDialog(true);
			}

			@Override
			public void onCompleted() {
				// Toast.makeText(, text, duration)
			}

			@Override
			public void onError(Throwable arg0) {
				iPhotoView.onGetFaces(null);
			}

			@Override
			public void onNext(List<Face> arg0) {
				iPhotoView.onGetFaces(arg0);
			}

		});
		// myObservable .subscribeOn(Schedulers.io());

	}

	private void parserJson(String string) {
		if (string == null) {
			iPhotoView.onGetFaces(null);
			return;
		}
		List<Face> faceList = new ArrayList<>();
		try {
			JSONArray jsonArray = new JSONArray(string);
			if (jsonArray.length() <= 0) {
				Log.w(TAG, "No Face");
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject item = jsonArray.getJSONObject(i);
				Face face = new Face();
				// face.faceId = item.optInt("faceId", 0);
				JSONObject faceRectangle = item.optJSONObject("faceRectangle");
				face.faceRectangle.left = faceRectangle.optInt("left", 0);
				face.faceRectangle.top = faceRectangle.optInt("top", 0);
				face.faceRectangle.width = faceRectangle.optInt("width", 65);
				face.faceRectangle.height = faceRectangle.optInt("height", 65);
				JSONObject attributes = item.optJSONObject("attributes");
				face.attributes.gender = attributes.optString("gender", "Female").equals("Female") ? 2 : 1;
				face.attributes.age1 = attributes.optInt("age", 17);
				faceList.add(face);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		iPhotoView.onGetFaces(faceList);
	}

	private AsyncHttpClient asyncHttpClient;

	private void postRequest(final Context context, String imagePath, final Bitmap bitmap) {
		iPhotoView.showProgressDialog(true);
		try {
			if (asyncHttpClient == null)
				asyncHttpClient = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("data", new File(imagePath)); // Upload a File
			// params.put("data", new Base64InputStream(new
			// FileInputStream(imagePath),Base64.DEFAULT)); // Upload a File
			params.put("isTest", "False");
			Log.d(TAG, "do post ");
			asyncHttpClient.post("http://how-old.net/Home/Analyze?isTest=False", params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					Log.d(TAG, "onSuccess: statusCode = " + statusCode);
					try {
						String string = new String(responseBody, "UTF-8");
						String str1 = string.replaceAll("\\\\", "");
						String str2 = str1.replaceAll("rn", "");
						String json = str2.substring(str2.indexOf("\"Faces\":[") + 8, str2.lastIndexOf("]") + 1);
						Log.d(TAG, "onSuccess: json = " + json);
						parserJson(json);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
					Log.d(TAG, "onFailure: statusCode = " + statusCode);
					// parserJson(null);
					postRequest(context, bitmap);// 离线识别
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
			// parserJson(null);
			postRequest(context, bitmap);// 离线识别
		}

		// new PostHandler().execute(imagePath);
	}

}

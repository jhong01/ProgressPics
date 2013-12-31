package com.hsi.progresspics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "progresspics4.db";
	private static final int SCHEMA_VERSION = 1;
	private static DatabaseHelper singleton = null;
	private Context ctxt = null;

	synchronized static DatabaseHelper getInstance(Context ctxt) {
		if (singleton == null) {
			singleton = new DatabaseHelper(ctxt.getApplicationContext());
		}
		return (singleton);
	}

	private DatabaseHelper(Context ctxt) {
		super(ctxt, DATABASE_NAME, null, SCHEMA_VERSION);
		this.ctxt = ctxt;
	}

	interface CardListener {
		void setCard(CardInfo card);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.beginTransaction();
			db.execSQL("CREATE TABLE cards (position INTEGER PRIMARY KEY, filename TEXT, weight TEXT, comments TEXT, date TEXT);");
			db.setTransactionSuccessful();
		} finally {

			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		throw new RuntimeException(ctxt.getString(R.string.on_upgrade_error));
	}

	void getNoteAsync(int position, CardListener listener) {
		PictureActivity.executeAsyncTask(new GetNoteTask(listener), position);
	}

	void saveNoteAsync(String filename, String weight, String comments,
			String date) {
		PictureActivity.executeAsyncTask(new SaveNoteTask(filename, weight,
				comments, date));
	}

	void deleteNoteAsync(int position) {
		PictureActivity.executeAsyncTask(new DeleteNoteTask(), position);
	}

	private class GetNoteTask extends AsyncTask<Integer, Void, CardInfo> {
		private CardListener listener = null;

		GetNoteTask(CardListener listener) {
			this.listener = listener;
		}

		@Override
		protected CardInfo doInBackground(Integer... params) {
			String[] args = { "filename", "weight", "comments", "date" };
			/*
			 * Cursor c = getReadableDatabase().rawQuery(
			 * "SELECT comments FROM cards WHERE position=?", args);
			 * c.moveToLast(); // c.moveToFirst(); if (c.isAfterLast()) { return
			 * (null); } String result = c.getString(0); c.close(); return
			 * (result);
			 */
			SQLiteDatabase db = getReadableDatabase();

			Cursor cursor = db.query("cards", args, "position" + "=?",
					new String[] { params[0].toString() }, null, null, null,
					null);
			if (cursor != null)
				cursor.moveToFirst();

			// Contact contact = new
			// Contact(Integer.parseInt(cursor.getString(0)),
			// cursor.getString(1), cursor.getString(2));
			CardInfo card = new CardInfo(cursor.getString(0),
					cursor.getString(1), cursor.getString(2));
			return card;
		}

		@Override
		public void onPostExecute(CardInfo card) {
			listener.setCard(card);
		}
	}

	private class SaveNoteTask extends AsyncTask<Void, Void, Void> {
		private int position;
		private String filename = null;
		private String weight;
		private String comments;
		private String date;

		SaveNoteTask(String filename, String weight, String comments,
				String formattedDate) {
			this.filename = filename;
			this.weight = weight;
			this.comments = comments;
			this.date = formattedDate;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// String[] args = { String.valueOf(position), note };
			/*
			 * getWritableDatabase() .execSQL(
			 * "INSERT OR REPLACE INTO cards (position, comments) VALUES (?, ?)"
			 * , args);
			 */
			SQLiteDatabase db = getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put("filename", filename);
			values.put("weight", weight);
			values.put("comments", comments);
			values.put("date", date);

			// Inserting Row
			db.insert("cards", null, values);

			// db.close(); // Closing database connection

			return (null);
		}
	}

	private class DeleteNoteTask extends AsyncTask<Integer, Void, Void> {
		@Override
		protected Void doInBackground(Integer... params) {
			String[] args = { params[0].toString() };
			getWritableDatabase().execSQL("DELETE FROM cards WHERE position=?",
					args);
			return (null);
		}
	}

	public static String formatDateTime(Context context) {
		String timeToFormat = "";
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		timeToFormat = df.format(c.getTime());
		String finalDateTime = "";

		SimpleDateFormat iso8601Format = new SimpleDateFormat("dd-MMM-yyyy");

		Date date = null;
		if (timeToFormat != null) {
			try {
				date = iso8601Format.parse(timeToFormat);
			} catch (ParseException e) {
				date = null;
			}

			if (date != null) {
				long when = date.getTime();
				int flags = 0;
				flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
				flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
				flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
				flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;

				finalDateTime = android.text.format.DateUtils.formatDateTime(
						context, when + TimeZone.getDefault().getOffset(when),
						flags);
			}
		}
		return finalDateTime;
	}
}
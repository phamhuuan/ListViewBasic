package com.example.listview;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
	ListView listView;
	ArrayList<String> arrayCourses, displayCourses;
	EditText editText;
	Button buttonAdd, buttonEdit, buttonCancel;
	int currentPosition = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		listView = findViewById(R.id.listView);
		editText = findViewById(R.id.editText);
		buttonAdd = findViewById(R.id.buttonAdd);c
		buttonEdit = findViewById(R.id.buttonEdit);
		buttonCancel = findViewById(R.id.buttonCancel);

		arrayCourses = new ArrayList<>();
		displayCourses = new ArrayList<>();
		displayCourses.addAll(arrayCourses);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayCourses);
		listView.setAdapter(adapter);

		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				buttonAdd.setEnabled(editText.getText().length() != 0);
				buttonEdit.setEnabled(editText.getText().length() != 0);
				displayCourses.clear();
				displayCourses.addAll(arrayCourses);
				filterList(displayCourses, editText.getText().toString());
				adapter.notifyDataSetChanged();
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		listView.setOnItemClickListener((parent, view, position, id) -> {
			currentPosition = position;
			editText.setText(displayCourses.get(position));
			buttonAdd.setVisibility(View.INVISIBLE);
			buttonCancel.setVisibility(View.VISIBLE);
			buttonEdit.setVisibility(View.VISIBLE);
			Log.d("Focus", "" + editText.requestFocus());
			inputMethodManager.showSoftInput(editText, 0);
			editText.setSelection(0, editText.getText().length());
		});

		listView.setOnItemLongClickListener((parent, view, position, id) -> {
			arrayCourses.remove(position);
			displayCourses.remove(position);
			adapter.notifyDataSetChanged();
			return true;
		});

		buttonAdd.setOnClickListener(view -> {
			inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
			addToList(arrayCourses, editText.getText().toString().trim());
			editText.setText("");
		});

		buttonCancel.setOnClickListener(view -> {
			buttonAdd.setVisibility(View.VISIBLE);
			buttonCancel.setVisibility(View.INVISIBLE);
			buttonEdit.setVisibility(View.INVISIBLE);
			editText.setText("");
			inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		});

		buttonEdit.setOnClickListener(view -> {
			buttonAdd.setVisibility(View.VISIBLE);
			buttonCancel.setVisibility(View.INVISIBLE);
			buttonEdit.setVisibility(View.INVISIBLE);
			arrayCourses.remove(currentPosition);
			addToList(arrayCourses, editText.getText().toString().trim());
			editText.setText("");
		});
	}

	private void addToList (ArrayList<String> list, String value) {
		if (list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).compareToIgnoreCase(value) > 0) {
					list.add(i, value);
					return;
				}
			}
		}
		arrayCourses.add(value);
	}

	private void filterList (ArrayList<String> list, String value) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			if (value.trim().length() > 0) {
				list.removeIf(course -> !course.toLowerCase().contains(value.toLowerCase()));
			}
		} else {
			for (int i = list.size() - 1; i >= 0; i--) {
				if (value.trim().length() > 0) {
					if (!list.get(i).toLowerCase().contains(value.toLowerCase())) {
						list.remove(i);
					}
				}
			}
		}
	}
}
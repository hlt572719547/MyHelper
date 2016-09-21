package com.example.stickyheader;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.entity.UserInfo;
import com.example.mydictionary.R;

import java.util.List;

/**
 * Shows a fake addressbook listing, loaded from randomuser.me, where the people are sorted
 * into sections by the first letter of last name.
 */
public class AddressBookDemoActivity extends DemoActivity implements PhoneInfoLoader.OnLoadCallback {

	private static final String TAG = AddressBookDemoActivity.class.getSimpleName();
	AddressBookAdapter adapter = new AddressBookAdapter();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		recyclerView.setLayoutManager(new StickyHeaderLayoutManager());
		recyclerView.setAdapter(adapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		progressBar.setVisibility(View.VISIBLE);
		recyclerView.setVisibility(View.GONE);
		PhoneInfoLoader.getInstance(this).load(this);
	}

	@Override
	public void onRandomUsersDidLoad(List<UserInfo> randomUsers) {
		progressBar.setVisibility(View.GONE);
		recyclerView.setVisibility(View.VISIBLE);
		adapter.setPeople(randomUsers);
	}

	@Override
	public void onRandomUserLoadFailure() {

		progressBar.setVisibility(View.GONE);
		recyclerView.setVisibility(View.GONE);

		Snackbar snackbar = Snackbar.make(recyclerView, "Unable to load addressbook", Snackbar.LENGTH_LONG);
		snackbar.setAction(R.string.demo_addressbook_load_error_action, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(AddressBookDemoActivity.this, "获取联系人失败！", Toast.LENGTH_SHORT).show();
			}
		});
		snackbar.show();
	}

	private void showRandomUserLoadError(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.demo_addressbook_load_error_dialog_title)
				.setMessage(message)
				.setPositiveButton(android.R.string.ok, null)
				.show();
	}
}

package de.devfest.gplustwall.fragments;

import com.google.api.client.googleapis.extensions.android2.auth.GoogleAccountManager;

import de.devfest.gplustwall.AccountListAdapter;
import de.devfest.gplustwall.R;
import de.devfest.gplustwall.utils.AuthUtils;

import android.accounts.Account;
import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class AccountListFragment extends ListFragment {

	 private Intent callback;

	  @Override
	  public void onCreate(Bundle savedState) {
	    super.onCreate(savedState);
	  }
	  
	  @Override
	  public void onListItemClick(ListView l, View v, int position, long id) {
		  final Account account = (Account) getListView().getItemAtPosition(position);
		  final SharedPreferences settings = getActivity().getSharedPreferences(AuthUtils.PREFS_NAME, 0);
		  final SharedPreferences.Editor editor = settings.edit();
		  editor.putString(AuthUtils.PREF_ACCOUNT_NAME, account.name);
		  editor.commit();
		  startActivity(callback);
		  getActivity().finish();
	  }

	  
	  @Override
	  public void onResume() {
		  super.onResume();
		  final Intent intent = getActivity().getIntent();
		  if (intent != null && intent.hasExtra("callback")) {
			  callback = (Intent) intent.getExtras().get("callback");
		  }	

	    
		  final Account[] accounts = new GoogleAccountManager(getActivity().getApplicationContext()).getAccounts();
		  setListAdapter(new AccountListAdapter(getActivity().getApplicationContext(), R.id.account, accounts));
	  }
	  
}

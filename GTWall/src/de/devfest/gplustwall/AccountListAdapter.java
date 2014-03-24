package de.devfest.gplustwall;

import android.accounts.Account;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public final class AccountListAdapter extends ArrayAdapter<Account> {

	private final Account[] accounts;
	public AccountListAdapter(Context context, int textViewResourceId,
			Account[] accounts) {
		super(context, textViewResourceId, accounts);
		this.accounts = accounts;
	}
	
	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		if (view == null) {
			final LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        view = layoutInflater.inflate(R.layout.account_list_item, null);
	      }
	      final Account account = accounts[position];
	      if (account != null) {
	        final TextView textView = (TextView) view.findViewById(R.id.account);
	        if (textView != null) {
	          textView.setText(account.name);
	        }
	      }
	      return view;
	    }
}

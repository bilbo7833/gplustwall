package de.devfest.gplustwall;

import android.accounts.Account;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.api.client.googleapis.extensions.android2.auth.GoogleAccountManager;

import de.devfest.gplustwall.utils.AuthUtils;

public final class AccountListActivity extends ListActivity {
    private Intent callback;

    @Override
    public void onCreate( Bundle savedState ) {
        super.onCreate( savedState );
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Intent intent = getIntent();
        if ( intent != null && intent.hasExtra( "callback" ) ) {
            callback = ( Intent ) intent.getExtras().get( "callback" );
        }

        final Account[] accounts = new GoogleAccountManager( getApplicationContext() )
                .getAccounts();
        this.setListAdapter( new AccountListAdapter( this, R.layout.acitivity_account_list,
                accounts ) );
    }

    @Override
    public void onListItemClick( ListView l, View v, int position, long id ) {
        final Account account = ( Account ) getListView().getItemAtPosition( position );
        final SharedPreferences settings = getSharedPreferences( AuthUtils.PREFS_NAME, 0 );
        final SharedPreferences.Editor editor = settings.edit();
        editor.putString( AuthUtils.PREF_ACCOUNT_NAME, account.name );
        editor.commit();
        startActivity( callback );
        finish();
    }

}

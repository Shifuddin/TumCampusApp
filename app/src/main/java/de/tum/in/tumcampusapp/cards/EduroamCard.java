package de.tum.in.tumcampusapp.cards;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.tum.in.tumcampusapp.R;
import de.tum.in.tumcampusapp.activities.SetupEduroamActivity;
import de.tum.in.tumcampusapp.cards.generic.Card;
import de.tum.in.tumcampusapp.cards.generic.NotificationAwareCard;
import de.tum.in.tumcampusapp.models.managers.CardManager;
import de.tum.in.tumcampusapp.models.managers.EduroamManager;

/**
 * Card that can start {@link SetupEduroamActivity}
 */
public class EduroamCard extends NotificationAwareCard {

    public EduroamCard(Context context) {
        super(CardManager.CARD_EDUROAM, context, "card_eduroam", false, true);
    }

    public static Card.CardViewHolder inflateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_eduroam, parent, false);
        return new Card.CardViewHolder(view);
    }

    @Override
    public void updateViewHolder(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    protected boolean shouldShow(SharedPreferences prefs) {
        EduroamManager manager = new EduroamManager(mContext);
        return !manager.isConfigured();
    }

    @Override
    protected void discard(SharedPreferences.Editor editor) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        prefs.edit().putBoolean("card_eduroam_start", false).apply();
    }

    @Override
    protected Notification fillNotification(NotificationCompat.Builder notificationBuilder) {
        return null;
    }

    @Override
    public String getTitle() {
        return mContext.getString(R.string.setup_eduroam);
    }

    @Override
    public Intent getIntent() {
        return new Intent(mContext, SetupEduroamActivity.class);
    }

    @Override
    public int getId() {
        return 0;
    }
}

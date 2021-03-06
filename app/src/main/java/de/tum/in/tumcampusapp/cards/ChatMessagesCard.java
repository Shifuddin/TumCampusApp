package de.tum.in.tumcampusapp.cards;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import de.tum.in.tumcampusapp.R;
import de.tum.in.tumcampusapp.activities.ChatActivity;
import de.tum.in.tumcampusapp.auxiliary.Const;
import de.tum.in.tumcampusapp.cards.generic.Card;
import de.tum.in.tumcampusapp.cards.generic.NotificationAwareCard;
import de.tum.in.tumcampusapp.models.ChatMessage;
import de.tum.in.tumcampusapp.models.ChatRoom;
import de.tum.in.tumcampusapp.models.managers.ChatMessageManager;

import static de.tum.in.tumcampusapp.models.managers.CardManager.CARD_CHAT;

/**
 * Card that shows the cafeteria menu
 */
public class ChatMessagesCard extends NotificationAwareCard {
    private List<ChatMessage> mUnread;
    private ChatMessageManager manager;
    private String mRoomName;
    private int mRoomId;
    private String mRoomIdString;

    public ChatMessagesCard(Context context) {
        super(CARD_CHAT, context, "card_chat");
    }

    public static Card.CardViewHolder inflateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new Card.CardViewHolder(view);
    }

    @Override
    public String getTitle() {
        return mRoomName;
    }

    @Override
    public void updateViewHolder(RecyclerView.ViewHolder viewHolder) {
        mCard = viewHolder.itemView;
        CardViewHolder cardsViewHolder = (CardViewHolder) viewHolder;
        List<View> addedViews = cardsViewHolder.getAddedViews();

        mLinearLayout = (LinearLayout) mCard.findViewById(R.id.card_view);
        mTitleView = (TextView) mCard.findViewById(R.id.card_title);
        mTitleView.setText(mRoomName);

        //Remove additional views
        for (View view : addedViews) {
            mLinearLayout.removeView(view);
        }

        // Show cafeteria menu
        for (ChatMessage message : mUnread) {
            addedViews.add(addTextView(message.getMember().getDisplayName() + ": " + message.getText()));
        }
    }

    /**
     * Sets the information needed to build the card
     *
     * @param roomName Name of the chat room
     * @param roomId   Id of the chat room
     */
    public void setChatRoom(String roomName, int roomId, String roomIdString) {
        mRoomName = roomName;
        mRoomName = mRoomName.replaceAll("[A-Z, 0-9(LV\\.Nr)=]+$", "");
        mRoomName = mRoomName.replaceAll("\\([A-Z]+[0-9]+\\)", "");
        mRoomName = mRoomName.replaceAll("\\[[A-Z]+[0-9]+\\]", "");
        mRoomName = mRoomName.trim();
        manager = new ChatMessageManager(mContext, roomId);
        mUnread = manager.getLastUnread();
        mRoomIdString = roomIdString;
        mRoomId = roomId;
    }

    @Override
    public Intent getIntent() {
        Bundle bundle = new Bundle();
        final Intent intent = new Intent(mContext, ChatActivity.class);
        ChatRoom currentChatRoom = new ChatRoom(mRoomIdString);
        currentChatRoom.setId(mRoomId);
        intent.putExtra(Const.CURRENT_CHAT_ROOM, new Gson().toJson(currentChatRoom));
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public int getId() {
        return mRoomId;
    }

    @Override
    protected void discard(Editor editor) {
        manager.markAsRead();
    }

    @Override
    protected boolean shouldShowNotification(SharedPreferences prefs) {
        return false;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.semanticwb.social.admin.resources;

import java.io.Writer;
import java.util.ArrayList;
import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.TwitterStream;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;

/**
 *
 * @author francisco.jimenez
 */
public class SocialUserStreamListener implements UserStreamListener {
    public ArrayList<Status> socialStatus;//ArrayList of Status
    private TwitterStream twitterStream;  //Reference to twitterStream to stop the listener
    public Long startTime;                //Time when the listener was started
    private Long totalRunningTime;        //Total time of execution of the listener
    public boolean streamActive = true;   //When the listener is halted the value changes
    
    public SocialUserStreamListener(){
        this.socialStatus = new ArrayList<Status>();
    }
    public SocialUserStreamListener(TwitterStream twitterStream){
        this.startTime = System.currentTimeMillis();
        this.twitterStream = twitterStream;
        this.socialStatus = new ArrayList<Status>();
    }   
    
    @Override
    public void onStatus(Status status) {
        this.socialStatus.add(status);
        this.totalRunningTime = System.currentTimeMillis() - this.startTime;
        System.out.println("difference: " + this.totalRunningTime);
        if (this.totalRunningTime > 60000*15){//Has been alive for 15 min since last interaction
           System.out.println("15 minutes of inactivity. Time to stop the thread!");
           this.twitterStream.cleanUp();
           this.twitterStream.shutdown();
           this.twitterStream = null;
           this.streamActive = false;
       }else{
            System.out.println("New status: @" + status.getUser().getScreenName());
       }
    }

    //We need to define actions for all the other events.
    @Override
    public void onDeletionNotice(long directMessageId, long userId) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onFriendList(long[] friendIds) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onFavorite(User source, User target, Status favoritedStatus) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onFollow(User source, User followedUser) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onDirectMessage(DirectMessage directMessage) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onUserListMemberAddition(User addedMember, User listOwner, UserList list) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onUserListSubscription(User subscriber, User listOwner, UserList list) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onUserListUnsubscription(User subscriber, User listOwner, UserList list) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onUserListCreation(User listOwner, UserList list) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onUserListUpdate(User listOwner, UserList list) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onUserListDeletion(User listOwner, UserList list) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onUserProfileUpdate(User updatedUser) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onBlock(User source, User blockedUser) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onUnblock(User source, User unblockedUser) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onStallWarning(StallWarning warning) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onException(Exception ex) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}

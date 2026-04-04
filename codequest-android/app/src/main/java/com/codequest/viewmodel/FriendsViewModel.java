package com.codequest.viewmodel;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.codequest.model.Friend;
import com.codequest.model.User;
import com.codequest.repository.FriendRepository;
import com.codequest.repository.UserRepository;
import java.util.List;
public class FriendsViewModel extends AndroidViewModel {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private LiveData<List<Friend>> friends;
    private LiveData<List<Friend>> pendingRequests;
    private final MutableLiveData<List<User>> searchResults = new MutableLiveData<>();
    public FriendsViewModel(@NonNull Application application) {
        super(application);
        friendRepository = new FriendRepository();
        userRepository = new UserRepository();
    }
    public LiveData<List<Friend>> getFriends() {
        if (friends == null) friends = friendRepository.getFriends();
        return friends;
    }
    public LiveData<List<Friend>> getPendingRequests() {
        if (pendingRequests == null) pendingRequests = friendRepository.getFriendRequests();
        return pendingRequests;
    }
    public LiveData<List<User>> getSearchResults() { return searchResults; }
    public void searchUsers(String query) {
        if (query == null || query.trim().length() < 2) {
            searchResults.setValue(null);
            return;
        }
        userRepository.searchUsers(query.trim()).observeForever(users -> searchResults.setValue(users));
    }
    public LiveData<Boolean> sendFriendRequest(long userId) {
        return friendRepository.sendFriendRequest(userId);
    }
    public LiveData<Boolean> acceptFriend(long friendId) {
        return friendRepository.acceptFriend(friendId);
    }
    public LiveData<Boolean> rejectFriend(long friendId) {
        return friendRepository.rejectFriend(friendId);
    }
    public LiveData<Boolean> removeFriend(long friendId) {
        return friendRepository.removeFriend(friendId);
    }
    public void refresh() {
        friends = friendRepository.getFriends();
        pendingRequests = friendRepository.getFriendRequests();
    }
}

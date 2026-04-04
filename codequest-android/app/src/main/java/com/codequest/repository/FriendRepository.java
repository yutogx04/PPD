package com.codequest.repository;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.codequest.model.Friend;
import com.codequest.model.dto.FriendRequest;
import com.codequest.network.RetrofitClient;
import java.util.Arrays;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class FriendRepository {
    private final boolean useMockData = false;
    public LiveData<List<Friend>> getFriends() {
        MutableLiveData<List<Friend>> data = new MutableLiveData<>();
        if (useMockData) {
            List<Friend> friends = Arrays.asList(
                    new Friend(2, "DevNinja", 8, 3200, 25, "ACCEPTED"),
                    new Friend(3, "HackMaster", 7, 2150, 14, "ACCEPTED"),
                    new Friend(4, "PyMaster", 6, 1890, 8, "ACCEPTED")
            );
            friends.get(0).setLastActivity("A résolu FizzBuzz");
            friends.get(1).setLastActivity("Badge Assidu débloqué");
            friends.get(2).setLastActivity("A terminé le module Boucles");
            data.setValue(friends);
            return data;
        }
        RetrofitClient.getApi().getFriends().enqueue(new Callback<List<Friend>>() {
            @Override public void onResponse(Call<List<Friend>> c, Response<List<Friend>> r) { data.setValue(r.body()); }
            @Override public void onFailure(Call<List<Friend>> c, Throwable t) { data.setValue(null); }
        });
        return data;
    }
    public LiveData<List<Friend>> getFriendRequests() {
        MutableLiveData<List<Friend>> data = new MutableLiveData<>();
        if (useMockData) {
            List<Friend> requests = Arrays.asList(
                    new Friend(5, "AlgoKing", 7, 1750, 10, "PENDING")
            );
            data.setValue(requests);
            return data;
        }
        RetrofitClient.getApi().getFriendRequests().enqueue(new Callback<List<Friend>>() {
            @Override public void onResponse(Call<List<Friend>> c, Response<List<Friend>> r) { data.setValue(r.body()); }
            @Override public void onFailure(Call<List<Friend>> c, Throwable t) { data.setValue(null); }
        });
        return data;
    }
    public LiveData<Boolean> sendFriendRequest(long userId) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        if (useMockData) { data.setValue(true); return data; }
        RetrofitClient.getApi().sendFriendRequest(new FriendRequest(userId))
                .enqueue(new Callback<Void>() {
                    @Override public void onResponse(Call<Void> c, Response<Void> r) { data.setValue(r.isSuccessful()); }
                    @Override public void onFailure(Call<Void> c, Throwable t) { data.setValue(false); }
                });
        return data;
    }
    public LiveData<Boolean> acceptFriend(long friendId) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        if (useMockData) { data.setValue(true); return data; }
        RetrofitClient.getApi().acceptFriend(friendId).enqueue(new Callback<Void>() {
            @Override public void onResponse(Call<Void> c, Response<Void> r) { data.setValue(r.isSuccessful()); }
            @Override public void onFailure(Call<Void> c, Throwable t) { data.setValue(false); }
        });
        return data;
    }
    public LiveData<Boolean> rejectFriend(long friendId) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        if (useMockData) { data.setValue(true); return data; }
        RetrofitClient.getApi().rejectFriend(friendId).enqueue(new Callback<Void>() {
            @Override public void onResponse(Call<Void> c, Response<Void> r) { data.setValue(r.isSuccessful()); }
            @Override public void onFailure(Call<Void> c, Throwable t) { data.setValue(false); }
        });
        return data;
    }
    public LiveData<Boolean> removeFriend(long friendId) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        if (useMockData) { data.setValue(true); return data; }
        RetrofitClient.getApi().removeFriend(friendId).enqueue(new Callback<Void>() {
            @Override public void onResponse(Call<Void> c, Response<Void> r) { data.setValue(r.isSuccessful()); }
            @Override public void onFailure(Call<Void> c, Throwable t) { data.setValue(false); }
        });
        return data;
    }
}

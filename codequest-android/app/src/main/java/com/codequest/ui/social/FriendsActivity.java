package com.codequest.ui.social;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.codequest.ui.base.BaseActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.codequest.R;
import com.codequest.ui.adapter.FriendAdapter;
import com.codequest.viewmodel.FriendsViewModel;
public class FriendsActivity extends BaseActivity {
    private FriendsViewModel viewModel;
    private RecyclerView rvFriends, rvPending;
    private TextView tvPendingTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        viewModel = new ViewModelProvider(this).get(FriendsViewModel.class);
        ImageView ivBack = findViewById(R.id.ivBack);
        EditText etSearch = findViewById(R.id.etSearch);
        rvFriends = findViewById(R.id.rvFriends);
        rvPending = findViewById(R.id.rvPending);
        tvPendingTitle = findViewById(R.id.tvPendingTitle);
        rvFriends.setLayoutManager(new LinearLayoutManager(this));
        rvPending.setLayoutManager(new LinearLayoutManager(this));
        ivBack.setOnClickListener(v -> finish());
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.searchUsers(s.toString());
            }
        });
        observeData();
    }
    private void observeData() {
        viewModel.getFriends().observe(this, friends -> {
            if (friends != null) {
                rvFriends.setAdapter(new FriendAdapter(friends, false,
                        friendId -> viewModel.removeFriend(friendId)));
            }
        });
        viewModel.getPendingRequests().observe(this, pending -> {
            if (pending != null && !pending.isEmpty()) {
                tvPendingTitle.setVisibility(View.VISIBLE);
                rvPending.setVisibility(View.VISIBLE);
                rvPending.setAdapter(new FriendAdapter(pending, true,
                        friendId -> viewModel.acceptFriend(friendId)));
            }
        });
    }
}


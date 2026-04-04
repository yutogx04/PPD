package com.codequest.network;
import com.codequest.model.*;
import com.codequest.model.dto.*;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.*;
public interface ApiService {
    @POST("api/v1/auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);
    @POST("api/v1/auth/verify-email")
    Call<AuthResponse> verifyEmail(@Body VerifyOTPRequest request);
    @POST("api/v1/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);
    @POST("api/v1/auth/refresh")
    Call<AuthResponse> refreshToken(@Body Map<String, String> body);
    @POST("api/v1/auth/forgot-password")
    Call<Void> forgotPassword(@Body ForgotPasswordRequest request);
    @POST("api/v1/auth/google")
    Call<AuthResponse> googleSignIn(@Body Map<String, String> body);
    @GET("api/v1/tracks")
    Call<List<Track>> getTracks();
    @GET("api/v1/tracks/{id}")
    Call<Track> getTrack(@Path("id") long id);
    @GET("api/v1/tracks/{id}/modules")
    Call<List<Module>> getModules(@Path("id") long trackId);
    @GET("api/v1/modules/{id}")
    Call<Module> getModule(@Path("id") long id);
    @GET("api/v1/modules/{id}/lessons")
    Call<List<Lesson>> getLessons(@Path("id") long moduleId);
    @GET("api/v1/modules/{id}/challenges")
    Call<List<Challenge>> getModuleChallenges(@Path("id") long moduleId);
    @GET("api/v1/lessons/{id}")
    Call<Lesson> getLesson(@Path("id") long id);
    @GET("api/v1/lessons/{id}/slides")
    Call<List<LessonSlide>> getLessonSlides(@Path("id") long lessonId);
    @PUT("api/v1/lessons/{id}/progress")
    Call<Void> updateLessonProgress(@Path("id") long id, @Body Map<String, Integer> body);
    @POST("api/v1/lessons/{id}/complete")
    Call<GamificationResult> completeLesson(@Path("id") long id);
    @POST("api/v1/lessons/{id}/favorite")
    Call<Void> toggleFavorite(@Path("id") long id);
    @GET("api/v1/favorites")
    Call<List<Lesson>> getFavorites();
    @GET("api/v1/challenges")
    Call<List<Challenge>> getChallenges();
    @GET("api/v1/challenges/{id}")
    Call<Challenge> getChallenge(@Path("id") long id);
    @GET("api/v1/challenges/daily")
    Call<DailyChallenge> getDailyChallenge();
    @POST("api/v1/challenges/{id}/run")
    Call<SubmissionResponse> runCode(@Path("id") long id, @Body SubmitCodeRequest request);
    @POST("api/v1/challenges/{id}/submit")
    Call<SubmissionResponse> submitCode(@Path("id") long id, @Body SubmitCodeRequest request);
    @GET("api/v1/challenges/{id}/hint")
    Call<Map<String, String>> getHint(@Path("id") long id);
    @GET("api/v1/challenges/{id}/solution")
    Call<Map<String, String>> getSolution(@Path("id") long id);
    @GET("api/v1/challenges/{id}/friends-scores")
    Call<List<LeaderboardEntry>> getFriendScoresOnChallenge(@Path("id") long id);
    @GET("api/v1/users/me")
    Call<User> getProfile();
    @PUT("api/v1/users/me")
    Call<User> updateProfile(@Body Map<String, String> body);
    @GET("api/v1/users/me/badges")
    Call<List<Badge>> getMyBadges();
    @GET("api/v1/users/me/stats")
    Call<UserStats> getDetailedStats();
    @GET("api/v1/users/search")
    Call<List<User>> searchUsers(@Query("q") String query);
    @GET("api/v1/leaderboard")
    Call<List<LeaderboardEntry>> getGlobalLeaderboard();
    @GET("api/v1/leaderboard/weekly")
    Call<List<LeaderboardEntry>> getWeeklyLeaderboard();
    @GET("api/v1/leaderboard/friends")
    Call<List<LeaderboardEntry>> getFriendsLeaderboard();
    @POST("api/v1/friends/request")
    Call<Void> sendFriendRequest(@Body FriendRequest request);
    @PUT("api/v1/friends/{id}/accept")
    Call<Void> acceptFriend(@Path("id") long id);
    @PUT("api/v1/friends/{id}/reject")
    Call<Void> rejectFriend(@Path("id") long id);
    @GET("api/v1/friends")
    Call<List<Friend>> getFriends();
    @GET("api/v1/friends/requests")
    Call<List<Friend>> getFriendRequests();
    @DELETE("api/v1/friends/{id}")
    Call<Void> removeFriend(@Path("id") long id);
    @POST("api/v1/notifications/register-token")
    Call<Void> registerFcmToken(@Body Map<String, String> body);
    @GET("api/v1/users/me/continue")
    Call<Lesson> getContinueLesson();
}

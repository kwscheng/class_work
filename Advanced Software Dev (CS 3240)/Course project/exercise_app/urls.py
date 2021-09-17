from django.urls import path
from . import views

app_name = 'exercise_app'
urlpatterns = [
	path('', views.IndexView.as_view(), name='index'),
	path('home/', views.HomeView.as_view(), name='home'),
	path('home/profile/', views.profile, name='profile'),
	path('home/exercises/', views.exercises, name='exercises'),
	path('home/achievements/', views.achievements, name='achievements'),
    path('home/update', views.update, name='update'),
    path('home/save_location', views.save_location, name='save_location'),

	# Public profiles
	path('users/', views.UserListView.as_view(), name='users'),
	path('users/<int:pk>', views.ProfileView.as_view(), name='user_profile'),

	# Exercises URLs
	path('exercises/submit/a/<int:exercise_pk>', views.log_cardio_exercise, name="log_cardio_exercise"),
	path('exercises/submit/b/<int:exercise_pk>', views.log_weightlifting_exercise, name="log_weightlifting_exercise"),
	path('exercises/submit/c/<int:exercise_pk>', views.log_custom_exercise, name="log_custom_exercise"),

	# Leaderboard URLs
	path('home/leaderboard/', views.LeaderboardView.as_view(), name='leaderboard'),

	# Friends URLs
	path('ajax/send_friend_request/', views.send_friend_request, name='send_friend_request'),
	path('ajax/friend_request_action/', views.friend_request_action, name='friend_request_action'),
	path('ajax/remove_friend', views.remove_friend, name='remove_friend'),
	path('home/friends', views.friends, name='friends'),

	# AJAX URLs
	path('ajax/get_exercise_info/', views.get_exercise_info, name='get_exercise_info'),
	path('ajax/get_leaderboard/', views.get_leaderboard, name='get_leaderboard'),
	path('ajax/get_exercise_table', views.get_exercise_table, name='get_exercise_table'),
  #weather
  path('home/weather',views.weather,name='weather'),

]

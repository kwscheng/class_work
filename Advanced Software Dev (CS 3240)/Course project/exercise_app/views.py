from django.views import generic
from django.http import HttpResponse, HttpResponseRedirect, request, JsonResponse, QueryDict
from django.urls import reverse
from django.contrib.auth.models import User
from django.contrib.auth import get_user_model
from django.contrib.auth.decorators import login_required
from datetime import date, timedelta, datetime
from django.utils.dateparse import parse_date

from django.dispatch import receiver
from django.db import models

from .models import AppUser, Exercise, ExerciseLogEntry, Achievements, FriendRequest
from .forms import *
from django.shortcuts import render,redirect

import requests
from .models import City
from .forms import CityForm
import json

# https://github.com/asmaaeltawil/Weather-Api-using-django
# https://stackoverflow.com/questions/57184835/keyerror-in-city-name/57185034#57185034
def weather(request):
	
	
	if request.method == 'POST':
		form = CityForm(request.POST)
		if form.is_valid():
			try:
				qd = QueryDict(mutable=True)
				qd.update(appid='0c42f7f6b53b244c78a418f4f181282a', units='imperial', q=form.cleaned_data['name'])
				url = 'http://api.openweathermap.org/data/2.5/weather?{}'.format(qd.urlencode())
				response = requests.get(url).json()
				City.objects.create(name=response['name'], owm_id=response['id'])
			except:
				return HttpResponseRedirect(reverse('exercise_app:weather'))
		return HttpResponseRedirect(reverse('exercise_app:weather'))

	else:
		form = CityForm()
		cities = City.objects.all()
		weather_data = []

		if cities:
			for city in cities:
				qd = QueryDict(mutable=True)
				qd.update(appid='0c42f7f6b53b244c78a418f4f181282a', units='imperial', id=city.owm_id)
				url = 'http://api.openweathermap.org/data/2.5/weather?{}'.format(qd.urlencode())
				response = requests.get(url).json()
				city_weather = {
					'city': response['name'],
					'temperature': response['main']['temp'],
					'description': response['weather'][0]['description'],
					'icon': response['weather'][0]['icon'],
				}
				weather_data.append(city_weather)

	return render(request, 'exercise_app/weather.html', {'weather_data': weather_data, 'form': form})

def delete(request, id):

	if request.method == 'POST':
		City.objects.filter(id=id).delete()

	return redirect('/')

class IndexView(generic.TemplateView):
	template_name = 'exercise_app/index.html'

class HomeView(generic.TemplateView):
	template_name = 'exercise_app/home.html'
  
class GoalsView(generic.TemplateView):
	template_name = 'exercise_app/goals.html'

class ProfileView(generic.DetailView):
	model = AppUser
	template_name = 'exercise_app/public_profile.html'

	def get_context_data(self, **kwargs):
		context = super().get_context_data(**kwargs)
		context['logs'] = ExerciseLogEntry.objects.filter(appUser=self.get_object())[:10]
		context['current_user'] = AppUser.objects.get(user=self.request.user)
		context['friends'] = AppUser.objects.get(user=self.request.user).friends.filter(user=self.get_object()).exists()
		context['request_sent'] = FriendRequest.objects.filter(from_user=AppUser.objects.get(user=self.request.user), to_user=self.get_object()).exists()
		return context
		
def update(request):
	#name, age, height, weight
	
	first = request.POST['first']
	last = request.POST['last']
	age = int(float(request.POST['age']))
	height_ft = int(float(request.POST['height-ft']))
	height_inch = int(float(request.POST['height-inch']))
	weight = int(float(request.POST['weight']))

	currentUser = AppUser.objects.get(user=request.user)
	
	currentUser.age = age
	currentUser.height = (height_ft*12)+height_inch
	currentUser.weight = weight

	currentUser.save()

	request.user.first_name = first
	request.user.last_name = last
	
	request.user.save()
	return HttpResponseRedirect(reverse('exercise_app:profile'))

def save_location(request):
	location = request.POST['accommodation_address']
	
	currentUser = AppUser.objects.get(user=request.user)

	currentUser.location = location
	currentUser.save()
	return HttpResponseRedirect(reverse('exercise_app:profile'))


def profile(request):
	currentUser = AppUser.objects.get(user=request.user)
	context = {'currentUser': currentUser,'feet': currentUser.height//12, "inch": currentUser.height%12, 'points': currentUser.points, 'email': request.user.email}
	return render(request, 'exercise_app/profile.html', context)

def exercises(request):
	currentUser = AppUser.objects.get(user=request.user)
	exercise_list = Exercise.objects.all()
	user_exercise_list = AppUser.objects.get(user=request.user).exercises.all()
			
	context = {'currentUser': currentUser, 'exercise_list': exercise_list, 'user_exercise_list': user_exercise_list}
	return render(request, 'exercise_app/exercises.html', context)

def achievements(request):
	currentUser = AppUser.objects.get(user=request.user)
	user_achievements = Achievements.objects.filter(awardedUsers=currentUser)
	unearned_achievements = Achievements.objects.exclude(awardedUsers=currentUser)
	context = {'currentUser': currentUser, 'user_achievements': user_achievements, 'unearned_achievements': unearned_achievements}
	return render(request, 'exercise_app/achievements.html', context)

class LeaderboardView(generic.TemplateView):
	template_name = 'exercise_app/leaderboard.html'
  
	def get_context_data(self, **kwargs):
		context = super().get_context_data(**kwargs)
		context['top3'] = AppUser.objects.all().order_by('-points')[:3]
		return context


@login_required
def get_leaderboard(request):
	user = AppUser.objects.get(user=request.user)
	if request.GET.get('filter', None) == 'friends':
		friend_list = user.friends.all().union(AppUser.objects.filter(user=request.user)).order_by('-points')
		return render( request, 'exercise_app/leaderboard_table.html', { 'user': user, 'user_list': friend_list} )
	else:
		return render( request, 'exercise_app/leaderboard_table.html', { 'user': user, 'user_list': AppUser.objects.all().order_by('-points') } )

@login_required
def log_cardio_exercise(request, exercise_pk):
	if request.method == 'POST':
		form = CardioLogForm(request.POST)
		if form.is_valid():
			log = form.save(commit=False)
			log.appUser = AppUser.objects.get(user=request.user)
			log.exercise = Exercise.objects.get(pk=exercise_pk)
			log.save()
			return HttpResponseRedirect(reverse('exercise_app:exercises'))
		else:
			print(form.errors)
	else:
		exercise = Exercise.objects.get(pk=exercise_pk)
		form = CardioLogForm()
		# https://stackoverflow.com/questions/16277997/field-labels-crispy-forms-django
		form.fields['distance'].label = exercise.distanceFieldDescription
		form.fields['time'].label = exercise.timeFieldDescription
		form.helper.form_action = reverse('exercise_app:log_cardio_exercise', args=[exercise_pk])
	return render(request, 'exercise_app/exercise_form.html', {'form': form, 'exercise_type': 'CA'})

@login_required
def log_weightlifting_exercise(request, exercise_pk):
	if request.method == 'POST':
		form = WeightliftingLogForm(request.POST)
		if form.is_valid():
			log = form.save(commit=False)
			log.appUser = AppUser.objects.get(user=request.user)
			log.exercise = Exercise.objects.get(pk=exercise_pk)
			log.save()
			return HttpResponseRedirect(reverse('exercise_app:exercises'))
		else:
			print(form.errors)
	else:
		form = WeightliftingLogForm()
		exercise = Exercise.objects.get(pk=exercise_pk)
		form.fields['weight'].label = exercise.weightFieldDescription
		form.fields['reps'].label = exercise.repsFieldDescription
		form.helper.form_action = reverse('exercise_app:log_weightlifting_exercise', args=[exercise_pk])
	return render(request, 'exercise_app/exercise_form.html', {'form': form, 'exercise_type': 'WL'})

@login_required
def log_custom_exercise(request, exercise_pk):
	if request.method == 'POST':
		form = CustomLogForm(request.POST)
		if form.is_valid():
			log = form.save(commit=False)
			log.appUser = AppUser.objects.get(user=request.user)
			log.exercise = Exercise.objects.get(pk=exercise_pk)
			log.save()
			return HttpResponseRedirect(reverse('exercise_app:exercises'))
		else:
			print(form.errors)
	else:
		form = CustomLogForm()
		exercise = Exercise.objects.get(pk=exercise_pk)
		form.fields['time'].label = exercise.timeFieldDescription
		form.helper.form_action = reverse('exercise_app:log_custom_exercise', args=[exercise_pk])
	return render(request, 'exercise_app/exercise_form.html', {'form': form, 'exercise_type': 'CU'})

@login_required
def get_exercise_info(request):
	exercise = Exercise.objects.get(name__iexact=request.GET.get('exercise', None))
	data = {
		'exerciseType': exercise.exercise_type,
		'exercisePK': exercise.pk
	}
	return JsonResponse(data)

@login_required
def get_exercise_table(request):
	exercise = Exercise.objects.get(name__iexact=request.GET.get('exercise', None))
	exercise_entries = ExerciseLogEntry.objects.filter(exercise=exercise, appUser=AppUser.objects.get(user=request.user)).order_by('-date')
	if exercise.exercise_type == "CA":
		field_descriptions = (exercise.distanceFieldDescription, exercise.timeFieldDescription)
		field_values = exercise_entries.values_list('date', 'calories', 'distance', 'time')
	elif exercise.exercise_type == "WL":
		field_descriptions = (exercise.weightFieldDescription, exercise.repsFieldDescription)
		field_values = exercise_entries.values_list('date', 'calories', 'weight', 'reps')
	else:
		field_descriptions = ("Time (min)", "Intensity (1-5)")
		field_values = exercise_entries.values_list('date', 'calories', 'time', 'intensity')
		
	return render( request, 'exercise_app/exercise_table.html', {'fields_descriptions': field_descriptions, 'field_values': field_values } )

class SuccessView(generic.TemplateView):
	template_name = 'exercise_app/log_success.html'

class UserListView(generic.ListView):
	model = AppUser
	context_object_name = 'users'
	template_name = 'exercise_app/users.html'

#https://medium.com/analytics-vidhya/add-friends-with-689a2fa4e41d --> based friends and friend requests off of this website
@login_required 
def send_friend_request(request):
	if request.method == 'POST':
		form = FriendForm(request.POST)
		if form.is_valid():
			print(form.cleaned_data)
			to_user = AppUser.objects.get(pk=form.cleaned_data['pk'])
			from_user = AppUser.objects.get(user=request.user)
			FriendRequest.objects.create(to_user=to_user, from_user=from_user)
			return HttpResponse(status=200)
	
	return HttpResponse(status=204)

@login_required
def friend_request_action(request):
	if request.method == 'POST':
		form = FriendRequestActionForm(request.POST)
		print(form)
		if form.is_valid():
			print(form.cleaned_data)
			current_user = AppUser.objects.get(user=request.user)
			other_user = AppUser.objects.get(pk=form.cleaned_data['other_user_pk'])
			if form.cleaned_data['action'] == FriendRequestActionForm.ACCEPT:
				friend_request = FriendRequest.objects.get(to_user=current_user, from_user=other_user)
				current_user.friends.add(other_user)
				friend_request.delete()
			elif form.cleaned_data['action'] == FriendRequestActionForm.REJECT:
				friend_request = FriendRequest.objects.get(to_user=current_user, from_user=other_user)
				friend_request.delete()
			elif form.cleaned_data['action'] == FriendRequestActionForm.CANCEL:
				friend_request = FriendRequest.objects.get(to_user=other_user, from_user=current_user)
				friend_request.delete()
			return HttpResponse(status=200)

	return HttpResponse(status=204)

@login_required
def remove_friend(request):
	if request.method == 'POST':
		form = RemoveFriendForm(request.POST)
		if form.is_valid():
			current_user = AppUser.objects.get(user=request.user)
			other_user = AppUser.objects.get(pk=form.cleaned_data['pk'])
			current_user.friends.remove(other_user)
			return HttpResponse(status=200)
	return HttpResponse(status=204)

@login_required
def friends(request):
	currentUser = AppUser.objects.get(user=request.user)
	friend_list = currentUser.friends.all()
	sent_list = FriendRequest.objects.filter(from_user=currentUser)
	pending_list = FriendRequest.objects.filter(to_user=currentUser)

	context = {'currentUser': currentUser, 'friend_list': friend_list, 'sent_list': sent_list, 'pending_list': pending_list}
	return render(request, 'exercise_app/friends.html', context)

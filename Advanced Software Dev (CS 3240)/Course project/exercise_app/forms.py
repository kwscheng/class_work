from django import forms
from django.http import QueryDict
from django.core.exceptions import ValidationError
from crispy_forms.helper import FormHelper
from crispy_forms.layout import Submit
from datetime import date, timedelta
from django.forms import ModelForm, TextInput
import requests
from .models import *

class ExerciseLogForm(forms.ModelForm):
	def __init__(self, *args, **kwargs):
		super().__init__(*args, **kwargs)
		self.helper = FormHelper()
		self.helper.form_method = 'post'
		self.fields['date'].widget = forms.DateInput(attrs={
			'required': True,
			'type': 'datetime-local',
		})
		self.helper.add_input(Submit('submit', 'Submit'))

	class Meta:
		model = ExerciseLogEntry
		exclude = ['']

class CardioLogForm(ExerciseLogForm):
	class Meta:
		model = ExerciseLogEntry
		exclude = ['appUser', 'exercise', 'weight', 'reps', 'intensity']

class WeightliftingLogForm(ExerciseLogForm):
	class Meta:
		model = ExerciseLogEntry
		exclude = ['appUser', 'exercise', 'distance', 'time', 'intensity']
class CustomLogForm(ExerciseLogForm):
	class Meta:
		model = ExerciseLogEntry
		exclude = ['appUser', 'exercise', 'distance', 'weight', 'reps']

class FriendForm(forms.Form):
	pk = forms.IntegerField(required=True)

class FriendRequestActionForm(forms.Form):
	ACCEPT = 'AC'
	REJECT = 'RE'
	CANCEL = 'CA'
	ACTION_CHOICES = [('AC', 'accept'), ('RE', 'reject'), ('CA', 'cancel')]
	action = forms.ChoiceField(choices=ACTION_CHOICES, required=True)
	other_user_pk = forms.IntegerField(required=True)

class RemoveFriendForm(forms.Form):
	pk = forms.IntegerField(required=True)

# https://github.com/asmaaeltawil/Weather-Api-using-django
class CityForm(ModelForm):

	def clean(self):
		qd = QueryDict(mutable=True)
		qd.update(appid='0c42f7f6b53b244c78a418f4f181282a', units='imperial', q=self.cleaned_data['name'])
		url = 'http://api.openweathermap.org/data/2.5/weather?{}'.format(qd.urlencode())
		response = requests.get(url).json()
		if response['cod'] == '404':
			raise ValidationError("Invalid city name")
	class Meta:
		model = City 
		fields = ['name']
		widgets = {'name' : TextInput(attrs={'class' : 'form-control', 'placeholder' : 'City Name'})}
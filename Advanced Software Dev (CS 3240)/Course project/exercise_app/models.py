from django.contrib.auth.models import User
from django.contrib.auth import get_user_model
from datetime import date, timedelta
from django.dispatch import receiver
from django.db import models
from django.db.models import Sum

class Exercise(models.Model):
	name = models.CharField(max_length=256)
	description = models.TextField()

	CARDIO = "CA"
	WEIGHTLIFTING = "WL"
	CUSTOM = "CU"
	EXERCISE_TYPES = [ (CARDIO, "cardio"), (WEIGHTLIFTING, "weightlifting"), (CUSTOM, "custom") ]
	exercise_type = models.CharField(max_length=2, choices=EXERCISE_TYPES, default=CARDIO)

	distanceFieldDescription = models.CharField(max_length=256, blank=True)
	timeFieldDescription = models.CharField(max_length=256, blank=True)
	weightFieldDescription = models.CharField(max_length=256, blank=True)
	repsFieldDescription = models.CharField(max_length=256, blank=True)

	def __str__(self):
		return self.name

# https://docs.djangoproject.com/en/3.1/topics/auth/customizing/#extending-the-existing-user-model
class AppUser(models.Model):
	user = models.OneToOneField(User, on_delete=models.CASCADE, primary_key=True)
	age = models.PositiveIntegerField(default=0)
	weight = models.PositiveIntegerField(default=0)
	height = models.PositiveIntegerField(default=0)
	points = models.PositiveIntegerField(default=0)


	exercises = models.ManyToManyField(Exercise, blank=True)
	friends = models.ManyToManyField('self', "User", blank=True)

	location = models.CharField(default="Charlottesville, VA",max_length=256)
	
	# TODO: implement goals, achievements, events, etc.
	# goals = models.ManyToManyField(Goal)

	def __str__(self):
		return self.user.get_username()

	def add_exercise(self, exercise):
		self.exercises.add(exercise) 

# Method to create an AppUser whenever a normal Django User is created
# https://stackoverflow.com/questions/52196365/django-automatically-create-a-model-instance-when-another-model-instance-is-cr/52196467
@receiver(models.signals.post_save, sender=get_user_model())
def create_app_user(sender, instance, created, **kwargs):
	if created:
		AppUser.objects.create(user=instance, age=0, weight=0, height=0, points=0)

class FriendRequest(models.Model):
	from_user = models.ForeignKey(AppUser, related_name="from_user", on_delete=models.CASCADE)
	to_user = models.ForeignKey(AppUser, related_name="to_user", on_delete=models.CASCADE)

class ExerciseLogEntry(models.Model):
	exercise = models.ForeignKey(Exercise, on_delete=models.CASCADE)
	appUser = models.ForeignKey(AppUser, on_delete=models.CASCADE, verbose_name="user")
	date = models.DateTimeField("log date")

	calories = models.PositiveIntegerField("calories burned", default=0)

	# fields for cadio exercises
	distance = models.FloatField("distance", default=0)
	time = models.DurationField("time", default=timedelta(0))

	# fields for weightlifting exercises
	weight = models.PositiveIntegerField("weight", default=0)
	reps = models.PositiveIntegerField("reps", default=0)

	#fields for custom exercises
	intensity = models.PositiveIntegerField("intensity", default=0)

	def __str__(self):
		return self.appUser.user.get_username() + ", " + self.exercise.name
# https://github.com/asmaaeltawil/Weather-Api-using-django
class City(models.Model):
	name = models.CharField(max_length=25)
	owm_id = models.PositiveIntegerField('id')

	def __str__(self):
		return self.name
	class Meta:
		verbose_name_plural = 'cities'

class Achievements(models.Model):
	name = models.CharField(max_length=256)
	description = models.TextField()
	exercise = models.ForeignKey(Exercise, on_delete=models.CASCADE)
	points = models.PositiveIntegerField("points")
	awardedUsers = models.ManyToManyField(AppUser, blank=True)
	requirements = models.PositiveIntegerField("requirements") # Duration requirements must be expressed in minutes

	CALORIES = "CA"
	TIME = "TI"
	DISTANCE = "DI"
	WEIGHT = "WE"
	REPS = "RE"
	FIELD_TRACKED = [ (CALORIES, "calories"), (TIME, "time"), (DISTANCE, "distance"), (WEIGHT, "weight"), (REPS, "reps") ]
	field_tracked = models.CharField(max_length=2, choices=FIELD_TRACKED, default=TIME)

	CUMULATIVE = "CU"
	SINGLE = "SI"
	ACHIEVEMENT_TYPES = [ (CUMULATIVE, "cumulative"), (SINGLE, "single")]
	achievement_type = models.CharField(max_length=2, choices=ACHIEVEMENT_TYPES, default=SINGLE)

	def __str__(self):
		return self.name

@receiver(models.signals.post_save, sender=ExerciseLogEntry)
def add_exercise(sender,instance, **kwargs):
	instance.appUser.exercises.add(instance.exercise)
	
@receiver(models.signals.pre_delete, sender=Achievements)
def deduct_points(sender, instance, **kwargs):
	awardedUsers = instance.awardedUsers.all()
	for user in awardedUsers:
		user.points = user.points - instance.points
		user.save()
	

@receiver(models.signals.post_save, sender=ExerciseLogEntry) 
def check_achievement(sender, instance, **kwargs): # distance and time
	achievements = Achievements.objects.filter(exercise=instance.exercise)	

	for tracked_field in Achievements.FIELD_TRACKED:
		
		cumulative_achievements = achievements.filter(field_tracked=tracked_field[0], achievement_type=Achievements.CUMULATIVE)
		single_achievements = achievements.filter(field_tracked=tracked_field[0], achievement_type=Achievements.SINGLE)
		# this is so bad

		cumulative_comparison = ExerciseLogEntry.objects.filter(exercise=instance.exercise, appUser=instance.appUser).aggregate(sum=Sum(tracked_field[1])).get("sum")
		if tracked_field[0] is Achievements.CALORIES:
			single_comparison = instance.calories
		elif tracked_field[0] is Achievements.DISTANCE:
			single_comparison = instance.distance
		elif tracked_field[0] is Achievements.WEIGHT:
			single_comparison = instance.weight
		elif tracked_field[0] is Achievements.REPS:
			single_comparison = instance.reps
		else:
			#cumulative_comparison = cumulative_comparison.seconds // 60
			single_comparison = instance.time.seconds // 60
		if tracked_field[0] is Achievements.TIME and isinstance(cumulative_comparison,timedelta):
			cumulative_comparison = cumulative_comparison.seconds // 60 
		# check all the cumulative achievements
		for i in cumulative_achievements:
			if i.requirements <= cumulative_comparison:
				if i not in instance.appUser.achievements_set.all():
					i.awardedUsers.add(instance.appUser)
					i.save()
					instance.appUser.points += i.points
					instance.appUser.save()

		# check all the single achievements
		for i in single_achievements:
			if i.requirements <= single_comparison:
				if i not in instance.appUser.achievements_set.all():
					i.awardedUsers.add(instance.appUser)
					i.save()
					instance.appUser.points += i.points
					instance.appUser.save()
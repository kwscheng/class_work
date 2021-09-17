from django.contrib import admin
from django.contrib.auth.admin import UserAdmin as BaseUserAdmin
from django.contrib.auth.models import User

from .models import *

class ExerciseLogEntryAdmin(admin.ModelAdmin):
	list_display = ('appUser', 'exercise', 'date')
	list_filter = ['appUser', 'exercise', 'date']
	verbose_name = 'exercise log entry'
	verbose_name_plural = 'exercise log entries'

class ExerciseAdmin(admin.ModelAdmin):
	list_display = ('name', 'description')
	list_filter = ['name']
	search_fields = ['name']

class AppUserInline(admin.StackedInline):
	model = AppUser
	
	can_delete = False
	verbose_name_plural = 'app user'

class UserAdmin(BaseUserAdmin):
	inlines = [AppUserInline]

admin.site.unregister(User)
admin.site.register(User, UserAdmin)
admin.site.register(Exercise, ExerciseAdmin)
admin.site.register(ExerciseLogEntry, ExerciseLogEntryAdmin)
admin.site.register(Achievements)
admin.site.register(City)
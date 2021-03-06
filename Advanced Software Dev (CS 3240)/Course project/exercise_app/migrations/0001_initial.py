# Generated by Django 3.1.5 on 2021-04-22 18:49

import datetime
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
        ('auth', '0012_alter_user_first_name_max_length'),
    ]

    operations = [
        migrations.CreateModel(
            name='AppUser',
            fields=[
                ('user', models.OneToOneField(on_delete=django.db.models.deletion.CASCADE, primary_key=True, serialize=False, to='auth.user')),
                ('age', models.PositiveIntegerField(default=0)),
                ('weight', models.PositiveIntegerField(default=0)),
                ('height', models.PositiveIntegerField(default=0)),
                ('points', models.PositiveIntegerField(default=0)),
            ],
        ),
        migrations.CreateModel(
            name='Exercise',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=256)),
                ('description', models.TextField()),
                ('exercise_type', models.CharField(choices=[('CA', 'cardio'), ('WL', 'weightlifting'), ('CU', 'custom')], default='CA', max_length=2)),
                ('distanceFieldDescription', models.CharField(blank=True, max_length=256)),
                ('timeFieldDescription', models.CharField(blank=True, max_length=256)),
                ('weightFieldDescription', models.CharField(blank=True, max_length=256)),
                ('repsFieldDescription', models.CharField(blank=True, max_length=256)),
            ],
        ),
        migrations.CreateModel(
            name='FriendRequest',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('from_user', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='from_user', to='exercise_app.appuser')),
                ('to_user', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='to_user', to='exercise_app.appuser')),
            ],
        ),
        migrations.CreateModel(
            name='ExerciseLogEntry',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('date', models.DateTimeField(verbose_name='log date')),
                ('calories', models.PositiveIntegerField(default=0, verbose_name='calories burned')),
                ('distance', models.FloatField(default=0, verbose_name='distance')),
                ('time', models.DurationField(default=datetime.timedelta(0), verbose_name='time')),
                ('weight', models.PositiveIntegerField(default=0, verbose_name='weight')),
                ('reps', models.PositiveIntegerField(default=0, verbose_name='reps')),
                ('intensity', models.PositiveIntegerField(default=0, verbose_name='intensity')),
                ('appUser', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='exercise_app.appuser', verbose_name='user')),
                ('exercise', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='exercise_app.exercise')),
            ],
        ),
        migrations.AddField(
            model_name='appuser',
            name='exercises',
            field=models.ManyToManyField(blank=True, to='exercise_app.Exercise'),
        ),
        migrations.AddField(
            model_name='appuser',
            name='friends',
            field=models.ManyToManyField(blank=True, related_name='_appuser_friends_+', to='exercise_app.AppUser'),
        ),
        migrations.CreateModel(
            name='Achievements',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=256)),
                ('description', models.TextField()),
                ('points', models.PositiveIntegerField(verbose_name='points')),
                ('requirements', models.PositiveIntegerField(verbose_name='requirements')),
                ('field_tracked', models.CharField(choices=[('CA', 'calories'), ('TI', 'time'), ('DI', 'distance'), ('WE', 'weight'), ('RE', 'reps')], default='TI', max_length=2)),
                ('achievement_type', models.CharField(choices=[('CU', 'cumulative'), ('SI', 'single')], default='SI', max_length=2)),
                ('awardedUsers', models.ManyToManyField(blank=True, to='exercise_app.AppUser')),
                ('exercise', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='exercise_app.exercise')),
            ],
        ),
    ]

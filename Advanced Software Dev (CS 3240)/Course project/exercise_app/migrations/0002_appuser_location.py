# Generated by Django 3.2 on 2021-05-06 07:30

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('exercise_app', '0001_initial'),
    ]

    operations = [
        migrations.AddField(
            model_name='appuser',
            name='location',
            field=models.CharField(default='Charlottesville, VA', max_length=256),
        ),
    ]

from django.test import Client, TestCase
from datetime import date, timedelta
from django.contrib.auth.models import User
from exercise_app.models import ExerciseLogEntry, AppUser, Exercise, Achievements


class ExerciseLogTests(TestCase):
    def setUp(self):
        testUser = User.objects.create_user(username='b-28test', password='3240Exercise') #Automatically makes an testAppUser?
        
        testUser.username = 'b-28test2'
        testUser.save()
        
        testAppUser = AppUser.objects.get(user=testUser)

        Exercise.objects.create(name="Running",description="hit feet",exercise_type = "CA") 
        Exercise.objects.create(name="Deadlift",description="Lift heavy object",exercise_type = "WL")
        Exercise.objects.create(name="Yoga",description="a Hindu spiritual and ascetic discipline, is widely practiced for health and relaxation.",
        exercise_type = "CU")

    def test_cardio_log_entry(self):
        """
		Tests that CardioLogEntry model can be succesfully created, stored, and pulled from database
		"""
        exerciseDate = date(2021,1,4) #Y,M,D
        exerciseTime = timedelta(0,seconds=30,minutes=30,hours=1) #When writing test cases, must use timedelta for DurationField
        cardioLog = ExerciseLogEntry.objects.create(exercise=Exercise.objects.get(name="Running"), distance=10,
        appUser=AppUser.objects.get(user=User.objects.get(username='b-28test2')),date=exerciseDate,calories=300,time=exerciseTime)
        self.assertEqual(cardioLog,ExerciseLogEntry.objects.get(date=exerciseDate))

    def test_weight_lighting_log_entry(self):
        """
		Tests that WeightLogEntry model can be succesfully created, stored, and pulled from database
		"""
        exerciseDate = date(2021,1,5) 
        weightLiftingLog = ExerciseLogEntry.objects.create(exercise=Exercise.objects.get(name="Deadlift"), reps=10,
        appUser=AppUser.objects.get(user=User.objects.get(username='b-28test2')),date=exerciseDate,calories=200,weight=300)
        self.assertEqual(weightLiftingLog,ExerciseLogEntry.objects.get(date=exerciseDate))

    def test_custom_log_entry(self):
        """
		Tests that CustomLogEntry model can be succesfully created, stored, and pulled from database
		"""
        exerciseDate = date(2021,1,6) 
        exerciseTime = timedelta(0,seconds=30,minutes=30,hours=1)
        YogaLog = ExerciseLogEntry.objects.create(exercise=Exercise.objects.get(name="Yoga"), intensity=2,
        appUser=AppUser.objects.get(user=User.objects.get(username='b-28test2')),date=exerciseDate,calories=100,time=exerciseTime)
        self.assertEqual(YogaLog,ExerciseLogEntry.objects.get(date=exerciseDate))

class RunningAchievementTests(TestCase):
    def setUp(self):
        testUser = User.objects.create_user(username='b-28test', password='3240Exercise') 

        Exercise.objects.create(name="Running",description="hit feet", exercise_type = "CA") 

    def test_running_achievement_IN_CU(self):
        """
        Tests that an INTENSITY/CUMULATIVE achievement can be created and then automatically awarded to Users upon log entry
        """
        testAppUser = AppUser.objects.get(user=User.objects.get(username='b-28test'))
        testAppUser.save()
        runningAchievement = Achievements.objects.create(
            name="Run50", description="Run 50 miles total!", exercise=Exercise.objects.get(name="Running"), 
            points=15,requirements=50,field_tracked="DI",achievement_type="CU")
        runningAchievement.save()

        exerciseDate = date(2020,8,4) #Y,M,D
        exerciseTime = timedelta(0,seconds=30,minutes=30,hours=5) #When writing test cases, must use timedelta for DurationField

        exerciseDate2 = date(2021,3,2) 
        exerciseTime2 = timedelta(0,seconds=30,minutes=30,hours=1) 

        cardioLog1 = ExerciseLogEntry.objects.create(exercise=Exercise.objects.get(name="Running"), distance=40,
        appUser=testAppUser,date=exerciseDate,calories=300,time=exerciseTime)
        cardioLog1.save()

        self.assertFalse(runningAchievement in testAppUser.achievements_set.all()) 

        cardioLog2 = ExerciseLogEntry.objects.create(exercise=Exercise.objects.get(name="Running"), distance=15,
        appUser=testAppUser,date=exerciseDate2,calories=300,time=exerciseTime2) #testAppUser should now have achievement
        cardioLog2.save()

        self.assertTrue(runningAchievement in testAppUser.achievements_set.all()) 

    def test_running_achievement_DU_CU(self):
        """
        Tests that an DURATION/CUMULATIVE achievement can be created and then automatically awarded to Users upon log entry
        """
        testAppUser = AppUser.objects.get(user=User.objects.get(username='b-28test'))
        testAppUser.save()
        runningAchievement = Achievements.objects.create(
            name="Run5HoursTotal", description="Run 5 hours total!", exercise=Exercise.objects.get(name="Running"), 
            points=15,requirements=300,field_tracked="TI",achievement_type="CU")
        runningAchievement.save()

        exerciseDate = date(2020,8,4) #Y,M,D
        exerciseTime = timedelta(0,seconds=30,minutes=30,hours=4) #When writing test cases, must use timedelta for DurationField

        exerciseDate2 = date(2021,3,2) 
        exerciseTime2 = timedelta(0,seconds=30,minutes=30,hours=1) 

        cardioLog1 = ExerciseLogEntry.objects.create(exercise=Exercise.objects.get(name="Running"), distance=40,
        appUser=testAppUser,date=exerciseDate,calories=300,time=exerciseTime)
        cardioLog1.save()

        self.assertFalse(runningAchievement in testAppUser.achievements_set.all()) 
        
        cardioLog2 = ExerciseLogEntry.objects.create(exercise=Exercise.objects.get(name="Running"), distance=15,
        appUser=testAppUser,date=exerciseDate2,calories=300,time=exerciseTime2) #testAppUser should now have achievement
        cardioLog2.save()

        self.assertTrue(runningAchievement in testAppUser.achievements_set.all()) 

    def test_running_achievement_IN_SI(self):
        """
        Tests that an INTENSITY/SINGLE achievement can be created and then automatically awarded to Users upon log entry
        """
        testAppUser = AppUser.objects.get(user=User.objects.get(username='b-28test'))
        testAppUser.save()
        runningAchievement = Achievements.objects.create(
            name="Run5", description="Log 5 or more miles in one entry!", exercise=Exercise.objects.get(name="Running"), 
            points=15,requirements=5,field_tracked="DI",achievement_type="SI")
        runningAchievement.save()

        exerciseDate = date(2020,8,4) #Y,M,D
        exerciseTime = timedelta(0,seconds=30,minutes=30,hours=5) #When writing test cases, must use timedelta for DurationField
        

        cardioLog1 = ExerciseLogEntry.objects.create(exercise=Exercise.objects.get(name="Running"), distance=10,
        appUser=testAppUser,date=exerciseDate,calories=300,time=exerciseTime)
        cardioLog1.save()
        
        self.assertTrue(runningAchievement in testAppUser.achievements_set.all(),
        msg='Log entry running distance: {0}'.format(cardioLog1.distance)) 
    
    def test_running_achievement_DU_SI(self):
        """
        Tests that an DURATION/SINGLE achievement can be created and then automatically awarded to Users upon log entry
        """
        testAppUser = AppUser.objects.get(user=User.objects.get(username='b-28test'))
        testAppUser.save()
        runningAchievementMin = Achievements.objects.create(
            name="Run30Minutes", description="Run for 30 minutes or longer in one session!", exercise=Exercise.objects.get(name="Running"), 
            points=15,requirements=30,field_tracked="TI",achievement_type="SI")
        runningAchievementMin.save()

        exerciseDate = date(2020,8,4) #Y,M,D
        exerciseTime = timedelta(0,seconds=30,minutes=30,hours=0) #When writing test cases, must use timedelta for DurationField
        

        cardioLog1 = ExerciseLogEntry.objects.create(exercise=Exercise.objects.get(name="Running"), distance=3, 
        appUser=testAppUser,date=exerciseDate,calories=300,time=exerciseTime)
        cardioLog1.save()
        
        self.assertTrue(runningAchievementMin in testAppUser.achievements_set.all(),
        msg='Log entry running distance: {0}'.format(cardioLog1.time)) 
        
